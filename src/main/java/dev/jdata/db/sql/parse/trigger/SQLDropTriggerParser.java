package dev.jdata.db.sql.parse.trigger;

import java.io.IOException;
import java.util.Objects;

import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.statements.trigger.SQLDropTriggerStatement;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;

public class SQLDropTriggerParser extends SQLStatementParser {

    public SQLDropTriggerStatement parseDropTrigger(SQLLexer lexer, long dropKeyword, long triggerKeyword) throws ParserException, IOException {

        Objects.requireNonNull(lexer);
        checkIsKeyword(dropKeyword);
        checkIsKeyword(triggerKeyword);

        final long name = lexer.lexName();

        return new SQLDropTriggerStatement(makeContext(), dropKeyword, triggerKeyword, name);
    }
}
