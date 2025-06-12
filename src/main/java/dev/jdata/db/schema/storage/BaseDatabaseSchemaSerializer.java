package dev.jdata.db.schema.storage;

import java.util.Objects;
import java.util.function.Function;

import org.jutils.io.buffers.BaseStringBuffers;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;

import dev.jdata.db.ddl.DDLCompleteSchemasHelper;
import dev.jdata.db.ddl.DDLCompleteSchemasHelper.DDLCompleteSchemaCachedObjects;
import dev.jdata.db.ddl.DDLSchemasHelper.SchemaObjectIdAllocator;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
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
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.schema.types.SchemaDataTypeVisitor;
import dev.jdata.db.schema.types.SmallIntType;
import dev.jdata.db.schema.types.TextObjectType;
import dev.jdata.db.schema.types.TimeType;
import dev.jdata.db.schema.types.TimestampType;
import dev.jdata.db.schema.types.VarCharType;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.SQLParserFactory;
import dev.jdata.db.sql.parse.SQLParserHelper;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.utils.adt.lists.IIndexList;

public abstract class BaseDatabaseSchemaSerializer implements IDatabaseSchemaSerializer {

    private final ISQLAllocator sqlAllocator;

    private final SQLParserHelper sqlParserHelper;
    private final DDLCompleteSchemaCachedObjects ddlCompleteSchemaCachedObjects;

    protected BaseDatabaseSchemaSerializer(SQLParserFactory sqlParserFactory, ISQLAllocator sqlAllocator, DDLCompleteSchemaCachedObjects ddlCompleteSchemaCachedObjects) {

        this.sqlAllocator = Objects.requireNonNull(sqlAllocator);
        this.ddlCompleteSchemaCachedObjects = Objects.requireNonNull(ddlCompleteSchemaCachedObjects);

        this.sqlParserHelper = new SQLParserHelper(sqlParserFactory.createParser());
    }

    @Override
    public final <E extends Exception> void serialize(IEffectiveDatabaseSchema databaseSchema, StringResolver stringResolver, ISQLOutputter<E> sqlOutputter) throws E {

        Objects.requireNonNull(databaseSchema);
        Objects.requireNonNull(stringResolver);
        Objects.requireNonNull(sqlOutputter);

        serializeTables(databaseSchema.getTables(), stringResolver, sqlOutputter);
    }

    private <E extends Exception> void serializeTables(IIndexList<Table> tables, StringResolver stringResolver, ISQLOutputter<E> sqlOutputter) throws E {

        final long numTables =  tables.getNumElements();

        for (int i = 0; i < numTables; ++ i) {

            serializeTable(tables.get(i), stringResolver, sqlOutputter);
        }
    }

    private <E extends Exception> void serializeTable(Table table, StringResolver stringResolver, ISQLOutputter<E> sqlOutputter) throws E {

        sqlOutputter.appendKeyword(SQLToken.CREATE).appendSeparator().appendKeyword(SQLToken.TABLE);

        sqlOutputter.appendSeparator().appendName(table.getParsedName(), stringResolver);

        sqlOutputter.appendSeparator().appendKeyword(SQLToken.LPAREN);

        final int numColumns = table.getNumColumns();

        for (int i = 0; i < numColumns; ++ i) {

            if (i > 0) {

                sqlOutputter.appendKeyword(SQLToken.COMMA).appendSeparator();
            }

            serializeColumn(table.getColumn(i), stringResolver, sqlOutputter);
        }

        sqlOutputter.appendKeyword(SQLToken.RPAREN);
    }

    private <E extends Exception> void serializeColumn(Column column, StringResolver stringResolver, ISQLOutputter<E> sqlOutputter) throws E {

        sqlOutputter.appendName(column.getParsedName(), stringResolver).appendSeparator();

        serializeType(column.getSchemaType(), stringResolver, sqlOutputter);

        if (!column.isNullable()) {

            sqlOutputter.appendKeyword(SQLToken.NOT).appendSeparator().appendKeyword(SQLToken.NULL);
        }
    }

    private static final SchemaDataTypeVisitor<ISQLOutputter<? extends Exception>, Void, Exception> dataTypeOutputter
            = new SchemaDataTypeVisitor<ISQLOutputter<? extends Exception>, Void, Exception>() {

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
    };

    protected <E extends Exception> void serializeType(SchemaDataType schemaDataType, StringResolver stringResolver, ISQLOutputter<E> sqlOutputter) throws E {

        @SuppressWarnings({ "unchecked", "rawtypes" })
        final SchemaDataTypeVisitor<ISQLOutputter<E>, Void, E> outputter = (SchemaDataTypeVisitor)dataTypeOutputter;

        schemaDataType.visit(outputter, sqlOutputter);
    }

    @Override
    public <E extends Exception, BUFFER extends BaseStringBuffers<E>, P> CompleteSchemaMaps deserialize(BUFFER buffer, Function<String, E> createEOFException,
            StringManagement stringManagement, SchemaObjectIdAllocator<P> schemaObjectIdAllocator) throws ParserException, E {

        Objects.requireNonNull(buffer);
        Objects.requireNonNull(createEOFException);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(schemaObjectIdAllocator);

        final CompleteSchemaMaps result;

        final IIndexList<BaseSQLStatement> sqlStatements = sqlParserHelper.parse(buffer, sqlAllocator, createEOFException);

        try {
            result = DDLCompleteSchemasHelper.processSQLStatements(sqlStatements, stringManagement, ddlCompleteSchemaCachedObjects, schemaObjectIdAllocator);
        }
        finally {

            sqlParserHelper.freeSQLStatements(sqlStatements);
        }

        return result;
    }
}
