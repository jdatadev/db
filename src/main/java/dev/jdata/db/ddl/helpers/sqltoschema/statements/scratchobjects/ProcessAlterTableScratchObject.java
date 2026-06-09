package dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IIntSetAllocator;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;

public final class ProcessAlterTableScratchObject<T extends IIntSetBuilder<?, ?>, U extends IIndexListBuilder<Column, ?, ?>> extends ProcessParsedScratchObject {

    private final ProcessAlterTableAddColumnsScratchObject<U> addColumnsScratchObject;
    private final ProcessAlterTableModifyColumnsScratchObject<U> modifyColumnsScratchObject;
    private final ProcessAlterTableDropColumnsScratchObject<T> dropColumnsScratchObject;
    private final ProcessAlterTableAddPrimaryConstraintScratchObject addPrimaryConstraintScratchObject;

    private DatabaseId databaseId;
    private Table table;
    private IIntSetAllocator<?, ?, T> intSetAllocator;
    private IIndexListAllocator<Column, ?, ?, U> columnIndexListAllocator;

    public ProcessAlterTableScratchObject(AllocationType allocationType) {
        super(allocationType);

        this.addColumnsScratchObject = new ProcessAlterTableAddColumnsScratchObject<>(allocationType);
        this.modifyColumnsScratchObject = new ProcessAlterTableModifyColumnsScratchObject<>(allocationType);
        this.dropColumnsScratchObject = new ProcessAlterTableDropColumnsScratchObject<>(allocationType);
        this.addPrimaryConstraintScratchObject = new ProcessAlterTableAddPrimaryConstraintScratchObject(allocationType);
    }

    public void initialize(DatabaseId databaseId, StringManagement stringManagement, Table table, IIntSetAllocator<?, ?, T> intSetAllocator,
            IIndexListAllocator<Column, ?, ?, U> columnIndexListAllocator) {

        initialize(stringManagement);

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
    }

    public ProcessAlterTableAddColumnsScratchObject<U> getAddColumnsScratchObject() {
        return addColumnsScratchObject;
    }

    public ProcessAlterTableModifyColumnsScratchObject<U> getModifyColumnsScratchObject() {
        return modifyColumnsScratchObject;
    }

    public ProcessAlterTableDropColumnsScratchObject<T> getDropColumnsScratchObject() {
        return dropColumnsScratchObject;
    }

    public ProcessAlterTableAddPrimaryConstraintScratchObject getAddPrimaryConstraintScratchObject() {
        return addPrimaryConstraintScratchObject;
    }

    public DatabaseId getDatabaseId() {
        return databaseId;
    }

    public Table getTable() {
        return table;
    }

    public IIndexListAllocator<Column, ?, ?, U> getColumnIndexListAllocator() {
        return columnIndexListAllocator;
    }
}
