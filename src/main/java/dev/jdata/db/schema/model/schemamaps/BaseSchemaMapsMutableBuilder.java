package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.utils.builders.BaseMutableBuilder;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseSchemaMapsMutableBuilder<T extends ISchemaMaps, U extends ISchemaMaps & IHeapSchemaMapsMarker, V> extends BaseMutableBuilder<T, U, V> {

    protected static <T> void checkHeapSchemaMapsBuildParameters(AllocationType allocationType, T[] ddlObjectTypeMutables) {

        checkBuildParameters(allocationType, ddlObjectTypeMutables);
        Checks.checkArrayLength(ddlObjectTypeMutables, DDLObjectType.getNumObjectTypes());
    }

    protected static <T> void checkCachedSchemaMapsBuildParameters(AllocationType allocationType, T[] ddlObjectTypeMutables) {

        checkCachedBuildParameters(allocationType, ddlObjectTypeMutables);
        Checks.checkArrayLength(ddlObjectTypeMutables, DDLObjectType.getNumObjectTypes());
    }

    <P> BaseSchemaMapsMutableBuilder(AllocationType allocationType, long minimumCapacity, P parameter, IBuilderMutableAllocator<V, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }
}
