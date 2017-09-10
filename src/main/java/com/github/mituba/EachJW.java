package com.github.mituba;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.docvalues.FloatDocValues;
import org.apache.lucene.queries.function.docvalues.IntDocValues;
import info.debatty.java.stringsimilarity.*;


public class EachJW extends ValueSource {
    protected ValueSource data;
    protected String inputString;

    public EachJW(ValueSource data, String inputString) {
        this.data = data;
        this.inputString = inputString;
    }

    @Override
    public FunctionValues getValues(Map context, LeafReaderContext readerContext) throws IOException {
        final FunctionValues dataFunc = data.getValues(context, readerContext);

        return new FloatDocValues(this) {
            @Override
            public float floatVal(int doc) {
                try {
                    JaroWinkler jw = new JaroWinkler();
                    String dataString = dataFunc.objectVal(doc).toString();
                    String[] dataStringArray = dataString.split(",");
                    String[] inputStringArray = inputString.split(",");
                    Float sum = new Float(0.0);
                    Float max = new Float(0.0);

                    // 一個固定全部回してその結果をsumにぶち込んでいく．

                    for(int i = 0; i < inputStringArray.length; i++){
                        for(int j = 0; j < dataStringArray.length; j++){
                            sum += (new Float(1.0) - new Float(jw.similarity(dataStringArray[j], inputStringArray[i])));
                            max = Math.max(max, new Float(1.0) - new Float(jw.similarity(dataStringArray[j], inputStringArray[i])));
                        }
                    }

//                    return (new Float(1.0) - new Float(levenshtein.distance(dataString, inputString)));
                    return sum / new Float(inputStringArray.length * dataStringArray.length);
                    // return max;
                }catch (Exception e){
                    e.printStackTrace();
                    return new Float(0.0);
                }
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (EachJW.class.equals(o.getClass())) {
            EachJW other = (EachJW) o;
            return Objects.equals(data, other.data) &&
                    Objects.equals(inputString, other.inputString);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, inputString);
    }

    @Override
    public String description() {
        return EachJW.class.getSimpleName();
    }
}

