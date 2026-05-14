package dev.jdata.db.engine.transactions.ddl;

import dev.jdata.db.engine.transactions.ddl.DDLTransaction.DDLTransactionStatement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IHeapMutableIndexList;
import dev.jdata.db.utils.adt.lists.IHeapMutableIndexListAllocator;
import dev.jdata.db.utils.adt.sets.IHeapIntSet;
import dev.jdata.db.utils.adt.sets.IHeapIntSetAllocator;
import dev.jdata.db.utils.adt.sets.IHeapIntSetBuilder;

final class HeapDDLTransaction

        extends DDLTransaction<

                    IHeapIndexList<DDLTransactionStatement>,
                    IHeapIndexListBuilder<DDLTransactionStatement>,
                    IHeapIndexList<DDLTransactionObject>,
                    IHeapIndexListBuilder<DDLTransactionObject>,
                    IHeapMutableIndexList<DDLTransactionObject>,
                    IHeapIntSet,
                    IHeapIntSetBuilder,
                    IHeapIndexList<Column>,
                    IHeapIndexListBuilder<Column>> {

    static final class HeapDDLTransactionCachedObjects

            extends DDLTransactionCachedObjects<

                            IHeapIndexList<DDLTransactionStatement>,
                            IHeapIndexListBuilder<DDLTransactionStatement>,
                            IHeapIndexList<DDLTransactionObject>,
                            IHeapIndexListBuilder<DDLTransactionObject>,
                            IHeapMutableIndexList<DDLTransactionObject>,
                            IHeapIntSet,
                            IHeapIntSetBuilder,
                            IHeapIndexList<Column>,
                            IHeapIndexListBuilder<Column>> {

        HeapDDLTransactionCachedObjects() {
            super(IHeapIndexListAllocator.create(DDLTransactionStatement[]::new), IHeapIndexListAllocator.create(DDLTransactionObject[]::new),
                    IHeapMutableIndexListAllocator.create(DDLTransactionObject[]::new), IHeapIntSetAllocator.create(), IHeapIndexListAllocator.create(Column[]::new));
        }
    }

    HeapDDLTransaction() {
        super(AllocationType.HEAP);
    }
}
