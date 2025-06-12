package dev.jdata.db.sql.parse.dml.select.result;

import java.util.Objects;

import org.jutils.ast.objects.list.IAddableList;
import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.io.strings.CharInput;
import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.dml.SQLOrderByOrGroupByClause;
import dev.jdata.db.sql.ast.statements.dml.SQLProjectionItem;
import dev.jdata.db.sql.parse.BaseSQLParser;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.dml.select.projection.SQLProjectionItemParser;

public abstract class BaseSQLGroupOrOrderByClauseParser<T extends BaseSQLElement, U extends SQLOrderByOrGroupByClause<T>> extends BaseSQLParser {

    private final SQLProjectionItemParser projectionItemParser;

    protected abstract <E extends Exception, I extends CharInput<E>> T parseElement(SQLExpressionLexer<E, I> lexer, Context context, SQLProjectionItem projectionItem) throws E;
    protected abstract U createGroupOrOrderByClause(Context context, IIndexListGetters<T> elements);

    protected BaseSQLGroupOrOrderByClauseParser(SQLProjectionItemParser projectionItemParser) {

        this.projectionItemParser = Objects.requireNonNull(projectionItemParser);
    }

    protected final <E extends Exception, I extends CharInput<E>> U parseGroupByOrOrderBy(SQLExpressionLexer<E, I> lexer) throws ParserException, E {

        final ISQLAllocator allocator = lexer.getAllocator();

        final U result;

        final IAddableList<T> list = allocator.allocateList(10);

        try {
            for (;;) {

                final SQLProjectionItem projectionItem = projectionItemParser.parseProjectionItem(lexer);

                final T element = parseElement(lexer, makeContext(), projectionItem);

                list.add(element);

                if (lexer.peek(SQLToken.COMMA)) {

                    lexer.lexSkip(SQLToken.COMMA);
                }
                else {
                    break;
                }
            }
        }
        finally {

            result = createGroupOrOrderByClause(makeContext(), list);

            allocator.freeList(list);
        }

        return result;
    }
}
