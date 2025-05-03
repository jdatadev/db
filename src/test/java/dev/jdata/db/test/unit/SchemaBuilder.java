package dev.jdata.db.test.unit;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import dev.jdata.db.DBConstants;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.IDatabaseSchema;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;
import dev.jdata.db.utils.checks.Checks;

public final class SchemaBuilder {

    private final String databaseName;
    private final ILongToObjectMaxDistanceMapAllocator<Table> longToObjectMapAllocator;

    private final int[] schemaObjectIdAllocators;

    private final IndexList.Builder<Table> tablesBuilder;

    private SchemaBuilder(String databaseName, ILongToObjectMaxDistanceMapAllocator<Table> longToObjectMapAllocator) {

        this.databaseName = Checks.isDatabaseName(databaseName);
        this.longToObjectMapAllocator = Objects.requireNonNull(longToObjectMapAllocator);

        this.schemaObjectIdAllocators = new int[DDLObjectType.values().length];

        Arrays.fill(schemaObjectIdAllocators, DBConstants.INITIAL_SCHEMA_OBJECT_ID);

        this.tablesBuilder = IndexList.createBuilder(Table[]::new);
    }

    private int allocateSchemaObjectId(DDLObjectType ddlObjectType) {

        return schemaObjectIdAllocators[ddlObjectType.ordinal()] ++;
    }

    public SchemaBuilder addTable(String tableName, Consumer<TableBuilder> b) {

        Checks.isTableName(tableName);
        Objects.requireNonNull(b);

        final int tableId = allocateSchemaObjectId(DDLObjectType.TABLE);

        final TableBuilder tableBuilder = TableBuilder.create(tableName, tableId);

        b.accept(tableBuilder);

        tablesBuilder.addTail(tableBuilder.build());

        return this;
    }

    public IDatabaseSchema build() {

        final SchemaMap<Table> tableSchemaMap = SchemaMap.of(tablesBuilder.build(), Table[]::new, longToObjectMapAllocator);

        final CompleteSchemaMaps schemaMaps = new CompleteSchemaMaps(tableSchemaMap, SchemaMap.empty(), SchemaMap.empty(), SchemaMap.empty(), SchemaMap.empty(),
                SchemaMap.empty());

        final DatabaseSchemaVersion schemaVersion = DatabaseSchemaVersion.of(DatabaseSchemaVersion.INITIAL_VERSION);

        final DatabaseId databaseId = new DatabaseId(DBConstants.INITIAL_DESCRIPTORABLE, databaseName);

        return CompleteDatabaseSchema.of(databaseId, schemaVersion, schemaMaps);
    }
}
