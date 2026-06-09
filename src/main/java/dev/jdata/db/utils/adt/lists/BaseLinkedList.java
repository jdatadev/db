package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.BiConsumer;

import dev.jdata.db.utils.adt.elements.BaseNumElements;
import dev.jdata.db.utils.adt.elements.ElementsExceptions;

abstract class BaseLinkedList<N extends SinglyLinkedNode<N, L>, L extends BaseLinkedList<N, L>> extends BaseNumElements<Void, Void, Void> {

    private N head;
    private N tail;

    BaseLinkedList(AllocationType allocationType) {
        super(allocationType);
    }

    final <P> void forEachNode(P parameter, BiConsumer<N, P> forEach) {

        Objects.requireNonNull(forEach);

        for (N node = head; node != null; node = node.next) {

            forEach.accept(node, parameter);
        }
    }

    final N getListHeadNode() {

        return head;
    }

    final N getListTailNode() {

        return tail;
    }

    final void clearElements() {

        this.head = this.tail = null;

        clearNumElements();
    }

    final void addHeadNode(N node) {

        Objects.requireNonNull(node);

        final N headNode = head;

        if (headNode != null) {

            node.next = headNode;
        }
        else {
            node.next = null;
            this.tail = node;
        }

        this.head = node;

        incrementNumElements();
    }

    final N addTailNodeAndReturnPreviousTailNode(N node) {

        final N result = tail;

        addTailNode(node);

        return result;
    }

    private void addTailNode(N node) {

        Objects.requireNonNull(node);

        final N tailNode = tail;

        if (tailNode == null) {

            this.head = node;
        }

        node.next = null;
        this.tail = node;

        incrementNumElements();
    }

    final N removeHeadNodeAndReturnNode() {

        final N headNode = head;

        if (headNode == null) {

            throw ElementsExceptions.emptyException();
        }
        else {
            if (headNode == tail) {

                this.head = this.tail = null;
            }
            else {
                this.head = headNode.next;
            }
        }

        decrementNumElements();

        return headNode;
    }

    final N removeTailNodeAndReturnNode(N previousNode) {

        final N tailNode = tail;

        if (tailNode == null) {

            throw ElementsExceptions.emptyException();
        }
        else {
            if (tailNode == head) {

                if (previousNode != null) {

                    throw new IllegalArgumentException();
                }

                this.head = this.tail = null;
            }
            else {
                Objects.requireNonNull(previousNode);

                if (previousNode.next != tailNode) {

                    throw new IllegalArgumentException();
                }

                previousNode.next = null;
                this.tail = previousNode;
            }
        }

        decrementNumElements();

        return tailNode;
    }
}
