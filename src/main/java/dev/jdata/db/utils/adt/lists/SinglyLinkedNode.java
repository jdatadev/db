package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.Initializable;

abstract class SinglyLinkedNode<N extends SinglyLinkedNode<N, L>, L extends BaseLinkedList<N, L>> {

    L list;
    N next;

    public final N getNext() {

        checkNodeAllocated();

        return next;
    }

    final void initialize(L list) {

        this.list = Initializable.checkNotYetInitialized(this.list, list);
    }

    final void reset() {

        this.list = Initializable.checkResettable(list);
    }

    final void checkNodeAllocated() {

        if (!isAllocated()) {

            throw new IllegalStateException();
        }
    }

    final boolean isAllocated() {

        return list != null;
    }
}
