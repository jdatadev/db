package dev.jdata.db.ddl.helpers;

import java.util.Objects;

import org.jutils.ast.objects.list.ASTList;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.ddl.model.diff.TableDiff;
import dev.jdata.db.ddl.scratchobjects.ProcessAlterTableScratchObject;
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
import dev.jdata.db.utils.adt.lists.ICachedIndexList;
import dev.jdata.db.utils.adt.lists.ICachedIndexListAllocator;
import dev.jdata.db.utils.adt.lists.ICachedIndexListBuilder;
import dev.jdata.db.utils.adt.sets.ICachedIntSetAllocator;
import dev.jdata.db.utils.adt.sets.ICachedIntSetBuilder;
import dev.jdata.db.utils.debug.PrintDebug;

public class DDLAlterTableSchemasHelper extends DDLTableSchemasHelper {

    private static final boolean DEBUG = DebugConstants.DEBUG_DDL_ALTER_TABLE_SCHEMAS_HELPER;

    private static final Class<?> debugClass = DDLAlterTableSchemasHelper.class;

    public static TableDiff processAlterTable(SQLAlterTableStatement sqlAlterTableStatement, DatabaseId databaseId, Table table, ICachedIntSetAllocator intSetAllocator,
            StringManagement stringManagement, DDLSchemaScratchObjects ddlSchemaScratchObjects) throws SQLValidationException {

        Objects.requireNonNull(sqlAlterTableStatement);
        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(table);
        Objects.requireNonNull(intSetAllocator);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(ddlSchemaScratchObjects);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("sqlAlterTableStatement", sqlAlterTableStatement).add("databaseId", databaseId).add("table", table)
                    .add("intSetAllocator", intSetAllocator).add("stringManagement", stringManagement).add("ddlSchemaScratchObjects", ddlSchemaScratchObjects));
        }

        final TableDiff result;

        final ProcessAlterTableScratchObject processAlterTableScratchObject = ddlSchemaScratchObjects.allocateProcessAlterTableScratchObject();

        processAlterTableScratchObject.initialize(databaseId, stringManagement, table, intSetAllocator, ddlSchemaScratchObjects);

        try {
            result = sqlAlterTableStatement.getOperation().visit(alterTableOperationVisitor, processAlterTableScratchObject);
        }
        finally {

            ddlSchemaScratchObjects.freeProcessAlterTableScratchObject(processAlterTableScratchObject);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private static final SQLAlterTableOperationVisitor<ProcessAlterTableScratchObject, TableDiff, SQLValidationException> alterTableOperationVisitor
            = new SQLAlterTableOperationVisitor<ProcessAlterTableScratchObject, TableDiff, SQLValidationException>() {

        @Override
        public TableDiff onAddColumn(SQLAddColumnsOperation addColumnOperation, ProcessAlterTableScratchObject parameter) throws SQLValidationException {

            return processAlterTableAddColumns(addColumnOperation, parameter);
        }

        @Override
        public TableDiff onModifyColumns(SQLModifyColumnsOperation addColumnOperation, ProcessAlterTableScratchObject parameter) throws SQLValidationException {

            return processAlterTableModifyColumns(addColumnOperation, parameter);
        }

        @Override
        public TableDiff onDropColumn(SQLDropColumnsOperation dropColumnOperation, ProcessAlterTableScratchObject parameter) throws SQLValidationException {

            return processAlterTableDropColumns(dropColumnOperation, parameter);
        }

        @Override
        public TableDiff onAddPrimaryKeyConstraint(SQLAddPrimaryKeyConstraintOperation addPrimaryKeyConstraintOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public TableDiff onAddForeignKeyConstraint(SQLAddForeignKeyConstraintOperation addForeignKeyConstraintOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public TableDiff onAddUniqueConstraint(SQLAddUniqueConstraintOperation addUniqueConstraintOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public TableDiff onAddNullConstraint(SQLAddNullConstraintOperation addNullConstraintOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public TableDiff onAddNotNullConstraint(SQLAddNotNullConstraintOperation addNotNullConstraintOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public TableDiff onDropConstraint(SQLDropConstraintOperation dropConstraintOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }
    };

    private static TableDiff processAlterTableAddColumns(SQLAddColumnsOperation sqlAddColumnsOperation, ProcessAlterTableScratchObject processAlterTableScratchObject)
            throws ColumnAlreadyExistsException {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("sqlAddColumnsOperation", sqlAddColumnsOperation).add("processAlterTableScratchObject", processAlterTableScratchObject));
        }

        validateAddColumns(sqlAddColumnsOperation, processAlterTableScratchObject);

        final TableDiff result;

        final DDLSchemaScratchObjects ddlSchemaScratchObjects = processAlterTableScratchObject.getDDLSchemaScratchObjects();
        final ICachedIndexListAllocator<Column> columnIndexListAllocator = ddlSchemaScratchObjects.getColumnIndexListAllocator();

        final ASTList<SQLAddColumnDefinition> sqlAddColumnDefinitions = sqlAddColumnsOperation.getColumnDefinitions();

        final int numAddedColumns = sqlAddColumnDefinitions.size();

        final Table table = processAlterTableScratchObject.getTable();

        final ICachedIndexListBuilder<Column> addedColumnsBuilder = columnIndexListAllocator.createBuilder(numAddedColumns);

        ICachedIndexList<Column> addedColumns = null;

        try {
            processAlterTableScratchObject.initialize(processAlterTableScratchObject.getStringManagement(), table.getMaxColumnId() + 1, addedColumnsBuilder);

            sqlAddColumnDefinitions.forEachWithParameter(processAlterTableScratchObject, (m, s) -> {

                final Table closureTable = s.getTable();

                final SQLTableColumnDefinition sqlTableColumnDefinition = m.getColumnDefinition();
                final long parsedColumnName = sqlTableColumnDefinition.getName();

                s.setParsedName(parsedColumnName);

                final Column existingColumn = closureTable.findAtMostOneColumn(s, (c, p) -> parsedEqualsStored(p, c));

                if (existingColumn != null) {

                    throw new ColumnAlreadyExistsException(s.getDatabaseId(), parsedColumnName);
                }

                final Column addedColumn = convertToColumn(sqlTableColumnDefinition, s.allocateColumnId(), s.getStringManagement());

                s.addColumn(addedColumn);
            });

            addedColumns = addedColumnsBuilder.buildOrNull();

            result = TableDiff.ofAddedColumns(table, addedColumns.toHeapAllocated());
        }
        finally {

            if (addedColumns != null) {

                columnIndexListAllocator.freeImmutable(addedColumns);
            }

            columnIndexListAllocator.freeBuilder(addedColumnsBuilder);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private static void validateAddColumns(SQLAddColumnsOperation sqlAddColumnsOperation, ProcessAlterTableScratchObject processAlterTableScratchObject)
            throws ColumnAlreadyExistsException {

        sqlAddColumnsOperation.getColumnDefinitions().forEachWithParameter(processAlterTableScratchObject, (a, s) -> {

            final long sqlColumnName = a.getColumnDefinition().getName();

            if (containsColumn(s.getTable(), sqlColumnName, s.getStringManagement())) {

                throw new ColumnAlreadyExistsException(s.getDatabaseId(), sqlColumnName);
            }
        });
    }

    private static TableDiff processAlterTableModifyColumns(SQLModifyColumnsOperation sqlModifyColumnsOperation, ProcessAlterTableScratchObject processAlterTableScratchObject)
            throws ColumnDoesNotExistException {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("sqlModifyColumnsOperation", sqlModifyColumnsOperation)
                    .add("processAlterTableScratchObject", processAlterTableScratchObject));
        }

        validateModifyColumns(sqlModifyColumnsOperation, processAlterTableScratchObject);

        final TableDiff result;

        final DDLSchemaScratchObjects ddlSchemaScratchObjects = processAlterTableScratchObject.getDDLSchemaScratchObjects();
        final ICachedIndexListAllocator<Column> columnIndexListAllocator = ddlSchemaScratchObjects.getColumnIndexListAllocator();

        final ASTList<SQLModifyColumn> sqlModifyColumns = sqlModifyColumnsOperation.getColumns();

        final ICachedIndexListBuilder<Column> modifiedColumnsBuilder = columnIndexListAllocator.createBuilder(sqlModifyColumns.size());

        ICachedIndexList<Column> modifiedColumns = null;

        try {
            processAlterTableScratchObject.initialize(processAlterTableScratchObject.getStringManagement(), -1, modifiedColumnsBuilder);

            sqlModifyColumns.forEachWithParameter(processAlterTableScratchObject, (m, s) -> {

                final Table table = s.getTable();

                final SQLTableColumnDefinition sqlTableColumnDefinition = m.getColumnDefinition();
                final long parsedColumnName = sqlTableColumnDefinition.getName();

                s.setParsedName(parsedColumnName);

                final Column existingColumn = table.findAtMostOneColumn(s, (c, p) -> parsedEqualsStored(p, c));

                if (existingColumn == null) {

                    throw new ColumnDoesNotExistException(s.getDatabaseId(), parsedColumnName);
                }

                final Column modifiedColumn = convertToColumn(sqlTableColumnDefinition, existingColumn.getId(), s.getStringManagement());

                s.addColumn(modifiedColumn);
            });

            modifiedColumns = modifiedColumnsBuilder.buildOrNull();

            result = TableDiff.ofModifiedColumns(processAlterTableScratchObject.getTable(), modifiedColumns.toHeapAllocated());
        }
        finally {

            if (modifiedColumns != null) {

                columnIndexListAllocator.freeImmutable(modifiedColumns);
            }

            columnIndexListAllocator.freeBuilder(modifiedColumnsBuilder);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private static void validateModifyColumns(SQLModifyColumnsOperation sqlModifyColumnsOperation, ProcessAlterTableScratchObject processAlterTableScratchObject)
            throws ColumnDoesNotExistException {

        sqlModifyColumnsOperation.getColumns().forEachWithParameter(processAlterTableScratchObject, (m, s) -> {

            final long sqlColumnName = m.getColumnDefinition().getName();

            if (!containsColumn(s.getTable(), sqlColumnName, s.getStringManagement())) {

                throw new ColumnDoesNotExistException(s.getDatabaseId(), sqlColumnName);
            }
        });
    }

    private static TableDiff processAlterTableDropColumns(SQLDropColumnsOperation sqlDropColumnsOperation, ProcessAlterTableScratchObject processAlterTableScratchObject)
            throws ColumnDoesNotExistException {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("sqlDropColumnsOperation", sqlDropColumnsOperation).add("processAlterTableScratchObject", processAlterTableScratchObject));
        }

        final TableDiff result;

        validateDropColumns(sqlDropColumnsOperation, processAlterTableScratchObject);

        final SQLColumnNames sqlColumnNames = sqlDropColumnsOperation.getNames();

        final long numColumnNames = sqlColumnNames.getNumElements();

        final ICachedIntSetAllocator intSetAllocator = processAlterTableScratchObject.getIntSetAllocator();

        final ICachedIntSetBuilder droppedColumnsBuilder = intSetAllocator.createBuilder(numColumnNames);

        try {
            final Table table = processAlterTableScratchObject.getTable();

            for (int i = 0; i < numColumnNames; ++ i) {

                final long sqlColumnName = sqlColumnNames.get(i);

                processAlterTableScratchObject.setParsedName(sqlColumnName);

                final Column existingColumn = table.findAtMostOneColumn(processAlterTableScratchObject,
                        (c, s) -> s.getStringManagement().parsedEqualsStored(s.getParsedName(), c.getParsedName(), false));

                if (existingColumn == null) {

                    throw new ColumnDoesNotExistException(processAlterTableScratchObject.getDatabaseId(), sqlColumnName);
                }

                droppedColumnsBuilder.addUnordered(existingColumn.getId());
            }

            result = TableDiff.ofDroppedColumns(processAlterTableScratchObject.getTable(), droppedColumnsBuilder.buildHeapAllocatedNotEmpty());
        }
        finally {

            intSetAllocator.freeBuilder(droppedColumnsBuilder);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private static void validateDropColumns(SQLDropColumnsOperation sqlDropColumnsOperation, ProcessAlterTableScratchObject processAlterTableScratchObject)
            throws ColumnDoesNotExistException {

        final SQLColumnNames sqlColumnNames = sqlDropColumnsOperation.getNames();

        final long numColumnNames = sqlColumnNames.getNumElements();

        final Table table = processAlterTableScratchObject.getTable();
        final StringManagement stringManagement = processAlterTableScratchObject.getStringManagement();

        for (int i = 0; i < numColumnNames; ++ i) {

            final long sqlColumnName = sqlColumnNames.get(i);

            if (!containsColumn(table, sqlColumnName, stringManagement)) {

                throw new ColumnDoesNotExistException(processAlterTableScratchObject.getDatabaseId(), sqlColumnName);
            }
        }
    }

    private static TableDiff processAlterTableAddPrimaryKeyConstraint(SQLAddPrimaryKeyConstraintOperation sqlAddPrimaryKeyConstraintOperation,
            ProcessAlterTableScratchObject processAlterTableScratchObject) throws ColumnDoesNotExistException {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("sqlAddPrimaryKeyConstraintOperation", sqlAddPrimaryKeyConstraintOperation)
                    .add("processAlterTableScratchObject", processAlterTableScratchObject));
        }

        validateAddPrimaryConstraint(sqlAddPrimaryKeyConstraintOperation, processAlterTableScratchObject);

        final TableDiff result = null;

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private static void validateAddPrimaryConstraint(SQLAddPrimaryKeyConstraintOperation sqlAddPrimaryKeyConstraintOperation,
            ProcessAlterTableScratchObject processAlterTableScratchObject) throws ColumnDoesNotExistException {

        final SQLColumnNames sqlColumnNames = sqlAddPrimaryKeyConstraintOperation.getColumnNames();

        final long numColumnNames = sqlColumnNames.getNumElements();

        final Table table = processAlterTableScratchObject.getTable();
        final StringManagement stringManagement = processAlterTableScratchObject.getStringManagement();

        for (int i = 0; i < numColumnNames; ++ i) {

            final long sqlColumnName = sqlColumnNames.get(i);

            if (!containsColumn(table, sqlColumnName, stringManagement)) {

                throw new ColumnDoesNotExistException(processAlterTableScratchObject.getDatabaseId(), sqlColumnName);
            }
        }
    }

    private static boolean containsColumn(Table table, long name, StringManagement stringManagement) {

        return table.containsColumn(name, false, stringManagement, (n, c, s, m) -> m.parsedEqualsStored(n, c, s));
    }

    private static boolean parsedEqualsStored(ProcessAlterTableScratchObject processAlterTableScratchObject, Column column) {

        return processAlterTableScratchObject.getStringManagement().parsedEqualsStored(processAlterTableScratchObject.getParsedName(), column.getParsedName(), false);
    }
}
