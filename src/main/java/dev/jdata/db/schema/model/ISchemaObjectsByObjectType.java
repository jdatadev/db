package dev.jdata.db.schema.model;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.mutability.IImmutable;
import dev.jdata.db.utils.checks.Checks;

public interface ISchemaObjectsByObjectType extends IImmutable {

    <R extends SchemaObject> R getSchemaObject(DDLObjectType ddlObjectType, int schemaObjectId);

    <R extends SchemaObject> R getSchemaObjectByName(DDLObjectType ddlObjectType, long schemaObjectName);

    <T extends SchemaObject> ISchemaObjects<T> getSchemaObjects(DDLObjectType ddlObjectType);

    <T extends SchemaObject> IIndexList<T> getSchemaObjectsList(DDLObjectType ddlObjectType);

    int computeMaxId(DDLObjectType ddlObjectType, int defaultValue);

    int getNumSchemaObjects();

    default Table getTable(int tableId) {

        Checks.isTableId(tableId);

        return getSchemaObject(DDLObjectType.TABLE, tableId);
    }

    default ISchemaObjects<Table> getTables() {

        return getSchemaObjects(DDLObjectType.TABLE);
    }

    default IIndexList<Table> getTablesList() {

        return getSchemaObjectsList(DDLObjectType.TABLE);
    }

    default IIndexList<View> getViewsList() {

        return getSchemaObjectsList(DDLObjectType.VIEW);
    }
}
