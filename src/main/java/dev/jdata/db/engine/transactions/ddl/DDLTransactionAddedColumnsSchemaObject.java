package dev.jdata.db.engine.transactions.ddl;

import dev.jdata.db.schema.model.objects.ColumnsObject;

final class DDLTransactionAddedColumnsSchemaObject extends DDLTransactionAddedSchemaObject<ColumnsObject> {

    @Override
    <P, R> R visit(DDLTransactionObjectVisitor<P, R> visitor, P parameter) {

        return visitor.onAddedColumnsSchemaObject(this, parameter);
    }
}
