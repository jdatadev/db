package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.utils.builders.BaseMutableBuilder;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseSchemaMapMutableBuilder<T extends ISchemaMap, U extends ISchemaMap & IHeapSchemaMapMarker, V> extends BaseMutableBuilder<T, U, V> {

    protected static <T> void checkSchemaMapBuildParameters(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism, T[] ddlObjectTypeMutables) {

        AllocationType.checkIsAllocationMechanism(allocationType, expectedAllocationMechanism);
        Checks.checkArrayLength(ddlObjectTypeMutables, DDLObjectType.getNumObjectTypes());
    }

    <P> BaseSchemaMapMutableBuilder(AllocationType allocationType, long minimumCapacity, P parameter, IBuilderMutableAllocator<V, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }
}
