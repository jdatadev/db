package dev.jdata.db.sql.parse.ddl.table;

import java.io.IOException;
import java.util.Objects;

import org.jutils.ast.objects.BaseASTElement;
import org.jutils.ast.objects.list.IAddableList;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.statements.table.SQLAddColumnDefinition;
import dev.jdata.db.sql.ast.statements.table.SQLAddColumnsOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddForeignKeyConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddNotNullConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddNullConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddPrimaryKeyConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddUniqueConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableAddConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLColumnNames;
import dev.jdata.db.sql.ast.statements.table.SQLDropColumnOperation;
import dev.jdata.db.sql.ast.statements.table.SQLDropConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLModifyColumn;
import dev.jdata.db.sql.ast.statements.table.SQLModifyColumnsOperation;
import dev.jdata.db.sql.ast.statements.table.SQLOnDeleteCascade;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLStatementParser;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.utils.adt.arrays.LongArray;

public class SQLAlterTableParser extends SQLStatementParser {

    private final SQLColumnDefinitionParser columnDefinitionParser;
    private final SQLToken[] schemaDataTypeTokens;
    private final SQLToken[] addConstraintTokens;

    public SQLAlterTableParser(SQLColumnDefinitionParser columnDefinitionParser, SQLToken[] schemaDataTypeTokens, SQLToken[] addConstraintTokens) {

        this.columnDefinitionParser = Objects.requireNonNull(columnDefinitionParser);
        this.schemaDataTypeTokens = Objects.requireNonNull(schemaDataTypeTokens);
        this.addConstraintTokens = Objects.requireNonNull(addConstraintTokens);
    }

    private static final SQLToken[] COMMAND_TOKENS = new SQLToken[] {

            SQLToken.ADD,
            SQLToken.MODIFY,
            SQLToken.DROP
    };

    private static final SQLToken[] ADD_OR_DROP_TOKENS = new SQLToken[] {

            SQLToken.CONSTRAINT
    };

