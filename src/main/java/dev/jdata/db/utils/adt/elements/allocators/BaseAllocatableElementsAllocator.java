package dev.jdata.db.utils.adt.elements.allocators;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.BaseAllocatableArrayAllocator;
import dev.jdata.db.utils.allocators.BaseArrayAllocator;

abstract class BaseAllocatableElementsAllocator<T extends Allocatable> extends BaseAllocatableArrayAllocator<T> {

    protected BaseAllocatableElementsAllocator<T>(IntFunction<T> createArray, ToIntFunction<T> arrayLengthGetter) {
        super(createArray, arrayLengthGetter);
    }

}
