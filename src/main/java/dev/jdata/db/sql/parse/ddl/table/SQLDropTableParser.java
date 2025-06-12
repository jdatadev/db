package dev.jdata.db.sql.parse.ddl.table;

import org.jutils.io.strings.CharInput;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.statements.table.SQLDropTableStatement;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;

public final class SQLDropTableParser extends SQLStatementParser {

    public <E extends Exception, I extends CharInput<E>> SQLDropTableStatement parseDropTable(SQLLexer<E, I> lexer, long dropKeyword, long tableKeyword)
            throws ParserException, E {

        final long tableName = lexer.lexName();

        return new SQLDropTableStatement(makeContext(), dropKeyword, tableKeyword, tableName);
    }
}
