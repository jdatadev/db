package dev.jdata.db.ddl;

import java.util.Objects;
import java.util.function.ToIntFunction;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.function.BiObjToIntFunction;

@Deprecated
public final class SchemaObjectIdAllocator<P> extends ObjectCacheNode implements IResettable {

    private final ToIntFunction<DDLObjectType> idAllocatorFunction;

    private P parameter;
    private BiObjToIntFunction<DDLObjectType, P> allocateSchemaObjectIdFunction;

    public SchemaObjectIdAllocator(AllocationType allocationType, P parameter, BiObjToIntFunction<DDLObjectType, P> allocateSchemaObjectIdFunction) {
        this(allocationType);

        initialize(parameter, allocateSchemaObjectIdFunction);
    }

    SchemaObjectIdAllocator(AllocationType allocationType) {
        super(allocationType);

        this.idAllocatorFunction = this::allocateSchemaObjectId;
    }

    void initialize(P parameter, BiObjToIntFunction<DDLObjectType, P> allocateSchemaObjectIdFunction) {

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

    public ToIntFunction<DDLObjectType> getIdAllocatorFunction() {
        return idAllocatorFunction;
    }

    private int allocateSchemaObjectId(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        checkIsAllocated();

        return allocateSchemaObjectIdFunction.apply(ddlObjectType, parameter);
    }
}
