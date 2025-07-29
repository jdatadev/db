package dev.jdata.db.utils.jdk.adt.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.collections.BaseCollectionsTest;
import dev.jdata.db.utils.jdk.adt.collections.Coll;

public final class ListsTest extends BaseCollectionsTest<List<Integer>, List<String>> {

    @Test
    @Category(UnitTest.class)
    public void testUnmodifiableCopyOf() {

        assertThatThrownBy(() -> Lists.unmodifiableCopyOf(null)).isInstanceOf(NullPointerException.class);

        assertThat(Lists.unmodifiableCopyOf(Collections.emptyList())).isSameAs(Collections.emptyList());
        assertThat(Lists.unmodifiableCopyOf(new ArrayList<>())).isSameAs(Collections.emptyList());

        checkUnmodifiableCopyOf(0);
        checkUnmodifiableCopyOf(1, 2, 3);
    }

    private void checkUnmodifiableCopyOf(Integer ... values) {

        final List<Integer> list = Arrays.asList(values);

        final List<Integer> unmodifiableCopy = Lists.unmodifiableCopyOf(list);

        assertThat(unmodifiableCopy).isNotSameAs(list);
        assertThat(unmodifiableCopy).isEqualTo(list);

        assertThatThrownBy(() -> unmodifiableCopy.add(123)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testRemoveFromArrayList() {

        assertThatThrownBy(() -> Lists.remove(null, 123, (e, p) -> true)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Lists.remove(new ArrayList<>(), 123, null)).isInstanceOf(NullPointerException.class);

        final Object parameter = new Object();

        Lists.remove(Coll.of(new int[] { 123 }, ArrayList::new), parameter, (e, p) -> {

            assertThat(p).isSameAs(parameter);

            return true;
        });

        final ArrayList<Integer> withNulls = new ArrayList<>();

        withNulls.add(123);
        withNulls.add(null);
        withNulls.add(null);
        withNulls.add(234);
        withNulls.add(null);

        final int numWithNulls = withNulls.size();

        final int[] expectedWithNullsRemoved = new int[] { 123, 234 };

        assertThat(Lists.remove(withNulls, null, (e, p) -> e == null)).isEqualTo(numWithNulls - expectedWithNullsRemoved.length);

        assertThat(withNulls).containsExactly(Array.boxed(expectedWithNullsRemoved));

        checkRemoveFromArrayList(new int[0], 123, new int[0]);

        checkRemoveFromArrayList(new int[] { 123, 234, 123, 345, 123 }, 123, new int[] { 234, 345 });
        checkRemoveFromArrayList(new int[] { 123, 234, 345 }, 456, new int[] { 123, 234, 345 });
    }

    private static void checkRemoveFromArrayList(int[] values, int toRemove, int[] expectedValues) {

        final ArrayList<Integer> list = Coll.of(values, ArrayList::new);

        assertThat(Lists.remove(list, null, (e, p) -> e.intValue() == toRemove)).isEqualTo(values.length - expectedValues.length);

        assertThat(list).isEqualTo(Lists.of(expectedValues));
    }

    @Override
    protected List<Integer> createCollection(Integer[] values) {

        return Arrays.asList(values);
    }

    @Override
    protected boolean isOrdered() {

        return true;
    }

    @Override
    protected <T> Collection<T> ofIntRange(int start, int numElements, IntFunction<T> mapper) {

        return Lists.ofIntRange(start, numElements, mapper);
    }

    @Override
    protected Collection<Integer> ofIntArray(int[] array) {

        return Lists.of(array);
    }

    @Override
    protected List<String> map(List<Integer> collection, Function<Integer, String> mapper) {

        return Lists.map(collection, mapper);
    }

    @Override
    protected List<String> filterAndMap(List<Integer> collection, Predicate<Integer> predicate, Function<Integer, String> mapper) {

        return Lists.filterAndMap(collection, predicate, mapper);
    }
}
