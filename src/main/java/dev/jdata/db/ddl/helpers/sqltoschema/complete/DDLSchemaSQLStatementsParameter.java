package dev.jdata.db.ddl.helpers.sqltoschema.complete;

import java.util.function.ToIntFunction;

import dev.jdata.db.ddl.helpers.sqltoschema.complete.scratchobjects.DDLSchemaScratchObjects;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMapBuilder;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;

final class DDLSchemaSQLStatementsParameter<T extends IIntSetBuilder<?, ?>, U extends IIndexListBuilder<Column, ?, ?>> extends DDLSchemaParameter<T, U> {

    private INonDiffSchemaMapBuilder<SchemaObject,?, ?, ?> nonDiffSchemaMapBuilder;
    private IIndexListAllocator<Column, ?, ?, U> columnIndexListAllocator;

    DDLSchemaSQLStatementsParameter(AllocationType allocationType) {
        super(allocationType);
    }

    final void initialize(StringManagement stringManagement, DDLSchemaScratchObjects<T, U> ddlSchemaScratchObjects, ToIntFunction<DDLObjectType> schemaObjectIdAllocator,
            INonDiffSchemaMapBuilder<SchemaObject, ?, ?, ?> nonDiffSchemaMapBuilder, IIndexListAllocator<Column, ?, ?, U> columnIndexListAllocator) {

        initialize(stringManagement, ddlSchemaScratchObjects, schemaObjectIdAllocator);

        this.nonDiffSchemaMapBuilder = Initializable.checkNotYetInitialized(this.nonDiffSchemaMapBuilder, nonDiffSchemaMapBuilder);
        this.columnIndexListAllocator = Initializable.checkNotYetInitialized(this.columnIndexListAllocator, columnIndexListAllocator);
    }

    @Override
    public void reset() {

        super.reset();

        this.nonDiffSchemaMapBuilder = Initializable.checkResettable(nonDiffSchemaMapBuilder);
        this.columnIndexListAllocator = Initializable.checkResettable(columnIndexListAllocator);
    }

    INonDiffSchemaMapBuilder<SchemaObject, ?, ?, ?> getSchemaMapBuilder() {
        return nonDiffSchemaMapBuilder;
    }

    IIndexListAllocator<Column, ?, ?, U> getColumnIndexListAllocator() {
        return columnIndexListAllocator;
    }
}
