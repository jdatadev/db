package dev.jdata.db.ddl.helpers;

import java.util.Objects;
import java.util.function.ToIntFunction;

import org.jutils.ast.objects.list.ASTList;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.ddl.scratchobjects.ProcessCreateTableScratchObject;
import dev.jdata.db.ddl.scratchobjects.ProcessTableColumnsScratchObject;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.utils.adt.lists.ICachedIndexListAllocator;
import dev.jdata.db.utils.adt.lists.ICachedIndexListBuilder;
import dev.jdata.db.utils.debug.PrintDebug;

public class DDLCreateTableSchemasHelper extends DDLTableSchemasHelper {

    private static final boolean DEBUG = DebugConstants.DEBUG_DDL_CREATE_TABLE_SCHEMAS_HELPER;

    private static final Class<?> debugClass = DDLCreateTableSchemasHelper.class;

    public static <P> Table processCreateTable(SQLCreateTableStatement sqlCreateTableStatement, StringManagement stringManagement,
            DDLSchemaScratchObjects ddlSchemaScratchObjects, P parameter, ToIntFunction<P> allocateTableIdFunction) {

        Objects.requireNonNull(sqlCreateTableStatement);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(ddlSchemaScratchObjects);
        Objects.requireNonNull(allocateTableIdFunction);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("sqlCreateTableStatement", sqlCreateTableStatement).add("stringManagement", stringManagement)
                    .add("ddlSchemaScrathObjects", ddlSchemaScratchObjects).add("allocateTableIdFunction", allocateTableIdFunction));
        }


        final Table result;

        final long parsedTableName = stringManagement.storeParsedStringRef(sqlCreateTableStatement.getName());

        final ICachedIndexListAllocator<Column> columnIndexListAllocator = ddlSchemaScratchObjects.getColumnIndexListAllocator();

        final ICachedIndexListBuilder<Column> columnsBuilder = columnIndexListAllocator.createBuilder(sqlCreateTableStatement.getColumns().size());

        final ProcessCreateTableScratchObject processCreateTableScratchObject = ddlSchemaScratchObjects.allocateProcessCreateTableScratchObject();

        try {
            processCreateTableScratchObject.initializeCreateTable(stringManagement, columnsBuilder);

            convertColumns(sqlCreateTableStatement.getColumns(), processCreateTableScratchObject);

            final int tableId = allocateTableIdFunction.applyAsInt(parameter);

            final long hashTableName = stringManagement.getHashStringRef(parsedTableName);

            result = new Table(parsedTableName, hashTableName, tableId, columnsBuilder.buildHeapAllocatedNotEmpty());
        }
        finally {

            columnIndexListAllocator.freeBuilder(columnsBuilder);

            ddlSchemaScratchObjects.freeProcessCreateTableScratchObject(processCreateTableScratchObject);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private static void convertColumns(ASTList<SQLTableColumnDefinition> sqlTableColumnDefinitions, ProcessTableColumnsScratchObject scratchObject) {

        Objects.requireNonNull(sqlTableColumnDefinitions);
        Objects.requireNonNull(scratchObject);

        sqlTableColumnDefinitions.forEachWithIndexAndParameter(scratchObject, (c, i, s) -> {

            final Column column = convertToColumn(c, s.allocateColumnId(), s.getStringManagement());

            s.addColumn(column);
        });
    }
}
