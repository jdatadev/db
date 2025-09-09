package dev.jdata.db.schema.allocators.databases.cache;

import java.util.function.BiFunction;
import java.util.function.Function;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

@Deprecated // currently not in use
abstract class NodeDDLObjectTypeCaches<T extends ObjectCacheNode> extends BaseDDLObjectTypeCaches<T, NodeObjectCache<T>> {

    NodeDDLObjectTypeCaches(BiFunction<DDLObjectType, Function<DDLObjectType, NodeObjectCache<T>>, T> createInstance) {
        super(NodeObjectCache[]::new, (t, g) -> new NodeObjectCache<>(() -> createInstance.apply(t, g)));
    }
}
