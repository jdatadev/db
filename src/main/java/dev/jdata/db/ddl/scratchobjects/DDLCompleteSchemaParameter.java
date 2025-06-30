package dev.jdata.db.ddl.scratchobjects;

import java.util.Objects;

import dev.jdata.db.ddl.DDLSchemasHelper.SchemaObjectIdAllocator;
import dev.jdata.db.ddl.allocators.DDLSchemaCachedObjects;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.schemamaps.SchemaMapBuilders;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public final class DDLCompleteSchemaParameter<P> extends ObjectCacheNode {

    private StringManagement stringManagement;
    private SchemaMapBuilders<?, ?, ?, ?, ?, ?, ?> schemaMapBuilders;
    private DDLSchemaCachedObjects<?> ddlSchemaCachedObjects;
    private SchemaObjectIdAllocator<P> schemaObjectIdAllocator;

    public void initialize(StringManagement stringManagement, SchemaMapBuilders<?, ?, ?, ?, ?, ?, ?> schemaMapBuilders, DDLSchemaCachedObjects<?> ddlSchemaCachedObjects,
            SchemaObjectIdAllocator<P> schemaObjectIdAllocator) {

        this.stringManagement = Objects.requireNonNull(stringManagement);
        this.schemaMapBuilders = Objects.requireNonNull(schemaMapBuilders);
        this.ddlSchemaCachedObjects = Objects.requireNonNull(ddlSchemaCachedObjects);
        this.schemaObjectIdAllocator = Objects.requireNonNull(schemaObjectIdAllocator);
    }

    public StringManagement getStringManagement() {
        return stringManagement;
    }

    public SchemaMapBuilders<?, ?, ?, ?, ?, ?, ?> getSchemaMapBuilders() {
        return schemaMapBuilders;
    }

    public DDLSchemaCachedObjects<?> getDDLSchemaCachedObjects() {
        return ddlSchemaCachedObjects;
    }

    public SchemaObjectIdAllocator<P> getSchemaObjectIdAllocator() {
        return schemaObjectIdAllocator;
    }
}
