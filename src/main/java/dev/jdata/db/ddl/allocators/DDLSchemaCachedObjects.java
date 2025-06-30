package dev.jdata.db.ddl.allocators;

import java.util.Objects;
import java.util.function.Supplier;

import dev.jdata.db.ddl.scratchobjects.ProcessAlterTableScratchObject;
import dev.jdata.db.ddl.scratchobjects.ProcessCreateTableScratchObject;
import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.schemamaps.SchemaMapBuilders;
import dev.jdata.db.utils.adt.lists.CachedIndexList.CacheIndexListAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public class DDLSchemaCachedObjects<T extends SchemaMapBuilders<?, ?, ?, ?, ?, ?, ?>> {

    private final SchemaMapBuilderAllocators schemaMapBuilderAllocators;

    private final CacheIndexListAllocator<Column> columnIndexListAllocator;
    private final NodeObjectCache<ProcessCreateTableScratchObject> processCreateTableScratchCache;
    private final NodeObjectCache<ProcessAlterTableScratchObject> processAlterTableScratchCache;
    private final NodeObjectCache<T> schemaMapBuildersCache;

    DDLSchemaCachedObjects(SchemaMapBuilderAllocators schemaMapBuilderAllocators, Supplier<T> createSchemaMapBuilders) {

        Objects.requireNonNull(schemaMapBuilderAllocators);
        Objects.requireNonNull(createSchemaMapBuilders);

        this.schemaMapBuilderAllocators = Objects.requireNonNull(schemaMapBuilderAllocators);

        this.columnIndexListAllocator = new CacheIndexListAllocator<>(Column[]::new);
        this.processCreateTableScratchCache = new NodeObjectCache<>(ProcessCreateTableScratchObject::new);
        this.processAlterTableScratchCache = new NodeObjectCache<>(ProcessAlterTableScratchObject::new);
        this.schemaMapBuildersCache = new NodeObjectCache<>(createSchemaMapBuilders);
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

    public final T allocateSchemaMapBuilders() {

        final T result =  schemaMapBuildersCache.allocate();

        result.initialize(schemaMapBuilderAllocators);

        return result;
    }

    public final void freeSchemaMapBuilders(T schemaMapBuilders) {

        Objects.requireNonNull(schemaMapBuilders);

        schemaMapBuilders.reset(schemaMapBuilderAllocators);

        schemaMapBuildersCache.free(schemaMapBuilders);
    }
}
