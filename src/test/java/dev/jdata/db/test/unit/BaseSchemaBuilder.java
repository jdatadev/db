package dev.jdata.db.test.unit;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

import dev.jdata.db.engine.database.IStringStorer;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMap;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseSchemaBuilder<T extends BaseSchemaBuilder<T>> {

    private final IStringStorer stringStorer;
    private final IHeapMutableLongToObjectDynamicMapAllocator<Table> longToObjectMapAllocator;
    private final ToIntFunction<DDLObjectType> schemaObjectIdAllocator;

    private final IHeapIndexListBuilder<Table> tablesBuilder;

    BaseSchemaBuilder(IStringStorer stringStorer, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {
        this(stringStorer, IHeapMutableLongToObjectDynamicMapAllocator.create(Table[]::new), schemaObjectIdAllocator);
    }

    private BaseSchemaBuilder(IStringStorer stringStorer, IHeapMutableLongToObjectDynamicMapAllocator<Table> longToObjectMapAllocator,
            ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

        this.stringStorer = Objects.requireNonNull(stringStorer);
        this.longToObjectMapAllocator = Objects.requireNonNull(longToObjectMapAllocator);
        this.schemaObjectIdAllocator = Objects.requireNonNull(schemaObjectIdAllocator);

        this.tablesBuilder = IHeapIndexListBuilder.create(Table[]::new);
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

    final IHeapAllCompleteSchemaMaps buildCompleteSchemaMaps() {

        final IHeapSchemaMap<Table> tableSchemaMap = IHeapSchemaMap.of(tablesBuilder.buildOrNull(), Table[]::new, longToObjectMapAllocator);

        return IHeapAllCompleteSchemaMaps.of(tableSchemaMap, IHeapSchemaMap.empty(), IHeapSchemaMap.empty(), IHeapSchemaMap.empty(), IHeapSchemaMap.empty(),
                IHeapSchemaMap.empty());
    }

    @SuppressWarnings("unchecked")
    private T getThis() {

        return (T)this;
    }
}
