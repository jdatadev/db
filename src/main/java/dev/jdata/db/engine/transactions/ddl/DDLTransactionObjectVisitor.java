package dev.jdata.db.engine.transactions.ddl;

interface DDLTransactionObjectVisitor<P, R> {

    R onAddedColumnsSchemaObject(DDLTransactionAddedColumnsSchemaObject addedColumnsSchemaObject, P parameter);
    R onAddedNonColumnsSchemaObject(DDLTransactionAddedNonColumnsSchemaObject addedNonColumnsSchemaObject, P parameter);
    R onColumnsDiffObject(DDLTransactionColumnsDiffObject columnsDiffObject, P parameter);
}
