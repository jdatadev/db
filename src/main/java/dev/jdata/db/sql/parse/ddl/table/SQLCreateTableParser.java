package dev.jdata.db.sql.parse.ddl.table;

import java.util.Objects;

import org.jutils.ast.objects.list.IAddableList;
import org.jutils.io.strings.CharInput;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.ISQLAllocator;
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

    public <E extends Exception, I extends CharInput<E>> SQLCreateTableStatement parseCreateTable(SQLExpressionLexer<E, I> lexer, long createKeyword, long tableKeyword)
            throws ParserException, E {

        final SQLCreateTableStatement result;

        final long tableName = lexer.lexName();

        final ISQLAllocator allocator = lexer.getAllocator();

        final IAddableList<SQLTableColumnDefinition> columnDefinitions = allocator.allocateList(100);

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

            result = new SQLCreateTableStatement(makeContext(), createKeyword, tableKeyword, tableName, columnDefinitions);

            allocator.freeList(columnDefinitions);
        }

        return result;
    }
}
