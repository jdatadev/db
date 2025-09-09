package dev.jdata.db.ddl.helpers.buildschema;

import java.util.function.ToIntFunction;

import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.utils.Initializable;

final class DDLSchemaSQLStatementsParameter extends DDLSchemaParameter {

    private ICompleteSchemaMapsBuilder<SchemaObject,?, ?> completeSchemaMapsBuilder;

    final void initialize(StringManagement stringManagement,DDLSchemaScratchObjects ddlSchemaScratchObjects, ToIntFunction<DDLObjectType> schemaObjectIdAllocator,
            ICompleteSchemaMapsBuilder<SchemaObject, ?, ?> completeSchemaMapsBuilder) {

        initialize(stringManagement, ddlSchemaScratchObjects, schemaObjectIdAllocator);

        this.completeSchemaMapsBuilder = Initializable.checkNotYetInitialized(this.completeSchemaMapsBuilder, completeSchemaMapsBuilder);
    }

    @Override
    public void reset() {

        super.reset();

        this.completeSchemaMapsBuilder = Initializable.checkResettable(completeSchemaMapsBuilder);
    }

    ICompleteSchemaMapsBuilder<SchemaObject, ?, ?> getCompleteSchemaMapsBuilder() {
        return completeSchemaMapsBuilder;
    }
}
