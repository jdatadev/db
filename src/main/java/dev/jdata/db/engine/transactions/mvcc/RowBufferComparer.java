package dev.jdata.db.engine.transactions.mvcc;

import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.DebugConstants;
import dev.jdata.db.data.RowDataNumBitsAndOffsets;
import dev.jdata.db.engine.transactions.RowValue;
import dev.jdata.db.engine.transactions.SelectColumn;
import dev.jdata.db.engine.transactions.SelectColumn.Nulledness;
import dev.jdata.db.engine.transactions.SelectColumn.SelectColumnOperatorType;
import dev.jdata.db.engine.transactions.TransactionSelect;
import dev.jdata.db.engine.transactions.TransactionSelect.ConditionOperator;
import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.buffers.BitBuffer;
import dev.jdata.db.utils.adt.maps.IHeapMutableIntToIntNonRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IBaseMutableLongSet;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

final class RowBufferComparer implements IClearable, PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_MVCC_ROW_BUFFER_COMPARER;

    private static final boolean ASSERT = AssertionContants.ASSERT_MVCC_ROW_BUFFER_COMPARER;

    private static final int NO_VALUE = -1;

    private final IHeapMutableIntToIntNonRemoveStaticMap scratchRowDataColumnIndexByTableColumn;
    private final int[] scratchTableColumnsKeysArray;
    private final RowDataNumBitsAndOffsets scratchDataNumBitsAndOffsets;

    private final ValueComparer valueComparer;

    RowBufferComparer() {

        if (DEBUG) {

            enter();
        }

        this.scratchRowDataColumnIndexByTableColumn = IHeapMutableIntToIntNonRemoveStaticMap.create(0);
        this.scratchTableColumnsKeysArray = new int[DBConstants.MAX_COLUMNS];
        this.scratchDataNumBitsAndOffsets = new RowDataNumBitsAndOffsets(DBConstants.MAX_COLUMNS);

        this.valueComparer = new ValueComparer();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        scratchRowDataColumnIndexByTableColumn.clear();
        scratchDataNumBitsAndOffsets.clear();
        valueComparer.clear();

        if (DEBUG) {

            exit();
        }
    }

    long compareRowForInsertOperation(TransactionSelect select, BitBuffer mvccBitBuffer, long startBufferBitOffset, IBaseMutableLongSet addedRowIdsDst) {

        Objects.requireNonNull(select);
        Objects.requireNonNull(mvccBitBuffer);
        Checks.isBufferBitsOffset(startBufferBitOffset);
        Objects.requireNonNull(addedRowIdsDst);

        if (DEBUG) {

            enter(b -> b.add("select", select).add("mvccBitBuffer", mvccBitBuffer).add("startBufferBitOffset", startBufferBitOffset).add("addedRowIdsDst", addedRowIdsDst));
        }

        final long result = compareRowsForInsertOperation(select, mvccBitBuffer, startBufferBitOffset, addedRowIdsDst, IBaseMutableLongSet::addUnordered);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    long compareRowForUpdateOperation(TransactionSelect select, BitBuffer mvccBitBuffer, long startBufferBitOffset, BufferedRows commitedRows, IBaseMutableLongSet addedRowIdsDst,
            IBaseMutableLongSet removedRowIdsDst) {

        Objects.requireNonNull(select);
        Objects.requireNonNull(mvccBitBuffer);
        Checks.isBufferBitsOffset(startBufferBitOffset);
        Objects.requireNonNull(commitedRows);
        Objects.requireNonNull(addedRowIdsDst);
        Objects.requireNonNull(removedRowIdsDst);

        if (DEBUG) {

            enter(b -> b.add("select", select).add("mvccBitBuffer", mvccBitBuffer).add("startBufferBitOffset", startBufferBitOffset).add("commitedRows", commitedRows)
                    .add("addedRowIdsDst", addedRowIdsDst).add("removedRowIdsDst", removedRowIdsDst));
        }

        final long result = compareRowsForUpdateOperation(select, mvccBitBuffer, startBufferBitOffset, commitedRows, addedRowIdsDst, IBaseMutableLongSet::addUnordered,
                removedRowIdsDst, IBaseMutableLongSet::addUnordered);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @FunctionalInterface
    private interface RowIdAdder<T> {

        void addRowId(T instance, long rowId);
    }

    private <T, U> long compareRowsForInsertOperation(TransactionSelect select, BitBuffer mvccBitBuffer, long startBufferBitOffset, T addedRowsInstance,
            RowIdAdder<T> addedRowIdsDst) {

        if (DEBUG) {

            enter(b -> b.add("select", select).add("mvccBitBuffer", mvccBitBuffer).add("startBufferBitOffset", startBufferBitOffset).add("addedRowsInstance", addedRowsInstance)
                    .add("addedRowIdsDst", addedRowIdsDst));
        }

        long bufferBitOffset = getTableColumnsFromBitBuffer(select, mvccBitBuffer, startBufferBitOffset);

        if (bufferBitOffset != -1L) {

            final long numRows = mvccBitBuffer.getUnsignedLong(bufferBitOffset);

            bufferBitOffset += 64L;

            final int numSelectColumns = select.getNumSelectColumns();

            if (DEBUG) {

                debug("retrieved number of rows from bit buffer", b -> b.add("numRows", numRows).add("numSelectColumns", numSelectColumns));
            }

            Boolean matches = null;

            for (long rowIndex = 0; rowIndex < numRows; ++ rowIndex) {

                final long rowId = mvccBitBuffer.getUnsignedLong(bufferBitOffset);

                bufferBitOffset += 64L;

                for (int i = 0; i < numSelectColumns; ++ i) {

                    final SelectColumn selectColumn = select.getSelectColumn(i);

                    final int tableColumn = selectColumn.getTableColumn();

                    final boolean columnMatches = compareColumn(select, selectColumn, mvccBitBuffer, tableColumn, bufferBitOffset);

                    matches = applyConditionOperator(select.getConditionOperator(), columnMatches, matches);

                    if (!matches) {

                        break;
                    }
                }

                final int totalNumRowDataBits = scratchDataNumBitsAndOffsets.getTotalNumRowDataBits();

                bufferBitOffset += totalNumRowDataBits;

                if (DEBUG) {

                    final Boolean closureMatches = matches;

                    debug("compared row", b -> b.add("rowId", rowId).add("matches", closureMatches).add("totalNumRowDataBits", totalNumRowDataBits));
                }

                if (matches) {

                    addedRowIdsDst.addRowId(addedRowsInstance, rowId);
                }
            }
        }

        if (DEBUG) {

            exit(bufferBitOffset);
        }

        return bufferBitOffset;
    }

    private <T, U> long compareRowsForUpdateOperation(TransactionSelect select, BitBuffer mvccBitBuffer, long startBufferBitOffset, BufferedRows committedRows,
            T addedRowsInstance, RowIdAdder<T> addedRowIdsDst, U removedRowsInstance, RowIdAdder<U> removedRowIdsDst) {

        if (DEBUG) {

            enter(b -> b.add("select", select).add("mvccBitBuffer", mvccBitBuffer).add("startBufferBitOffset", startBufferBitOffset).add("addedRowsInstance", addedRowsInstance)
                    .add("addedRowIdsDst", addedRowIdsDst).add("removedRowsInstance", removedRowsInstance).add("removedRowIdsDst", removedRowIdsDst));
        }

        final int tableId = select.getTableId();

        long bufferBitOffset = getTableColumnsFromBitBuffer(select, mvccBitBuffer, startBufferBitOffset);

        if (bufferBitOffset != -1L) {

            final long numRows = mvccBitBuffer.getUnsignedLong(bufferBitOffset);

            bufferBitOffset += 64L;

            final int numSelectColumns = select.getNumSelectColumns();

            if (DEBUG) {

                debug("retrieved number of rows from bit buffer", b -> b.add("numRows", numRows).add("numColumnsToCompare", numSelectColumns));
            }

            Boolean matches = null;

            for (long rowIndex = 0; rowIndex < numRows; ++ rowIndex) {

                final long rowId = mvccBitBuffer.getUnsignedLong(bufferBitOffset);

                bufferBitOffset += 64L;

                for (int i = 0; i < numSelectColumns; ++ i) {

                    final SelectColumn selectColumn = select.getSelectColumn(i);

                    final int tableColumn = selectColumn.getTableColumn();

                    final int rowDataColumnIndex = scratchRowDataColumnIndexByTableColumn.get(tableColumn);

                    final boolean columnMatches;

                    if (rowDataColumnIndex != NO_VALUE) {

                        columnMatches = compareColumn(select, selectColumn, mvccBitBuffer, tableColumn, bufferBitOffset);
                    }
                    else {
                        columnMatches = committedRows.compareColumn(tableId, rowId, selectColumn, tableColumn);
                    }

                    matches = applyConditionOperator(select.getConditionOperator(), columnMatches, matches);

                    if (!matches) {

                        break;
                    }
                }

                final int totalNumRowDataBits = scratchDataNumBitsAndOffsets.getTotalNumRowDataBits();

                bufferBitOffset += totalNumRowDataBits;

                if (DEBUG) {

                    final Boolean closureMatches = matches;

                    debug("compared row", b -> b.add("rowId", rowId).add("matches", closureMatches).add("totalNumRowDataBits", totalNumRowDataBits));
                }

                if (matches) {

                    addedRowIdsDst.addRowId(addedRowsInstance, rowId);
                }
                else {
                    removedRowIdsDst.addRowId(removedRowsInstance, rowId);
                }
            }
        }

        if (DEBUG) {

            exit(bufferBitOffset);
        }

        return bufferBitOffset;
    }

    private long getTableColumnsFromBitBuffer(TransactionSelect select, BitBuffer mvccBitBuffer, long startBufferBitOffset) {

        if (DEBUG) {

            enter(b -> b.add("select", select).add("mvccBitBuffer", mvccBitBuffer).add("startBufferBitOffset", startBufferBitOffset));
        }

        if (ASSERT) {

            Assertions.isEmpty(scratchRowDataColumnIndexByTableColumn);
            Assertions.isEmpty(scratchDataNumBitsAndOffsets);
        }

        long bufferBitOffset = startBufferBitOffset;

        final int numBitBufferTableColumns = mvccBitBuffer.getUnsignedShort(bufferBitOffset);

        bufferBitOffset += 16L;

        if (DEBUG) {

            debug("num table columns", b -> b.add("numTableColumns", numBitBufferTableColumns));
        }

        for (int i = 0; i < numBitBufferTableColumns; ++ i) {

            final int bitBufferTableColumn = mvccBitBuffer.getUnsignedShort(bufferBitOffset);

            final boolean isComparedColumn = select.containsSelectColumnByTableColumn(bitBufferTableColumn);

            if (isComparedColumn) {

                scratchRowDataColumnIndexByTableColumn.put(bitBufferTableColumn, i);
            }

            bufferBitOffset += 16L;

            int columnBitOffset = 0;

            final int columnNumBits = mvccBitBuffer.getUnsignedShort(bufferBitOffset);

            scratchDataNumBitsAndOffsets.addNumBitsAndOffset(columnNumBits, columnBitOffset);

            if (DEBUG) {

                final int closureI = i;

                debug("compared column", b -> b.add("i", closureI).add("tableColumn", bitBufferTableColumn).add("isComparedColumn", isComparedColumn) // .add("rowDataColumn", rowDataColumn)
                        .add("columnNumBits", columnNumBits));
            }

            bufferBitOffset += 16L;
        }

        final long result = scratchRowDataColumnIndexByTableColumn.isEmpty() ? -1L : bufferBitOffset;

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private boolean compareColumn(TransactionSelect select, SelectColumn selectColumn, BitBuffer mvccBitBuffer, int tableColumn, long bufferBitOffset) {

        if (DEBUG) {

            enter(b -> b.add("select", select).add("mvccBitBuffer", mvccBitBuffer).add("tableColumn", tableColumn).add("bufferBitOffset", bufferBitOffset));
        }

        final int rowDataColumnIndex = scratchRowDataColumnIndexByTableColumn.get(tableColumn);

        final boolean result = compareColumn(select, selectColumn, mvccBitBuffer, tableColumn, bufferBitOffset, rowDataColumnIndex);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private boolean compareColumn(TransactionSelect select, SelectColumn selectColumn, BitBuffer mvccBitBuffer, int tableColumn, long bufferBitOffset, int rowDataColumnIndex) {

        if (DEBUG) {

            enter(b -> b.add("select", select).add("mvccBitBuffer", mvccBitBuffer).add("tableColumn", tableColumn).add("bufferBitOffset", bufferBitOffset)
                    .add("rowDataColumnIndex", rowDataColumnIndex));
        }

        final boolean columnMatches;

        if (rowDataColumnIndex != NO_VALUE) {

            switch (selectColumn.getOperatorType()) {

            case NULLEDNESS:

                columnMatches = selectColumn.getNulledness() == Nulledness.IS_NOT_NULL;
                break;

            case COMPARISON:

                final int columnBitOffset = scratchDataNumBitsAndOffsets.getRowDataBitOffset(rowDataColumnIndex);

                final RowValue rowValue = selectColumn.getRowValue();

                final long bufferColumnBitOffset = bufferBitOffset + columnBitOffset;

                final int compareResult = valueComparer.compareAll(select, selectColumn, mvccBitBuffer, rowValue, bufferColumnBitOffset);

                columnMatches = ValueComparer.computeMatches(selectColumn, compareResult);
                break;

            default:
                throw new UnsupportedOperationException();
            }
        }
        else {
            columnMatches = selectColumn.getOperatorType() == SelectColumnOperatorType.NULLEDNESS && selectColumn.getNulledness() == Nulledness.IS_NULL;
        }

        if (DEBUG) {

            exit(columnMatches);
        }

        return columnMatches;
    }

    private static boolean applyConditionOperator(ConditionOperator conditionOperator, boolean columnMatches, Boolean matches) {

        Boolean matchesValue = matches;

        if (conditionOperator == null) {

            matchesValue = columnMatches;
        }
        else if (conditionOperator == ConditionOperator.OR) {

            matchesValue = matchesValue != null ? matchesValue || columnMatches : columnMatches;
        }
        else if (conditionOperator == ConditionOperator.AND) {

            if (matchesValue != null) {

                if (matchesValue) {

                    if (columnMatches) {

                        matchesValue = true;
                    }
                    else {

                    }
                }
                else {

                }
            }
            else {
                matchesValue = columnMatches;
            }
        }

        return matchesValue;
    }

    long compareRowForUpdateAllOperation(TransactionSelect select, BitBuffer mvccBitBuffer, long startBufferBitOffset, BufferedRows commitedRows,
            IBaseMutableLongSet addedRowIdsDst, IBaseMutableLongSet removedRowIdsDst) {

        Objects.requireNonNull(select);
        Objects.requireNonNull(mvccBitBuffer);
        Checks.isBufferBitsOffset(startBufferBitOffset);
        Objects.requireNonNull(commitedRows);
        Objects.requireNonNull(addedRowIdsDst);
        Objects.requireNonNull(removedRowIdsDst);

        if (DEBUG) {

            enter(b -> b.add("select", select).add("mvccBitBuffer", mvccBitBuffer).add("startBufferBitOffset", startBufferBitOffset).add("commitedRows", commitedRows)
                    .add("addedRowIdsDst", addedRowIdsDst).add("removedRowIdsDst", removedRowIdsDst));
        }

        long bufferBitOffset = getTableColumnsFromBitBuffer(select, mvccBitBuffer, startBufferBitOffset);

        if (Boolean.TRUE) {

            throw new UnsupportedOperationException();
        }

        if (DEBUG) {

            exit(bufferBitOffset);
        }

        return bufferBitOffset;
    }
}
