package dev.jdata.db.schema.storage;

import java.util.function.BiConsumer;
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
import dev.jdata.db.ddl.helpers.buildschema.HeapDDLSchemaSQLStatementsWorkerObjects;
import dev.jdata.db.engine.database.DatabaseStringManagement;
import dev.jdata.db.engine.database.IStringStorer;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.engine.database.strings.IStringCache;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IHeapEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
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
import dev.jdata.db.utils.adt.lists.ICachedIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.allocators.AddableListAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.CharacterBuffersAllocator;
import dev.jdata.db.utils.allocators.IAddableListAllocator;

public abstract class BaseDatabaseSchemaSerializerTest<T extends BaseDatabaseSchemaSerialization<?, ?, ?, IHeapAllCompleteSchemaMaps>> extends BaseDBTest {

    protected abstract SQLParserFactory getSQLParserFactory();
    protected abstract T createDatabaseSchemaSerializer(ISQLAllocator sqlAllocator, HeapDDLSchemaSQLStatementsWorkerObjects ddlSchemaSQLStatementsWorkerObjects);

    @Test
    @Category(UnitTest.class)
    public final void testSerializeSchemaObjectsSameStringStorer() throws ParserException {

        final IStringStorer builderStringStorer = createStringStorer();
        final IStringStorer deserializeStringStorer = builderStringStorer;

        checkSerializeSchemaObjects(builderStringStorer, deserializeStringStorer, (a, e) -> {

            assertThat(a).isNotSameAs(e);
            assertThat(a).isEqualTo(e);
        });
    }

    @Test
    @Category(UnitTest.class)
    public final void testSerializeSchemaObjectsDifferentStringStorer() throws ParserException {

        final IStringStorer builderStringStorer = createStringStorer();
        final IStringStorer deserializeStringStorer = createStringStorer();

        checkSerializeSchemaObjects(builderStringStorer, deserializeStringStorer, (a, e) -> {

            assertThat(a).isNotSameAs(e);
            assertThat(a.isEqualTo(builderStringStorer, e, deserializeStringStorer)).isTrue();
        });
    }

    private void checkSerializeSchemaObjects(IStringStorer builderStringStorer, IStringStorer deserializeStringStorer,
            BiConsumer<IEffectiveDatabaseSchema, IEffectiveDatabaseSchema> compareDatabaseSchemas) throws ParserException {

        final SQLParserFactory sqlParserFactory = getSQLParserFactory();
        final ISQLAllocator sqlAllocator = createSQLAllocator();

        final T databaseSchemaSerializer = createDatabaseSchemaSerializer(sqlAllocator, createDDLSchemaSQLStatementsWorkerObjects());

        final ToIntFunction<DDLObjectType> schemaObjectIdAllocator = t -> DBConstants.INITIAL_SCHEMA_OBJECT_ID;

        final IEffectiveDatabaseSchema effectiveDatabaseSchema = createTestEffectiveDatabaseSchema(getTestDatabaseId(), deserializeStringStorer, schemaObjectIdAllocator);

        final String serializedSQL = serialize(effectiveDatabaseSchema, builderStringStorer, databaseSchemaSerializer);

        checkParse(effectiveDatabaseSchema, builderStringStorer, serializedSQL, sqlParserFactory, sqlAllocator);

        checkDeserialize(effectiveDatabaseSchema, serializedSQL, databaseSchemaSerializer, deserializeStringStorer, schemaObjectIdAllocator, compareDatabaseSchemas);
    }

    private static String serialize(IEffectiveDatabaseSchema effectiveDatabaseSchema, IStringStorer stringStorer,
            BaseDatabaseSchemaSerialization<?, ?, ?, IHeapAllCompleteSchemaMaps> databaseSchemaSerializer) {

        final StringBuilderSQLOutputter sqlOutputter = new StringBuilderSQLOutputter();

        final ICharactersBufferAllocator charactersBufferAllocator = new CharacterBuffersAllocator();
        final StringBuilder sb = new StringBuilder();

        sqlOutputter.initialize(charactersBufferAllocator, sb);

        databaseSchemaSerializer.serialize(effectiveDatabaseSchema, stringStorer, sqlOutputter);

        return sb.toString();
    }

