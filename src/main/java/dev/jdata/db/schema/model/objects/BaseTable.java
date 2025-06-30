package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseTable extends NullableColumnsObject {

    BaseTable(long parsedName, long hashName, int id, IIndexList<Column> columns) {
        super(parsedName, hashName, id, columns);

        Checks.isTableId(id);
    }

    BaseTable(BaseTable toCopy, IIndexList<Column> columns) {
        super(toCopy, columns);
    }
}
