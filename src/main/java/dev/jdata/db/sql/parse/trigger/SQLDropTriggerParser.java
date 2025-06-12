package dev.jdata.db.sql.parse.trigger;

import java.util.Objects;

import org.jutils.io.strings.CharInput;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.statements.trigger.SQLDropTriggerStatement;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;
public class SQLDropTriggerParser extends SQLStatementParser {

    public <E extends Exception, I extends CharInput<E>> SQLDropTriggerStatement parseDropTrigger(SQLLexer<E, I> lexer, long dropKeyword, long triggerKeyword)
            throws ParserException, E {

        Objects.requireNonNull(lexer);
        checkIsKeyword(dropKeyword);
        checkIsKeyword(triggerKeyword);

        final long name = lexer.lexName();

        return new SQLDropTriggerStatement(makeContext(), dropKeyword, triggerKeyword, name);
    }
}
