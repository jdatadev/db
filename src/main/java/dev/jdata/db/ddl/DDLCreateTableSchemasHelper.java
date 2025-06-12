package dev.jdata.db.ddl;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.SchemaMapBuilders;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;

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

        Objects.requireNonNull(sqlCreateTableStatement);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(allocateTableIdFunction);
        Objects.requireNonNull(ddlSchemaCachedObjects);

        final Table result;

        final long parsedTableName = stringManagement.resolveParsedStringRef(sqlCreateTableStatement.getName());

        final IndexListAllocator<Column> columnIndexListAllocator = ddlSchemaCachedObjects.getColumnIndexListAllocator();

        final IndexList.Builder<Column> columnsBuilder = IndexList.createBuilder(sqlCreateTableStatement.getColumns().size(), columnIndexListAllocator);

        IIndexList<Column> columns = null;

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

    public static final class ProcessCreateTableScratchObject extends ProcessTableScratchObject<SQLTableColumnDefinition> {

        void initializeCreateTable(StringManagement stringManagement, IndexList.Builder<Column> columnsBuilder) {

            initialize(stringManagement, DBConstants.INITIAL_COLUMN_ID, columnsBuilder, Function.identity());
        }
    }
}
