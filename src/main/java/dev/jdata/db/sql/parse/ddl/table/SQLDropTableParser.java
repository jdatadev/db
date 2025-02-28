package dev.jdata.db.sql.parse.ddl.table;

import java.io.IOException;

import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.statements.table.SQLDropTableStatement;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;

public final class SQLDropTableParser extends SQLStatementParser {

    public SQLDropTableStatement parseDropTable(SQLLexer lexer, long dropKeyword, long tableKeyword) throws ParserException, IOException {

        final long tableName = lexer.lexName();

        return new SQLDropTableStatement(makeContext(), dropKeyword, tableKeyword, tableName);
    }
}
