package dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects;

import dev.jdata.db.ddl.helpers.sqltoschema.statements.ISQLToSchemaStringManagement;
import dev.jdata.db.utils.checks.Checks;

abstract class ProcessTableColumnsIdScratchObject extends ProcessParsedScratchObject {

    private int columnIdSequenceNo;

    ProcessTableColumnsIdScratchObject(AllocationType allocationType) {
        super(allocationType);
    }

    final void initialize(ISQLToSchemaStringManagement sqlToSchemaStringManagement, int initialColumnIdSequenceNo) {

        initialize(sqlToSchemaStringManagement);

        this.columnIdSequenceNo = Checks.isColumnId(initialColumnIdSequenceNo);
    }

    public final int allocateColumnId() {

        return columnIdSequenceNo ++;
    }
}
