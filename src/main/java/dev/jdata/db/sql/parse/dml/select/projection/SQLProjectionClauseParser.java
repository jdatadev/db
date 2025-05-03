package dev.jdata.db.sql.parse.dml.select.projection;

import java.io.IOException;
import java.util.Objects;

import org.jutils.ast.objects.list.IAddableList;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.statements.dml.SQLProjectionClause;
import dev.jdata.db.sql.ast.statements.dml.SQLProjectionItem;
import dev.jdata.db.sql.parse.BaseSQLParser;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLToken;

public final class SQLProjectionClauseParser extends BaseSQLParser {

    private final SQLProjectionItemParser projectionItemParser;

    public SQLProjectionClauseParser(SQLProjectionItemParser projectionItemParser) {

        this.projectionItemParser = Objects.requireNonNull(projectionItemParser);
    }

    private static final SQLToken[] COMMA_OR_FROM_TOKENS = new SQLToken[] {

            SQLToken.COMMA,
            SQLToken.FROM
    };

    public SQLProjectionClause parseProjectionClause(SQLExpressionLexer lexer)
            throws ParserException, IOException {

        final SQLToken[] expectedTokens = COMMA_OR_FROM_TOKENS;

        boolean done = false;

        final SQLAllocator allocator = lexer.getAllocator();

        final IAddableList<SQLProjectionItem> projectionItems = allocator.allocateList(100);

        try {
            do {
                switch (lexer.lex(expectedTokens)) {

                case COMMA:

                    final SQLProjectionItem projectionItem = projectionItemParser.parseProjectionItem(lexer);

                    projectionItems.add(projectionItem);
                    break;

                case FROM:

                    done = true;
                    break;

                default:
                    throw new UnsupportedOperationException();
                }

            } while (!done);
        }
        finally {

            allocator.freeList(projectionItems);
        }

        return new SQLProjectionClause(makeContext(), projectionItems);
    }
}
