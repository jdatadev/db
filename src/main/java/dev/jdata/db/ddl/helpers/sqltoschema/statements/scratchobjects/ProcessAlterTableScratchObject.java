package dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects;

import java.util.Objects;

import org.jutils.io.strings.StringRef;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IIntSetAllocator;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;

public final class ProcessAlterTableScratchObject<T extends IIntSetBuilder<?, ?>, U extends IIndexListBuilder<Column, ?, ?>> extends ProcessTableColumnsScratchObject {

    private DatabaseId databaseId;
    private Table table;
    private IIntSetAllocator<?, ?, T> intSetAllocator;
    private IIndexListAllocator<Column, ?, ?, U> columnIndexListAllocator;

    private Column existingColumn;
    private long parsedName;

    public ProcessAlterTableScratchObject(AllocationType allocationType) {
        super(allocationType);
    }

    public void initialize(DatabaseId databaseId, StringManagement stringManagement, Table table, IIntSetAllocator<?, ?, T> intSetAllocator,
            IIndexListAllocator<Column, ?, ?, U> columnIndexListAllocator) {

        initialize(stringManagement, table.getMaxColumnId() + 1);

        this.databaseId = Initializable.checkNotYetInitialized(this.databaseId, databaseId);
        this.table = Initializable.checkNotYetInitialized(this.table, table);
        this.intSetAllocator = Initializable.checkNotYetInitialized(this.intSetAllocator, intSetAllocator);
        this.columnIndexListAllocator = Initializable.checkNotYetInitialized(this.columnIndexListAllocator, columnIndexListAllocator);
    }

    @Override
    public void reset() {

        super.reset();

        this.databaseId = Initializable.checkResettable(databaseId);
        this.table = Initializable.checkResettable(table);
        this.intSetAllocator = Initializable.checkResettable(intSetAllocator);
        this.columnIndexListAllocator = Initializable.checkResettable(columnIndexListAllocator);

        this.existingColumn = null;
        this.parsedName = StringRef.STRING_NONE;
    }

    public DatabaseId getDatabaseId() {
        return databaseId;
    }

    public Table getTable() {
        return table;
    }

    public IIntSetAllocator<?, ?, T> getIntSetAllocator() {
        return intSetAllocator;
    }

    public IIndexListAllocator<Column, ?, ?, U> getColumnIndexListAllocator() {
        return columnIndexListAllocator;
    }

    Column getExistingColumn() {
        return existingColumn;
    }

    void setExistingColumn(Column existingColumn) {

        this.existingColumn = Objects.requireNonNull(existingColumn);
    }

    public long getParsedName() {
        return parsedName;
    }

    public void setParsedName(long parsedName) {

        this.parsedName = StringRef.checkIsString(parsedName);
    }
}
