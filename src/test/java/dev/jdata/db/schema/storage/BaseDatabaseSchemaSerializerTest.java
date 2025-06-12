package dev.jdata.db.schema.storage;

import java.util.function.BiConsumer;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.io.buffers.LoadStreamStringBuffers;
import org.jutils.io.loadstream.LoadStream;
import org.jutils.io.loadstream.StringLoadStream;
import org.jutils.io.strings.StringResolver;
import org.jutils.io.strings.StringResolver.ICharactersBufferAllocator;
import org.jutils.parse.ParserException;

import dev.jdata.db.DBConstants;
import dev.jdata.db.ddl.DDLCompleteSchemasHelper.DDLCompleteSchemaCachedObjects;
import dev.jdata.db.ddl.DDLSchemasHelper.SchemaObjectIdAllocator;
import dev.jdata.db.engine.database.DatabaseStringManagement;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.engine.database.StringStorer;
import dev.jdata.db.engine.server.SQLAllocator;
import dev.jdata.db.schema.model.SchemaMap.HeapSchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.effective.EffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.storage.sqloutputter.StringBuilderSQLOutputter;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.sql.parse.SQLParser;
import dev.jdata.db.sql.parse.SQLParserFactory;
import dev.jdata.db.sql.parse.SQLParserHelper;
import dev.jdata.db.sql.parse.expression.SQLScratchExpressionValues;
import dev.jdata.db.test.unit.BaseDBTest;
import dev.jdata.db.test.unit.SchemaBuilder;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList.CacheIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList.HeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.allocators.AddableListAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.CharacterBuffersAllocator;
import dev.jdata.db.utils.allocators.IAddableListAllocator;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;
import dev.jdata.db.utils.allocators.LongToObjectMaxDistanceMapAllocator;

public abstract class BaseDatabaseSchemaSerializerTest<T extends BaseDatabaseSchemaSerializer> extends BaseDBTest {

    protected abstract SQLParserFactory getSQLParserFactory();
    protected abstract T createDatabaseSchemaSerializer(ISQLAllocator sqlAllocator, DDLCompleteSchemaCachedObjects ddlCompleteSchemaCachedObjects);

    @Test
    @Category(UnitTest.class)
    public final void testSerializeSchemaObjectsSameStringStorer() throws ParserException {

        final StringStorer builderStringStorer = createStringStorer();
        final StringStorer deserializeStringStorer = builderStringStorer;

        checkSerializeSchemaObjects(builderStringStorer, deserializeStringStorer, (a, e) -> {

            assertThat(a).isNotSameAs(e);
            assertThat(a).isEqualTo(e);
        });
    }

    @Test
    @Category(UnitTest.class)
    public final void testSerializeSchemaObjectsDifferentStringStorer() throws ParserException {

        final StringStorer builderStringStorer = createStringStorer();
        final StringStorer deserializeStringStorer = createStringStorer();

        checkSerializeSchemaObjects(builderStringStorer, deserializeStringStorer, (a, e) -> {

            assertThat(a).isNotSameAs(e);
            assertThat(a.isEqualTo(builderStringStorer, e, deserializeStringStorer)).isTrue();
        });
    }

    private void checkSerializeSchemaObjects(StringStorer builderStringStorer, StringStorer deserializeStringStorer,
            BiConsumer<IEffectiveDatabaseSchema, IEffectiveDatabaseSchema> compareDatabaseSchemas) throws ParserException {

        final SQLParserFactory sqlParserFactory = getSQLParserFactory();
        final ISQLAllocator sqlAllocator = new SQLAllocator();

        final SchemaMapBuilderAllocators schemaMapBuilderAllocators = new SchemaMapBuilderAllocators(t -> createSchemaMapBuilderAllocator(t.getCreateArray()));

        final DDLCompleteSchemaCachedObjects ddlCompleteSchemaCachedObjects = new DDLCompleteSchemaCachedObjects(schemaMapBuilderAllocators);

        final T databaseSchemaSerializer = createDatabaseSchemaSerializer(sqlAllocator, ddlCompleteSchemaCachedObjects);

        final ToIntFunction<DDLObjectType> schemaObjectIdAllocator = t -> DBConstants.INITIAL_SCHEMA_OBJECT_ID;

        final IEffectiveDatabaseSchema effectiveDatabaseSchema = SchemaBuilder.create("testdb", builderStringStorer, schemaObjectIdAllocator)
                .addTable("testtable", b -> createTestTable(b))
                .buildEffectiveSchema();

        final String serializedSQL = serialize(effectiveDatabaseSchema, builderStringStorer, databaseSchemaSerializer);

        checkParse(effectiveDatabaseSchema, builderStringStorer, serializedSQL, sqlParserFactory, sqlAllocator);

        checkDeserialize(effectiveDatabaseSchema, serializedSQL, databaseSchemaSerializer, deserializeStringStorer, schemaObjectIdAllocator, compareDatabaseSchemas);
    }

    private static String serialize(IEffectiveDatabaseSchema effectiveDatabaseSchema, StringStorer stringStorer, BaseDatabaseSchemaSerializer databaseSchemaSerializer) {

        final StringBuilderSQLOutputter sqlOutputter = new StringBuilderSQLOutputter();

        final ICharactersBufferAllocator charactersBufferAllocator = new CharacterBuffersAllocator();
        final StringBuilder sb = new StringBuilder();

        sqlOutputter.initialize(charactersBufferAllocator, sb);

        databaseSchemaSerializer.serialize(effectiveDatabaseSchema, stringStorer, sqlOutputter);

        return sb.toString();
    }

