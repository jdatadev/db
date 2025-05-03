package dev.jdata.db.utils.adt.hashed;

import java.util.function.Consumer;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.LargeExponentArray;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseLargeArrayHashed<T extends LargeExponentArray> extends BaseLongCapacityHashed<T> {

    private final int innerCapacityExponent;

    protected BaseLargeArrayHashed(int initialOuterCapacity, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialOuterCapacity * CapacityExponents.computeCapacity(innerCapacityExponent), loadFactor, () -> createHashed.apply(initialOuterCapacity, innerCapacityExponent),
                clearHashed);

        Checks.isInitialCapacity(initialOuterCapacity);
        Checks.isCapacityExponent(innerCapacityExponent);

        this.innerCapacityExponent = innerCapacityExponent;
    }

    @Override
    protected final long increaseCapacity(long currentCapacity) {

        return currentCapacity << 2;
    }

    protected final int getInnerCapacityExponent() {
        return innerCapacityExponent;
    }

    protected static int computeOuterCapacity(long newCapacity, int innerCapacityExponent) {

        Checks.isCapacity(newCapacity);
        Checks.isCapacityExponent(innerCapacityExponent);

        return Integers.checkUnsignedLongToUnsignedInt(newCapacity / CapacityExponents.computeCapacity(innerCapacityExponent));
    }

    protected static <T> LargeLongMultiHeadSinglyLinkedList<T> createBuckets(int initialOuterCapacity, int innerCapacityExponent) {

        return new LargeLongMultiHeadSinglyLinkedList<>(initialOuterCapacity, CapacityExponents.computeCapacity(innerCapacityExponent));
    }
}
