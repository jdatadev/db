package dev.jdata.db.test.unit;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.database.StringStorer;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;
import dev.jdata.db.utils.allocators.LongToObjectMaxDistanceMapAllocator;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseSchemaBuilder<T extends BaseSchemaBuilder<T>> {

    public static final class SchemaObjectIdAllocators implements ToIntFunction<DDLObjectType> {

        private final int[] schemaObjectIdAllocators;

        public SchemaObjectIdAllocators() {

            this.schemaObjectIdAllocators = new int[DDLObjectType.values().length];

            Arrays.fill(schemaObjectIdAllocators, DBConstants.INITIAL_SCHEMA_OBJECT_ID);
        }

        @Override
        public int applyAsInt(DDLObjectType value) {

            return schemaObjectIdAllocators[value.ordinal()] ++;
        }
    }

    private final StringStorer stringStorer;
    private final ILongToObjectMaxDistanceMapAllocator<Table> longToObjectMapAllocator;
    private final ToIntFunction<DDLObjectType> schemaObjectIdAllocator;

    private final IndexList.Builder<Table> tablesBuilder;

    BaseSchemaBuilder(StringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {
        this(stringStorer, new LongToObjectMaxDistanceMapAllocator<>(Table[]::new), schemaObjectIdAllocator);
    }

    private BaseSchemaBuilder(StringStorer stringStorer, ILongToObjectMaxDistanceMapAllocator<Table> longToObjectMapAllocator,
            ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

        this.stringStorer = Objects.requireNonNull(stringStorer);
        this.longToObjectMapAllocator = Objects.requireNonNull(longToObjectMapAllocator);
        this.schemaObjectIdAllocator = Objects.requireNonNull(schemaObjectIdAllocator);

        this.tablesBuilder = IndexList.createBuilder(Table[]::new);
    }

    private int allocateSchemaObjectId(DDLObjectType ddlObjectType) {

        return schemaObjectIdAllocator.applyAsInt(ddlObjectType);
    }

    public final T addTable(String tableName, Consumer<TableBuilder> b) {

        Checks.isTableName(tableName);
        Objects.requireNonNull(b);

        final int tableId = allocateSchemaObjectId(DDLObjectType.TABLE);

        final TableBuilder tableBuilder = TableBuilder.create(tableName, tableId, stringStorer);

        b.accept(tableBuilder);

        tablesBuilder.addTail(tableBuilder.build());

        return getThis();
    }

    final CompleteSchemaMaps buildCompleteSchemaMaps() {

        final SchemaMap<Table> tableSchemaMap = SchemaMap.of(tablesBuilder.build(), Table[]::new, longToObjectMapAllocator);

        return new CompleteSchemaMaps(tableSchemaMap, SchemaMap.empty(), SchemaMap.empty(), SchemaMap.empty(), SchemaMap.empty(), SchemaMap.empty());
    }

    @SuppressWarnings("unchecked")
    private T getThis() {

        return (T)this;
    }
}
