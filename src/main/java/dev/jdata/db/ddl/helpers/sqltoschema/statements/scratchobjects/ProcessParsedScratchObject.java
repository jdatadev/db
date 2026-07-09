package dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects;

import dev.jdata.db.ddl.helpers.sqltoschema.statements.ISQLToSchemaStringManagement;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

abstract class ProcessParsedScratchObject extends ObjectCacheNode implements IResettable {

    private ISQLToSchemaStringManagement sqlToSchemaStringManagement;

    ProcessParsedScratchObject(AllocationType allocationType) {
        super(allocationType);
    }

    final void initialize(ISQLToSchemaStringManagement sqlToSchemaStringManagement) {

        this.sqlToSchemaStringManagement = Initializable.checkNotYetInitialized(this.sqlToSchemaStringManagement, sqlToSchemaStringManagement);
    }

    @Override
    public void reset() {

        this.sqlToSchemaStringManagement = Initializable.checkResettable(sqlToSchemaStringManagement);
    }

    public final ISQLToSchemaStringManagement getSQLToSchemaStringManagement() {
        return sqlToSchemaStringManagement;
    }

    private long storeParsedStringRef(long stringRef) {

        return sqlToSchemaStringManagement.storeParsedStringRef(stringRef);
    }
}
