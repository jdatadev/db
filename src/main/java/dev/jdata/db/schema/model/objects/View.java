package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.checks.Checks;

public final class View extends ColumnsObject {

    public View(long parsedName, long hashName, int id, IIndexList<Column> columns) {
        super(parsedName, hashName, id, columns);

        Checks.isViewId(id);
    }

    private View(View toCopy, IIndexList<Column> columns) {
        super(toCopy, columns);
    }

    @Override
    public ColumnsObject makeCopy(IIndexList<Column> columns) {

        return new View(this, columns);
    }
}
