package dev.jdata.db.ddl;

import java.util.Objects;

import org.jutils.ast.objects.Keyword;
import org.jutils.ast.objects.list.ASTList;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.SchemaMapBuilders;
import dev.jdata.db.sql.ast.statements.table.SQLAddColumnDefinition;
import dev.jdata.db.sql.ast.statements.table.SQLAddColumnsOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddForeignKeyConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddNotNullConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddNullConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddPrimaryKeyConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAddUniqueConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableOperationVisitor;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLDropColumnOperation;
import dev.jdata.db.sql.ast.statements.table.SQLDropConstraintOperation;
import dev.jdata.db.sql.ast.statements.table.SQLModifyColumnsOperation;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;

public class DDLAlterTableSchemasHelper extends DDLTableSchemasHelper {

    public static void processAlterTable(SQLAlterTableStatement sqlAlterTableStatement, Table table, StringManagement stringManagement,
            SchemaMapBuilders schemaMapBuilders, DDLSchemaCachedObjects ddlSchemaCachedObjects) {

        Objects.requireNonNull(sqlAlterTableStatement);
        Objects.requireNonNull(table);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(ddlSchemaCachedObjects);

        final Table alteredTable = processAlterTable(sqlAlterTableStatement, table, stringManagement, ddlSchemaCachedObjects);

//        schemaMapBuilders.addSchemaObject(DDLObjectType.TABLE, alteredTable);
    }

    private static Table processAlterTable(SQLAlterTableStatement sqlAlterTableStatement, Table table, StringManagement stringManagement,
            DDLSchemaCachedObjects ddlSchemaCachedObjects) {

        Objects.requireNonNull(sqlAlterTableStatement);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(table);
        Objects.requireNonNull(ddlSchemaCachedObjects);

        final Table result;

        final ProcessAlterTableScratchObject processAlterTableScratchObject = ddlSchemaCachedObjects.allocateProcessAlterTableScratchObject();

        processAlterTableScratchObject.initialize(table, ddlSchemaCachedObjects);

        try {
            result = sqlAlterTableStatement.getOperation().visit(alterTableOperationVisitor, processAlterTableScratchObject);
        }
        finally {

            ddlSchemaCachedObjects.freeProcessAlterTableScratchObject(processAlterTableScratchObject);
        }

        return result;
    }

    private static final SQLAlterTableOperationVisitor<ProcessAlterTableScratchObject, Table> alterTableOperationVisitor
            = new SQLAlterTableOperationVisitor<ProcessAlterTableScratchObject, Table>() {

        @Override
        public Table onAddColumn(SQLAddColumnsOperation addColumnOperation, ProcessAlterTableScratchObject parameter) {

            return processAlterTableAddColumns(addColumnOperation, parameter);
        }

        @Override
        public Table onModifyColumns(SQLModifyColumnsOperation addColumnOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Table onDropColumn(SQLDropColumnOperation dropColumnOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Table onAddPrimaryKeyConstraint(SQLAddPrimaryKeyConstraintOperation addPrimaryKeyConstraintOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Table onAddForeignKeyConstraint(SQLAddForeignKeyConstraintOperation addForeignKeyConstraintOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Table onAddUniqueConstraint(SQLAddUniqueConstraintOperation addUniqueConstraintOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Table onAddNullConstraint(SQLAddNullConstraintOperation addNullConstraintOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Table onAddNotNullConstraint(SQLAddNotNullConstraintOperation addNotNullConstraintOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Table onDropConstraint(SQLDropConstraintOperation dropConstraintOperation, ProcessAlterTableScratchObject parameter) {

            throw new UnsupportedOperationException();
        }
    };

    private static Table processAlterTableAddColumns(SQLAddColumnsOperation sqlAddColumnsOperation, ProcessAlterTableScratchObject processAlterTableScratchObject) {

        final Table result;

        final StringManagement stringManagement = processAlterTableScratchObject.getStringManagement();
        final DDLSchemaCachedObjects ddlSchemaCachedObjects = processAlterTableScratchObject.ddlSchemaCachedObjects;
        final IndexListAllocator<Column> columnIndexListAllocator = ddlSchemaCachedObjects.getColumnIndexListAllocator();

        final ASTList<SQLAddColumnDefinition> sqlAddColumnDefinitions = sqlAddColumnsOperation.getColumnDefinitions();

        final int numAddedColumns = sqlAddColumnDefinitions.size();

        final Table table = processAlterTableScratchObject.table;
        final int numExistingColumns = table.getNumColumns();

        final IndexList.Builder<Column> allColumnsBuilder = IndexList.createBuilder(numExistingColumns + numAddedColumns, columnIndexListAllocator);

        IIndexList<Column> allColumns = null;

        try {
            final int maxColumnId = table.getMaxColumnId();

            for (int i = 0; i < numExistingColumns; ++ i) {

                final Column existingColumn = table.getColumn(i);

                final SQLAddColumnDefinition sqlAddColumnDefinition = sqlAddColumnDefinitions.findWithParameter(stringManagement,
                        (d, s) ->    d.getBeforeKeyword() != Keyword.NO_KEYWORD
                                  && s.parsedEqualsStored(d.getBeforeColumnName(), existingColumn.getParsedName(), false));

                if (sqlAddColumnDefinition != null) {

                    final int columnId = processAlterTableScratchObject.allocateColumnId();

                    final Column column = convertToColumn(sqlAddColumnDefinition.getColumnDefinition(), columnId, stringManagement);

                    allColumnsBuilder.addTail(column);
                }

                allColumnsBuilder.addTail(existingColumn);
            }

            allColumns = allColumnsBuilder.build();

            result = table.makeCopy(allColumns);
        }
        finally {

            if (allColumns != null) {

                columnIndexListAllocator.freeIndexList(allColumns);
            }

            columnIndexListAllocator.freeIndexListBuilder(allColumnsBuilder);
            ddlSchemaCachedObjects.freeProcessAlterTableScratchObject(processAlterTableScratchObject);
        }

        return result;
    }

    public static final class ProcessAlterTableScratchObject extends ProcessTableScratchObject<SQLAddColumnDefinition> {

        private Table table;
        private DDLSchemaCachedObjects ddlSchemaCachedObjects;

        void initialize(Table table, DDLSchemaCachedObjects ddlSchemaCachedObjects) {

            this.table = Initializable.checkNotYetInitialized(this.table, table);
            this.ddlSchemaCachedObjects = Initializable.checkNotYetInitialized(this.ddlSchemaCachedObjects, ddlSchemaCachedObjects);
        }

        @Override
        public void reset() {

            super.reset();

            this.table = Initializable.checkResettable(table);
            this.ddlSchemaCachedObjects = Initializable.checkResettable(ddlSchemaCachedObjects);
        }

        public Table getTable() {
            return table;
        }

        public DDLSchemaCachedObjects getDDlSchemaCachedObjects() {
            return ddlSchemaCachedObjects;
        }
    }
}
