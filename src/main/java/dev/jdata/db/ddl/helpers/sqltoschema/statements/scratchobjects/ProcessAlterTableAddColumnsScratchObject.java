package dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects;

import org.jutils.io.strings.StringRef;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;

public final class ProcessAlterTableAddColumnsScratchObject<T extends IIndexListBuilder<Column, ?, ?>> extends ProcessTableColumnsScratchObject implements IResettable {

    private DatabaseId databaseId;
    private Table table;
    private IIndexListAllocator<Column, ?, ?, T> columnIndexListAllocator;

    private long parsedName;

    public ProcessAlterTableAddColumnsScratchObject(AllocationType allocationType) {
        super(allocationType);
    }

    public void initialize(DatabaseId databaseId, StringManagement stringManagement, Table table, IIndexListAllocator<Column, ?, ?, T> columnIndexListAllocator) {

        initialize(stringManagement, table.getMaxColumnId() + 1);

        this.databaseId = Initializable.checkNotYetInitialized(this.databaseId, databaseId);
        this.table = Initializable.checkNotYetInitialized(this.table, table);
        this.columnIndexListAllocator = Initializable.checkNotYetInitialized(this.columnIndexListAllocator, columnIndexListAllocator);
    }

    @Override
    public void reset() {

        super.reset();

        this.databaseId = Initializable.checkResettable(databaseId);
        this.table = Initializable.checkResettable(table);
        this.columnIndexListAllocator = Initializable.checkResettable(columnIndexListAllocator);

        this.parsedName = StringRef.STRING_NONE;
    }

    public DatabaseId getDatabaseId() {
        return databaseId;
    }

    public Table getTable() {
        return table;
    }

    public IIndexListAllocator<Column, ?, ?, T> getColumnIndexListAllocator() {
        return columnIndexListAllocator;
    }

    public long getParsedName() {
        return parsedName;
    }

    public void setParsedName(long parsedName) {

        this.parsedName = StringRef.checkIsString(parsedName);
    }
}
