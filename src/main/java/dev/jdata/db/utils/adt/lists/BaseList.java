package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.BaseElements;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseList<T, U extends BaseList<T, U, V>, V extends BaseValues<T, U, V>> extends BaseElements {

    public static final long NO_NODE = -1L;

    static long checkIsNode(long node) {

        return Checks.isNotNegative(node);
    }

    abstract long getNextNode(long node);

    private final V values;

    BaseList(V values) {

        this.values = values;
    }

    final V getValues() {
        return values;
    }

    final T toListArrayValues(long headNode) {

        checkIsNode(headNode);

        return toListArrayValues(headNode, intNumElements(getNumElements(headNode)));
    }

    final T toListArrayValues(long headNode, int numElements) {

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
    private U getThis() {

        return (U)this;
    }
}
