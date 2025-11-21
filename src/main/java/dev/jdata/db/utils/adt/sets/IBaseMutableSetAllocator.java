package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.elements.IMutableOnlyElementsAllocator;

@Deprecated // necessary?
interface IBaseMutableSetAllocator<T extends IMutableElements & ISetTypeView, U extends IMutableFrom> extends IMutableOnlyElementsAllocator<T, U> {

}
