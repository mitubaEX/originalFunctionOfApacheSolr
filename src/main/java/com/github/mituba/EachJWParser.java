
package com.github.mituba;

import org.apache.lucene.queries.function.ValueSource;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.search.ValueSourceParser;

public class EachJWParser extends ValueSourceParser {

    @Override
    public ValueSource parse(FunctionQParser fp) throws SyntaxError {
        ValueSource data = fp.parseValueSource();
        String inputString = fp.parseArg();

        return new EachJW(data, inputString);
    }
}
