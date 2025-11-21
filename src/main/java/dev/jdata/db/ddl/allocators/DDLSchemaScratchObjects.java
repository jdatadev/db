package dev.jdata.db.ddl.allocators;

import java.util.Objects;

import dev.jdata.db.ddl.scratchobjects.ProcessAlterTableScratchObject;
import dev.jdata.db.ddl.scratchobjects.ProcessCreateTableScratchObject;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.utils.adt.lists.ICachedIndexListAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public class DDLSchemaScratchObjects implements IAllocators {

    private final ICachedIndexListAllocator<Column> columnIndexListAllocator;
    private final NodeObjectCache<ProcessCreateTableScratchObject> processCreateTableScratchCache;
    private final NodeObjectCache<ProcessAlterTableScratchObject> processAlterTableScratchCache;

    public DDLSchemaScratchObjects() {

        this.columnIndexListAllocator = ICachedIndexListAllocator.create(Column[]::new);
        this.processCreateTableScratchCache = new NodeObjectCache<>(ProcessCreateTableScratchObject::new);
        this.processAlterTableScratchCache = new NodeObjectCache<>(ProcessAlterTableScratchObject::new);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("columnIndexListAllocator", RefType.INSTANTIATED, Column.class, columnIndexListAllocator);
        statisticsGatherer.addNodeObjectCache("processCreateTableScratchCache", ProcessCreateTableScratchObject.class, processCreateTableScratchCache);
        statisticsGatherer.addNodeObjectCache("processAlterTableScratchCache", ProcessAlterTableScratchObject.class, processAlterTableScratchCache);
    }

    public final ICachedIndexListAllocator<Column> getColumnIndexListAllocator() {
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
