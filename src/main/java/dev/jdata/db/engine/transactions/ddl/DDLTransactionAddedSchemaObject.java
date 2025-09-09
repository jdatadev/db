package dev.jdata.db.engine.transactions.ddl;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.Initializable;

abstract class DDLTransactionAddedSchemaObject<T extends SchemaObject> extends DDLTransactionObject {

    private T schemaObject;

    final void initialize(T schemaObject) {

        this.schemaObject = Initializable.checkNotYetInitialized(this.schemaObject, schemaObject);
    }

    @Override
    public final void reset() {

        this.schemaObject = Initializable.checkResettable(schemaObject);
    }

    final T getSchemaObject() {
        return schemaObject;
    }
}
