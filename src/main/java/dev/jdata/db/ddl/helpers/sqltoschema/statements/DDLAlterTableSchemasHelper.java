package dev.jdata.db.ddl.helpers.sqltoschema.statements;

import java.util.Objects;

import org.jutils.ast.objects.list.ASTList;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects.ProcessAlterTableAddColumnsScratchObject;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects.ProcessAlterTableAddPrimaryConstraintScratchObject;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects.ProcessAlterTableDropColumnsScratchObject;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects.ProcessAlterTableModifyColumnsScratchObject;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects.ProcessAlterTableScratchObject;
import dev.jdata.db.ddl.model.diff.TableDiff;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.engine.validation.exceptions.ColumnAlreadyExistsException;
import dev.jdata.db.engine.validation.exceptions.ColumnDoesNotExistException;
import dev.jdata.db.engine.validation.exceptions.SQLValidationException;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.sql.ast.statements.table.SQLAddColumnDefinition;
import dev.jdata.db.sql.ast.statements.table.SQLAddColumnsOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddForeignKeyConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddNotNullConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddNullConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddPrimaryKeyConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddUniqueConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableOperationVisitor;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLColumnNames;
import dev.jdata.db.sql.ast.statements.table.SQLDropColumnsOperation;
import dev.jdata.db.sql.ast.statements.table.SQLDropConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLModifyColumn;
import dev.jdata.db.sql.ast.statements.table.SQLModifyColumnsOperation;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IHeapIntSet;
import dev.jdata.db.utils.adt.sets.IIntSetAllocator;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;

public class DDLAlterTableSchemasHelper extends DDLTableSchemasHelper {

    private static final boolean DEBUG = DebugConstants.DEBUG_DDL_ALTER_TABLE_SCHEMAS_HELPER;

    private static final Class<?> debugClass = DDLAlterTableSchemasHelper.class;

    public static <T extends IIntSetBuilder<?, ? extends IHeapIntSet>, U extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>, P> TableDiff processAlterTable(
            SQLAlterTableStatement sqlAlterTableStatement, DatabaseId databaseId, Table table, StringManagement stringManagement, IIntSetAllocator<?, ?, T> intSetAllocator,
            IIndexListAllocator<Column, ?, ?, U> columnIndexListAllocator, ProcessAlterTableScratchObject<T, U> alterTableScratchObject) throws SQLValidationException {

        Objects.requireNonNull(sqlAlterTableStatement);
        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(table);
        Objects.requireNonNull(intSetAllocator);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(columnIndexListAllocator);
        Objects.requireNonNull(alterTableScratchObject);

        if (DEBUG) {

            enter(debugClass, b -> b.add("sqlAlterTableStatement", sqlAlterTableStatement).add("databaseId", databaseId).add("table", table)
                    .add("intSetAllocator", intSetAllocator).add("stringManagement", stringManagement).add("columnIndexListAllocator", columnIndexListAllocator)
                    .add("alterTableScratchObject", alterTableScratchObject));
        }

        final TableDiff result;

        try {
            alterTableScratchObject.initialize(databaseId, stringManagement, table, intSetAllocator, columnIndexListAllocator);

            result = sqlAlterTableStatement.getOperation().visit(alterTableOperationVisitor, alterTableScratchObject);
        }
        finally {

            alterTableScratchObject.reset();
        }

        if (DEBUG) {

            exit(debugClass, result);
        }

        return result;
    }

    private static final SQLAlterTableOperationVisitor<
                    ProcessAlterTableScratchObject<? extends IIntSetBuilder<?, ? extends IHeapIntSet>, ? extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>>,
                    TableDiff,
                    SQLValidationException> alterTableOperationVisitor

