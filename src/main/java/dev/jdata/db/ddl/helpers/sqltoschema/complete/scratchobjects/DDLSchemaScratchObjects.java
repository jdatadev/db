package dev.jdata.db.ddl.helpers.sqltoschema.complete.scratchobjects;

import java.util.Objects;

import dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects.ProcessAlterTableScratchObject;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects.ProcessCreateTableScratchObject;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.classes.Classes;

@Deprecated // rename all such to allocators
public class DDLSchemaScratchObjects<T extends IIntSetBuilder<?, ?>, U extends IIndexListBuilder<Column, ?, ?>> implements IAllocators {

    private final NodeObjectCache<ProcessCreateTableScratchObject> processCreateTableScratchCache;
    private final NodeObjectCache<ProcessAlterTableScratchObject<T, U>> processAlterTableScratchCache;

    public DDLSchemaScratchObjects() {

        this.processCreateTableScratchCache = new NodeObjectCache<>(ProcessCreateTableScratchObject::new);
        this.processAlterTableScratchCache = new NodeObjectCache<>(ProcessAlterTableScratchObject::new);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addNodeObjectCache("processCreateTableScratchCache", ProcessCreateTableScratchObject.class, processCreateTableScratchCache);
        statisticsGatherer.addNodeObjectCache("processAlterTableScratchCache", Classes.genericClass(ProcessAlterTableScratchObject.class), processAlterTableScratchCache);
    }

    public final ProcessCreateTableScratchObject allocateProcessCreateTableScratchObject() {

        return processCreateTableScratchCache.allocate();
    }

    public final void freeProcessCreateTableScratchObject(ProcessCreateTableScratchObject processCreateTableScratchObject) {

        processCreateTableScratchCache.free(processCreateTableScratchObject);
    }

    public final ProcessAlterTableScratchObject<T, U> allocateProcessAlterTableScratchObject() {

        return processAlterTableScratchCache.allocate();
    }

    public final void freeProcessAlterTableScratchObject(ProcessAlterTableScratchObject<T, U> processAlterTableScratchObject) {

        processAlterTableScratchCache.free(processAlterTableScratchObject);
    }
}
