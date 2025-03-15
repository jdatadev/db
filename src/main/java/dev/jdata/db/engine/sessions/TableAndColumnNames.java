package dev.jdata.db.engine.sessions;

import java.util.Objects;

import org.jutils.io.strings.StringRef;

import dev.jdata.db.common.StringRefLookup;
import dev.jdata.db.schema.Column;
import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.Table;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.sql.ast.statements.dml.SQLObjectName;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.maps.LongToIntMap;
import dev.jdata.db.utils.adt.maps.LongToIntMapGetters;
import dev.jdata.db.utils.checks.Checks;

public final class TableAndColumnNames {

    public interface LongToIntMapAllocator {

        LongToIntMap allocateLongToIntMap(int initialCapacityExponent);

        void freeLongToIntMap(LongToIntMap longToIntMap);
    }

    private final LongToIntMap tableIdByTableName;

    private DatabaseSchema databaseSchema;
    private LongToIntMap[] columnNameToIndexMapsByTableId;
    private int maxTableId;

    public TableAndColumnNames() {

        this.tableIdByTableName = new LongToIntMap(0);
    }

    public void initialize(DatabaseSchema databaseSchema, LongToIntMapAllocator allocator, StringRefLookup stringRefLookup) {

        Objects.requireNonNull(databaseSchema);
        Objects.requireNonNull(allocator);
        Objects.requireNonNull(stringRefLookup);

        this.databaseSchema = databaseSchema;

        final int newMaxTableId = databaseSchema.getMaxTableId();

        final int arrayLength = columnNameToIndexMapsByTableId.length;

        final int oldMaxTableId = maxTableId;

        for (int i = 0; i <= oldMaxTableId; ++ i) {

            final LongToIntMap longToIntMap = columnNameToIndexMapsByTableId[i];

            if (longToIntMap != null) {

                allocator.freeLongToIntMap(longToIntMap);

                columnNameToIndexMapsByTableId[i] = null;
            }
        }

        if (newMaxTableId >= arrayLength) {

            this.columnNameToIndexMapsByTableId = new LongToIntMap[newMaxTableId + 1];
        }

        tableIdByTableName.clear();

        for (int tableId = 0; tableId <= newMaxTableId; ++ tableId) {

            final Table table = databaseSchema.getTable(tableId);

            final long tableNameStringRef = stringRefLookup.getStringRef(table.getName());

            tableIdByTableName.put(tableNameStringRef, tableId);

            final int numColumns = table.getNumColumns();

            final int capacityExponent = CapacityExponents.computeCapacityExponent(numColumns);

            final LongToIntMap longToIntMap = allocator.allocateLongToIntMap(capacityExponent);

            for (int columnIndex = 0; columnIndex < numColumns; ++ columnIndex) {

                final Column tableColumn = table.getColumn(columnIndex);

                final String columnName = tableColumn.getName();

                final long columnNameStringRef = stringRefLookup.getStringRef(columnName);

                longToIntMap.put(columnNameStringRef, columnIndex);
            }

            columnNameToIndexMapsByTableId[tableId] = longToIntMap;
        }

        this.maxTableId = newMaxTableId;
    }

    public int getTableId(long tableName) {

        StringRef.checkIsString(tableName);

        return tableIdByTableName.get(tableName);
    }

    public int getTableId(SQLObjectName objectName) {

        Objects.requireNonNull(objectName);

        return getTableId(objectName.getName());
    }

    public Table getTable(int tableId) {

        Checks.isTableId(tableId);

        return databaseSchema.getTable(tableId);
    }

    public LongToIntMapGetters getColumnIndices(int tableId) {

        checkTableId(tableId);

        return columnNameToIndexMapsByTableId[tableId];
    }

    public int getColumnIndex(int tableId, long columnName) {

        checkTableId(tableId);
        StringRef.checkIsString(columnName);

        return columnNameToIndexMapsByTableId[tableId].get(columnName);
    }

    public SchemaDataType getSchemaDataType(int tableId, int columnIndex) {

        checkTableId(tableId);
        Checks.isColumnIndex(columnIndex);

        return databaseSchema.getTable(tableId).getColumn(columnIndex).getSchemaType();
    }

    private void checkTableId(int tableId) {

        Objects.checkIndex(tableId, maxTableId + 1);
    }
}
