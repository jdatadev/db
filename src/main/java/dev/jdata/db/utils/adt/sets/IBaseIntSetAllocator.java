package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntUnorderedOnlyElementsAllocator;

interface IBaseIntSetAllocator<T extends IBaseIntSet, U extends IBaseMutableIntSet, V extends IBaseIntSetBuilder<T, ?>> extends IIntUnorderedOnlyElementsAllocator<T, U, V> {

}
