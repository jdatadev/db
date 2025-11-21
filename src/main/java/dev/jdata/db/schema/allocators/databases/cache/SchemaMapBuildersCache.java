package dev.jdata.db.schema.allocators.databases.cache;

import java.util.function.BiFunction;
import java.util.function.Function;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.CachedSchemaMapBuilder;
import dev.jdata.db.utils.allocators.NodeObjectCache;

@Deprecated // currently not in use
final class SchemaMapBuildersCache extends NodeDDLObjectTypeCaches<CachedSchemaMapBuilder<? extends SchemaObject>> {

    private SchemaMapBuildersCache(
            BiFunction<
                            DDLObjectType,
                            Function<DDLObjectType, NodeObjectCache<CachedSchemaMapBuilder<? extends SchemaObject>>>,
                            CachedSchemaMapBuilder<? extends SchemaObject>> createInstance) {
        super(createInstance);

        throw new UnsupportedOperationException();
    }

    @Override
    Class<CachedSchemaMapBuilder<? extends SchemaObject>> getCachedObjectClass() {

        throw new UnsupportedOperationException();
    }
}
