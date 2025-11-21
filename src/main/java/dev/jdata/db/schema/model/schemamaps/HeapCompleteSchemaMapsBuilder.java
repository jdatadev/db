package dev.jdata.db.schema.model.schemamaps;

import java.util.Objects;

import dev.jdata.db.review.HeapCompleteSchemaMaps;
import dev.jdata.db.review.IHeapCompleteSchemaMaps;
import dev.jdata.db.review.IHeapCompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMap;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;

@Deprecated // currently not in use
final class HeapCompleteSchemaMapsBuilder<SCHEMA_OBJECT extends SchemaObject> extends CompleteSchemaMapsBuilder<

                SCHEMA_OBJECT,
                IHeapIndexList<SCHEMA_OBJECT>,
                IHeapIndexListBuilder<SCHEMA_OBJECT>,
                IHeapIndexListAllocator<SCHEMA_OBJECT>,
                IHeapSchemaMap<SCHEMA_OBJECT>,
                IHeapSchemaMapBuilder<SCHEMA_OBJECT>,
                IHeapCompleteSchemaMaps,
                IHeapCompleteSchemaMaps,
                IHeapCompleteSchemaMapsBuilder<SCHEMA_OBJECT>
                > {

    HeapCompleteSchemaMapsBuilder(AllocationType allocationType) {
        super(allocationType, IHeapSchemaMapBuilder[]::new);

        initialize(HeapSchemaMapBuilderAllocators.INSTANCE);
    }

    @Override
    protected IHeapCompleteSchemaMaps build(AllocationType allocationType, IHeapSchemaMapBuilder<SCHEMA_OBJECT>[] mutable) {

        checkHeapSchemaMapsBuildParameters(allocationType, mutable);

        return new HeapCompleteSchemaMaps(allocationType, mapOrEmpty(mutable, DDLObjectType.TABLE), mapOrEmpty(mutable, DDLObjectType.VIEW),
                mapOrEmpty(mutable, DDLObjectType.INDEX), mapOrEmpty(mutable, DDLObjectType.TRIGGER), mapOrEmpty(mutable, DDLObjectType.FUNCTION),
                mapOrEmpty(mutable, DDLObjectType.PROCEDURE));
    }

    @Override
    protected IHeapCompleteSchemaMaps empty() {

        return HeapCompleteSchemaMaps.empty();
    }

    @Override
    protected IHeapCompleteSchemaMaps heapBuild(AllocationType allocationType, IHeapSchemaMapBuilder<SCHEMA_OBJECT>[] mutable) {

        checkHeapSchemaMapsBuildParameters(allocationType, mutable);

        return build(allocationType, mutable);
    }

    @Override
    protected IHeapCompleteSchemaMaps heapEmpty() {

        return empty();
    }

    @SuppressWarnings("unchecked")
    private <R extends ISchemaMap<?>> R mapOrEmpty(IHeapSchemaMapBuilder<SCHEMA_OBJECT>[] schemaMapBuilders, DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        final IHeapSchemaMapBuilder<SCHEMA_OBJECT> mapBuilder = schemaMapBuilders[ddlObjectType.ordinal()];

        return mapBuilder != null ? (R)mapBuilder.buildOrEmpty() : (R)IHeapSchemaMap.empty();
    }
}
