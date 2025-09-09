package dev.jdata.db.engine.transactions.ddl;

import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

abstract class DDLTransactionObject extends ObjectCacheNode implements IResettable {

    abstract <P, R> R visit(DDLTransactionObjectVisitor<P, R> visitor, P parameter);
}
