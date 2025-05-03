package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.BaseElements;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseList<
                INSTANCE,
                LIST extends BaseList<INSTANCE, LIST, VALUES>,
                VALUES extends BaseValues<INSTANCE, LIST, VALUES>>

        extends BaseElements {

    public static final long NO_NODE = -1L;

    static long checkIsNode(long node) {

        return Checks.isNotNegative(node);
    }

    abstract long getNextNode(long node);

    private final VALUES values;

    BaseList(VALUES values) {

        this.values = values;
    }

    protected final VALUES getValues() {
        return values;
    }

    final INSTANCE toListArrayValues(long headNode) {

        checkIsNode(headNode);

        return toListArrayValues(headNode, intNumElements(getNumElements(headNode)));
    }

    final INSTANCE toListArrayValues(long headNode, int numElements) {

        checkIsNode(headNode);
        Checks.isNumElements(numElements);

        return values.toArray(getThis(), headNode, numElements);
    }

    private long getNumElements(long headNode) {

        long count = 0L;

        for (long node = headNode; node != NO_NODE; node = getNextNode(node)) {

            ++ count;
        }

        return count;
    }

    @SuppressWarnings("unchecked")
    private LIST getThis() {

        return (LIST)this;
    }
}