    private static void checkParse(IEffectiveDatabaseSchema effectiveDatabaseSchema, StringResolver stringResolver, String serializedSQL, SQLParserFactory sqlParserFactory,
            ISQLAllocator sqlAllocator) throws ParserException {

        final SQLParser sqlParser = sqlParserFactory.createParser();

        final LoadStream<RuntimeException> loadStream = new StringLoadStream(serializedSQL);
        final LoadStreamStringBuffers<RuntimeException> stringBuffers = new LoadStreamStringBuffers<>(loadStream);

        final SQLScratchExpressionValues sqlScratchExpressionValues = new SQLScratchExpressionValues(AllocationType.HEAP);
        final IAddableListAllocator addableListAllocator = new AddableListAllocator();
        final ICachedIndexListAllocator<BaseSQLStatement> indexListAllocator = ICachedIndexListAllocator.create(BaseSQLStatement[]::new);

        final IIndexList<BaseSQLStatement> sqlStatements = SQLParserHelper.parse(sqlParser, stringBuffers, RuntimeException::new, sqlScratchExpressionValues, sqlAllocator,
                addableListAllocator, indexListAllocator);

        assertThat(sqlStatements).hasNumElements(1L);

        final SQLCreateTableStatement createTableStatement = (SQLCreateTableStatement)sqlStatements.getHead();

        checkTable(createTableStatement, stringBuffers, effectiveDatabaseSchema.getTables().getHead(), stringResolver);
    }

    private static void checkTable(SQLCreateTableStatement createTableStatement, StringResolver createTableResolver, Table table, StringResolver tableResolver) {

        assertThat(createTableResolver.asString(createTableStatement.getName())).isEqualTo(tableResolver.asString(table.getParsedName()));

        final ASTList<SQLTableColumnDefinition> columnDefinitions = createTableStatement.getColumns();

        columnDefinitions.forEachWithIndexAndParameter(null, (e, i, p) -> {

            final Column column = table.getColumn(i);

            assertThat(createTableResolver.asString(e.getName())).isEqualTo(tableResolver.asString(column.getParsedName()));
            assertThat(e.getType()).isEqualTo(column.getSchemaType());
            assertThat(column.isNullable()).isTrue();
        });
    }

    private static <T extends BaseDatabaseSchemaSerialization<?, ?, ?, IHeapAllCompleteSchemaMaps>> void checkDeserialize(IEffectiveDatabaseSchema effectiveDatabaseSchema,
            String serializedSQL, T databaseSchemaSerializer, IStringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator,
            BiConsumer<IEffectiveDatabaseSchema, IEffectiveDatabaseSchema> compareDatabaseSchemas) throws ParserException {

        final IEffectiveDatabaseSchema deserializedDatabaseSchema = deserialize(effectiveDatabaseSchema, serializedSQL, databaseSchemaSerializer, stringStorer,
                schemaObjectIdAllocator);

        compareDatabaseSchemas.accept(effectiveDatabaseSchema, deserializedDatabaseSchema);
    }

    private static <T extends BaseDatabaseSchemaSerialization<?, ?, ?, IHeapAllCompleteSchemaMaps>> IEffectiveDatabaseSchema deserialize(
            IEffectiveDatabaseSchema effectiveDatabaseSchema, String serializedSQL, T databaseSchemaSerializer, IStringStorer stringStorer,
            ToIntFunction<DDLObjectType> schemaObjectIdAllocator) throws ParserException {

        final LoadStream<RuntimeException> loadStream = new StringLoadStream(serializedSQL);
        final LoadStreamStringBuffers<RuntimeException> stringBuffers = new LoadStreamStringBuffers<>(loadStream);

        final CharacterBuffersAllocator characterBuffersAllocator = new CharacterBuffersAllocator();
        final DatabaseStringManagement databaseStringManagement = new DatabaseStringManagement(characterBuffersAllocator, stringStorer);
        final IStringCache stringCache = IStringCache.create(0, 0);
        final StringManagement stringManagement = new StringManagement(AllocationType.HEAP);

        stringManagement.initialize(databaseStringManagement, stringBuffers, stringCache);

        final IHeapAllCompleteSchemaMaps completeSchemaMaps = databaseSchemaSerializer.deserialize(stringBuffers, RuntimeException::new, stringManagement, schemaObjectIdAllocator);

        final IEffectiveDatabaseSchema deserializedDatabaseSchema = IHeapEffectiveDatabaseSchema.of(effectiveDatabaseSchema.getDatabaseId(), effectiveDatabaseSchema.getVersion(),
                completeSchemaMaps);

        return deserializedDatabaseSchema;
    }
}
