package dev.jdata.db.schema.model.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.SchemaObject;

public abstract class AllSimpleCompleteSchemaMapsBuilder<

                T extends SchemaMap<SchemaObject, ?, ?>,
                U extends AllCompleteSchemaMaps<T>,
                V extends AllSimpleCompleteSchemaMapsBuilder<T, U, V>>

        extends SimpleCompleteSchemaMapsBuilder<T, U, V> {

    protected AllSimpleCompleteSchemaMapsBuilder(IntFunction<T[]> createSchemaMapsArray) {
        super(createSchemaMapsArray);
    }
}
