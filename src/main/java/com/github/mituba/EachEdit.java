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


public class EachEdit extends ValueSource {
    protected ValueSource data;
    protected String inputString;

    public EachEdit(ValueSource data, String inputString) {
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
                    NormalizedLevenshtein levenshtein = new NormalizedLevenshtein();
                    String dataString = dataFunc.objectVal(doc).toString();

                    return (new Float(1.0) - new Float(levenshtein.distance(dataString, inputString)));
                }catch (Exception e){
                    e.printStackTrace();
                    return new Float(0.0);
                }
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (EachEdit.class.equals(o.getClass())) {
            EachEdit other = (EachEdit) o;
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
        return EachEdit.class.getSimpleName();
    }
}

