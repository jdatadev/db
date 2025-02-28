package dev.jdata.db.sql.parse.ddl.table;

import java.io.IOException;
import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.types.StringRef;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;

import dev.jdata.db.schema.types.BigIntType;
import dev.jdata.db.schema.types.CharType;
import dev.jdata.db.schema.types.DateType;
import dev.jdata.db.schema.types.DecimalType;
import dev.jdata.db.schema.types.DoubleType;
import dev.jdata.db.schema.types.FloatType;
import dev.jdata.db.schema.types.IntegerType;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.schema.types.SmallIntType;
import dev.jdata.db.schema.types.VarCharType;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.sql.parse.BaseSQLParser;
import dev.jdata.db.sql.parse.SQLExpressionLexer;
import dev.jdata.db.sql.parse.SQLLexer;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.sql.parse.expression.SQLExpressionParser;

public final class SQLColumnDefinitionParser extends BaseSQLParser {

    private static final SQLToken[] COLUMN_DEFINITION_PEEK_TOKENS = new SQLToken[] {

            SQLToken.NOT,
            SQLToken.COMMA
    };

    private static final SQLToken[] COLUMN_DEFINITION_NOT_NULL_PEEK_TOKENS = new SQLToken[] {

            SQLToken.DEFAULT,
            SQLToken.COMMA
    };

    private final SQLExpressionParser expressionParser;

    public SQLColumnDefinitionParser(SQLExpressionParser expressionParser) {

        this.expressionParser = Objects.requireNonNull(expressionParser);
    }

    public SQLTableColumnDefinition parseTableColumnDefinition(SQLExpressionLexer lexer, SQLToken[] schemaDataTypeTokens) throws ParserException, IOException {

        final long columnName = lexer.lexName();

        final SQLToken schemaTypeToken = lexer.lex(schemaDataTypeTokens);

        if (schemaTypeToken == SQLToken.NONE) {

            throw lexer.unexpectedToken(schemaDataTypeTokens);
        }

        final long schemaTypeName = lexer.getStringRef();

        final SchemaDataType schemaDataType = parseSchemaType(lexer, lexer.getStringResolver(), schemaTypeToken, schemaDataTypeTokens);

        final SQLToken afterDataTypeToken = lexer.peek(COLUMN_DEFINITION_PEEK_TOKENS);

        final long notKeyword;
        final long nullKeyword;

        final long defaultKeyword;
        final Expression defaultExpression;

        switch (afterDataTypeToken) {

        case NOT:

            notKeyword = lexer.getStringRef();
            nullKeyword = lexer.lexKeyword(SQLToken.NULL);

            final SQLToken afterNotNullToken = lexer.peek(COLUMN_DEFINITION_NOT_NULL_PEEK_TOKENS);

            switch (afterNotNullToken) {

            case DEFAULT:

                defaultKeyword = lexer.getStringRef();
                defaultExpression = expressionParser.parseExpression(lexer);
                break;

            default:

                defaultKeyword = StringRef.NO_STRING;
                defaultExpression = null;
                break;
            }
            break;

        default:

            notKeyword = StringRef.NO_STRING;
            nullKeyword = StringRef.NO_STRING;
            defaultKeyword = StringRef.NO_STRING;
            defaultExpression = null;
            break;
        }

        return new SQLTableColumnDefinition(makeContext(), columnName, schemaTypeName, schemaDataType, notKeyword, nullKeyword, defaultKeyword, defaultExpression);
    }

    private static SchemaDataType parseSchemaType(SQLLexer lexer, StringResolver stringResolver, SQLToken schemaTypeToken, SQLToken[] schemaDataTypeTokens)
            throws ParserException, IOException {

        final SchemaDataType result;

        switch (schemaTypeToken) {

        case SMALLINT:

            result = SmallIntType.INSTANCE;
            break;

        case INTEGER:

            result = IntegerType.INSTANCE;
            break;

        case BIGINT:

            result = BigIntType.INSTANCE;
            break;

        case FLOAT:

            result = FloatType.INSTANCE;
            break;

        case DOUBLE:

            result = DoubleType.INSTANCE;
            break;

        case DECIMAL:

            final int precision = parseUnsignedInt(lexer, stringResolver);

            lexer.lexExpect(SQLToken.COMMA);

            final int scale = parseUnsignedInt(lexer, stringResolver);

            result = DecimalType.of(precision, scale);
            break;

        case DATE:

            result = DateType.INSTANCE;
            break;

        case CHAR:

            lexer.lexExpect(SQLToken.LPAREN);

            result = CharType.of(parseUnsignedInt(lexer, stringResolver));

            lexer.lexExpect(SQLToken.RPAREN);
            break;

        case VARCHAR:

            lexer.lexExpect(SQLToken.LPAREN);

            final int length1 = parseUnsignedInt(lexer, stringResolver);

            if (lexer.peek(SQLToken.COMMA)) {

                lexer.lexSkip(SQLToken.COMMA);

                result = VarCharType.of(length1, parseUnsignedInt(lexer, stringResolver));
            }
            else {
                result = VarCharType.of(length1);
            }

            lexer.lexExpect(SQLToken.RPAREN);
            break;

        default:
            throw lexer.unexpectedToken(schemaDataTypeTokens);
        }

        return result;
    }
}
