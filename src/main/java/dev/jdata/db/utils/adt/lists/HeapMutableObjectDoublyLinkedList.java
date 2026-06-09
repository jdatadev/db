package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

final class HeapMutableObjectDoublyLinkedList<T> extends MutableObjectDoublyLinkedList<T> implements IHeapMutableDoublyLinkedList<T> {

    static <T> HeapMutableObjectDoublyLinkedList<T> create(AllocationType allocationType) {

        AllocationType.checkIsHeap(allocationType);

        return new HeapMutableObjectDoublyLinkedList<>(allocationType, new NodeAllocator<T>() {

            @Override
            public Node<T> allocate() {

                return new Node<>();
            }

            @Override
            public void free(Node<T> node) {

                Objects.requireNonNull(node);
            }
        });
    }

    private HeapMutableObjectDoublyLinkedList(AllocationType allocationType, NodeAllocator<T> nodeAllocator) {
        super(allocationType, nodeAllocator);
    }
}
