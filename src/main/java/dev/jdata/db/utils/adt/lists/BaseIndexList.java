package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.BaseArrayAllocator;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseIndexList<T> extends BaseObjectArrayList<T> implements IIndexListGetters<T> {

    static final class MutableIndexListArrayAllocator<T, U extends MutableIndexList<T>> extends BaseArrayAllocator<U> {

        MutableIndexListArrayAllocator(AllocationType allocationType, IntFunction<U> createList) {
            super(createList, l -> Integers.checkUnsignedLongToUnsignedInt(l.getNumElements()));
        }

        U allocateMutableIndexList(int minimumCapacity) {

            return allocateArrayInstance(minimumCapacity);
        }

        void freeMutableIndexList(U list) {

            freeArrayInstance(list);
        }
    }

    BaseIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    BaseIndexList(IntFunction<T[]> createElementsArray, IIndexList<T> toCopy) {
        super(createElementsArray, toCopy);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IIndexList<T> toCopy) {
        super(allocationType, createElementsArray, toCopy);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T instance) {
        super(allocationType, createElementsArray, instance);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances) {
        super(allocationType, createElementsArray, instances);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances, int numElements) {
        super(allocationType, createElementsArray, instances, numElements);
    }

    BaseIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray);
    }

    BaseIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }

    BaseIndexList(BaseIndexList<T> toCopy) {
        super(toCopy);
    }
}
