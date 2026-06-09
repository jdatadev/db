package dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.Initializable;

public final class ProcessAlterTableAddPrimaryConstraintScratchObject extends ProcessParsedScratchObject {

    private DatabaseId databaseId;
    private Table table;

    public ProcessAlterTableAddPrimaryConstraintScratchObject(AllocationType allocationType) {
        super(allocationType);
    }

    public void initialize(DatabaseId databaseId, StringManagement stringManagement, Table table) {

        initialize(stringManagement);

        this.databaseId = Initializable.checkNotYetInitialized(this.databaseId, databaseId);
        this.table = Initializable.checkNotYetInitialized(this.table, table);
    }

    @Override
    public void reset() {

        super.reset();

        this.databaseId = Initializable.checkResettable(databaseId);
        this.table = Initializable.checkResettable(table);
    }

    public DatabaseId getDatabaseId() {
        return databaseId;
    }

    public Table getTable() {
        return table;
    }
}
