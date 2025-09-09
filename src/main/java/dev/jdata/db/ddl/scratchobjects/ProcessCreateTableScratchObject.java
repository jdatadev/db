package dev.jdata.db.ddl.scratchobjects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListBuilder;

public final class ProcessCreateTableScratchObject extends ProcessTableColumnsScratchObject {

    public void initializeCreateTable(StringManagement stringManagement, IndexListBuilder<Column, ?, ?> columnsBuilder) {

        initialize(stringManagement, DBConstants.INITIAL_COLUMN_ID, columnsBuilder);
    }
}
