package dev.jdata.db.sql.parse;

import java.io.IOException;
import java.util.Objects;

import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.statements.dml.SQLObjectName;
import dev.jdata.db.sql.ast.statements.dml.SQLTableName;
import dev.jdata.db.sql.ast.statements.table.SQLColumnNames;
import dev.jdata.db.utils.adt.arrays.LongArray;

public abstract class BaseSQLParser extends BaseParser {

    protected static int parseUnsignedInt(SQLLexer lexer, StringResolver stringResolver) throws ParserException, IOException {

        final long stringRef = lexer.lexStringRef(SQLToken.INTEGER_NUMBER);

        return stringResolver.asInt(stringRef);
    }

    protected static Context makeContext() {

        return null;
//        throw new UnsupportedOperationException();
    }

    protected static SQLObjectName parseObjectName(SQLLexer lexer) throws ParserException, IOException {

        final long objectName = lexer.lexName();

        return new SQLObjectName(makeContext(), objectName);
    }

    protected static SQLTableName parseTableName(SQLLexer lexer) throws ParserException, IOException {

        final long tableName = lexer.lexName();

        return new SQLTableName(makeContext(), tableName);
    }

    protected static SQLColumnNames parseColumnNames(SQLLexer lexer, SQLAllocator allocator) throws ParserException, IOException {

        return parseColumnNames(lexer, allocator, true);
    }

    protected static SQLColumnNames parseColumnNames(SQLLexer lexer, SQLAllocator allocator, boolean hasParenthesis) throws ParserException, IOException {

        Objects.requireNonNull(lexer);
        Objects.requireNonNull(allocator);

        if (hasParenthesis) {

            lexer.lexExpect(SQLToken.LPAREN);
        }

        final LongArray names = parseNames(lexer, allocator);

        if (hasParenthesis) {

            lexer.lexExpect(SQLToken.RPAREN);
        }

        return new SQLColumnNames(makeContext(), names);
    }

    protected static LongArray parseNames(SQLLexer lexer, SQLAllocator allocator) throws ParserException, IOException {

        Objects.requireNonNull(lexer);
        Objects.requireNonNull(allocator);

        final LongArray names = allocator.allocateLongArray(10);

        for (;;) {

            final long name = lexer.lexName();

            names.add(name);

            if (!lexer.lex(SQLToken.COMMA)) {

                break;
            }
        }

        return names;
    }
}
