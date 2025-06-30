package dev.jdata.db.test.unit;

import java.util.function.ToIntFunction;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.database.StringStorer;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.IDatabaseSchema;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.effective.EffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.schemamaps.HeapCompleteSchemaMaps;
import dev.jdata.db.utils.checks.Checks;

public final class SchemaBuilder extends BaseSchemaBuilder<SchemaBuilder> {

    public static SchemaBuilder create(String databaseName, StringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

        return new SchemaBuilder(databaseName, stringStorer, schemaObjectIdAllocator);
    }

    public static SchemaBuilder create(String databaseName, int databaseId, StringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

        return new SchemaBuilder(databaseName, databaseId, stringStorer, schemaObjectIdAllocator);
    }

    private final String databaseName;
    private final int databaseId;

    private SchemaBuilder(String databaseName, StringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {
        this(databaseName, DBConstants.INITIAL_DESCRIPTORABLE, stringStorer, schemaObjectIdAllocator);
    }

    private SchemaBuilder(String databaseName, int databaseId, StringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {
        super(stringStorer, schemaObjectIdAllocator);

        this.databaseName = Checks.isDatabaseName(databaseName);
        this.databaseId = Checks.isDatabaseId(databaseId);
    }

    @FunctionalInterface
    private interface SchemaFactory<T extends IDatabaseSchema> {

        T create(DatabaseId databaseId, DatabaseSchemaVersion version, HeapCompleteSchemaMaps schemaMaps);
    }

    private <T extends IDatabaseSchema> T build(SchemaFactory<T> schemaFactory) {

        final HeapCompleteSchemaMaps schemaMaps = buildCompleteSchemaMaps();

        final DatabaseSchemaVersion schemaVersion = DatabaseSchemaVersion.of(DatabaseSchemaVersion.INITIAL_VERSION);

        final DatabaseId databaseIdObject = new DatabaseId(databaseId, databaseName);

        return schemaFactory.create(databaseIdObject, schemaVersion, schemaMaps);
    }

    public IDatabaseSchema buildCompleteSchema() {

        return build(CompleteDatabaseSchema::of);
    }

    public IEffectiveDatabaseSchema buildEffectiveSchema() {

        return build(EffectiveDatabaseSchema::of);
    }
}
