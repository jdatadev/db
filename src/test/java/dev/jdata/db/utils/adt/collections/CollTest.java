package dev.jdata.db.utils.adt.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.lists.Lists;

public final class CollTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testMax() {

        assertThatThrownBy(() -> Coll.max(null, 0, Integer::intValue)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Coll.max(Lists.unmodifiableOf(1, 2, 3), 0, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Coll.max(Lists.unmodifiableOf(1, 2, 3), 0, null)).isInstanceOf(NullPointerException.class);

        assertThat(Coll.max(Lists.empty(), 0,   Integer::intValue)).isEqualTo(0);
        assertThat(Coll.max(Lists.empty(), 123, Integer::intValue)).isEqualTo(123);

        assertThat(Coll.max(Lists.unmodifiableOf(0),                123, Integer::intValue)).isEqualTo(0);
        assertThat(Coll.max(Lists.unmodifiableOf(0, 1),             123, Integer::intValue)).isEqualTo(1);
        assertThat(Coll.max(Lists.unmodifiableOf(1, 2 ,0),          123, Integer::intValue)).isEqualTo(2);
        assertThat(Coll.max(Lists.unmodifiableOf(1, 0, -2),         123, Integer::intValue)).isEqualTo(1);
        assertThat(Coll.max(Lists.unmodifiableOf(1, 0, 1, -2),      123, Integer::intValue)).isEqualTo(1);
        assertThat(Coll.max(Lists.unmodifiableOf(-1, 0, -1, -2),    123, Integer::intValue)).isEqualTo(0);
        assertThat(Coll.max(Lists.unmodifiableOf(-1, -3, -1, -2),   123, Integer::intValue)).isEqualTo(-1);
    }

    @Test
    @Category(UnitTest.class)
    public void testCount() {

        assertThatThrownBy(() -> Coll.count(null, e -> true)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Coll.count(Lists.unmodifiableOf(1, 2, 3), null)).isInstanceOf(NullPointerException.class);

        assertThat(Coll.count(Lists.empty(), e -> true)).isEqualTo(0);

        assertThat(Coll.count(Lists.unmodifiableOf(0),              e -> false)).isEqualTo(0);
        assertThat(Coll.count(Lists.unmodifiableOf(0),              e -> true)).isEqualTo(1);
        assertThat(Coll.count(Lists.unmodifiableOf(0, 1),           e -> false)).isEqualTo(0);
        assertThat(Coll.count(Lists.unmodifiableOf(0, 1),           e -> true)).isEqualTo(2);
        assertThat(Coll.count(Lists.unmodifiableOf(0, 1),           e -> e < 1)).isEqualTo(1);
        assertThat(Coll.count(Lists.unmodifiableOf(1, 2 ,0),        e -> false)).isEqualTo(0);
        assertThat(Coll.count(Lists.unmodifiableOf(1, 2 ,0),        e -> true)).isEqualTo(3);
        assertThat(Coll.count(Lists.unmodifiableOf(1, 2 ,0),        e -> e < 2)).isEqualTo(2);
        assertThat(Coll.count(Lists.unmodifiableOf(1, 0, 1, -2),    e -> e != 0)).isEqualTo(3);
    }

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
        checkSorted(sorter, resultConsumer, Set::of, values, expectedValues);
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

    @Test
    @Category(UnitTest.class)
    public void testToArrayByIndex() {

        assertThatThrownBy(() -> Coll.toArray(null, 1, null, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Coll.toArray((List<Integer>)null, 1, Integer[]::new, List::get)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Coll.toArray(Lists.unmodifiableOf(1), 1, null, List::get)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Coll.toArray(Lists.unmodifiableOf(1), 1, Integer[]::new, null)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> Coll.toArray(Lists.unmodifiableOf(1), -1, Integer[]::new, List::get)).isInstanceOf(IllegalArgumentException.class);

        assertThat(Coll.toArray(Lists.empty(), 0, Integer[]::new, List::get)).isEmpty();
        assertThat(Coll.toArray(Lists.unmodifiableOf(1), 1, Integer[]::new, List::get)).containsExactly(1);
        assertThat(Coll.toArray(Lists.unmodifiableOf(1, 2, 3), 1, Integer[]::new, List::get)).containsExactly(1);
        assertThat(Coll.toArray(Lists.unmodifiableOf(1, 2, 3), 2, Integer[]::new, List::get)).containsExactly(1, 2);
        assertThat(Coll.toArray(Lists.unmodifiableOf(1, 2, 3), 3, Integer[]::new, List::get)).containsExactly(1, 2, 3);
    }
}
