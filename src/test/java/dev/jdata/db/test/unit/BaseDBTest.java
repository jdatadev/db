package dev.jdata.db.test.unit;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Objects;
import java.util.function.ToIntFunction;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DBConstants;
import dev.jdata.db.custom.ansi.schema.storage.HeapANSIDatabaseSchemaSerialization;
import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.ddl.helpers.SchemaObjectIdAllocators;
import dev.jdata.db.ddl.helpers.buildschema.HeapDDLSchemaSQLStatementsWorkerObjects;
import dev.jdata.db.engine.database.BucketsStringCache;
import dev.jdata.db.engine.database.DatabaseStringManagement;
import dev.jdata.db.engine.database.IStringCache;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.engine.database.StringStorer;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaManager;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.allocators.SchemaManagementAllocators;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.HeapCompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.storage.sqloutputter.StringBuilderSQLOutputter;
import dev.jdata.db.schema.storage.sqloutputter.TextSQLOutputter;
import dev.jdata.db.schema.storage.sqloutputter.TextToByteOutputPrerequisites;
import dev.jdata.db.schema.types.BigIntType;
import dev.jdata.db.schema.types.BlobType;
import dev.jdata.db.schema.types.BooleanType;
import dev.jdata.db.schema.types.CharType;
import dev.jdata.db.schema.types.DateType;
import dev.jdata.db.schema.types.DecimalType;
import dev.jdata.db.schema.types.DoubleType;
import dev.jdata.db.schema.types.FloatType;
import dev.jdata.db.schema.types.IntegerType;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.schema.types.SmallIntType;
import dev.jdata.db.schema.types.TextObjectType;
import dev.jdata.db.schema.types.TimeType;
import dev.jdata.db.schema.types.TimestampType;
import dev.jdata.db.schema.types.VarCharType;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.allocators.ByteArrayByteBufferAllocator;
import dev.jdata.db.utils.allocators.CharBufferAllocator;
import dev.jdata.db.utils.allocators.CharacterBuffersAllocator;
import dev.jdata.db.utils.allocators.MutableIntMaxDistanceNonBucketSetAllocator;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseDBTest extends BaseSQLTest {

    protected static final long TEST_TRANSACTION_ID = 0L;

    protected static final String TEST_DATABASE_NAME = "testdb";
    protected static final DatabaseId TEST_DATABASE_ID = new DatabaseId(DBConstants.INITIAL_DESCRIPTORABLE, TEST_DATABASE_NAME);

    protected static final DatabaseSchemaVersion TEST_DATABASE_SCHEMA_VERSION = DatabaseSchemaVersion.of(DatabaseSchemaVersion.INITIAL_VERSION);

    private static final String TEST_TABLE_NAME = "testtable";

    private static IIndexList<SchemaDataType> schemaDataTypes = IndexList.of(

            BooleanType.INSTANCE,
            SmallIntType.INSTANCE,
            IntegerType.INSTANCE,
            BigIntType.INSTANCE,
            FloatType.INSTANCE,
            DoubleType.INSTANCE,
            DecimalType.of(1, 2),
            CharType.of(3),
            VarCharType.of(4, 5),
            DateType.INSTANCE,
            TimeType.INSTANCE,
            TimestampType.INSTANCE,
            BlobType.INSTANCE,
            TextObjectType.INSTANCE);

    protected static Table createTestTable(int tableId, StringStorer stringStorer) {

        Checks.isTableId(tableId);
        Objects.requireNonNull(stringStorer);

        return createTestTable(tableId, TEST_TABLE_NAME, stringStorer);
    }

    protected static Table createTestTable(int tableId, String tableName, StringStorer stringStorer) {

        Checks.isTableId(tableId);
        Checks.isTableName(tableName);
        Objects.requireNonNull(stringStorer);

        final TableBuilder tableBuilder = TableBuilder.create(tableName, tableId, stringStorer);

        return createTestTable(tableBuilder).build();
    }

    protected static TableBuilder createTestTable(TableBuilder tableBuilder) {

        Objects.requireNonNull(tableBuilder);

        schemaDataTypes.forEach(tableBuilder, (t, b) -> b.addColumn(t.getClass().getSimpleName().toLowerCase() + "_column", t));

        return tableBuilder;
    }

    protected static StringStorer createStringStorer() {

        return new StringStorer(1, 0);
    }

    protected static DatabaseId getTestDatabaseId() {

        return TEST_DATABASE_ID;
    }

    protected static IEffectiveDatabaseSchema createEffectiveDatabaseSchema(DatabaseId databaseId, StringStorer stringStorer,
            ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(stringStorer);
        Objects.requireNonNull(schemaObjectIdAllocator);

        final String tableName = TEST_TABLE_NAME;

        return SchemaBuilder.create(databaseId, stringStorer, schemaObjectIdAllocator)
                .addTable(tableName, b -> createTestTable(b))
                .buildEffectiveSchema();
    }

    protected static StringManagement createStringManagement(DatabaseStringManagement databaseStringManagement, StringResolver parserStringResolver) {

        Objects.requireNonNull(databaseStringManagement);
        Objects.requireNonNull(parserStringResolver);

        return createStringManagement(databaseStringManagement, parserStringResolver, createStringCache());
    }

    private static StringManagement createStringManagement(DatabaseStringManagement databaseStringManagement, StringResolver parserStringResolver, IStringCache stringCache) {

        Objects.requireNonNull(databaseStringManagement);
        Objects.requireNonNull(parserStringResolver);
        Objects.requireNonNull(stringCache);

        final StringManagement stringManagement = new StringManagement();

        stringManagement.initialize(databaseStringManagement, parserStringResolver, stringCache);

        return stringManagement;
    }

    protected static DatabaseStringManagement createDatabaseStringManagement(StringStorer stringStorer) {

        Objects.requireNonNull(stringStorer);

        final CharacterBuffersAllocator characterBuffersAllocator = new CharacterBuffersAllocator();

        return new DatabaseStringManagement(characterBuffersAllocator, stringStorer);
    }

    protected static IStringCache createStringCache() {

        return new BucketsStringCache(0, 3);
    }

    protected static DatabaseSchemaManager createDatabaseSchemaManager(DatabaseId databaseId) {

        Objects.requireNonNull(databaseId);

        final DatabaseSchemaVersion schemaVersion = DatabaseSchemaVersion.of(DatabaseSchemaVersion.INITIAL_VERSION);

        final MutableIntMaxDistanceNonBucketSetAllocator intSetAllocator = new MutableIntMaxDistanceNonBucketSetAllocator();

        final SchemaManagementAllocators schemaManagementAllocators = new SchemaManagementAllocators(intSetAllocator);

        final CompleteDatabaseSchema databaseSchema = CompleteDatabaseSchema.empty(databaseId, schemaVersion);

        return DatabaseSchemaManager.of(databaseId, databaseSchema, schemaManagementAllocators.getSchemaManagerAllocator());
    }

    protected static HeapANSIDatabaseSchemaSerialization createANSIDatabaseSchemaSerializer() {

        return new HeapANSIDatabaseSchemaSerialization(createSQLAllocator(), createDDLSchemaSQLStatementsWorkerObjects(), createDDLSchemaScratchObjects());
    }

    protected static final class TestSQLOutputter extends TextSQLOutputter<StringBuilder, IOException> {

        private final StringBuilder sb;

        public TestSQLOutputter(StringBuilder sb) {

            this.sb = Objects.requireNonNull(sb);

            initialize(new CharacterBuffersAllocator(), sb, (c, b) -> b.append(c));
        }

        @Override
        public void reset() {

            sb.setLength(0);

            super.reset();
        }
    }

    protected static TestSQLOutputter createSQLOutputterWitIOException(StringBuilder sb) {

        Objects.requireNonNull(sb);

        return new TestSQLOutputter(sb);
    }

    private static StringBuilderSQLOutputter createStringBuilderSQLOutputter(StringBuilder sb) {

        Objects.requireNonNull(sb);

        final StringBuilderSQLOutputter result = new StringBuilderSQLOutputter();

        result.initialize(new CharacterBuffersAllocator(), sb);

        return result;
    }

    protected static TextToByteOutputPrerequisites createTextToByteOutputPrerequisites() {

        final CharsetEncoder charsetEncoder = Charset.defaultCharset().newEncoder();
        final CharBufferAllocator charBufferAllocator = new CharBufferAllocator();
        final ByteArrayByteBufferAllocator byteBufferAllocator = new ByteArrayByteBufferAllocator();

        return new TextToByteOutputPrerequisites(charsetEncoder, charBufferAllocator, byteBufferAllocator);
    }

    protected static ICompleteSchemaMapsBuilder<SchemaObject, ?, HeapAllCompleteSchemaMaps> createCompleteSchemaMapsBuilder() {

        return new HeapCompleteSchemaMapsBuilder();
    }

    protected static HeapDDLSchemaSQLStatementsWorkerObjects createDDLSchemaSQLStatementsWorkerObjects() {

        return new HeapDDLSchemaSQLStatementsWorkerObjects();
    }

    protected static DDLSchemaScratchObjects createDDLSchemaScratchObjects() {

        return new DDLSchemaScratchObjects();
    }

    protected static ToIntFunction<DDLObjectType> createSchemaObjectIdAllocators() {

        final SchemaObjectIdAllocators schemaObjectIdAllocators = SchemaObjectIdAllocators.ofInitial();

        return schemaObjectIdAllocators::allocate;
    }
}
