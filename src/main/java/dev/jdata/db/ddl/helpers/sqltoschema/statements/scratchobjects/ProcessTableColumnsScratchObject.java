package dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;

public abstract class ProcessTableColumnsScratchObject extends ProcessTableColumnsIdScratchObject {

    private IIndexListBuilder<Column, ?, ?> columnsBuilder;

    ProcessTableColumnsScratchObject(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    public void reset() {

        super.reset();

        this.columnsBuilder = null;
    }

    public final void setColumnsBuilder(IIndexListBuilder<Column, ?, ?> columnsBuilder) {

        this.columnsBuilder = Objects.requireNonNull(columnsBuilder);
    }

    public final void addColumn(Column column) {

        Objects.requireNonNull(column);

        columnsBuilder.addTail(column);
    }
}
