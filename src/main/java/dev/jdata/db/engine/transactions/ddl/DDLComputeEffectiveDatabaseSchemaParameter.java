package dev.jdata.db.engine.transactions.ddl;

import java.util.function.BiFunction;
import java.util.function.ToIntFunction;

import dev.jdata.db.ddl.model.diff.ColumnsObjectDiff;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IMutableDoublyLinkedList;
import dev.jdata.db.utils.adt.lists.IMutableDoublyLinkedListAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public final class DDLComputeEffectiveDatabaseSchemaParameter<

                T extends IMutableDoublyLinkedList<SchemaObject>,
                U extends ICompleteSchemaMapBuilder<?, ? extends IHeapCompleteSchemaMap, U>>

        extends ObjectCacheNode
        implements IResettable {

    private IMutableDoublyLinkedListAllocator<SchemaObject, T> schemaObjectMutableLinkedListAllocator;
    private ICompleteSchemaMapBuilderAllocator<?, ? extends IHeapCompleteSchemaMap, U> completeSchemaMapBuilderAllocator;
    private ToIntFunction<DDLObjectType> schemaObjectIdAllocator;
    private BiFunction<ColumnsObject, ColumnsObjectDiff, ColumnsObject> columnsObjectDiffApplier;

    public DDLComputeEffectiveDatabaseSchemaParameter(AllocationType allocationType) {
        super(allocationType);
    }

    void initialize(IMutableDoublyLinkedListAllocator<SchemaObject, T> schemaObjectMutableLinkedListAllocator,
            ICompleteSchemaMapBuilderAllocator<?, ? extends IHeapCompleteSchemaMap, U> completeSchemaMapBuilderAllocator, ToIntFunction<DDLObjectType> schemaObjectIdAllocator,
            BiFunction<ColumnsObject, ColumnsObjectDiff, ColumnsObject> columnsObjectDiffApplier) {

        this.schemaObjectMutableLinkedListAllocator = Initializable.checkNotYetInitialized(this.schemaObjectMutableLinkedListAllocator, schemaObjectMutableLinkedListAllocator);
        this.completeSchemaMapBuilderAllocator = Initializable.checkNotYetInitialized(this.completeSchemaMapBuilderAllocator, completeSchemaMapBuilderAllocator);
        this.schemaObjectIdAllocator = Initializable.checkNotYetInitialized(this.schemaObjectIdAllocator, schemaObjectIdAllocator);
        this.columnsObjectDiffApplier = Initializable.checkNotYetInitialized(this.columnsObjectDiffApplier, columnsObjectDiffApplier);
    }

    @Override
    public void reset() {

        this.schemaObjectMutableLinkedListAllocator = Initializable.checkResettable(schemaObjectMutableLinkedListAllocator);
        this.completeSchemaMapBuilderAllocator = Initializable.checkResettable(completeSchemaMapBuilderAllocator);
        this.schemaObjectIdAllocator = Initializable.checkResettable(schemaObjectIdAllocator);
        this.columnsObjectDiffApplier = Initializable.checkResettable(columnsObjectDiffApplier);
    }

    IMutableDoublyLinkedListAllocator<SchemaObject, T> getSchemaObjectMutableLinkedListAllocator() {
        return schemaObjectMutableLinkedListAllocator;
    }

    ICompleteSchemaMapBuilderAllocator<?, ? extends IHeapCompleteSchemaMap, U> getCompleteSchemaMapBuilderAllocator() {
        return completeSchemaMapBuilderAllocator;
    }

    ToIntFunction<DDLObjectType> getSchemaObjectIdAllocator() {
        return schemaObjectIdAllocator;
    }

    BiFunction<ColumnsObject, ColumnsObjectDiff, ColumnsObject> getColumnsObjectDiffApplier() {
        return columnsObjectDiffApplier;
    }
}
