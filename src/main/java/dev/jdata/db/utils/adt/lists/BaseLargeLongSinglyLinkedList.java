package dev.jdata.db.utils.adt.lists;

abstract class BaseLargeLongSinglyLinkedList<T> extends BaseLargeSinglyLinkedList<T, long[], LongValues> {

    BaseLargeLongSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity, LongValues::new);
    }

    @Override
    final void reallocateOuter(int newOuterLength) {

    }

    final long addHead(long value, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long newHeadNode = addHeadNodeAndReturnNode(headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(newHeadNode, value);

        return newHeadNode;
    }

    final long addTail(long value, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        final long newTailNode = addTailNodeAndReturnNode(headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(newTailNode, value);

        return newTailNode;
    }

    final long removeHead(long headNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        return getValue(removeHeadNodeAndReturnNode(headNode, headNodeSetter, tailNodeSetter));
    }

    private long getValue(long node) {

        return getValues().getValue(this, node);
    }

    private void setValue(long node, long value) {

        getValues().setValue(this, node, value);
    }
}
