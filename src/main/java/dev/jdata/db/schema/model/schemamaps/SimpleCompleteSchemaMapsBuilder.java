package dev.jdata.db.schema.model.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.SchemaObject;

public abstract class SimpleCompleteSchemaMapsBuilder<

                T extends SchemaMap<? extends SchemaObject, ?, ?>,
                U extends CompleteSchemaMaps<T>,
                V extends SimpleCompleteSchemaMapsBuilder<T, U, V>>

        extends BaseSimpleSchemaMapsBuilder<T, V> {

    SimpleCompleteSchemaMapsBuilder(IntFunction<T[]> createSchemaMapsArray) {
        super(createSchemaMapsArray);
    }

    public abstract U build();
}
