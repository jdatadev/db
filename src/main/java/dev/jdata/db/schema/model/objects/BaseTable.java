package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.adt.lists.HeapIndexList;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseTable extends NullableColumnsObject {

    BaseTable(long parsedName, long hashName, int id, HeapIndexList<Column> columns) {
        super(parsedName, hashName, id, columns);

        Checks.isTableId(id);
    }

    BaseTable(BaseTable toCopy, HeapIndexList<Column> columns) {
        super(toCopy, columns);
    }

    BaseTable(BaseTable toCopy, int newSchemaObjectId) {
        super(toCopy, newSchemaObjectId);
    }
}
