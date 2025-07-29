package dev.jdata.db.utils.jdk.adt.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.elements.BaseElementsAggregatesTest;
import dev.jdata.db.utils.jdk.adt.lists.Lists;
import dev.jdata.db.utils.jdk.adt.sets.Sets;

public final class CollTest extends BaseElementsAggregatesTest<Collection<Integer>> {

    @Test
    @Category(UnitTest.class)
    public void testSorted() {

        checkSorted(Coll::unmodifiableSorted, l -> {

            final int toAdd = 123;

            l.add(toAdd);

            assertThat(l).endsWith(toAdd);
        });
    }

    @Test
    @Category(UnitTest.class)
    public void testUnmodifiableSorted() {

        checkSorted(Coll::unmodifiableSorted, l -> assertThatThrownBy(() -> l.add(123)).isInstanceOf(UnsupportedOperationException.class));
    }

    private void checkSorted(BiFunction<Collection<Integer>, Comparator<Integer>, List<Integer>> sorter, Consumer<List<Integer>> resultConsumer) {

        assertThatThrownBy(() -> sorter.apply(null, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> sorter.apply(null, Integer::compareTo)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> sorter.apply(Lists.empty(), null)).isInstanceOf(NullPointerException.class);

        checkSorted(sorter, resultConsumer, new Integer[0]);
        checkSorted(sorter, resultConsumer, new Integer[] { 0 }, 0);
        checkSorted(sorter, resultConsumer, new Integer[] { 1, 2, 3 }, 1, 2, 3);
        checkSorted(sorter, resultConsumer, new Integer[] { 2, 3, 1 }, 1, 2, 3);
        checkSorted(sorter, resultConsumer, new Integer[] { 2, 0, 3, -1, 1 }, -1, 0, 1, 2, 3);
    }

    private void checkSorted(BiFunction<Collection<Integer>, Comparator<Integer>, List<Integer>> sorter, Consumer<List<Integer>> resultConsumer, Integer[] values,
            Integer ... expectedValues) {

        checkSorted(sorter, resultConsumer, Arrays::asList, values, expectedValues);
        checkSorted(sorter, resultConsumer, Sets::of, values, expectedValues);
    }

    private <T extends Collection<Integer>> void checkSorted(BiFunction<Collection<Integer>, Comparator<Integer>, List<Integer>> sorter, Consumer<List<Integer>> resultConsumer,
            Function<Integer[], T> createCollection, Integer[] values, Integer ... expectedValues) {

        final T collection = createCollection.apply(values);

        final List<Integer> sorted = Coll.sorted(collection, Integer::compare);

        assertThat(sorted).isNotSameAs(collection);
        assertThat(sorted).containsExactly(expectedValues);
    }

    @Test
    @Category(UnitTest.class)
    public void testToArray() {

        assertThatThrownBy(() -> Coll.toArray(null, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Coll.toArray(null, Integer[]::new)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Coll.toArray(Lists.unmodifiableOf(1), null)).isInstanceOf(NullPointerException.class);

        assertThat(Coll.toArray(Lists.empty(), Integer[]::new)).isEmpty();
        assertThat(Coll.toArray(Lists.unmodifiableOf(1), Integer[]::new)).containsExactly(1);
        assertThat(Coll.toArray(Lists.unmodifiableOf(1, 2, 3), Integer[]::new)).containsExactly(1, 2, 3);
    }

    @Override
    protected Collection<Integer> createTestElements(Integer[] elementsToAdd) {

        final Collection<Integer> collection = new ArrayList<>();

        collection.addAll(Arrays.asList(elementsToAdd));

        return collection;
    }

    @Override
    protected <P> long count(Collection<Integer> elements, P parameter, BiPredicate<Integer, P> predicate) {

        return Coll.count(elements, parameter, predicate);
    }

    @Override
    protected long countWithClosure(Collection<Integer> elements, Predicate<Integer> predicate) {

        return Coll.closureOrConstantCount(elements, predicate);
    }

    @Override
    protected int maxInt(Collection<Integer> elements, int defaultValue, ToIntFunction<Integer> mapper) {

        return Coll.maxInt(elements, defaultValue, mapper);
    }

    @Override
    protected long maxLong(Collection<Integer> elements, long defaultValue, ToLongFunction<Integer> mapper) {

        return Coll.maxLong(elements, defaultValue, mapper);
    }
}
