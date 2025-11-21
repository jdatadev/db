package dev.jdata.db.engine.transactions.ddl;

import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.engine.transactions.ddl.DDLTransaction.DDLTransactionStatement;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IHeapMutableIndexList;
import dev.jdata.db.utils.adt.lists.IHeapMutableIndexListAllocator;

final class HeapDDLTransaction

        extends DDLTransaction<
                    IHeapIndexList<DDLTransactionStatement>,
                    IHeapIndexListBuilder<DDLTransactionStatement>,
                    IHeapIndexList<DDLTransactionObject>,
                    IHeapIndexListBuilder<DDLTransactionObject>,
                    IHeapMutableIndexList<DDLTransactionObject>> {

    static final class HeapDDLTransactionCachedObjects

            extends DDLTransactionCachedObjects<
                            IHeapIndexList<DDLTransactionStatement>,
                            IHeapIndexListBuilder<DDLTransactionStatement>,
                            IHeapIndexList<DDLTransactionObject>,
                            IHeapIndexListBuilder<DDLTransactionObject>,
                            IHeapMutableIndexList<DDLTransactionObject>> {

        public HeapDDLTransactionCachedObjects() {
            super(IHeapIndexListAllocator.create(DDLTransactionStatement[]::new), IHeapIndexListAllocator.create(DDLTransactionObject[]::new),
                    IHeapMutableIndexListAllocator.create(DDLTransactionObject[]::new), new DDLSchemaScratchObjects());
        }
    }

    HeapDDLTransaction() {
        super(AllocationType.HEAP);
    }
}
