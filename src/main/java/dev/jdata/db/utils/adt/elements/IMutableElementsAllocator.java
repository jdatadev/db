package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.allocators.IMutableAllocator;
import dev.jdata.db.utils.allocators.ITypedInstanceAllocator;

public interface IMutableElementsAllocator<T extends IMutableElements, U extends IMutableFrom>

        extends IBaseElementsAllocator, IMutableAllocator<T, U>, ITypedInstanceAllocator<T> {

}
