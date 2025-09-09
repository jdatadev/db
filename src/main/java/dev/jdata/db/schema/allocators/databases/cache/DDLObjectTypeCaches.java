package dev.jdata.db.schema.allocators.databases.cache;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.utils.allocators.ObjectCache;

@Deprecated // currently not in use
abstract class DDLObjectTypeCaches<T> extends BaseDDLObjectTypeCaches<T, ObjectCache<T>> {

    DDLObjectTypeCaches(IntFunction<T[]> createArray, BiFunction<DDLObjectType, Function<DDLObjectType, ObjectCache<T>>, T> createInstance) {
        super(ObjectCache[]::new, (t, g) -> new ObjectCache<>(() -> createInstance.apply(t, g), createArray));
    }
}
