package dev.jdata.db.engine.transactions;

import java.util.Objects;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.arrays.IObjectArray;
import dev.jdata.db.utils.adt.maps.MutableIntToObjectMaxDistanceNonBucketMap;
import dev.jdata.db.utils.adt.sets.ILongSetGetters;
import dev.jdata.db.utils.adt.sets.IMutableLongSet;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

public final class TransactionSelect extends ObjectCacheNode implements IClearable {

    public enum ConditionOperator {

        AND,
        OR;
    }

    public interface TransactionSelectAllocator {

        TransactionSelect allocateTransactionSelect();

        void freeTransactionSelect(TransactionSelect transactionSelect);
    }

    private int tableId;
    private ConditionOperator conditionOperator;
    private IObjectArray<SelectColumn> selectColumns;
    private ILongSetGetters rowIdsToFilter;
    private StringLookup stringLookup;
    private final MutableIntToObjectMaxDistanceNonBucketMap<SelectColumn> selectColumnsMap;

    public TransactionSelect() {

        this.selectColumnsMap = new MutableIntToObjectMaxDistanceNonBucketMap<>(0, SelectColumn[]::new);
    }

    public void initialize(int tableId, ConditionOperator conditionOperator, IObjectArray<SelectColumn> selectColumns, IMutableLongSet rowIdsToFilter,
            StringLookup stringLookup) {

        this.tableId = Checks.isTableId(tableId);

        final long numColumns = selectColumns.getLimit();

        this.conditionOperator = numColumns > 1 ? Objects.requireNonNull(conditionOperator) : conditionOperator;

        this.selectColumns = Objects.requireNonNull(selectColumns);
        this.rowIdsToFilter = Objects.requireNonNull(rowIdsToFilter);
        this.stringLookup = Objects.requireNonNull(stringLookup);

        if (!selectColumnsMap.isEmpty()) {

            throw new IllegalStateException();
        }

        for (int i = 0; i < numColumns; ++ i) {

            final SelectColumn selectColumn = selectColumns.get(i);

            selectColumnsMap.put(selectColumn.getTableColumn(), selectColumn);
        }
    }

    @Override
    public void clear() {

        selectColumnsMap.clear();
    }

    public int getTableId() {
        return tableId;
    }

    public ConditionOperator getConditionOperator() {
        return conditionOperator;
    }

    public boolean containsSelectColumnByTableColumn(int tableColumn) {

        Checks.isColumnIndex(tableColumn);

        return selectColumnsMap.containsKey(tableColumn);
    }

    public SelectColumn getSelectColumnByTableColumn(int tableColumn) {

        Checks.isColumnIndex(tableColumn);

        return selectColumnsMap.get(tableColumn);
    }

    public SelectColumn getSelectColumn(int index) {

        Checks.isIndex(index);

        return selectColumns.get(index);
    }

    public int getNumSelectColumns() {

        return (int)selectColumns.getLimit();
    }

    public ILongSetGetters getRowIdsToFilter() {
        return rowIdsToFilter;
    }

    public StringLookup getStringLookup() {
        return stringLookup;
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [tableId=" + tableId + ", conditionOperator=" + conditionOperator + ", selectColumns=" + selectColumns +
                ", rowIdsToFilter=" + rowIdsToFilter + ", stringLookup=" + stringLookup + ", selectColumnsMap=" + selectColumnsMap + "]";
    }
}
