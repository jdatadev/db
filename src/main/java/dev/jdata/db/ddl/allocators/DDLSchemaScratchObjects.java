package dev.jdata.db.ddl.allocators;

import dev.jdata.db.ddl.scratchobjects.ProcessAlterTableScratchObject;
import dev.jdata.db.ddl.scratchobjects.ProcessCreateTableScratchObject;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.utils.adt.lists.CachedIndexList.CacheIndexListAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public class DDLSchemaScratchObjects {

    private final CacheIndexListAllocator<Column> columnIndexListAllocator;
    private final NodeObjectCache<ProcessCreateTableScratchObject> processCreateTableScratchCache;
    private final NodeObjectCache<ProcessAlterTableScratchObject> processAlterTableScratchCache;

    public DDLSchemaScratchObjects() {

        this.columnIndexListAllocator = new CacheIndexListAllocator<>(Column[]::new);
        this.processCreateTableScratchCache = new NodeObjectCache<>(ProcessCreateTableScratchObject::new);
        this.processAlterTableScratchCache = new NodeObjectCache<>(ProcessAlterTableScratchObject::new);
    }

    public final CacheIndexListAllocator<Column> getColumnIndexListAllocator() {
        return columnIndexListAllocator;
    }

    public final ProcessCreateTableScratchObject allocateProcessCreateTableScratchObject() {

        return processCreateTableScratchCache.allocate();
    }

    public final void freeProcessCreateTableScratchObject(ProcessCreateTableScratchObject processCreateTableScratchObject) {

        processCreateTableScratchCache.free(processCreateTableScratchObject);
    }

    public final ProcessAlterTableScratchObject allocateProcessAlterTableScratchObject() {

        return processAlterTableScratchCache.allocate();
    }

    public final void freeProcessAlterTableScratchObject(ProcessAlterTableScratchObject processAlterTableScratchObject) {

        processAlterTableScratchCache.free(processAlterTableScratchObject);
    }
}
