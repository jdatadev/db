package dev.jdata.db.sql.parse.ddl.index;

import org.jutils.ast.objects.list.IAddableList;
import org.jutils.io.strings.CharInput;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.dml.SQLObjectName;
import dev.jdata.db.sql.ast.statements.index.SQLCreateIndexStatement;
import dev.jdata.db.sql.ast.statements.index.SQLIndexColumn;
import dev.jdata.db.sql.ast.statements.index.SQLIndexTypeOptions;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;
import dev.jdata.db.sql.parse.SQLToken;

public class SQLCreateIndexParser extends SQLStatementParser {

    public <E extends Exception, I extends CharInput<E>> SQLCreateIndexStatement parseCreateIndex(SQLExpressionLexer<E, I> lexer, long createKeyword, long indexKeyword)
            throws ParserException, E {

        final long indexName = lexer.lexName();

        final SQLIndexTypeOptions indexTypeOptions = parseIndexTypeOptions(lexer);

        final long onKeyword = lexer.lexKeyword(SQLToken.ON);

        final SQLObjectName objectName = parseObjectName(lexer);

        final ISQLAllocator allocator = lexer.getAllocator();

        final IAddableList<SQLIndexColumn> columns = allocator.allocateList(10);

        final SQLCreateIndexStatement result;

        try {
            lexer.lexExpect(SQLToken.LPAREN);

            for (;;) {

                final long columnName = lexer.lexName();

                final SQLIndexColumn column = new SQLIndexColumn(makeContext(), columnName);

                columns.add(column);

                if (!lexer.lex(SQLToken.COMMA)) {

                    break;
                }
            }

            lexer.lexExpect(SQLToken.RPAREN);

            result = new SQLCreateIndexStatement(makeContext(), createKeyword, indexTypeOptions, indexKeyword, indexName, onKeyword, objectName, columns);
        }
        finally {

            allocator.freeList(columns);
        }

        lexer.lexExpect(SQLToken.RPAREN);

        return result;
    }

    protected <E extends Exception, I extends CharInput<E>> SQLIndexTypeOptions parseIndexTypeOptions(SQLLexer<E, I> lexer) throws E {

        final SQLIndexTypeOptions result;

        if (lexer.lex(SQLToken.UNIUQE)) {

            result = new SQLIndexTypeOptions(makeContext(), lexer.getStringRef());
        }
        else {
            result = null;
        }

        return result;
    }
}
