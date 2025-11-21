package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseTable extends NullableColumnsObject {

    BaseTable(long parsedName, long hashName, int id, IHeapIndexList<Column> columns) {
        super(parsedName, hashName, id, columns);

        Checks.isTableId(id);
    }

    BaseTable(BaseTable toCopy, IHeapIndexList<Column> columns) {
        super(toCopy, columns);
    }

    BaseTable(BaseTable toCopy, int newSchemaObjectId) {
        super(toCopy, newSchemaObjectId);
    }
}
