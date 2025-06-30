package dev.jdata.db.ddl.scratchobjects;

import org.jutils.io.strings.StringRef;

import dev.jdata.db.ddl.allocators.DDLSchemaCachedObjects;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.Initializable;

public final class ProcessAlterTableScratchObject extends ProcessTableColumnsScratchObject {

    private DatabaseId databaseId;
    private Table table;
    private DDLSchemaCachedObjects<?> ddlSchemaCachedObjects;

    private Column existingColumn;
    private long parsedName;

    public void initialize(DatabaseId databaseId, StringManagement stringManagement, Table table, DDLSchemaCachedObjects<?> ddlSchemaCachedObjects) {

        initialize(stringManagement, table.getMaxColumnId() + 1);

        this.databaseId = Initializable.checkNotYetInitialized(this.databaseId, databaseId);
        this.table = Initializable.checkNotYetInitialized(this.table, table);
        this.ddlSchemaCachedObjects = Initializable.checkNotYetInitialized(this.ddlSchemaCachedObjects, ddlSchemaCachedObjects);
    }

    @Override
    public void reset() {

        super.reset();

        this.databaseId = Initializable.checkResettable(databaseId);
        this.table = Initializable.checkResettable(table);
        this.ddlSchemaCachedObjects = Initializable.checkResettable(ddlSchemaCachedObjects);

        this.existingColumn = null;
        this.parsedName = StringRef.STRING_NONE;
    }

    public DatabaseId getDatabaseId() {
        return databaseId;
    }

    public Table getTable() {
        return table;
    }

    public DDLSchemaCachedObjects<?> getDDLSchemaCachedObjects() {
        return ddlSchemaCachedObjects;
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
