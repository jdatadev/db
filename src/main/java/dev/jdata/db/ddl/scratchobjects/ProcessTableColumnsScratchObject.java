package dev.jdata.db.ddl.scratchobjects;

import java.util.Objects;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;

public abstract class ProcessTableColumnsScratchObject extends ProcessTableColumnsIdScratchObject {

    private IIndexListBuilder<Column, ?, ?> columnsBuilder;

    ProcessTableColumnsScratchObject(AllocationType allocationType) {
        super(allocationType);
    }

    final void initialize(StringManagement stringManagement, IIndexListBuilder<Column, ?, ?> columnsBuilder) {

        initialize(stringManagement, -1, columnsBuilder);
    }

    public final void initialize(StringManagement stringManagement, int initialColumnIdSequenceNo, IIndexListBuilder<Column, ?, ?> columnsBuilder) {

        initialize(stringManagement, initialColumnIdSequenceNo);

        this.columnsBuilder = Initializable.checkNotYetInitialized(this.columnsBuilder, columnsBuilder);
    }

    @Override
    public void reset() {

        super.reset();

        this.columnsBuilder = Initializable.checkResettable(columnsBuilder);
    }

    public final void addColumn(Column column) {

        Objects.requireNonNull(column);

        columnsBuilder.addTail(column);
    }
}
