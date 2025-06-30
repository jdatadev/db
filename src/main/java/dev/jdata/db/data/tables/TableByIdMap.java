package dev.jdata.db.data.tables;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CheckedExceptionFunction;

public final class TableByIdMap<T> extends SchemaObjectByIdMap<Table, T> {

    public <E extends Exception> TableByIdMap(IEffectiveDatabaseSchema databaseSchema, IntFunction<T[]> createArray, CheckedExceptionFunction<Table, T, E> elementMapper)
            throws E {

        super(DDLObjectType.TABLE, databaseSchema, createArray, elementMapper);
    }

    public T getTable(int tableId) {

        Checks.isTableId(tableId);

        return getObject(tableId);
    }
}
