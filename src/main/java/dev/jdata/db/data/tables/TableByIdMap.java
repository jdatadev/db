package dev.jdata.db.data.tables;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.Table;
import dev.jdata.db.utils.function.CheckedExceptionFunction;

public final class TableByIdMap<T> {

    private final T[] tables;

    public <E extends Exception> TableByIdMap(DatabaseSchema databaseSchema, IntFunction<T[]> createArray, CheckedExceptionFunction<Table, T, E> elementMapper) throws E {

        Objects.requireNonNull(databaseSchema);
        Objects.requireNonNull(createArray);

        final int arraySize = databaseSchema.getMaxTableId() + 1;

        this.tables = createArray.apply(arraySize);

        for (Table table : databaseSchema.getTables().getSchemaObjects()) {

            tables[table.getId()] = elementMapper.apply(table);
        }
    }

    public T getTable(int tableId) {

        return tables[tableId];
    }
}
