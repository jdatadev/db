package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import dev.jdata.db.utils.function.ObjLongFunction;

public class SortHelper {

    public static <
                    ITERABLE extends IElementsIterable & IOnlyElementsView,
                    COMPARATOR,
                    IMMUTABLE extends IElements & IOnlyElementsView,
//                    CLASS_MUTABLE extends IMutableElements & IOnlyElementsMutable & IOrderedAddable<ITERABLE> & IOrderedElementsMutators<COMPARATOR>,
                    CLASS_MUTABLE extends IMutableElements & IOnlyElementsMutable & IOrderedAddable<ITERABLE> & IOrderedElementsMutators<COMPARATOR>,
                    P>

    IMMUTABLE sortedOf(ITERABLE elements, COMPARATOR comparator, P parameter, ObjLongFunction<P, CLASS_MUTABLE> allocateMutable, BiConsumer<CLASS_MUTABLE, P> freeMutable,
            BiFunction<CLASS_MUTABLE, P, IMMUTABLE> copyToImmutable, Function<P, IMMUTABLE> emptyImmutableSupplier) {

        Objects.requireNonNull(elements);
        Objects.requireNonNull(comparator);
        Objects.requireNonNull(allocateMutable);
        Objects.requireNonNull(freeMutable);
        Objects.requireNonNull(copyToImmutable);
        Objects.requireNonNull(emptyImmutableSupplier);

        final IMMUTABLE result;

        final long numElements = elements.getNumElements();

        if (numElements != 0L) {

            final CLASS_MUTABLE sorted = allocateMutable.apply(parameter, numElements);

            try {
                sorted.addTail(elements);

                sorted.sort(comparator);

                result = copyToImmutable.apply(sorted, parameter);
            }
            finally {
                freeMutable.accept(sorted, parameter);
            }
        }
        else {
            result = emptyImmutableSupplier.apply(parameter);
        }

        return result;
    }
}
