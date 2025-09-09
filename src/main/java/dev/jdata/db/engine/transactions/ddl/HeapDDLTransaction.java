package dev.jdata.db.engine.transactions.ddl;

import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.engine.transactions.ddl.DDLTransaction.DDLTransactionStatement;
import dev.jdata.db.utils.adt.lists.HeapIndexList;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.HeapMutableIndexList;
import dev.jdata.db.utils.adt.lists.HeapMutableIndexList.HeapMutableIndexListAllocator;

final class HeapDDLTransaction

        extends DDLTransaction<
                    HeapIndexList<DDLTransactionStatement>,
                    HeapIndexListBuilder<DDLTransactionStatement>,
                    HeapIndexList<DDLTransactionObject>,
                    HeapIndexListBuilder<DDLTransactionObject>,
                    HeapMutableIndexList<DDLTransactionObject>> {

    static final class HeapDDLTransactionCachedObjects

            extends DDLTransactionCachedObjects<
                            HeapIndexList<DDLTransactionStatement>,
                            HeapIndexListBuilder<DDLTransactionStatement>,
                            HeapIndexList<DDLTransactionObject>,
                            HeapIndexListBuilder<DDLTransactionObject>,
                            HeapMutableIndexList<DDLTransactionObject>> {

        public HeapDDLTransactionCachedObjects() {
            super(new HeapIndexListAllocator<>(DDLTransactionStatement[]::new), new HeapIndexListAllocator<>(DDLTransactionObject[]::new),
                    new HeapMutableIndexListAllocator<>(DDLTransactionObject[]::new), new DDLSchemaScratchObjects());
        }
    }

    HeapDDLTransaction() {
        super(AllocationType.HEAP);
    }
}
