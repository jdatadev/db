package dev.jdata.db.ddl.helpers.sqltoschema.complete;

import java.util.Objects;
import java.util.function.ToIntFunction;

import dev.jdata.db.ddl.helpers.sqltoschema.complete.scratchobjects.DDLSchemaScratchObjects;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

abstract class DDLSchemaParameter<T extends IIntSetBuilder<?, ?>, U extends IIndexListBuilder<Column, ?, ?>> extends ObjectCacheNode implements IResettable {

    private StringManagement stringManagement;
    private DDLSchemaScratchObjects<T, U> ddlSchemaScratchObjects;
    private ToIntFunction<DDLObjectType> schemaObjectIdAllocator;

    DDLSchemaParameter(AllocationType allocationType) {
        super(allocationType);
    }

    final void initialize(StringManagement stringManagement, DDLSchemaScratchObjects<T, U> ddlSchemaScratchObjects, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

        this.stringManagement = Initializable.checkNotYetInitialized(this.stringManagement, stringManagement);
        this.ddlSchemaScratchObjects = Initializable.checkNotYetInitialized(this.ddlSchemaScratchObjects, ddlSchemaScratchObjects);
        this.schemaObjectIdAllocator = Initializable.checkNotYetInitialized(this.schemaObjectIdAllocator, schemaObjectIdAllocator);
    }

    @Override
    public void reset() {

        this.stringManagement = Initializable.checkResettable(stringManagement);
        this.ddlSchemaScratchObjects = Initializable.checkResettable(ddlSchemaScratchObjects);
        this.schemaObjectIdAllocator = Initializable.checkResettable(schemaObjectIdAllocator);
    }

    final StringManagement getStringManagement() {
        return stringManagement;
    }

    final DDLSchemaScratchObjects<T, U> getDDLSchemaScratchObjects() {
        return ddlSchemaScratchObjects;
    }

    final ToIntFunction<DDLObjectType> getSchemaObjectIdAllocator() {
        return schemaObjectIdAllocator;
    }

    final int allocateSchemaObjectId(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return schemaObjectIdAllocator.applyAsInt(ddlObjectType);
    }
}
