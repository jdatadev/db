package dev.jdata.db.utils.adt.lists;

interface IBaseMutableIntDoublyLinkedSingleHeadNodeList extends IMutableIntSingleHeadNodeList, IIntDoublyLinkedSingleHeadNodeListMutable {

    @Override
    default boolean removeAtMostOne(int element) {

        final boolean result;

        final long node = findAtMostOneNode(element);

        if (node != LargeNodeLists.NO_LONG_NODE) {

            removeNode(node);

            result = true;
        }
        else {
            result = false;
        }

        return result;
    }
}
