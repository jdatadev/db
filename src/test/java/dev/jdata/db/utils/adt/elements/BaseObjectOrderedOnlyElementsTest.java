package dev.jdata.db.utils.adt.elements;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.jutils.Counter;

public abstract class BaseObjectOrderedOnlyElementsTest<

                T,
                U extends IObjectIterable<Integer> & IOnlyElementsView,
                V extends IObjectIterable<T> & IOnlyElementsView> extends BaseOnlyElementsTest<U, V> {

    @Override
    protected final <P> long count(U elements, P parameter, BiPredicate<Integer, P> predicate) {

        return elements.count(parameter, predicate);
    }

    @Override
    protected final long countWithClosure(U elements, Predicate<Integer> predicate) {

        return elements.closureOrConstantCount(predicate);
    }

    @Override
    protected final int maxInt(U elements, int defaultValue, ToIntFunction<Integer> mapper) {

        return elements.maxInt(defaultValue, mapper);
    }

    @Override
    protected final long maxLong(U elements, long defaultValue, ToLongFunction<Integer> mapper) {

        return elements.maxLong(defaultValue, mapper);
    }

    protected void checkElementsSameAs(V elements, @SuppressWarnings("unchecked") T ... expectedElements) {

        checkOnlyElementsSameAs(elements, expectedElements);
    }

    @SafeVarargs
    private static <T, U extends IObjectIterable<T> & IOnlyElementsView> void checkOnlyElementsSameAs(U elements, T ... expectedElements) {

        final Counter counter = new Counter(0);

        final int numElements = expectedElements.length;

        checkNumOnlyElements(elements, numElements);

        elements.forEach(expectedElements, counter, (element, expected, indexCounter) -> {

            final int index = indexCounter.getAndIncrement();

            assertThat(element).isSameAs(expectedElements[index]);
        });
    }
}
