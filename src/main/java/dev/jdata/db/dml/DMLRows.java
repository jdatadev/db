package dev.jdata.db.dml;

import dev.jdata.db.utils.debug.ToStringable;

public abstract class DMLRows<T extends DMLRows.DMLRow> extends StorageRows<T[]> {

    public static abstract class DMLRow extends ToStringable {

    }
}
