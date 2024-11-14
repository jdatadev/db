package dev.jdata.db.schema;

import java.util.List;

public final class View extends ColumnsObject {

    public View(String name, int id, List<Column> columns) {
        super(name, id, columns);
    }
}
