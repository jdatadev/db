package dev.jdata.db.engine.transactions.ddl;

import dev.jdata.db.ddl.model.diff.SchemaObjectDiff;
import dev.jdata.db.utils.Initializable;

abstract class DDLTransactionDiffObject<T extends SchemaObjectDiff> extends DDLTransactionObject {

    private T schemaObjectDiff;

    DDLTransactionDiffObject(AllocationType allocationType) {
        super(allocationType);
    }

    final void initialize(T schemaObjectDiff) {

        this.schemaObjectDiff = Initializable.checkNotYetInitialized(this.schemaObjectDiff, schemaObjectDiff);
    }

    @Override
    public final void reset() {

        this.schemaObjectDiff = Initializable.checkResettable(schemaObjectDiff);
    }
}
