package dev.jdata.db.ddl.helpers.sqltoschema.complete;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapMarker;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMap;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMapBuilder;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.utils.allocators.ObjectCache;

abstract class DDLNonDiffSchemaMapAllocators<

                SCHEMA_OBJECTS extends ISchemaObjects<SchemaObject>,
                NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap,
                HEAP_NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap & IHeapSchemaMapMarker,
                NON_DIFF_SCHEMA_MAP_BUILDER extends INonDiffSchemaMapBuilder<SchemaObject, NON_DIFF_SCHEMA_MAP, HEAP_NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER>>

        implements IDDLNonDiffSchemaMapAllocators<NON_DIFF_SCHEMA_MAP, HEAP_NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER> {

    private final ObjectCache<NON_DIFF_SCHEMA_MAP_BUILDER> schemaMapBuilderCache;

    abstract NON_DIFF_SCHEMA_MAP_BUILDER createSchemaMapBuilder();

    DDLNonDiffSchemaMapAllocators(IntFunction<NON_DIFF_SCHEMA_MAP_BUILDER[]> createNonDiffSchemaMapBuilderArray) {

        Objects.requireNonNull(createNonDiffSchemaMapBuilderArray);

        this.schemaMapBuilderCache = new ObjectCache<>(this::createSchemaMapBuilder, createNonDiffSchemaMapBuilderArray);
    }

    @Override
    public final NON_DIFF_SCHEMA_MAP_BUILDER allocateSchemaMapBuilder() {

        return schemaMapBuilderCache.allocate();
    }
    @Override
    public final void freeSchemaMapBuilder(NON_DIFF_SCHEMA_MAP_BUILDER schemaMapBuilder) {

        Objects.requireNonNull(schemaMapBuilder);

        schemaMapBuilderCache.free(schemaMapBuilder);
    }
}