    public final SQLAlterTableStatement parseAlterTable(SQLExpressionLexer lexer, long alterKeyword, long tableKeyword) throws ParserException, IOException {

        final long tableName = lexer.lexName();

        final SQLToken commandToken = lexer.lex(COMMAND_TOKENS);
        final long commandKeyword = lexer.getStringRef();

        final SQLAlterTableOperation alterTableOperation;

        switch (commandToken) {

        case ADD:

            final SQLToken addSubCommandToken = lexer.peek(ADD_OR_DROP_TOKENS);

            switch (addSubCommandToken) {

            case CONSTRAINT:

                final long constraintKeyword = lexer.getStringRef();

                alterTableOperation = parseAddConstraint(lexer, commandKeyword, constraintKeyword);
                break;

            default:

                alterTableOperation = parseAddColumns(lexer, commandKeyword);
                break;
            }
            break;

        case MODIFY:

            alterTableOperation = parseModifyColumns(lexer, commandKeyword);
            break;

        case DROP:

            final SQLToken dropSubCommandToken = lexer.peek(ADD_OR_DROP_TOKENS);

            switch (dropSubCommandToken) {

            case CONSTRAINT:

                final long constraintKeyword = lexer.getStringRef();

                alterTableOperation = parseDropConstraint(lexer, commandKeyword, constraintKeyword);
                break;

            default:

                alterTableOperation = parseDropColumn(lexer, commandKeyword);
                break;
            }
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return new SQLAlterTableStatement(makeContext(), commandKeyword, tableKeyword, tableName, alterTableOperation);
    }

    private SQLAddColumnsOperation parseAddColumns(SQLExpressionLexer lexer, long addKeyword) throws ParserException, IOException {

        final SQLAllocator allocator = lexer.getAllocator();

        final IAddableList<SQLAddColumnDefinition> addColumnDefinitions = allocator.allocateList(10);

        try {
            for (;;) {

                final SQLTableColumnDefinition columnDefinition = columnDefinitionParser.parseTableColumnDefinition(lexer, schemaDataTypeTokens);

                final long beforeKeyword;
                final long beforeColumnName;

                if (lexer.peek(SQLToken.BEFORE)) {

                    beforeKeyword = lexer.lexKeyword(SQLToken.BEFORE);
                    beforeColumnName = lexer.lexName();
                }
                else {
                    beforeKeyword = BaseASTElement.NO_KEYWORD;
                    beforeColumnName = BaseASTElement.NO_NAME;
                }

                final SQLAddColumnDefinition addColumnDefinition = new SQLAddColumnDefinition(makeContext(), columnDefinition, beforeKeyword, beforeColumnName);

                addColumnDefinitions.add(addColumnDefinition);

                if (!lexer.lex(SQLToken.COMMA)) {

                    break;
                }
            }
        }
        finally {

            allocator.freeList(addColumnDefinitions);
        }

        return new SQLAddColumnsOperation(makeContext(), addKeyword, addColumnDefinitions);
    }

    private SQLModifyColumnsOperation parseModifyColumns(SQLExpressionLexer lexer, long modifyKeyword) throws ParserException, IOException {

        final SQLAllocator allocator = lexer.getAllocator();

        final IAddableList<SQLModifyColumn> modifyColumns = allocator.allocateList(10);

        try {
            for (;;) {

                final SQLTableColumnDefinition columnDefinition = columnDefinitionParser.parseTableColumnDefinition(lexer, schemaDataTypeTokens);

                final SQLModifyColumn modifyColumn = new SQLModifyColumn(makeContext(), columnDefinition);

                modifyColumns.add(modifyColumn);

                if (!lexer.lex(SQLToken.COMMA)) {

                    break;
                }
            }
        }
        finally {

            allocator.freeList(modifyColumns);
        }

        return new SQLModifyColumnsOperation(makeContext(), modifyKeyword, modifyColumns);
    }

    private SQLAlterTableAddConstraintOperation parseAddConstraint(SQLExpressionLexer lexer, long addKeyword, long constraintKeyword) throws ParserException, IOException {

        final SQLToken subCommandToken = lexer.lex(addConstraintTokens);

        final long subCommandKeyword = lexer.getStringRef();

        final SQLAlterTableAddConstraintOperation addConstraintOperation;

        switch (subCommandToken) {

        case UNIUQE:
        case DISTINCT: {

            final SQLColumnNames columnNames = parseColumnNames(lexer, lexer.getAllocator());

            final long constraintName = lexer.lexName();

            addConstraintOperation = new SQLAddUniqueConstraintOperation(makeContext(), addKeyword, subCommandKeyword, constraintKeyword, columnNames, constraintName);
            break;
        }

        case NOT: {

            final long nullKeyword = lexer.lexKeyword(SQLToken.NULL);

            final SQLColumnNames columnNames = parseColumnNames(lexer, lexer.getAllocator());

            final long constraintName = lexer.lexName();

            addConstraintOperation = new SQLAddNotNullConstraintOperation(makeContext(), addKeyword, constraintKeyword, subCommandKeyword, nullKeyword, columnNames,
                    constraintName);
            break;
        }

        case NULL: {

            final SQLColumnNames columnNames = parseColumnNames(lexer, lexer.getAllocator());

            final long constraintName = lexer.lexName();

            addConstraintOperation = new SQLAddNullConstraintOperation(makeContext(), addKeyword, constraintKeyword, subCommandKeyword, columnNames, constraintName);
            break;
        }

        case PRIMARY: {

            final long keyKeyword = lexer.lexKeyword(SQLToken.KEY);

            final SQLColumnNames columnNames = parseColumnNames(lexer, lexer.getAllocator());

            final long constraintName = lexer.lexName();

            addConstraintOperation = new SQLAddPrimaryKeyConstraintOperation(makeContext(), addKeyword, constraintKeyword, subCommandKeyword, keyKeyword, columnNames,
                    constraintName);
            break;
        }

        case FOREIGN: {

            final long keyKeyword = lexer.lexKeyword(SQLToken.KEY);

            final SQLColumnNames columnNames = parseColumnNames(lexer, lexer.getAllocator());

            final long referencesKeyword = lexer.lexKeyword(SQLToken.REFERENCES);
            final long referencesTableName = lexer.lexName();
            final SQLColumnNames referencesColumnNames = parseColumnNames(lexer, lexer.getAllocator());

            final SQLOnDeleteCascade onDeleteCascade;

            if (lexer.peek(SQLToken.ON)) {

                onDeleteCascade = new SQLOnDeleteCascade(makeContext(), lexer.getStringRef(), lexer.lexKeyword(SQLToken.DELETE), lexer.lexKeyword(SQLToken.CASCADE));
            }
            else {
                onDeleteCascade = null;
            }

            final long constraintName = lexer.lexName();

            addConstraintOperation = new SQLAddForeignKeyConstraintOperation(makeContext(), addKeyword, constraintKeyword, subCommandKeyword, keyKeyword, columnNames,
                    referencesKeyword, referencesTableName, referencesColumnNames, onDeleteCascade, constraintName);
            break;
        }

        default:
            throw new UnsupportedOperationException();
        }

        return addConstraintOperation;
    }

    private static SQLDropConstraintOperation parseDropConstraint(SQLExpressionLexer lexer, long dropKeyword, long constraintsKeyword) throws ParserException, IOException {

        final boolean hasParenthesis = lexer.lex(SQLToken.LPAREN);

        final LongArray constraintNames = parseNames(lexer, lexer.getAllocator());

        if (hasParenthesis) {

            lexer.lexExpect(SQLToken.RPAREN);
        }

        return new SQLDropConstraintOperation(makeContext(), dropKeyword, constraintsKeyword, constraintNames);
    }

    private static SQLDropColumnOperation parseDropColumn(SQLExpressionLexer lexer, long dropKeyword) throws ParserException, IOException {

        final boolean hasParenthesis = lexer.lex(SQLToken.LPAREN);

        final LongArray names;

        if (hasParenthesis) {

            names = parseNames(lexer, lexer.getAllocator());

            lexer.lexExpect(SQLToken.RPAREN);
        }
        else {
            names = lexer.getAllocator().allocateLongArray(1);

            names.add(lexer.lexName());
        }

        final SQLColumnNames dropColumnNames = new SQLColumnNames(makeContext(), names);

        return new SQLDropColumnOperation(makeContext(), dropKeyword, dropColumnNames);
    }
}