    private static <T extends SchemaObject> SchemaMapBuilderAllocator<T> createSchemaMapBuilderAllocator(IntFunction<T[]> createArray) {

        final IndexListAllocator<T> indexListAllocator = new HeapIndexListAllocator<>(createArray);
        final ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator = new LongToObjectMaxDistanceMapAllocator<>(createArray);

        return new HeapSchemaMapBuilderAllocator<>(createArray, indexListAllocator, longToObjectMapAllocator);
    }

    private static void checkParse(IEffectiveDatabaseSchema effectiveDatabaseSchema, StringResolver stringResolver, String serializedSQL, SQLParserFactory sqlParserFactory,
            ISQLAllocator sqlAllocator) throws ParserException {

        final SQLParser sqlParser = sqlParserFactory.createParser();

        final LoadStream<RuntimeException> loadStream = new StringLoadStream(serializedSQL);
        final LoadStreamStringBuffers<RuntimeException> stringBuffers = new LoadStreamStringBuffers<>(loadStream);

        final SQLScratchExpressionValues sqlScratchExpressionValues = new SQLScratchExpressionValues();
        final IAddableListAllocator addableListAllocator = new AddableListAllocator();
        final IndexListAllocator<BaseSQLStatement> indexListAllocator = new CacheIndexListAllocator<BaseSQLStatement>(BaseSQLStatement[]::new);

        final IIndexList<BaseSQLStatement> sqlStatements = SQLParserHelper.parse(sqlParser, stringBuffers, RuntimeException::new, sqlScratchExpressionValues, sqlAllocator,
                addableListAllocator, indexListAllocator);

        assertThat(sqlStatements).hasNumElements(1L);

        final SQLCreateTableStatement createTableStatement = (SQLCreateTableStatement)sqlStatements.getHead();

        checkTable(createTableStatement, stringBuffers, effectiveDatabaseSchema.getTables().getHead(), stringResolver);
    }

    private static void checkTable(SQLCreateTableStatement createTableStatement, StringResolver createTableResolver, Table table, StringResolver tableResolver) {

        assertThat(createTableResolver.asString(createTableStatement.getName())).isEqualTo(tableResolver.asString(table.getParsedName()));

        final ASTList<SQLTableColumnDefinition> columnDefinitions = createTableStatement.getColumns();

        columnDefinitions.foreachWithIndexAndParameter(null, (e, i, p) -> {

            final Column column = table.getColumn(i);

            assertThat(createTableResolver.asString(e.getName())).isEqualTo(tableResolver.asString(column.getParsedName()));
            assertThat(e.getType()).isEqualTo(column.getSchemaType());
            assertThat(column.isNullable()).isTrue();
        });
    }

    private static <T extends BaseDatabaseSchemaSerializer> void checkDeserialize(IEffectiveDatabaseSchema effectiveDatabaseSchema, String serializedSQL,
            T databaseSchemaSerializer, StringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator,
            BiConsumer<IEffectiveDatabaseSchema, IEffectiveDatabaseSchema> compareDatabaseSchemas) throws ParserException {

        final EffectiveDatabaseSchema deserializedDatabaseSchema = deserialize(effectiveDatabaseSchema, serializedSQL, databaseSchemaSerializer, stringStorer,
                schemaObjectIdAllocator);

        compareDatabaseSchemas.accept(effectiveDatabaseSchema, deserializedDatabaseSchema);
    }

    private static <T extends BaseDatabaseSchemaSerializer> EffectiveDatabaseSchema deserialize(IEffectiveDatabaseSchema effectiveDatabaseSchema, String serializedSQL,
            T databaseSchemaSerializer, StringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) throws ParserException {

        final LoadStream<RuntimeException> loadStream = new StringLoadStream(serializedSQL);
        final LoadStreamStringBuffers<RuntimeException> stringBuffers = new LoadStreamStringBuffers<>(loadStream);

        final CharacterBuffersAllocator characterBuffersAllocator = new CharacterBuffersAllocator();
        final DatabaseStringManagement databaseStringManagement = new DatabaseStringManagement(characterBuffersAllocator, stringStorer);
        final StringManagement stringManagement = new StringManagement();

        stringManagement.initialize(databaseStringManagement, stringBuffers);

        final SchemaObjectIdAllocator<Void> schemaObjectIdAllocatorObject = new SchemaObjectIdAllocator<>(AllocationType.HEAP, null,
                (t, p) -> schemaObjectIdAllocator.applyAsInt(t));

        final CompleteSchemaMaps completeSchemaMaps = databaseSchemaSerializer.deserialize(stringBuffers, RuntimeException::new, stringManagement,
                schemaObjectIdAllocatorObject);

        final EffectiveDatabaseSchema deserializedDatabaseSchema = EffectiveDatabaseSchema.of(effectiveDatabaseSchema.getDatabaseId(), effectiveDatabaseSchema.getVersion(),
                completeSchemaMaps);

        return deserializedDatabaseSchema;
    }
}
