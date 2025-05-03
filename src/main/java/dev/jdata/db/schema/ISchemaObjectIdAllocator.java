package dev.jdata.db.schema;

import dev.jdata.db.schema.model.objects.DDLObjectType;

public interface ISchemaObjectIdAllocator {

    int allocateSchemaObjectId(DDLObjectType ddlObjectType);

    void rollbackSchemaObjectIdAllocation(DDLObjectType ddlObjectType, int schemaObjectId);

    default int allocateTableId() {

        return allocateSchemaObjectId(DDLObjectType.TABLE);
    }

    default int allocateViewId() {

        return allocateSchemaObjectId(DDLObjectType.VIEW);
    }

    default int allocateIndexId() {

        return allocateSchemaObjectId(DDLObjectType.INDEX);
    }

    default int allocateTriggerId() {

        return allocateSchemaObjectId(DDLObjectType.TRIGGER);
    }

    default int allocateFunctionId() {

        return allocateSchemaObjectId(DDLObjectType.FUNCTION);
    }

    default int allocateProcedureId() {

        return allocateSchemaObjectId(DDLObjectType.PROCEDURE);
    }
}
