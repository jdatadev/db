package dev.jdata.db.ddl.scratchobjects;

import org.jutils.io.strings.StringRef;

import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.Initializable;

public final class ProcessAlterTableScratchObject extends ProcessTableColumnsScratchObject {

    private DatabaseId databaseId;
    private Table table;
    private DDLSchemaScratchObjects ddlSchemaScratchObjects;

    private Column existingColumn;
    private long parsedName;

    public void initialize(DatabaseId databaseId, StringManagement stringManagement, Table table, DDLSchemaScratchObjects ddlSchemaScratchObjects) {

        initialize(stringManagement, table.getMaxColumnId() + 1);

        this.databaseId = Initializable.checkNotYetInitialized(this.databaseId, databaseId);
        this.table = Initializable.checkNotYetInitialized(this.table, table);
        this.ddlSchemaScratchObjects = Initializable.checkNotYetInitialized(this.ddlSchemaScratchObjects, ddlSchemaScratchObjects);
    }

    @Override
    public void reset() {

        super.reset();

        this.databaseId = Initializable.checkResettable(databaseId);
        this.table = Initializable.checkResettable(table);
        this.ddlSchemaScratchObjects = Initializable.checkResettable(ddlSchemaScratchObjects);

        this.existingColumn = null;
        this.parsedName = StringRef.STRING_NONE;
    }

    public DatabaseId getDatabaseId() {
        return databaseId;
    }

    public Table getTable() {
        return table;
    }

    public DDLSchemaScratchObjects getDDLSchemaScratchObjects() {
        return ddlSchemaScratchObjects;
    }

    Column getExistingColumn() {
        return existingColumn;
    }

    void setExistingColumn(Column existingColumn) {
        this.existingColumn = existingColumn;
    }

    public long getParsedName() {
        return parsedName;
    }

    public void setParsedName(long parsedName) {

        this.parsedName = StringRef.checkIsString(parsedName);
    }
}
