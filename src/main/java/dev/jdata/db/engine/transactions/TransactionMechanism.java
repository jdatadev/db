package dev.jdata.db.engine.transactions;

import dev.jdata.db.data.BaseRowMap;

public abstract class TransactionMechanism<T> extends BaseRowMap implements TransactionOperations<T> {

}
