package dev.jdata.db.schema.model;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.checks.Checks;

public interface IDatabaseSchemaObjects {

    <T extends SchemaObject> T getSchemaObject(DDLObjectType ddlObjectType, int schemaObjectId);

    <T extends SchemaObject> ISchemaMap<T> getSchemaMap(DDLObjectType ddlObjectType);

    <T extends SchemaObject> IIndexList<T> getSchemaObjects(DDLObjectType ddlObjectType);

    default Table getTable(int tableId) {

        Checks.isTableId(tableId);

        return getSchemaObject(DDLObjectType.TABLE, tableId);
    }

    default ISchemaMap<Table> getTableMap() {

        return getSchemaMap(DDLObjectType.TABLE);
    }

    default IIndexList<Table> getTables() {

        return getSchemaObjects(DDLObjectType.TABLE);
    }

    default IIndexList<View> getViews() {

        return getSchemaObjects(DDLObjectType.VIEW);
    }
}
