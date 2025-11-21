package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IMutableOnlyElementsAllocator;

interface IBaseMutableSetAllocator<T extends IMutableElements & ISetView> extends IMutableOnlyElementsAllocator<T> {

}
