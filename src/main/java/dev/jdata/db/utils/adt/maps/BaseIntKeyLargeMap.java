package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IHeapMutableIntLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableIntLargeArray;
import dev.jdata.db.utils.adt.elements.IIntAnyOrderAddable;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseIntKeyLargeMap<VALUES> extends BaseIntegerKeyLargeMap<IMutableIntLargeArray, VALUES> implements IIntKeyMapCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_INT_KEY_MAP;

    static final long NO_INDEX = LargeHashArray.NO_INDEX;

    static final int NO_KEY = -1;

    protected abstract long getHashArrayIndex(int key, long keyMask);

    BaseIntKeyLargeMap(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<IMutableIntLargeArray> createHashed, Consumer<IMutableIntLargeArray> clearHashed, BiIntToObjectFunction<VALUES> createValues) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed, createValues);
    }

    BaseIntKeyLargeMap(AllocationType allocationType, BaseIntKeyLargeMap<VALUES> toCopy, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, IHeapMutableIntLargeArray::copyOf, copyValuesContent);
    }

    @Override
    public final long keys(IIntAnyOrderAddable addable) {

        Objects.requireNonNull(addable);

        if (DEBUG) {

            enter(b -> b.add("addable", addable));
        }

        final long result = keysAndValues(addable, null, (srcIndex, kSrc, vSrc, dstIndex, kDst, vDst) -> kDst.addInAnyOrder(kSrc.get(srcIndex)));

        if (DEBUG) {

            exit(result, b -> b.add("addable", addable));
        }

        return result;
    }
}
