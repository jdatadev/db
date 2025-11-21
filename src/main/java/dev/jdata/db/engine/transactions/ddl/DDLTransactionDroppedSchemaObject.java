package dev.jdata.db.engine.transactions.ddl;

import dev.jdata.db.DBConstants;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;

public final class DDLTransactionDroppedSchemaObject extends DDLTransactionObject implements IResettable {

    private DDLObjectType ddlObjectType;
    private int schemaObjectId;

    DDLTransactionDroppedSchemaObject(AllocationType allocationType) {
        super(allocationType);

        this.ddlObjectType = null;
        this.schemaObjectId = Initializable.clearToResetValue(DBConstants.NO_SCHEMA_OBJECT_ID);
    }

    void initialize(DDLObjectType ddlObjectType, int schemaObjectId) {

        this.ddlObjectType = Initializable.checkNotYetInitialized(this.ddlObjectType, ddlObjectType);
        this.schemaObjectId = Initializable.checkNotYetInitialized(this.schemaObjectId, schemaObjectId, DBConstants.NO_SCHEMA_OBJECT_ID);
    }

    @Override
    public void reset() {

        this.ddlObjectType = Initializable.checkResettable(ddlObjectType);
        this.schemaObjectId = Initializable.checkResettable(schemaObjectId, DBConstants.NO_SCHEMA_OBJECT_ID);
    }

    int getSchemaObjectId() {
        return schemaObjectId;
    }

    public void setSchemaObjectId(int schemaObjectId) {
        this.schemaObjectId = schemaObjectId;
    }

    @Override
    <P, R> R visit(DDLTransactionObjectVisitor<P, R> visitor, P parameter) {

        return visitor.onDroppedSchemaObject(this, parameter);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [super=" + super.toString() + ", ddlObjectType=" + ddlObjectType + ", schemaObjectId=" + schemaObjectId + ']';
    }
}
