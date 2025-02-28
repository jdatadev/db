package dev.jdata.db.sql.parse.ddl.table;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;
import dev.jdata.db.sql.parse.SQLToken;

public class SQLCreateTableParser extends SQLStatementParser {

    private final SQLColumnDefinitionParser columnDefinitionParser;
    private final SQLToken[] schemaDataTypeTokens;

    public SQLCreateTableParser(SQLColumnDefinitionParser columnDefinitionParser, SQLToken[] schemaDataTypeTokens) {

        this.columnDefinitionParser = Objects.requireNonNull(columnDefinitionParser);
        this.schemaDataTypeTokens = Objects.requireNonNull(schemaDataTypeTokens);
    }

    public SQLCreateTableStatement parseCreateTable(SQLExpressionLexer lexer, long createKeyword, long tableKeyword) throws ParserException, IOException {

        final long tableName = lexer.lexName();

        final SQLAllocator allocator = lexer.getAllocator();

        final List<SQLTableColumnDefinition> columnDefinitions = allocator.allocateList(100);

        try {
            lexer.lexExpect(SQLToken.LPAREN);

            if (!lexer.lex(SQLToken.RPAREN)) {

                for (;;) {

                    final SQLTableColumnDefinition columnDefinition = columnDefinitionParser.parseTableColumnDefinition(lexer, schemaDataTypeTokens);

                    columnDefinitions.add(columnDefinition);

                    if (!lexer.lex(SQLToken.COMMA)) {

                        break;
                    }
                }

                lexer.lexExpect(SQLToken.RPAREN);
            }
        }
        finally {

            allocator.freeList(columnDefinitions);
        }

        return new SQLCreateTableStatement(makeContext(), createKeyword, tableKeyword, tableName, columnDefinitions);
    }
}
