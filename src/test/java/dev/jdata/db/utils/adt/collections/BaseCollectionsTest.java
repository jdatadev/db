package dev.jdata.db.utils.adt.collections;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.lists.Lists;

public abstract class BaseCollectionsTest<IC extends Collection<Integer>, SC extends Collection<String>> extends BaseTest {

    protected abstract IC createCollection(Integer[] values);
    protected abstract boolean isOrdered();

    protected abstract <T> Collection<T> ofIntRange(int start, int numElements, IntFunction<T> mapper);
    protected abstract SC map(IC collection, Function<Integer, String> mapper);
    protected abstract SC filterAndMap(IC collection, Predicate<Integer> predicate, Function<Integer, String> mapper);

    @Test
    @Category(UnitTest.class)
    public final void testOfIntRange() {

        assertThatThrownBy(() -> Lists.ofIntRange(0, -1, Integer::valueOf)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Lists.ofIntRange(0, 0, Integer::valueOf)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Lists.ofIntRange(0, 1, null)).isInstanceOf(NullPointerException.class);

        checkOfIntRange(0, 1, 0);
        checkOfIntRange(1, 1, 1);
        checkOfIntRange(1, 3, 1, 2, 3);
        checkOfIntRange(-2, 6, -2, -1, 0, 1, 2, 3);

        final List<Integer> modifiableIntRangeList = Lists.ofIntRange(0, 3, Integer::valueOf);

        checkModifiable(modifiableIntRangeList, 4);
    }

    private void checkOfIntRange(int start, int numElements, Integer ... expectedValues) {

        final Collection<Integer> collection = ofIntRange(start, numElements, Integer::valueOf);

        if (isOrdered()) {

            assertThat(collection).containsExactly(expectedValues);
        }
        else {
            assertThat(collection).containsExactlyInAnyOrder(expectedValues);
        }

        checkModifiable(collection, Integer.MAX_VALUE);
    }

    @Test
    @Category(UnitTest.class)
    public final void testMap() {

        assertThatThrownBy(() -> map(null, e -> null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> map(create(1), null)).isInstanceOf(NullPointerException.class);

        assertThat(map(create(1), String::valueOf)).containsExactly("1");
        assertThat(map(create(1, 2, 3), String::valueOf)).containsExactly("1", "2", "3");

        final Collection<String> modifiableMappedCollection = map(create(1, 2, 3), String::valueOf);

        checkModifiable(modifiableMappedCollection, "4");
    }

    @Test
    @Category(UnitTest.class)
    public final void testFilterAndMap() {

        assertThatThrownBy(() -> filterAndMap(null, e -> true, e -> null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> filterAndMap(create(1), null, e -> null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> filterAndMap(create(1), e -> true, null)).isInstanceOf(NullPointerException.class);

        assertThat(filterAndMap(create(1), e -> true, String::valueOf)).containsExactly("1");
        assertThat(filterAndMap(create(1, 2, 3), e -> true, String::valueOf)).containsExactly("1", "2", "3");
        assertThat(filterAndMap(create(1, 2, 3), e -> e != 2, String::valueOf)).containsExactly("1", "3");

        final Collection<String> modifiableUnchangedFilterMappedCollection = filterAndMap(create(1, 2, 3), e -> true, String::valueOf);

        checkModifiable(modifiableUnchangedFilterMappedCollection, "4");

    final Collection<String> modifiableChangedFilterMappedCollection = filterAndMap(create(1, 2, 3), e -> e != 2, String::valueOf);

        checkModifiable(modifiableChangedFilterMappedCollection, "4");
    }

    private IC create(Integer ... values) {

        return createCollection(values);
    }
}
