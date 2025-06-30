package dev.jdata.db.ddl.scratchobjects;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

abstract class ProcessParsedScratchObject extends ObjectCacheNode implements IResettable {

    private StringManagement stringManagement;

    final void initialize(StringManagement stringManagement) {

        this.stringManagement = Initializable.checkNotYetInitialized(this.stringManagement, stringManagement);
    }

    @Override
    public void reset() {

        this.stringManagement = Initializable.checkResettable(stringManagement);
    }

    public final StringManagement getStringManagement() {
        return stringManagement;
    }

    final long getParsedStringRef(long stringRef) {

        return stringManagement.resolveParsedStringRef(stringRef);
    }
}
