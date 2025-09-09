package dev.jdata.db.schema.storage;

import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;
import dev.jdata.db.schema.types.BigIntType;
import dev.jdata.db.schema.types.BlobType;
import dev.jdata.db.schema.types.BooleanType;
import dev.jdata.db.schema.types.CharType;
import dev.jdata.db.schema.types.DateType;
import dev.jdata.db.schema.types.DecimalType;
import dev.jdata.db.schema.types.DoubleType;
import dev.jdata.db.schema.types.FloatType;
import dev.jdata.db.schema.types.IntegerType;
import dev.jdata.db.schema.types.SchemaCustomType;
import dev.jdata.db.schema.types.SchemaDataTypeVisitor;
import dev.jdata.db.schema.types.SmallIntType;
import dev.jdata.db.schema.types.TextObjectType;
import dev.jdata.db.schema.types.TimeType;
import dev.jdata.db.schema.types.TimestampType;
import dev.jdata.db.schema.types.VarCharType;
import dev.jdata.db.sql.parse.SQLToken;

final class SchemaDataTypeOutputter implements SchemaDataTypeVisitor<ISQLOutputter<? extends Exception>, Void, Exception> {

    @Override
    public Void onBooleanType(BooleanType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.BOOLEAN);

        return null;
    }

    @Override
    public Void onSmallIntType(SmallIntType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.SMALLINT);

        return null;
    }

    @Override
    public Void onIntegerType(IntegerType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.INTEGER);

        return null;
    }

    @Override
    public Void onBigIntType(BigIntType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.BIGINT);

        return null;
    }

    @Override
    public Void onFloatType(FloatType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.FLOAT);

        return null;
    }

    @Override
    public Void onDoubleType(DoubleType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.DOUBLE);

        return null;
    }

    @Override
    public Void onDecimalType(DecimalType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.DECIMAL);

        parameter.appendKeyword(SQLToken.LPAREN);
        parameter.appendIntegerLiteral(schemaDataType.getPrecision());
        parameter.appendKeyword(SQLToken.COMMA).appendSeparator();
        parameter.appendIntegerLiteral(schemaDataType.getScale());
        parameter.appendKeyword(SQLToken.RPAREN);

        return null;
    }

    @Override
    public Void onCharType(CharType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.CHAR);

        parameter.appendKeyword(SQLToken.LPAREN);
        parameter.appendIntegerLiteral(schemaDataType.getLength());
        parameter.appendKeyword(SQLToken.RPAREN);

        return null;
    }

    @Override
    public Void onVarCharType(VarCharType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.VARCHAR);

        parameter.appendKeyword(SQLToken.LPAREN);

        final int minLength = schemaDataType.getMinLength();

        if (minLength >= 0) {

            parameter.appendIntegerLiteral(minLength);

            parameter.appendKeyword(SQLToken.COMMA).appendSeparator();
        }

        parameter.appendIntegerLiteral(schemaDataType.getMaxLength());

        parameter.appendKeyword(SQLToken.RPAREN);

        return null;
    }

    @Override
    public Void onDateType(DateType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.DATE);

        return null;
    }

    @Override
    public Void onTimeType(TimeType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.TIME);

        return null;
    }

    @Override
    public Void onTimestampType(TimestampType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.TIMESTAMP);

        return null;
    }

    @Override
    public Void onBlobType(BlobType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.BLOB);

        return null;
    }

    @Override
    public Void onTextObjectType(TextObjectType schemaDataType, ISQLOutputter<? extends Exception> parameter) throws Exception {

        parameter.appendKeyword(SQLToken.TEXT);

        return null;
    }

    @Override
    public Void onCustomType(SchemaCustomType schemaDataType, ISQLOutputter<? extends Exception> parameter) {

        throw new UnsupportedOperationException();
    }
}
