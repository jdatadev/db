package dev.jdata.db.engine.transactions.ddl;

import dev.jdata.db.ddl.model.diff.ColumnsObjectDiff;

final class DDLTransactionColumnsDiffObject extends DDLTransactionDiffObject<ColumnsObjectDiff> {

    DDLTransactionColumnsDiffObject(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    <P, R> R visit(DDLTransactionObjectVisitor<P, R> visitor, P parameter) {

        return visitor.onColumnsDiffObject(this, parameter);
    }
}
