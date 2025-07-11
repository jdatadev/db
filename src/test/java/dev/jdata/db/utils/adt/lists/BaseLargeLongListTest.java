package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongElements.IContainsOnlyPredicate;

abstract class BaseLargeLongListTest<T extends BaseLargeList<?, ?, ?> & IMutableLargeLongList> extends BaseLargeListTest<T> {

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
    final boolean containsOnly(T list, long value, IContainsOnlyPredicate predicate) {

        return list.containsOnly(value, predicate);
    }

    @Override
    final long[] toArray(T list) {

        return list.toArray();
    }

    @Override
    final long addHead(T list, long value) {

        return list.addHead(value);
    }

    @Override
    final long addTail(T list, long value) {

        return list.addTail(value);
    }

    @Override
    final long removeHead(T list) {

        return list.removeHead();
    }
}
