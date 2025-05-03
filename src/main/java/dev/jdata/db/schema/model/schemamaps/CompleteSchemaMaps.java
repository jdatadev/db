package dev.jdata.db.schema.model.schemamaps;

import java.util.function.Function;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
public final class CompleteSchemaMaps extends BaseSchemaMaps<SchemaMap<?>> {

    public interface CompleteSchemaMapsBuilderAllocator {

        Builder allocateCompleteSchemaMapsBuilder();

        void freeCompleteSchemaMapsBuilder(Builder builder);
    }

    public static final class Builder extends BaseBuilder<Builder> {

        private Builder(CompleteSchemaMapsBuilderAllocator allocator) {

        }

        public CompleteSchemaMaps build() {

            return new CompleteSchemaMaps(mapOrEmpty(DDLObjectType.TABLE), mapOrEmpty(DDLObjectType.VIEW), mapOrEmpty(DDLObjectType.INDEX), mapOrEmpty(DDLObjectType.TRIGGER),
                    mapOrEmpty(DDLObjectType.FUNCTION), mapOrEmpty(DDLObjectType.PROCEDURE));
        }
    }

    public static final CompleteSchemaMaps EMPTY = new CompleteSchemaMaps(SchemaMap.empty(), SchemaMap.empty(), SchemaMap.empty(), SchemaMap.empty(), SchemaMap.empty(),
            SchemaMap.empty());

    public static Builder createBuilder(CompleteSchemaMapsBuilderAllocator allocator) {

        return new Builder(allocator);
    }

    public CompleteSchemaMaps(SchemaMap<Table> tables, SchemaMap<View> views, SchemaMap<Index> indices, SchemaMap<Trigger> triggers, SchemaMap<DBFunction> functions,
            SchemaMap<Procedure> procedures) {
        super(Function.identity(), SchemaMap[]::new, Function.identity(), tables, views, indices, triggers, functions, procedures);
    }

    CompleteSchemaMaps() {
        super(Function.identity(), SchemaMap[]::new, Function.identity());
    }
}
