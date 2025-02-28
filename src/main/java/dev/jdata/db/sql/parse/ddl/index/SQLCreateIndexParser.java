package dev.jdata.db.sql.parse.ddl.index;

import java.io.IOException;
import java.util.List;

import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.statements.dml.SQLObjectName;
import dev.jdata.db.sql.ast.statements.index.SQLCreateIndexStatement;
import dev.jdata.db.sql.ast.statements.index.SQLIndexColumn;
import dev.jdata.db.sql.ast.statements.index.SQLIndexTypeOptions;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;
import dev.jdata.db.sql.parse.SQLToken;

public class SQLCreateIndexParser extends SQLStatementParser {

    public SQLCreateIndexStatement parseCreateIndex(SQLExpressionLexer lexer, long createKeyword, long indexKeyword) throws ParserException, IOException {

        final long indexName = lexer.lexName();

        final SQLIndexTypeOptions indexTypeOptions = parseIndexTypeOptions(lexer);

        final long onKeyword = lexer.lexKeyword(SQLToken.ON);

        final SQLObjectName objectName = parseObjectName(lexer);

        final SQLAllocator allocator = lexer.getAllocator();

        final List<SQLIndexColumn> columns = allocator.allocateList(10);

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

    protected SQLIndexTypeOptions parseIndexTypeOptions(SQLLexer lexer) throws IOException {

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
