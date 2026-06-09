package dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects;

import org.jutils.io.strings.StringRef;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.sets.IIntSetAllocator;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;

public final class ProcessAlterTableDropColumnsScratchObject<T extends IIntSetBuilder<?, ?>> extends ProcessParsedScratchObject {

    private DatabaseId databaseId;
    private Table table;
    private IIntSetAllocator<?, ?, T> intSetAllocator;

    private long parsedName;

    public ProcessAlterTableDropColumnsScratchObject(AllocationType allocationType) {
        super(allocationType);
    }

    public void initialize(DatabaseId databaseId, StringManagement stringManagement, Table table, IIntSetAllocator<?, ?, T> intSetAllocator) {

        initialize(stringManagement);

        this.databaseId = Initializable.checkNotYetInitialized(this.databaseId, databaseId);
        this.table = Initializable.checkNotYetInitialized(this.table, table);
        this.intSetAllocator = Initializable.checkNotYetInitialized(this.intSetAllocator, intSetAllocator);
    }

    @Override
    public void reset() {

        super.reset();

        this.databaseId = Initializable.checkResettable(databaseId);
        this.table = Initializable.checkResettable(table);
        this.intSetAllocator = Initializable.checkResettable(intSetAllocator);

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

    public long getParsedName() {
        return parsedName;
    }

    public void setParsedName(long parsedName) {

        this.parsedName = StringRef.checkIsString(parsedName);
    }
}
