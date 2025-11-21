package dev.jdata.db.ddl.scratchobjects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;

public final class ProcessCreateTableScratchObject extends ProcessTableColumnsScratchObject {

    public ProcessCreateTableScratchObject(AllocationType allocationType) {
        super(allocationType);
    }

    public void initializeCreateTable(StringManagement stringManagement, IIndexListBuilder<Column, ?, ?> columnsBuilder) {

        initialize(stringManagement, DBConstants.INITIAL_COLUMN_ID, columnsBuilder);
    }
}
