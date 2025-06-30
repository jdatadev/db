package dev.jdata.db.sql.parse;

import java.util.Objects;

import org.jutils.io.strings.CharInput;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.dml.SQLObjectName;
import dev.jdata.db.sql.ast.statements.dml.SQLTableName;
import dev.jdata.db.sql.ast.statements.table.SQLColumnNames;
import dev.jdata.db.utils.adt.lists.CachedLongIndexList.CachedLongIndexListBuilder;
import dev.jdata.db.utils.adt.lists.LongIndexList;

public abstract class BaseSQLParser extends BaseParser {

    protected static <E extends Exception, I extends CharInput<E>> int parseUnsignedInt(SQLLexer<E, I> lexer, StringResolver stringResolver) throws ParserException, E {

        final long stringRef = lexer.lexStringRef(SQLToken.INTEGER_NUMBER);

        return stringResolver.asInt(stringRef);
    }

    protected static Context makeContext() {

        return null;
//        throw new UnsupportedOperationException();
    }

    protected static <E extends Exception, I extends CharInput<E>> SQLObjectName parseObjectName(SQLLexer<E, I> lexer) throws ParserException, E {

        final long objectName = lexer.lexName();

        return new SQLObjectName(makeContext(), objectName);
    }

    protected static <E extends Exception, I extends CharInput<E>> SQLTableName parseTableName(SQLLexer<E, I> lexer) throws ParserException, E {

        final long tableName = lexer.lexName();

        return new SQLTableName(makeContext(), tableName);
    }

    protected static <E extends Exception, I extends CharInput<E>> SQLColumnNames parseColumnNames(SQLLexer<E, I> lexer, ISQLAllocator allocator) throws ParserException, E {

        return parseColumnNames(lexer, allocator, true);
    }

    protected static <E extends Exception, I extends CharInput<E>> SQLColumnNames parseColumnNames(SQLLexer<E, I> lexer, ISQLAllocator allocator, boolean hasParenthesis)
            throws ParserException, E {

        Objects.requireNonNull(lexer);
        Objects.requireNonNull(allocator);

        if (hasParenthesis) {

            lexer.lexExpect(SQLToken.LPAREN);
        }

        final LongIndexList names = parseNames(lexer, allocator);

        if (hasParenthesis) {

            lexer.lexExpect(SQLToken.RPAREN);
        }

        return new SQLColumnNames(makeContext(), names);
    }

    protected static <E extends Exception, I extends CharInput<E>> LongIndexList parseNames(SQLLexer<E, I> lexer, ISQLAllocator allocator) throws ParserException, E {

        Objects.requireNonNull(lexer);
        Objects.requireNonNull(allocator);

        final LongIndexList result;

        final CachedLongIndexListBuilder namesBuilder = allocator.allocateLongIndexListBuilder(10);

        try {
            for (;;) {

                final long name = lexer.lexName();

                namesBuilder.addTail(name);

                if (!lexer.lex(SQLToken.COMMA)) {

                    break;
                }
            }

            result = namesBuilder.build();
        }
        finally {

            allocator.freeLongIndexListBuilder(namesBuilder);
        }

        return result;
    }
}
