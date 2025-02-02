package dev.jdata.db.engine.transactions;

import java.util.Objects;

import dev.jdata.db.engine.transactions.mvcc.ComparisonOperator;
import dev.jdata.db.utils.checks.Checks;

public final class SelectColumn {

    public enum SelectColumnOperatorType {

        NULLEDNESS,
        COMPARISON,
        LIKE;
    }

    public enum Nulledness {

        IS_NULL,
        IS_NOT_NULL;
    }

    private int tableColumn;
    private SelectColumnOperatorType operatorType;
    private Nulledness nulledness;
    private ComparisonOperator comparisonOperator;
    private RowValue rowValue;

    public void initialize(int tableColumn, Nulledness nulledness) {

        initialize(tableColumn, SelectColumnOperatorType.NULLEDNESS);
    }

    public void initialize(int tableColumn, ComparisonOperator comparisonOperator, RowValue rowValue) {

        initialize(tableColumn, SelectColumnOperatorType.COMPARISON);

        this.comparisonOperator = Objects.requireNonNull(comparisonOperator);
        this.rowValue = Objects.requireNonNull(rowValue);
    }

    private void initialize(int tableColumn, SelectColumnOperatorType operatorType) {

        this.tableColumn = Checks.isColumnIndex(tableColumn);
        this.operatorType = Objects.requireNonNull(operatorType);
    }

    public int getTableColumn() {
        return tableColumn;
    }

    public SelectColumnOperatorType getOperatorType() {
        return operatorType;
    }

    public Nulledness getNulledness() {
        return nulledness;
    }

    public ComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public RowValue getRowValue() {
        return rowValue;
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [tableColumn=" + tableColumn + ", comparisonOperator=" + comparisonOperator + ", rowValue=" + rowValue + "]";
    }
}
