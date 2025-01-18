package dev.jdata.db.utils.adt.maps;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.hashed.BaseHashedTest;
import dev.jdata.db.utils.adt.sets.LongSet;

abstract class BaseLongToIntegerOrObjectTest<M extends LongKeyMap> extends BaseHashedTest {

    abstract M createMap(int initialCapacity);
    abstract int get(M map, long key);
    abstract void put(M map, long key, int value);

    @Test
    @Category(UnitTest.class)
    public final void testPutAndGetWithOverwrite() {

        final M map = createMap(0);

        assertThat(map).isEmpty();
        assertThat(map).hasNumElements(0L);

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(i + 1);
            }

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(numElements);
            }

            assertThat(map).hasNumElements(numElements);

            for (long i = 0; i < numElements; ++ i) {

                assertThat(get(map, i)).isEqualTo(i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(numElements);
            }

            assertThat(map).hasNumElements(numElements);

            checkGetKeys(map, numElements);

            map.clear();
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testPutGetAndRemoveNewMap() {

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final M map = createMap(0);

            assertThat(map).isEmpty();
            assertThat(map).hasNumElements(0);

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(i + 1);
            }

            assertThat(map).hasNumElements(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(get(map, i)).isEqualTo(i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(numElements);
            }

            checkGetKeys(map, numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(map.remove(i)).isTrue();

                assertThat(map.isEmpty()).isEqualTo(i == numElements - 1);
                assertThat(map).hasNumElements(numElements - i - 1);
            }

            checkGetKeys(map, 0);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(map.remove(i)).isFalse();

                assertThat(map).isEmpty();
                assertThat(map).hasNumElements(0L);
            }

            checkGetKeys(map, 0);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testPutAndGetWithClear() {

        final M map = createMap(0);

        assertThat(map).isEmpty();
        assertThat(map).hasNumElements(0L);

        for (int numElements = 1; numElements < MAX_ELEMENTS; numElements *= 10) {

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(i + 1);
            }

            for (int i = 0; i < numElements; ++ i) {

                assertThat(get(map, i)).isEqualTo(i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(numElements);
            }

            checkGetKeys(map, numElements);

            map.clear();

            assertThat(map).isEmpty();
            assertThat(map).hasNumElements(0L);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testContainsKey() {

        final M map = createMap(0);

        assertThat(map).isEmpty();

        put(map, 123);

        assertThat(map).containsKey(123L);
        assertThat(map).doesNotContainKey(234L);
    }

    @Test
    @Category(UnitTest.class)
    public final void testKeys() {

        final M map = createMap(0);

        assertThat(map).isEmpty();

        put(map, 123);
        put(map, 234);
        put(map, 345);

        assertThat(map).hasExactlyKeys(123L, 234L, 345L);
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmpty() {

        final M map = createMap(0);

        assertThat(map).isEmpty();

        put(map, 1);

        assertThat(map).isNotEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final M map = createMap(0);

        assertThat(map).hasNumElements(0L);

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            put(map, i);

            assertThat(map).hasNumElements(i + 1);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testClear() {

        final M map = createMap(0);

        assertThat(map).isEmpty();

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            put(map, i);
        }

        assertThat(map).isNotEmpty();

        map.clear();

        assertThat(map).isEmpty();
    }

    private void checkGetKeys(M map, int numElements) {

        final long[] keys = map.keys();

        assertThat(keys.length).isEqualTo(numElements);

        final LongSet values = new LongSet(keys);

        for (int i = 0; i < numElements; ++ i) {

            assertThat(values).contains(i);
        }
    }

    private void put(M map, int integer) {

        put(map, integer, integer + 1000);
    }
}
