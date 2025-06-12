package dev.jdata.db.ddl;

import java.util.Objects;

import dev.jdata.db.ddl.DDLAlterTableSchemasHelper.ProcessAlterTableScratchObject;
import dev.jdata.db.ddl.DDLCreateTableSchemasHelper.ProcessCreateTableScratchObject;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.schemamaps.SchemaMapBuilders;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IndexList.CacheIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.function.BiObjToIntFunction;

public class DDLSchemasHelper {

    public static final class SchemaObjectIdAllocator<P> extends ObjectCacheNode implements IResettable {

        private P parameter;
        private BiObjToIntFunction<DDLObjectType, P> allocateSchemaObjectIdFunction;

        public SchemaObjectIdAllocator(AllocationType allocationType, P parameter, BiObjToIntFunction<DDLObjectType, P> allocateSchemaObjectIdFunction) {
            super(allocationType);

            initialize(parameter, allocateSchemaObjectIdFunction);
        }

        public void initialize(P parameter, BiObjToIntFunction<DDLObjectType, P> allocateSchemaObjectIdFunction) {

            checkIsAllocated();

            this.parameter = parameter;
            this.allocateSchemaObjectIdFunction = Initializable.checkNotYetInitialized(this.allocateSchemaObjectIdFunction, allocateSchemaObjectIdFunction);
        }

        @Override
        public void reset() {

            checkIsAllocated();

            this.parameter = null;
            this.allocateSchemaObjectIdFunction = Initializable.checkResettable(allocateSchemaObjectIdFunction);
        }

        public int allocateSchemaObjectId(DDLObjectType ddlObjectType) {

            Objects.requireNonNull(ddlObjectType);

            checkIsAllocated();

            return allocateSchemaObjectIdFunction.apply(ddlObjectType, parameter);
        }
    }

    public static class DDLSchemaCachedObjects {

        private final SchemaMapBuilderAllocators schemaMapBuilderAllocators;

        private final IndexListAllocator<Column> columnIndexListAllocator;
        private final NodeObjectCache<ProcessCreateTableScratchObject> processCreateTableScratchCache;
        private final NodeObjectCache<ProcessAlterTableScratchObject> processAlterTableScratchCache;
        private final NodeObjectCache<SchemaMapBuilders> schemaMapBuildersCache;

        DDLSchemaCachedObjects(SchemaMapBuilderAllocators schemaMapBuilderAllocators) {

            this.schemaMapBuilderAllocators = Objects.requireNonNull(schemaMapBuilderAllocators);

            this.columnIndexListAllocator = new CacheIndexListAllocator<>(Column[]::new);
            this.processCreateTableScratchCache = new NodeObjectCache<>(ProcessCreateTableScratchObject::new);
            this.processAlterTableScratchCache = new NodeObjectCache<>(ProcessAlterTableScratchObject::new);
            this.schemaMapBuildersCache = new NodeObjectCache<>(SchemaMapBuilders::new);
        }

        public final IndexListAllocator<Column> getColumnIndexListAllocator() {
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

        public final SchemaMapBuilders allocateSchemaMapBuilders() {

            final SchemaMapBuilders result =  schemaMapBuildersCache.allocate();

            result.initialize(schemaMapBuilderAllocators);

            return result;
        }

        public final void freeSchemaMapBuilders(SchemaMapBuilders schemaMapBuilders) {

            schemaMapBuilders.reset(schemaMapBuilderAllocators);

            schemaMapBuildersCache.free(schemaMapBuilders);
        }
    }

    static abstract class ProcessParsedScratchObject extends ObjectCacheNode implements IResettable {

        private StringManagement stringManagement;

        final void initialize(StringManagement stringManagement) {

            this.stringManagement = Initializable.checkNotYetInitialized(this.stringManagement, stringManagement);
        }

        @Override
        public void reset() {

            this.stringManagement = Initializable.checkResettable(stringManagement);
        }

        final StringManagement getStringManagement() {
            return stringManagement;
        }

        final long getParsedStringRef(long stringRef) {

            return stringManagement.resolveParsedStringRef(stringRef);
        }
    }
}
