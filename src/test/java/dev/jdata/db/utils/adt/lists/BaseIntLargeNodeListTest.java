package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongContainsOnlyPredicate;

abstract class BaseIntLargeNodeListTest<T extends BaseLargeNodeList<?, ?, ?> & IMutableIntSingleHeadNodeList> extends BaseLargeNodeListTest<T> {

    @Override
    final long getValue(T list, long node) {

        return list.getValue(node);
    }

    @Override
    final boolean contains(T list, int value) {

        return list.contains(value);
    }

    @Override
    final boolean containsOnly(T list, int value) {

        return list.containsOnly(value);
    }

    @Override
    final boolean containsOnly(T list, int value, ILongContainsOnlyPredicate predicate) {

        return list.containsOnly(value, (i, l) -> predicate.test(i, l));
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
    final long addHeadAndReturnNode(T list, int value) {

        return list.addHeadAndReturnNode(value);
    }

    @Override
    final void addHead(T list, int value) {

        list.addHead(value);
    }

    @Override
    final long addTailAndReturnNode(T list, int value) {

        return list.addTailAndReturnNode(value);
    }

    @Override
    final long removeHeadAndReturnValue(T list) {

        return list.removeHeadAndReturnValue();
    }
}
