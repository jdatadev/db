package dev.jdata.db.utils.jdk.adt.sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.collections.BaseCollectionsTest;

public final class SetsTest extends BaseCollectionsTest<Set<Integer>, Set<String>> {

    @Test
    @Category(UnitTest.class)
    public void testDistinct() {

        assertThatThrownBy(() -> Sets.distinct(null, Function.identity())).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Sets.distinct(Arrays.asList(1), null)).isInstanceOf(NullPointerException.class);

        assertThat(Sets.distinct(Collections.emptyList(), Function.identity())).isEmpty();
        assertThat(Sets.distinct(Arrays.asList(1), String::valueOf)).containsExactly("1");
        assertThat(Sets.distinct(Arrays.asList(1, 2, 3), String::valueOf)).containsExactlyInAnyOrder("1", "2", "3");
        assertThat(Sets.distinct(Arrays.asList(3, 1, 2, 3, 2, 3, 1), String::valueOf)).containsExactlyInAnyOrder("1", "2", "3");

        final Set<String> modifiableDistinctSet = Sets.distinct(Arrays.asList(1, 2, 3), String::valueOf);

        checkModifiable(modifiableDistinctSet, "4");
    }

    @Override
    protected Set<Integer> createCollection(Integer[] values) {

        return Sets.of(values);
    }

    @Override
    protected boolean isOrdered() {

        return false;
    }

    @Override
    protected <T> Collection<T> ofIntRange(int start, int numElements, IntFunction<T> mapper) {

        return Sets.ofIntRange(start, numElements, mapper);
    }

    @Override
    protected Collection<Integer> ofIntArray(int[] array) {

        return Sets.of(array);
    }

    @Override
    protected Set<String> map(Set<Integer> collection, Function<Integer, String> mapper) {

        return Sets.map(collection, mapper);
    }

    @Override
    protected Set<String> filterAndMap(Set<Integer> collection, Predicate<Integer> predicate, Function<Integer, String> mapper) {

        return Sets.filterAndMap(collection, predicate, mapper);
    }
}
