package dev.jdata.db.ddl.helpers;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.schema.model.IDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;

public final class SchemaObjectIdAllocators {

    private final int[] schemaObjectIdAllocators;

    public static SchemaObjectIdAllocators ofInitial() {

        return new SchemaObjectIdAllocators();
    }

    private SchemaObjectIdAllocators() {

        this.schemaObjectIdAllocators = new int[DDLObjectType.getNumObjectTypes()];

        Arrays.fill(schemaObjectIdAllocators, DBConstants.INITIAL_SCHEMA_OBJECT_ID);
    }

    public void initialize(IDatabaseSchema databaseSchema) {

        Objects.requireNonNull(databaseSchema);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final int maxId = databaseSchema.computeMaxId(ddlObjectType, DBConstants.NO_SCHEMA_OBJECT_ID);

            schemaObjectIdAllocators[ddlObjectType.ordinal()] = maxId == DBConstants.NO_SCHEMA_OBJECT_ID ? DBConstants.INITIAL_SCHEMA_OBJECT_ID : maxId + 1;
        }
    }

    public int allocate(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return schemaObjectIdAllocators[ddlObjectType.ordinal()] ++;
    }
}
