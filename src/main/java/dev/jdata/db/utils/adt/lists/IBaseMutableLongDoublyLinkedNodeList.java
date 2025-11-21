package dev.jdata.db.utils.adt.lists;

interface IBaseMutableLongDoublyLinkedNodeList extends IMutableLongSingleHeadNodeList, ILongDoublyLinkedSingleHeadNodeListMutable {

    @Override
    default boolean removeAtMostOne(long element) {

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
