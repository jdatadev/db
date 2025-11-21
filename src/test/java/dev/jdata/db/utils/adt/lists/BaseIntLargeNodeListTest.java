package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongContainsOnlyPredicate;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseIntLargeNodeListTest<T extends BaseLargeNodeList<?, ?, ?> & IMutableIntSingleHeadNodeList> extends BaseLargeNodeListTest<T> {

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
    final boolean containsOnly(T list, long value, ILongContainsOnlyPredicate predicate) {

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
    final long addHeadReturnNode(T list, long value) {

        return list.addHeadAndReturnNode(Integers.checkLongToInt(value));
    }

    @Override
    final long addTailAndReturnNode(T list, long value) {

        return list.addTailAndReturnNode(Integers.checkLongToInt(value));
    }

    @Override
    final long removeHeadAndReturnValue(T list) {

        return list.removeHeadAndReturnValue();
    }
}
