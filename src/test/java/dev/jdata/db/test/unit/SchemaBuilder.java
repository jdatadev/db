package dev.jdata.db.test.unit;

import java.util.Objects;
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
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMaps;

public final class SchemaBuilder extends BaseSchemaBuilder<SchemaBuilder> {

    public static SchemaBuilder create(String databaseName, StringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

        return new SchemaBuilder(databaseName, stringStorer, schemaObjectIdAllocator);
    }

    public static SchemaBuilder create(DatabaseId databaseId, StringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

        return new SchemaBuilder(databaseId, stringStorer, schemaObjectIdAllocator);
    }

    private final DatabaseId databaseId;

    private SchemaBuilder(String databaseName, StringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {
        this(new DatabaseId(DBConstants.INITIAL_DESCRIPTORABLE, databaseName), stringStorer, schemaObjectIdAllocator);
    }

    private SchemaBuilder(DatabaseId databaseId, StringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {
        super(stringStorer, schemaObjectIdAllocator);

        this.databaseId = Objects.requireNonNull(databaseId);
    }

    @FunctionalInterface
    private interface SchemaFactory<T extends IDatabaseSchema> {

        T create(DatabaseId databaseId, DatabaseSchemaVersion version, HeapAllCompleteSchemaMaps schemaMaps);
    }

    private <T extends IDatabaseSchema> T build(SchemaFactory<T> schemaFactory) {

        final HeapAllCompleteSchemaMaps schemaMaps = buildCompleteSchemaMaps();

        final DatabaseSchemaVersion schemaVersion = DatabaseSchemaVersion.of(DatabaseSchemaVersion.INITIAL_VERSION);

        return schemaFactory.create(databaseId, schemaVersion, schemaMaps);
    }

    public IDatabaseSchema buildCompleteSchema() {

        return build(CompleteDatabaseSchema::of);
    }

    public IEffectiveDatabaseSchema buildEffectiveSchema() {

        return build(EffectiveDatabaseSchema::of);
    }
}
