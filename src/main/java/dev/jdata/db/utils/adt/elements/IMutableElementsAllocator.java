package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.allocators.IMutableAllocator;
import dev.jdata.db.utils.allocators.ITypedInstanceAllocator;

public interface IMutableElementsAllocator<T extends IMutableElements> extends IBaseElementsAllocator, IMutableAllocator<T>, ITypedInstanceAllocator<T> {

}
