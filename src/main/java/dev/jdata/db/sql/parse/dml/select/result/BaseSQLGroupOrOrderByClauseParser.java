package dev.jdata.db.sql.parse.dml.select.result;

import java.io.IOException;
import java.util.Objects;

import org.jutils.ast.objects.list.IAddableList;
import org.jutils.ast.objects.list.IListGetters;
import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;
import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.statements.dml.SQLOrderByOrGroupByClause;
import dev.jdata.db.sql.ast.statements.dml.SQLProjectionItem;
import dev.jdata.db.sql.parse.BaseSQLParser;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.dml.select.projection.SQLProjectionItemParser;

public abstract class BaseSQLGroupOrOrderByClauseParser<T extends BaseSQLElement, U extends SQLOrderByOrGroupByClause<T>> extends BaseSQLParser {

    private final SQLProjectionItemParser projectionItemParser;

    protected abstract T parseElement(SQLExpressionLexer lexer, Context context, SQLProjectionItem projectionItem) throws IOException;
    protected abstract U createGroupOrOrderByClause(Context context, IListGetters<T> elements);

    protected BaseSQLGroupOrOrderByClauseParser(SQLProjectionItemParser projectionItemParser) {

        this.projectionItemParser = Objects.requireNonNull(projectionItemParser);
    }

    protected final U parseGroupByOrOrderBy(SQLExpressionLexer lexer) throws ParserException, IOException {

        final SQLAllocator allocator = lexer.getAllocator();

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
