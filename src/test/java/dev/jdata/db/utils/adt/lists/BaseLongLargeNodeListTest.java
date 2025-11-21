package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongContainsOnlyPredicate;

abstract class BaseLongLargeNodeListTest<T extends BaseLargeNodeList<?, ?, ?> & IMutableLongSingleHeadNodeList> extends BaseLargeNodeListTest<T> {

    @Override
    final long getValue(T list, long node) {

        return list.getValue(node);
    }

    @Override
    final boolean contains(T list, long value) {

        return list.contains(value);
    }

    @Override
    final boolean containsOnly(T list, long value) {

        return list.containsOnly(value);
    }

    @Override
    final boolean containsOnly(T list, long value, ILongContainsOnlyPredicate predicate) {

        return list.containsOnly(value, predicate);
    }

    @Override
    final long[] toArray(T list) {

        return list.toArray();
    }

    @Override
    final long addHeadReturnNode(T list, long value) {

        return list.addHeadAndReturnNode(value);
    }

    @Override
    final long addTailAndReturnNode(T list, long value) {

        return list.addTailAndReturnNode(value);
    }

    @Override
    final long removeHeadAndReturnValue(T list) {

        return list.removeHeadAndReturnValue();
    }
}
