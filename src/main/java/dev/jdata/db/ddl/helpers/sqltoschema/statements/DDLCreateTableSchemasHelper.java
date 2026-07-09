package dev.jdata.db.ddl.helpers.sqltoschema.statements;

import java.util.Objects;
import java.util.function.ToIntFunction;

import org.jutils.ast.objects.list.ASTList;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects.ProcessCreateTableScratchObject;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects.ProcessTableColumnsScratchObject;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;

public class DDLCreateTableSchemasHelper extends DDLTableSchemasHelper {

    private static final boolean DEBUG = DebugConstants.DEBUG_DDL_CREATE_TABLE_SCHEMAS_HELPER;

    private static final Class<?> debugClass = DDLCreateTableSchemasHelper.class;

    public static <T extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>, P> Table processCreateTable(SQLCreateTableStatement sqlCreateTableStatement,
            ISQLToSchemaStringManagement sqlToSchemaStringManagement, IIndexListAllocator<Column, ?, ?, T> columnIndexListAllocator,
            ProcessCreateTableScratchObject processCreateTableScratchObject, P parameter, ToIntFunction<P> allocateTableIdFunction) {

        Objects.requireNonNull(sqlCreateTableStatement);
        Objects.requireNonNull(sqlToSchemaStringManagement);
        Objects.requireNonNull(columnIndexListAllocator);
        Objects.requireNonNull(processCreateTableScratchObject);
        Objects.requireNonNull(allocateTableIdFunction);

        if (DEBUG) {

            enter(debugClass, b -> b.add("sqlCreateTableStatement", sqlCreateTableStatement).add("sqlToSchemaStringManagement", sqlToSchemaStringManagement)
                    .add("columnIndexListAllocator", columnIndexListAllocator).add("processCreateTableScratchObject", processCreateTableScratchObject)
                    .add("allocateTableIdFunction", allocateTableIdFunction));
        }

        final Table result;

        final long parsedTableName = sqlToSchemaStringManagement.storeParsedStringRef(sqlCreateTableStatement.getName());

        final T columnsBuilder = columnIndexListAllocator.createBuilder(sqlCreateTableStatement.getColumns().size());

        processCreateTableScratchObject.initializeCreateTable(sqlToSchemaStringManagement, columnsBuilder);

        try {
            convertColumns(sqlCreateTableStatement.getColumns(), processCreateTableScratchObject);

            final int tableId = allocateTableIdFunction.applyAsInt(parameter);

            final long hashTableName = sqlToSchemaStringManagement.storeHashStringRefFromStoredSQLStringRef(parsedTableName);

            result = new Table(parsedTableName, hashTableName, tableId, columnsBuilder.buildHeapAllocatedNotEmpty());
        }
        finally {

            processCreateTableScratchObject.reset();

            columnIndexListAllocator.freeBuilder(columnsBuilder);
        }

        if (DEBUG) {

            exit(debugClass, result);
        }

        return result;
    }

    private static void convertColumns(ASTList<SQLTableColumnDefinition> sqlTableColumnDefinitions, ProcessTableColumnsScratchObject scratchObject) {

        Objects.requireNonNull(sqlTableColumnDefinitions);
        Objects.requireNonNull(scratchObject);

        sqlTableColumnDefinitions.forEachWithIndexAndParameter(scratchObject, (c, i, s) -> {

            final Column column = convertToColumn(c, s.allocateColumnId(), s.getSQLToSchemaStringManagement());

            s.addColumn(column);
        });
    }
}
