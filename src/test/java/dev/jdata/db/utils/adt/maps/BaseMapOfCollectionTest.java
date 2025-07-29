package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

abstract class BaseMapOfCollectionTest<C extends Collection<String>, M extends Map<Integer, C>, MOC extends MapOfCollection<Integer, String, C, M>> extends BaseTest {

    abstract MOC creatMapOfCollection(int initialCapacity);

    @Test
    @Category(UnitTest.class)
    public final void testAddAndClear() {

        final MOC mapOfCollection = creatMapOfCollection(10);

        mapOfCollection.add(123, "abc");
        mapOfCollection.add(123, "bcd");
        mapOfCollection.add(234, "cde");

        final String testValue = "testValue";
        final int testKey = -1;

        assertThat(checkUnmodifiable(mapOfCollection.getUnmodifiable(123), testValue)).isEqualTo(Arrays.asList("abc", "bcd"));
        assertThat(checkUnmodifiable(mapOfCollection.getUnmodifiable(234), testValue)).isEqualTo(Arrays.asList("cde"));

        assertThat(mapOfCollection.isEmpty()).isFalse();
        assertThat(mapOfCollection.getNumKeys()).isEqualTo(2);
        assertThat(mapOfCollection.getNumElements()).isEqualTo(3);

        assertThat(checkUnmodifiable(mapOfCollection.unmdifiableKeySet(), testKey)).containsExactlyInAnyOrder(123, 234);

        mapOfCollection.clear();

        assertThat(mapOfCollection.isEmpty()).isTrue();
        assertThat(mapOfCollection.getNumElements()).isEqualTo(0);
        assertThat(mapOfCollection.getNumKeys()).isEqualTo(0);
        assertThat(checkUnmodifiableEmptyIsEmptySet(mapOfCollection.unmdifiableKeySet(), testKey)).isEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmpty() {

        final MOC mapOfCollection = creatMapOfCollection(10);

        assertThat(mapOfCollection.isEmpty()).isTrue();

        mapOfCollection.add(123, "abc");
        assertThat(mapOfCollection.isEmpty()).isFalse();

        mapOfCollection.add(123, "bcd");
        assertThat(mapOfCollection.isEmpty()).isFalse();

        mapOfCollection.add(234, "cde");
        assertThat(mapOfCollection.isEmpty()).isFalse();

        mapOfCollection.clear();
        assertThat(mapOfCollection.isEmpty()).isTrue();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumKeys() {

        final MOC mapOfCollection = creatMapOfCollection(10);

        assertThat(mapOfCollection.getNumKeys()).isEqualTo(0);

        mapOfCollection.add(123, "abc");
        assertThat(mapOfCollection.getNumKeys()).isEqualTo(1);

        mapOfCollection.add(123, "bcd");
        assertThat(mapOfCollection.getNumKeys()).isEqualTo(1);

        mapOfCollection.add(234, "cde");
        assertThat(mapOfCollection.getNumKeys()).isEqualTo(2);

        mapOfCollection.clear();
        assertThat(mapOfCollection.getNumElements()).isEqualTo(0);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final MOC mapOfCollection = creatMapOfCollection(10);

        assertThat(mapOfCollection.getNumElements()).isEqualTo(0);

        mapOfCollection.add(123, "abc");
        assertThat(mapOfCollection.getNumElements()).isEqualTo(1);

        mapOfCollection.add(123, "bcd");
        assertThat(mapOfCollection.getNumElements()).isEqualTo(2);

        mapOfCollection.add(234, "cde");
        assertThat(mapOfCollection.getNumElements()).isEqualTo(3);

        mapOfCollection.clear();
        assertThat(mapOfCollection.getNumElements()).isEqualTo(0);
    }
}
