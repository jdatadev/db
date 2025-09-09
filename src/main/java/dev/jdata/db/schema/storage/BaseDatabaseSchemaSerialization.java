package dev.jdata.db.schema.storage;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import org.jutils.io.buffers.BaseStringBuffers;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;

import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.ddl.helpers.buildschema.DDLCompleteSchemasHelper;
import dev.jdata.db.ddl.helpers.buildschema.IDDLSchemaSQLStatementsWorkerObjects;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMaps;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.schema.types.SchemaDataTypeVisitor;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.SQLParserFactory;
import dev.jdata.db.sql.parse.SQLParserHelper;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListBuilder;

public abstract class BaseDatabaseSchemaSerialization<

                INDEX_LIST extends IndexList<BaseSQLStatement>,
                INDEX_LIST_BUILDER extends IndexListBuilder<BaseSQLStatement, INDEX_LIST, INDEX_LIST_BUILDER>,
                INDEX_LIST_ALLOCATOR extends IndexListAllocator<BaseSQLStatement, INDEX_LIST, INDEX_LIST_BUILDER, ?>,
                COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps>

        implements IDatabaseSchemaSerialization<COMPLETE_SCHEMA_MAPS> {

    private static final SchemaDataTypeOutputter dataTypeOutputter = new SchemaDataTypeOutputter();

    private final ISQLAllocator sqlAllocator;

    private final SQLParserHelper<INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR> sqlParserHelper;
    private final IDDLSchemaSQLStatementsWorkerObjects<COMPLETE_SCHEMA_MAPS, ?> ddlSchemaSQLStatementsWorkerObjects;
    private final DDLSchemaScratchObjects ddlSchemaScratchObjects;

    protected BaseDatabaseSchemaSerialization(SQLParserFactory sqlParserFactory, ISQLAllocator sqlAllocator,
            IDDLSchemaSQLStatementsWorkerObjects<COMPLETE_SCHEMA_MAPS, ?> ddlSchemaSQLStatementsWorkerObjects, DDLSchemaScratchObjects ddlSchemaScratchObjects,
            Function<IntFunction<BaseSQLStatement[]>, INDEX_LIST_ALLOCATOR> createIndexListAllocator) {

        this.sqlAllocator = Objects.requireNonNull(sqlAllocator);
        this.ddlSchemaSQLStatementsWorkerObjects = Objects.requireNonNull(ddlSchemaSQLStatementsWorkerObjects);
        this.ddlSchemaScratchObjects = Objects.requireNonNull(ddlSchemaScratchObjects);

        this.sqlParserHelper = new SQLParserHelper<>(sqlParserFactory.createParser(), createIndexListAllocator);
    }

    @Override
    public final <E extends Exception> void serialize(IEffectiveDatabaseSchema databaseSchema, StringResolver stringResolver, ISQLOutputter<E> sqlOutputter) throws E {

        Objects.requireNonNull(databaseSchema);
        Objects.requireNonNull(stringResolver);
        Objects.requireNonNull(sqlOutputter);

        safeSerializeSchemaObjects(databaseSchema.getTables(), stringResolver, sqlOutputter, BaseDatabaseSchemaSerialization::serializeTable);
    }

    @FunctionalInterface
    private interface SchemaObjectSerializer<T extends SchemaObject, E extends Exception> {

        void serialize(T schemaObject, StringResolver stringResolver, ISQLOutputter<E> sqlOutputter) throws E;
    }

    private <T extends SchemaObject, E extends Exception> void safeSerializeSchemaObjects(IIndexList<T> schemaObjects, StringResolver stringResolver,
            ISQLOutputter<E> sqlOutputter, SchemaObjectSerializer<T, E> schemaObjectSerializer) throws E {

        if (schemaObjects != null) {

            final long numTables =  schemaObjects.getNumElements();

            for (int i = 0; i < numTables; ++ i) {

                schemaObjectSerializer.serialize(schemaObjects.get(i), stringResolver, sqlOutputter);
            }
        }
    }

    private static <E extends Exception> void serializeTable(Table table, StringResolver stringResolver, ISQLOutputter<E> sqlOutputter) throws E {

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

    private static <E extends Exception> void serializeColumn(Column column, StringResolver stringResolver, ISQLOutputter<E> sqlOutputter) throws E {

        sqlOutputter.appendName(column.getParsedName(), stringResolver).appendSeparator();

        serializeType(column.getSchemaType(), stringResolver, sqlOutputter);

        if (!column.isNullable()) {

            sqlOutputter.appendKeyword(SQLToken.NOT).appendSeparator().appendKeyword(SQLToken.NULL);
        }
    }

    protected static <E extends Exception> void serializeType(SchemaDataType schemaDataType, StringResolver stringResolver, ISQLOutputter<E> sqlOutputter) throws E {

        @SuppressWarnings({ "unchecked", "rawtypes" })
        final SchemaDataTypeVisitor<ISQLOutputter<E>, Void, E> outputter = (SchemaDataTypeVisitor)dataTypeOutputter;

        schemaDataType.visit(outputter, sqlOutputter);
    }

    @Override
    public <E extends Exception, BUFFER extends BaseStringBuffers<E>> COMPLETE_SCHEMA_MAPS deserialize(BUFFER buffer, Function<String, E> createEOFException,
            StringManagement stringManagement, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) throws ParserException, E {

        Objects.requireNonNull(buffer);
        Objects.requireNonNull(createEOFException);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(schemaObjectIdAllocator);

        final COMPLETE_SCHEMA_MAPS result;

        final INDEX_LIST sqlStatements = sqlParserHelper.parse(buffer, sqlAllocator, createEOFException);

        try {
            result = DDLCompleteSchemasHelper.createSchemasFromSQLStatements(sqlStatements, stringManagement, ddlSchemaSQLStatementsWorkerObjects, ddlSchemaScratchObjects,
                    schemaObjectIdAllocator);
        }
        finally {

            sqlParserHelper.freeSQLStatements(sqlStatements);
        }

        return result;
    }
}
