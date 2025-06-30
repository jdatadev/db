package dev.jdata.db.ddl.scratchobjects;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.utils.checks.Checks;

abstract class ProcessTableColumnsIdScratchObject extends ProcessParsedScratchObject {

    private int columnIdSequenceNo;

    final void initialize(StringManagement stringManagement, int initialColumnIdSequenceNo) {

        initialize(stringManagement);

        this.columnIdSequenceNo = Checks.isColumnId(initialColumnIdSequenceNo);
    }

    public final int allocateColumnId() {

        return columnIdSequenceNo ++;
    }
}
