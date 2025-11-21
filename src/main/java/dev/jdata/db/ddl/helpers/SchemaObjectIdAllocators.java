package dev.jdata.db.ddl.helpers;

import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.schema.model.databaseschema.IDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;

public final class SchemaObjectIdAllocators implements IResettable {

    private final int[] schemaObjectIdAllocators;

    public static SchemaObjectIdAllocators ofInitial() {

        return new SchemaObjectIdAllocators();
    }

    private SchemaObjectIdAllocators() {

        this.schemaObjectIdAllocators = new int[DDLObjectType.getNumObjectTypes()];

        Initializable.clearToResetValue(schemaObjectIdAllocators, DBConstants.NO_SCHEMA_OBJECT_ID);
    }

    public void initialize(IDatabaseSchema databaseSchema) {

        Objects.requireNonNull(databaseSchema);

        Initializable.checkNotYetInitialized(schemaObjectIdAllocators, DBConstants.NO_SCHEMA_OBJECT_ID);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final int maxId = databaseSchema.computeMaxId(ddlObjectType, DBConstants.NO_SCHEMA_OBJECT_ID);

            schemaObjectIdAllocators[ddlObjectType.ordinal()] = maxId == DBConstants.NO_SCHEMA_OBJECT_ID ? DBConstants.INITIAL_SCHEMA_OBJECT_ID : maxId + 1;
        }
    }

    @Override
    public void reset() {

        Initializable.checkResettable(schemaObjectIdAllocators, DBConstants.NO_SCHEMA_OBJECT_ID, e -> e >= DBConstants.INITIAL_SCHEMA_OBJECT_ID);
    }

    public int allocate(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return schemaObjectIdAllocators[ddlObjectType.ordinal()] ++;
    }
}
