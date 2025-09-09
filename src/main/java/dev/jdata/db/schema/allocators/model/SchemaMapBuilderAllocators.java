package dev.jdata.db.schema.allocators.model;

import java.util.function.Function;

import dev.jdata.db.schema.DDLObjectTypeAllocators;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListBuilder;

public class SchemaMapBuilderAllocators extends DDLObjectTypeAllocators<SchemaMapBuilderAllocator<?, ?, ?, ?, ?, ?>> {

    public SchemaMapBuilderAllocators(Function<DDLObjectType, SchemaMapBuilderAllocator<?, ?, ?, ?, ?, ?>> createSchemaMapBuilderAllocator) {
        super(SchemaMapBuilderAllocator<?, ?, ?, ?, ?, ?>[]::new, (t, g) -> createSchemaMapBuilderAllocator.apply(t));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final Class<SchemaMapBuilderAllocator<?, ?, ?, ?, ?, ?>> getCachedObjectClass() {

        return (Class<SchemaMapBuilderAllocator<?, ?, ?, ?, ?, ?>>)(Class<?>)SchemaMapBuilderAllocator.class;
    }

    @SuppressWarnings("unchecked")
    public final <
                    SCHEMA_OBJECT extends SchemaObject,
                    INDEX_LIST extends IndexList<SCHEMA_OBJECT>,
                    INDEX_LIST_BUILDER extends IndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER>,
                    INDEX_LIST_ALLOCATOR extends IndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, ?>,
                    SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, SCHEMA_MAP>,
                    SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>>

    SchemaMapBuilderAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>
                    getAllocator(DDLObjectType ddlObjectType) {

        return (SchemaMapBuilderAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>)getInstance(ddlObjectType);
    }
}
