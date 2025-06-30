package dev.jdata.db.ddl;

import java.util.Objects;
import java.util.function.ToIntFunction;

import org.jutils.ast.objects.list.ASTList;

import dev.jdata.db.ddl.allocators.DDLSchemaCachedObjects;
import dev.jdata.db.ddl.scratchobjects.ProcessCreateTableScratchObject;
import dev.jdata.db.ddl.scratchobjects.ProcessTableColumnsScratchObject;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.SchemaMapBuilders;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.utils.adt.lists.CachedIndexList;
import dev.jdata.db.utils.adt.lists.CachedIndexList.CacheIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList;

public class DDLCreateTableSchemasHelper extends DDLTableSchemasHelper {

    public static <P> void processCreateTable(SQLCreateTableStatement sqlCreateTableStatement, StringManagement stringManagement, SchemaMapBuilders schemaMapBuilders,
            DDLSchemaCachedObjects ddlSchemaCachedObjects, P parameter, ToIntFunction<P> allocateTableIdFunction) {

        Objects.requireNonNull(sqlCreateTableStatement);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(schemaMapBuilders);
        Objects.requireNonNull(allocateTableIdFunction);
        Objects.requireNonNull(ddlSchemaCachedObjects);

        final Table table = processCreateTable(sqlCreateTableStatement, stringManagement, parameter, allocateTableIdFunction, ddlSchemaCachedObjects);

        schemaMapBuilders.addSchemaObject(DDLObjectType.TABLE, table);
    }

    private static <P> Table processCreateTable(SQLCreateTableStatement sqlCreateTableStatement, StringManagement stringManagement, P parameter,
            ToIntFunction<P> allocateTableIdFunction, DDLSchemaCachedObjects ddlSchemaCachedObjects) {

        final Table result;

        final long parsedTableName = stringManagement.resolveParsedStringRef(sqlCreateTableStatement.getName());

        final CacheIndexListAllocator<Column> columnIndexListAllocator = ddlSchemaCachedObjects.getColumnIndexListAllocator();

        final CachedIndexList.CachedIndexListBuilder<Column> columnsBuilder = IndexList.createBuilder(sqlCreateTableStatement.getColumns().size(), columnIndexListAllocator);

        CachedIndexList<Column> columns = null;

        final ProcessCreateTableScratchObject processCreateTableScratchObject = ddlSchemaCachedObjects.allocateProcessCreateTableScratchObject();

        try {
            processCreateTableScratchObject.initializeCreateTable(stringManagement, columnsBuilder);

            convertColumns(sqlCreateTableStatement.getColumns(), processCreateTableScratchObject);

            final int tableId = allocateTableIdFunction.applyAsInt(parameter);

            columns = columnsBuilder.build();

            final long hashTableName = stringManagement.getHashStringRef(parsedTableName);

            result = new Table(parsedTableName, hashTableName, tableId, columns);
        }
        finally {

            if (columns != null) {

                columnIndexListAllocator.freeIndexList(columns);
            }

            columnIndexListAllocator.freeIndexListBuilder(columnsBuilder);

            ddlSchemaCachedObjects.freeProcessCreateTableScratchObject(processCreateTableScratchObject);
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
