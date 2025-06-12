package dev.jdata.db.custom.ansi.sql.parser;

import dev.jdata.db.sql.parse.SQLParser;
import dev.jdata.db.sql.parse.SQLParserFactory;

public final class ANSISQLParserFactory extends SQLParserFactory {

    public static final ANSISQLParserFactory INSTANCE = new ANSISQLParserFactory();

    private ANSISQLParserFactory() {

    }

    @Override
    public SQLParser createParser() {

        return new ANSISQLParser(getSQLstatementTokens(), getSQLCreateOrDropTokens(), getSQLAlterTokens(), this);
    }
}
