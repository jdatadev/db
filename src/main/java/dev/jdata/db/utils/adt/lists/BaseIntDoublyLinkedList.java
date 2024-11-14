package dev.jdata.db.utils.adt.lists;

@Deprecated
abstract class BaseIntDoublyLinkedList<T> extends BaseIntList<BaseLongDoublyLinkedList<T>> {

    BaseIntDoublyLinkedList(BaseLongDoublyLinkedList<T> delegate) {
        super(delegate);
    }

    final int addHeadValue(int value, int head, int tail, IntNodeSetter<T> headSetter, IntNodeSetter<T> tailSetter) {

        return (int)delegate.addHeadValue(value, head, tail, (o, h) -> headSetter.setNode(o, (int)h), (o, t) -> tailSetter.setNode(o, (int)t));
    }
}
