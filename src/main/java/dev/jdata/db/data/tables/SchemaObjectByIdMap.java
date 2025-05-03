package dev.jdata.db.data.tables;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CheckedExceptionFunction;

abstract class SchemaObjectByIdMap<T extends SchemaObject, U> {

    private final U[] objects;

    <E extends Exception> SchemaObjectByIdMap(DDLObjectType ddlObjectType, IEffectiveDatabaseSchema databaseSchema, IntFunction<U[]> createArray,
            CheckedExceptionFunction<T, U, E> elementMapper) throws E {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(databaseSchema);
        Objects.requireNonNull(createArray);
        Objects.requireNonNull(elementMapper);

        final int arrayLength = databaseSchema.computeMaxId(ddlObjectType, -1) + 1;

        this.objects = createArray.apply(arrayLength);

        final IIndexList<T> schemaObjectList = databaseSchema.getSchemaObjects(ddlObjectType);

        final long numTables = schemaObjectList.getNumElements();

        for (long i = 0L; i < numTables; ++ i) {

            final T schemaObject = schemaObjectList.get(i);

            objects[schemaObject.getId()] = elementMapper.apply(schemaObject);
        }
    }

    final U getObject(int schemaObjectId) {

        Checks.isSchemaObjectId(schemaObjectId);

        return objects[schemaObjectId];
    }
}
