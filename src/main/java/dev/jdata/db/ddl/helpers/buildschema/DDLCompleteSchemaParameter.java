package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.ddl.SchemaObjectIdAllocator;
import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;

@Deprecated
public final class DDLCompleteSchemaParameter<T extends ICompleteSchemaMapsBuilder<?, ?, ?, ?>, P> extends DDLSchemaParameter implements IResettable {

    private StringManagement stringManagement;
    private T schemaMapBuilders;
    private DDLSchemaWorkerObjects<T> ddlSchemaWorkerObjects;
    private DDLSchemaScratchObjects ddlSchemaScratchObjects;
    private SchemaObjectIdAllocator<P> schemaObjectIdAllocator;

    public DDLCompleteSchemaParameter(AllocationType allocationType) {
        super(allocationType);
    }

    public void initialize(StringManagement stringManagement, T schemaMapBuilders, DDLSchemaWorkerObjects<T> ddlSchemaWorkerObjects,
            DDLSchemaScratchObjects ddlSchemaScratchObjects, SchemaObjectIdAllocator<P> schemaObjectIdAllocator) {

        this.stringManagement = Initializable.checkNotYetInitialized(this.stringManagement, stringManagement);
        this.schemaMapBuilders = Initializable.checkNotYetInitialized(this.schemaMapBuilders, schemaMapBuilders);
        this.ddlSchemaWorkerObjects = Initializable.checkNotYetInitialized(this.ddlSchemaWorkerObjects, ddlSchemaWorkerObjects);
        this.ddlSchemaScratchObjects = Initializable.checkNotYetInitialized(this.ddlSchemaScratchObjects, ddlSchemaScratchObjects);
        this.schemaObjectIdAllocator = Initializable.checkNotYetInitialized(this.schemaObjectIdAllocator, schemaObjectIdAllocator);
    }

    @Override
    public void reset() {

        this.stringManagement = Initializable.checkResettable(stringManagement);
        this.schemaMapBuilders = Initializable.checkResettable(schemaMapBuilders);
        this.ddlSchemaWorkerObjects = Initializable.checkResettable(ddlSchemaWorkerObjects);
        this.ddlSchemaScratchObjects = Initializable.checkResettable(ddlSchemaScratchObjects);
        this.schemaObjectIdAllocator = Initializable.checkResettable(schemaObjectIdAllocator);
    }

    T getSchemaMapBuilders() {
        return schemaMapBuilders;
    }

    DDLSchemaWorkerObjects<T> getDDLSchemaWorkerObjects() {
        return ddlSchemaWorkerObjects;
    }
}