            = new SQLAlterTableOperationVisitor<
                            ProcessAlterTableScratchObject<
                                            ? extends IIntSetBuilder<?, ? extends IHeapIntSet>,
                                            ? extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>>,
                            TableDiff,
                            SQLValidationException>() {

        @Override
        public TableDiff onAddColumn(SQLAddColumnsOperation addColumnOperation,
                ProcessAlterTableScratchObject<
                        ? extends IIntSetBuilder<?, ? extends IHeapIntSet>,
                        ? extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> parameter) throws SQLValidationException {

            return processAlterTableAddColumns(addColumnOperation, parameter);
        }

        @Override
        public TableDiff onModifyColumns(SQLModifyColumnsOperation addColumnOperation,
                ProcessAlterTableScratchObject<
                        ? extends IIntSetBuilder<?, ? extends IHeapIntSet>,
                        ? extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> parameter) throws SQLValidationException {

            return processAlterTableModifyColumns(addColumnOperation, parameter.getModifyColumnsScratchObject());
        }

        @Override
        public TableDiff onDropColumn(SQLDropColumnsOperation dropColumnOperation,
                ProcessAlterTableScratchObject<
                        ? extends IIntSetBuilder<?, ? extends IHeapIntSet>,
                        ? extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> parameter) throws SQLValidationException {

            return processAlterTableDropColumns(dropColumnOperation, parameter.getDropColumnsScratchObject());
        }

        @Override
        public TableDiff onAddPrimaryKeyConstraint(SQLAddPrimaryKeyConstraintOperation addPrimaryKeyConstraintOperation,
                ProcessAlterTableScratchObject<
                        ? extends IIntSetBuilder<?, ? extends IHeapIntSet>,
                        ? extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> parameter) throws SQLValidationException {

            return processAlterTableAddPrimaryKeyConstraint(addPrimaryKeyConstraintOperation, parameter.getAddPrimaryConstraintScratchObject());
        }

        @Override
        public TableDiff onAddForeignKeyConstraint(SQLAddForeignKeyConstraintOperation addForeignKeyConstraintOperation,
                ProcessAlterTableScratchObject<
                        ? extends IIntSetBuilder<?, ? extends IHeapIntSet>,
                        ? extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public TableDiff onAddUniqueConstraint(SQLAddUniqueConstraintOperation addUniqueConstraintOperation,
                ProcessAlterTableScratchObject<
                        ? extends IIntSetBuilder<?, ? extends IHeapIntSet>,
                        ? extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public TableDiff onAddNullConstraint(SQLAddNullConstraintOperation addNullConstraintOperation,
                ProcessAlterTableScratchObject<
                        ? extends IIntSetBuilder<?, ? extends IHeapIntSet>,
                        ? extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public TableDiff onAddNotNullConstraint(SQLAddNotNullConstraintOperation addNotNullConstraintOperation,
                ProcessAlterTableScratchObject<
                        ? extends IIntSetBuilder<?, ? extends IHeapIntSet>,
                        ? extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public TableDiff onDropConstraint(SQLDropConstraintOperation dropConstraintOperation,
                ProcessAlterTableScratchObject<
                        ? extends IIntSetBuilder<?, ? extends IHeapIntSet>,
                        ? extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> parameter) {

            throw new UnsupportedOperationException();
        }
    };

    private static <T extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> TableDiff processAlterTableAddColumns(SQLAddColumnsOperation sqlAddColumnsOperation,
            ProcessAlterTableScratchObject<?, T> alterTableScratchObject) throws ColumnAlreadyExistsException {

        final TableDiff result;

        final ProcessAlterTableAddColumnsScratchObject<T> addColumnsScratchObject = alterTableScratchObject.getAddColumnsScratchObject();

        addColumnsScratchObject.initialize(alterTableScratchObject.getDatabaseId(), alterTableScratchObject.getStringManagement(), alterTableScratchObject.getTable(),
                alterTableScratchObject.getColumnIndexListAllocator());

        try {
            result = processAlterTableAddColumns(sqlAddColumnsOperation, addColumnsScratchObject);
        }
        finally {

            addColumnsScratchObject.reset();
        }

        return result;
    }

    private static <T extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> TableDiff processAlterTableAddColumns(SQLAddColumnsOperation sqlAddColumnsOperation,
            ProcessAlterTableAddColumnsScratchObject<T> addColumnsScratchObject) throws ColumnAlreadyExistsException {

        if (DEBUG) {

            enter(debugClass, b -> b.add("sqlAddColumnsOperation", sqlAddColumnsOperation).add("addColumnsScratchObject", addColumnsScratchObject));
        }

        validateAddColumns(sqlAddColumnsOperation, addColumnsScratchObject);

        final TableDiff result;

        final IIndexListAllocator<Column, ?, ?, T> columnIndexListAllocator = addColumnsScratchObject.getColumnIndexListAllocator();

        final ASTList<SQLAddColumnDefinition> sqlAddColumnDefinitions = sqlAddColumnsOperation.getColumnDefinitions();

        final int numAddedColumns = sqlAddColumnDefinitions.size();

        final Table table = addColumnsScratchObject.getTable();

        final T addedColumnsBuilder = columnIndexListAllocator.createBuilder(numAddedColumns);

        try {
            addColumnsScratchObject.setColumnsBuilder(addedColumnsBuilder);

            sqlAddColumnDefinitions.forEachWithParameter(addColumnsScratchObject, (m, s) -> {

                final Table closureTable = s.getTable();

                final SQLTableColumnDefinition sqlTableColumnDefinition = m.getColumnDefinition();
                final long parsedColumnName = sqlTableColumnDefinition.getName();

                s.setParsedName(parsedColumnName);

                final Column existingColumn = closureTable.findAtMostOneColumn(s, (c, p) -> parsedEqualsStored(p.getStringManagement(), p.getParsedName(), c));

                if (existingColumn != null) {

                    throw new ColumnAlreadyExistsException(s.getDatabaseId(), parsedColumnName);
                }

                final Column addedColumn = convertToColumn(sqlTableColumnDefinition, s.allocateColumnId(), s.getStringManagement());

                s.addColumn(addedColumn);
            });

            result = TableDiff.ofAddedColumns(table, addedColumnsBuilder.buildHeapAllocatedNotEmpty());
        }
        finally {

            columnIndexListAllocator.freeBuilder(addedColumnsBuilder);
        }

        if (DEBUG) {

            exit(debugClass, result);
        }

        return result;
    }

    private static void validateAddColumns(SQLAddColumnsOperation sqlAddColumnsOperation, ProcessAlterTableAddColumnsScratchObject<?> addColumnScratchObject)
            throws ColumnAlreadyExistsException {

        final ASTList<SQLAddColumnDefinition> columnDefinitions = sqlAddColumnsOperation.getColumnDefinitions();

        if (columnDefinitions.isEmpty()) {

            throw new IllegalArgumentException();
        }

        columnDefinitions.forEachWithParameter(addColumnScratchObject, (a, s) -> {

            final long sqlColumnName = a.getColumnDefinition().getName();

            if (containsColumn(s.getTable(), sqlColumnName, s.getStringManagement())) {

                throw new ColumnAlreadyExistsException(s.getDatabaseId(), sqlColumnName);
            }
        });
    }

    private static <T extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> TableDiff processAlterTableModifyColumns(
            SQLModifyColumnsOperation sqlModifyColumnsOperation, ProcessAlterTableModifyColumnsScratchObject<T> modifyColumnsScratchObject) throws ColumnDoesNotExistException {

        if (DEBUG) {

            enter(debugClass, b -> b.add("sqlModifyColumnsOperation", sqlModifyColumnsOperation).add("modifyColumnsScratchObject", modifyColumnsScratchObject));
        }

        validateModifyColumns(sqlModifyColumnsOperation, modifyColumnsScratchObject);

        final TableDiff result;

        final IIndexListAllocator<Column, ?, ?, T> columnIndexListAllocator = modifyColumnsScratchObject.getColumnIndexListAllocator();

        final ASTList<SQLModifyColumn> sqlModifyColumns = sqlModifyColumnsOperation.getColumns();

        final T modifiedColumnsBuilder = columnIndexListAllocator.createBuilder(sqlModifyColumns.size());

        try {
            modifyColumnsScratchObject.setColumnsBuilder(modifiedColumnsBuilder);

            sqlModifyColumns.forEachWithParameter(modifyColumnsScratchObject, (m, s) -> {

                final Table table = s.getTable();

                final SQLTableColumnDefinition sqlTableColumnDefinition = m.getColumnDefinition();
                final long parsedColumnName = sqlTableColumnDefinition.getName();

                s.setParsedName(parsedColumnName);

                final Column existingColumn = table.findAtMostOneColumn(s, (c, p) -> parsedEqualsStored(p.getStringManagement(), p.getParsedName(), c));

                if (existingColumn == null) {

                    throw new ColumnDoesNotExistException(s.getDatabaseId(), parsedColumnName);
                }

                final Column modifiedColumn = convertToColumn(sqlTableColumnDefinition, existingColumn.getId(), s.getStringManagement());

                s.addColumn(modifiedColumn);
            });

            result = TableDiff.ofModifiedColumns(modifyColumnsScratchObject.getTable(), modifiedColumnsBuilder.buildHeapAllocatedNotEmpty());
        }
        finally {

            columnIndexListAllocator.freeBuilder(modifiedColumnsBuilder);
        }

        if (DEBUG) {

            exit(debugClass, result);
        }

        return result;
    }

    private static void validateModifyColumns(SQLModifyColumnsOperation sqlModifyColumnsOperation, ProcessAlterTableModifyColumnsScratchObject modifyColumnsScratchObject)
            throws ColumnDoesNotExistException {

        sqlModifyColumnsOperation.getColumns().forEachWithParameter(modifyColumnsScratchObject, (m, s) -> {

            final long sqlColumnName = m.getColumnDefinition().getName();

            if (!containsColumn(s.getTable(), sqlColumnName, s.getStringManagement())) {

                throw new ColumnDoesNotExistException(s.getDatabaseId(), sqlColumnName);
            }
        });
    }

    private static <T extends IIntSetBuilder<?, ? extends IHeapIntSet>> TableDiff processAlterTableDropColumns(SQLDropColumnsOperation sqlDropColumnsOperation,
            ProcessAlterTableDropColumnsScratchObject<T> dropColumnsScratchObject) throws ColumnDoesNotExistException {

        if (DEBUG) {

            enter(debugClass, b -> b.add("sqlDropColumnsOperation", sqlDropColumnsOperation).add("dropColumnsScratchObject", dropColumnsScratchObject));
        }

        final TableDiff result;

        validateDropColumns(sqlDropColumnsOperation, dropColumnsScratchObject);

        final SQLColumnNames sqlColumnNames = sqlDropColumnsOperation.getNames();

        final long numColumnNames = sqlColumnNames.getNumElements();

        final IIntSetAllocator<?, ?, T> intSetAllocator = dropColumnsScratchObject.getIntSetAllocator();

        final T droppedColumnsBuilder = intSetAllocator.createBuilder(numColumnNames);

        try {
            final Table table = dropColumnsScratchObject.getTable();

            for (int i = 0; i < numColumnNames; ++ i) {

                final long sqlColumnName = sqlColumnNames.get(i);

                dropColumnsScratchObject.setParsedName(sqlColumnName);

                final Column existingColumn = table.findAtMostOneColumn(dropColumnsScratchObject,
                        (c, s) -> s.getStringManagement().parsedEqualsStored(s.getParsedName(), c.getParsedName(), false));

                if (existingColumn == null) {

                    throw new ColumnDoesNotExistException(dropColumnsScratchObject.getDatabaseId(), sqlColumnName);
                }

                droppedColumnsBuilder.addUnordered(existingColumn.getId());
            }

            result = TableDiff.ofDroppedColumns(dropColumnsScratchObject.getTable(), droppedColumnsBuilder.buildHeapAllocatedNotEmpty());
        }
        finally {

            intSetAllocator.freeBuilder(droppedColumnsBuilder);
        }

        if (DEBUG) {

            exit(debugClass, result);
        }

        return result;
    }

    private static void validateDropColumns(SQLDropColumnsOperation sqlDropColumnsOperation, ProcessAlterTableDropColumnsScratchObject<?> dropColumnsScratchObject)
            throws ColumnDoesNotExistException {

        final SQLColumnNames sqlColumnNames = sqlDropColumnsOperation.getNames();

        final long numColumnNames = sqlColumnNames.getNumElements();

        final Table table = dropColumnsScratchObject.getTable();
        final StringManagement stringManagement = dropColumnsScratchObject.getStringManagement();

        for (int i = 0; i < numColumnNames; ++ i) {

            final long sqlColumnName = sqlColumnNames.get(i);

            if (!containsColumn(table, sqlColumnName, stringManagement)) {

                throw new ColumnDoesNotExistException(dropColumnsScratchObject.getDatabaseId(), sqlColumnName);
            }
        }
    }

    private static TableDiff processAlterTableAddPrimaryKeyConstraint(SQLAddPrimaryKeyConstraintOperation sqlAddPrimaryKeyConstraintOperation,
            ProcessAlterTableAddPrimaryConstraintScratchObject addPrimaryConstraintScratchObject) throws ColumnDoesNotExistException {

        if (DEBUG) {

            enter(debugClass, b -> b.add("sqlAddPrimaryKeyConstraintOperation", sqlAddPrimaryKeyConstraintOperation)
                    .add("addPrimaryConstraintScratchObject", addPrimaryConstraintScratchObject));
        }

        validateAddPrimaryConstraint(sqlAddPrimaryKeyConstraintOperation, addPrimaryConstraintScratchObject);

        final TableDiff result = null;

        if (DEBUG) {

            exit(debugClass, result);
        }

        return result;
    }

    private static void validateAddPrimaryConstraint(SQLAddPrimaryKeyConstraintOperation sqlAddPrimaryKeyConstraintOperation,
            ProcessAlterTableAddPrimaryConstraintScratchObject addPrimaryConstraintScratchObject) throws ColumnDoesNotExistException {

        final SQLColumnNames sqlColumnNames = sqlAddPrimaryKeyConstraintOperation.getColumnNames();

        final long numColumnNames = sqlColumnNames.getNumElements();

        final Table table = addPrimaryConstraintScratchObject.getTable();
        final StringManagement stringManagement = addPrimaryConstraintScratchObject.getStringManagement();

        for (int i = 0; i < numColumnNames; ++ i) {

            final long sqlColumnName = sqlColumnNames.get(i);

            if (!containsColumn(table, sqlColumnName, stringManagement)) {

                throw new ColumnDoesNotExistException(addPrimaryConstraintScratchObject.getDatabaseId(), sqlColumnName);
            }
        }
    }

    private static boolean containsColumn(Table table, long name, StringManagement stringManagement) {

        return table.containsColumn(name, false, stringManagement, (n, c, s, m) -> m.parsedEqualsStored(n, c, s));
    }

    private static boolean parsedEqualsStored(StringManagement stringManagement, long parsedName, Column column) {

        return stringManagement.parsedEqualsStored(parsedName, column.getParsedName(), false);
    }
}
