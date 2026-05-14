package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IElementsAllocators;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IOnlyElementsBuilder;
import dev.jdata.db.utils.adt.elements.IOnlyElementsMutable;
import dev.jdata.db.utils.adt.elements.OnlyElementsAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

abstract class MapAllocator<

                IMMUTABLE extends IElements & IMapType,
                HEAP_IMMUTABLE extends IElements & IMapType & IHeapContainsMarker,
                INTERFACE_MUTABLE extends IMutableElements & IMutableMapType,
                CLASS_MUTABLE extends BaseADTElements<?, CLASS_MUTABLE, ?> & IMutableElements & IOnlyElementsMutable & IMutableMapType,
                BUILDER extends IOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends OnlyElementsAllocator<IMMUTABLE, HEAP_IMMUTABLE, Void, INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER> {

    MapAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, Void> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }

    @Override
    protected final long getElementsArrayLength(Void elementsArray) {

        Objects.requireNonNull(elementsArray);

        throw new UnsupportedOperationException();
    }
}
