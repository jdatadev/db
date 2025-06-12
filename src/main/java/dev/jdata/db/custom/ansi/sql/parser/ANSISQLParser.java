package dev.jdata.db.custom.ansi.sql.parser;

import dev.jdata.db.sql.parse.SQLParser;
import dev.jdata.db.sql.parse.SQLParserFactory;
import dev.jdata.db.sql.parse.SQLToken;

final class ANSISQLParser extends SQLParser {

    ANSISQLParser(SQLToken[] statementTokens, SQLToken[] createOrDropTokens, SQLToken[] alterTokens, SQLParserFactory parserFactory) {
        super(statementTokens, createOrDropTokens, alterTokens, parserFactory);
    }
}
