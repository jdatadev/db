package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.lists.ILongList.ContainsOnlyPredicate;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLargeIntListTest<T extends BaseLargeList<?, ?, ?> & LargeIntList> extends BaseLargeListTest<T> {

    @Override
    final long getValue(T list, long node) {

        return list.getValue(node);
    }

    @Override
    final boolean contains(T list, long value) {

        return list.contains(Integers.checkLongToInt(value));
    }

    @Override
    final boolean containsOnly(T list, long value) {

        return list.containsOnly(Integers.checkLongToInt(value));
    }

    @Override
    final boolean containsOnly(T list, long value, ContainsOnlyPredicate predicate) {

        return list.containsOnly(Integers.checkLongToInt(value), (i, l) -> predicate.test(i, l));
    }

    @Override
    final long[] toArray(T list) {

        final int[] intArray = list.toArray();

        final int numElements = intArray.length;

        final long[] result = new long[numElements];

        for (int i = 0; i < numElements; ++ i) {

            result[i] = intArray[i];
        }

        return result;
    }

    @Override
    final long addHead(T list, long value) {

        return list.addHead(Integers.checkLongToInt(value));
    }

    @Override
    final long addTail(T list, long value) {

        return list.addTail(Integers.checkLongToInt(value));
    }

    @Override
    final long removeHead(T list) {

        return list.removeHead();
    }
}
