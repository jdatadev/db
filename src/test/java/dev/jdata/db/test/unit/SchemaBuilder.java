package dev.jdata.db.test.unit;

import java.util.Objects;
import java.util.function.ToIntFunction;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.database.IStringStorer;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.databaseschema.IDatabaseSchema;
import dev.jdata.db.schema.model.databaseschema.IGenericCompleteDatabaseSchema;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;

public final class SchemaBuilder extends BaseSchemaBuilder<SchemaBuilder> {

    public static SchemaBuilder create(String databaseName, IStringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

        return new SchemaBuilder(databaseName, stringStorer, schemaObjectIdAllocator);
    }

    public static SchemaBuilder create(DatabaseId databaseId, IStringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

        return new SchemaBuilder(databaseId, stringStorer, schemaObjectIdAllocator);
    }

    private final DatabaseId databaseId;

    private SchemaBuilder(String databaseName, IStringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {
        this(new DatabaseId(DBConstants.INITIAL_DESCRIPTORABLE, databaseName), stringStorer, schemaObjectIdAllocator);
    }

    private SchemaBuilder(DatabaseId databaseId, IStringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {
        super(stringStorer, schemaObjectIdAllocator);

        this.databaseId = Objects.requireNonNull(databaseId);
    }

    @FunctionalInterface
    private interface SchemaFactory<T extends IDatabaseSchema> {

        T create(DatabaseId databaseId, DatabaseSchemaVersion version, IHeapAllCompleteSchemaMaps schemaMaps);
    }

    private <T extends IDatabaseSchema> T build(SchemaFactory<T> schemaFactory) {

        final IHeapAllCompleteSchemaMaps schemaMaps = buildCompleteSchemaMaps();

        final DatabaseSchemaVersion schemaVersion = DatabaseSchemaVersion.of(DatabaseSchemaVersion.INITIAL_VERSION);

        return schemaFactory.create(databaseId, schemaVersion, schemaMaps);
    }

    public IGenericCompleteDatabaseSchema buildGenericCompleteSchema() {

        return build(IGenericCompleteDatabaseSchema::of);
    }

    public IEffectiveDatabaseSchema buildEffectiveSchema() {

        return build(IEffectiveDatabaseSchema::of);
    }
}
