package dev.jdata.db.ddl;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.function.BiObjToIntFunction;

public class DDLSchemasHelper {

    public static final class SchemaObjectIdAllocator<P> extends ObjectCacheNode implements IResettable {

        private P parameter;
        private BiObjToIntFunction<DDLObjectType, P> allocateSchemaObjectIdFunction;

        public SchemaObjectIdAllocator(AllocationType allocationType, P parameter, BiObjToIntFunction<DDLObjectType, P> allocateSchemaObjectIdFunction) {
            super(allocationType);

            initialize(parameter, allocateSchemaObjectIdFunction);
        }

        public void initialize(P parameter, BiObjToIntFunction<DDLObjectType, P> allocateSchemaObjectIdFunction) {

            checkIsAllocated();

            this.parameter = parameter;
            this.allocateSchemaObjectIdFunction = Initializable.checkNotYetInitialized(this.allocateSchemaObjectIdFunction, allocateSchemaObjectIdFunction);
        }

        @Override
        public void reset() {

            checkIsAllocated();

            this.parameter = null;
            this.allocateSchemaObjectIdFunction = Initializable.checkResettable(allocateSchemaObjectIdFunction);
        }

        public int allocateSchemaObjectId(DDLObjectType ddlObjectType) {

            Objects.requireNonNull(ddlObjectType);

            checkIsAllocated();

            return allocateSchemaObjectIdFunction.apply(ddlObjectType, parameter);
        }
    }
}
