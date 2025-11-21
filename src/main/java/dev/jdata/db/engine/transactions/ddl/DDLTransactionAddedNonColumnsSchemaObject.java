package dev.jdata.db.engine.transactions.ddl;

import dev.jdata.db.schema.model.objects.SchemaObject;

final class DDLTransactionAddedNonColumnsSchemaObject extends DDLTransactionAddedSchemaObject<SchemaObject> {

    DDLTransactionAddedNonColumnsSchemaObject(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    <P, R> R visit(DDLTransactionObjectVisitor<P, R> visitor, P parameter) {

        return visitor.onAddedNonColumnsSchemaObject(this, parameter);
    }
}
