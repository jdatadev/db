package dev.jdata.db.utils.adt.lists;

import java.util.Comparator;
import java.util.Objects;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;

abstract class MutableObjectDoublyLinkedList<T> extends BaseObjectDoublyLinkedList<T> implements IMutableDoublyLinkedList<T> {

    interface NodeAllocator<T> {

        Node<T> allocate();

        void free(Node<T> node);
    }

    private final NodeAllocator<T> nodeAllocator;

    MutableObjectDoublyLinkedList(AllocationType allocationType, NodeAllocator<T> nodeAllocator) {
        super(allocationType);

        this.nodeAllocator = Objects.requireNonNull(nodeAllocator);
    }

    @Override
    public final long getCapacity() {

        return Long.MAX_VALUE;
    }

    @Override
    public final void clear() {

        forEachNode(this, (n, i) -> i.freeNode(n));

        clearElements();
    }

    @Override
    public final void addHead(T instance) {

        Objects.requireNonNull(instance);

        final Node<T> node = allocateNode();

        node.element = instance;

        addHeadNode(node);

        final Node<T> nextNode = node.next;

        if (nextNode != null) {

            nextNode.previous = node;
        }

        node.previous = null;
        node.element = instance;
    }

    @Override
    public final void addTail(T instance) {

        Objects.requireNonNull(instance);

        final Node<T> node = allocateNode();

        node.element = instance;

        final Node<T> previousNode = addTailNodeAndReturnPreviousTailNode(node);

        if (previousNode != null) {

            previousNode.next = node;
            node.previous = previousNode;
        }
        else {
            node.previous = null;
        }
    }

    @Override
    public final T removeHeadAndReturnValue() {

        final Node<T> removedNode = removeHeadNodeAndReturnNode();

        final T result = removedNode.element;

        freeNode(removedNode);

        final Node<T> nextNode = removedNode.next;

        if (nextNode != null) {

            nextNode.previous = null;
        }

        return result;
    }

    @Override
    public final T removeTailAndReturnValue() {

        final T result;

        final Node<T> removedNode;

        final Node<T> tailNode = getListTailNode();

        if (tailNode == null) {

            throw ElementsExceptions.emptyException();
        }
        else {
            final Node<T> previousNode = tailNode.previous;

            removedNode = removeTailNodeAndReturnNode(previousNode);

            result = removedNode.element;

            freeNode(removedNode);
        }

        return result;
    }

    @Override
    public final void removeNode(Node<T> node) {

        Objects.requireNonNull(node);

        if (!node.isAllocated()) {

            throw new IllegalArgumentException();
        }

        final long numElements = getNumElements();

        if (numElements == 0L) {

            throw ElementsExceptions.emptyException();
        }
        else if (numElements == 1L) {

            if (node != getListTailNode()) {

                throw new IllegalArgumentException();
            }

            removeTail();
        }
        else if (node == getListHeadNode()) {

            removeHead();
        }
        else if (node == getListTailNode()) {

            removeTail();
        }
        else {
            final Node<T> previousNode = node.previous;
            final Node<T> nextNode = node.next;

            previousNode.next = nextNode;
            nextNode.previous = previousNode;

            freeNode(node);

            decrementNumElements();
        }
    }

    @Override
    public final void sort(Comparator<? super T> comparator) {

        Objects.requireNonNull(comparator);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<Void, Void, P, R> makeFromElements) {

        checkMakeFromElementsParameters(allocationType, parameter, makeFromElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void recreateElements() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void resetToNull() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final Void copyValues(Void values, long startIndex, long numElements) {

        checkLongCopyValuesParameters(values, 0L, startIndex, numElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void initializeWithValues(Void values, long numElements) {

        checkIntIntitializeWithValuesParameters(values, 0L, numElements);

        throw new UnsupportedOperationException();
    }

    private Node<T> allocateNode() {

        final Node<T> node = nodeAllocator.allocate();

        node.initialize(this);

        return node;
    }

    private void freeNode(Node<T> node) {

        checkNode(node);

        node.reset();

        nodeAllocator.free(node);
    }
}
