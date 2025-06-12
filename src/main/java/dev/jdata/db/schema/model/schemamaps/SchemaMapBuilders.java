package dev.jdata.db.schema.model.schemamaps;

import java.util.Objects;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.Builder;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public final class SchemaMapBuilders extends ObjectCacheNode {

    private final SchemaMap.Builder<?>[] schemaMapBuilders;

    private boolean initialized;

    public SchemaMapBuilders() {

        this.schemaMapBuilders = new SchemaMap.Builder<?>[DDLObjectType.getNumObjectTypes()];
    }

    public void initialize(SchemaMapBuilderAllocators schemaMapBuilderAllocators) {

        Objects.requireNonNull(schemaMapBuilderAllocators);

        checkIsAllocated();

        this.initialized = Initializable.checkNotYetInitialized(initialized);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            schemaMapBuilders[ddlObjectType.ordinal()] = SchemaMap.createBuilder(1, schemaMapBuilderAllocators.getAllocator(ddlObjectType));
        }
    }

    public void reset(SchemaMapBuilderAllocators schemaMapBuilderAllocators) {

        Objects.requireNonNull(schemaMapBuilderAllocators);

        checkIsAllocated();

        this.initialized = Initializable.checkResettable(initialized);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final int index = ddlObjectType.ordinal();

            final SchemaMapBuilderAllocator<SchemaObject> schemaMapBuilderAllocator = schemaMapBuilderAllocators.getAllocator(ddlObjectType);

            @SuppressWarnings("unchecked")
            final SchemaMap.Builder<SchemaObject> schemaMapBuilder = (Builder<SchemaObject>)schemaMapBuilders[index];

            schemaMapBuilderAllocator.freeSchemaMapBuilder(schemaMapBuilder);

            schemaMapBuilders[index] = null;
        }
    }

    public <T extends SchemaObject> void addSchemaObject(DDLObjectType ddlObjectType, T schemaObject) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(schemaObject);

        @SuppressWarnings("unchecked")
        final SchemaMap.Builder<T> tableSchemaMapBuilder = (SchemaMap.Builder<T>)schemaMapBuilders[ddlObjectType.ordinal()];

        tableSchemaMapBuilder.add(schemaObject);
    }

    public CompleteSchemaMaps build() {

        return new CompleteSchemaMaps(build(DDLObjectType.TABLE), build(DDLObjectType.VIEW), build(DDLObjectType.INDEX), build(DDLObjectType.TRIGGER),
                build(DDLObjectType.FUNCTION), build(DDLObjectType.PROCEDURE));
    }

    private <T extends SchemaObject> SchemaMap<T> build(DDLObjectType ddlObjectType) {

        Initializable.checkIsInitialized(initialized);

        @SuppressWarnings("unchecked")
        final SchemaMap.Builder<T> schemaMapBuilder = (SchemaMap.Builder<T>)schemaMapBuilders[ddlObjectType.ordinal()];

        return schemaMapBuilder.build();
    }
}
