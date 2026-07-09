package dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.ISQLToSchemaStringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;

public final class ProcessCreateTableScratchObject extends ProcessTableColumnsScratchObject {

    public ProcessCreateTableScratchObject(AllocationType allocationType) {
        super(allocationType);
    }

    public void initializeCreateTable(ISQLToSchemaStringManagement sqlToSchemaStringManagement, IIndexListBuilder<Column, ?, ?> columnsBuilder) {

        initialize(sqlToSchemaStringManagement, DBConstants.INITIAL_COLUMN_ID);

        setColumnsBuilder(columnsBuilder);
    }
}
