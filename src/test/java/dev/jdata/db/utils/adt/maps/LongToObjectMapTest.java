package dev.jdata.db.utils.adt.maps;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.hashed.BaseHashedTest;
import dev.jdata.db.utils.adt.sets.LongSet;

public final class LongToObjectMapTest extends BaseHashedTest {

    @Test
    @Category(UnitTest.class)
    public void testPutAndGetWithOverwrite() {

        final LongToObjectMap<String> map = createMap(0);

        assertThat(map.isEmpty()).isTrue();
        assertThat(map.getNumElements()).isEqualTo(0);

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map.isEmpty()).isFalse();
                assertThat(map.getNumElements()).isEqualTo(i + 1);
            }

            assertThat(map.getNumElements()).isEqualTo(numElements);

            for (long i = 0; i < numElements; ++ i) {

                assertThat(map.get(i)).isEqualTo(String.valueOf(i));

                assertThat(map.isEmpty()).isFalse();
                assertThat(map.getNumElements()).isEqualTo(i + 1);
            }

            assertThat(map.getNumElements()).isEqualTo(numElements);

            checkGetKeys(map, numElements);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testPutGetAndRemoveNewMap() {

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final LongToObjectMap<String> map = createMap(0);

            assertThat(map.isEmpty()).isTrue();
            assertThat(map.getNumElements()).isEqualTo(0);

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map.isEmpty()).isTrue();
                assertThat(map.getNumElements()).isEqualTo(i + 1);
            }

            assertThat(map.getNumElements()).isEqualTo(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(map.get(i)).isEqualTo(String.valueOf(i));

                assertThat(map.isEmpty()).isFalse();
                assertThat(map.getNumElements()).isEqualTo(numElements);
            }

            checkGetKeys(map, numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(map.remove(i)).isTrue();

                assertThat(map.isEmpty()).isEqualTo(i != numElements - 1);
                assertThat(map.getNumElements()).isEqualTo(numElements - i - 1);
            }

            for (int i = 0; i < numElements; ++ i) {

                assertThat(map.remove(i)).isFalse();

                assertThat(map.isEmpty()).isTrue();
                assertThat(map.getNumElements()).isEqualTo(0L);
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testPutAndGetWithClear() {

        final LongToObjectMap<String> map = createMap(0);

        assertThat(map.isEmpty()).isTrue();
        assertThat(map.getNumElements()).isEqualTo(0);

        for (int numElements = 1; numElements < MAX_ELEMENTS; numElements *= 10) {

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map.isEmpty()).isFalse();
                assertThat(map.getNumElements()).isEqualTo(i + 1);
            }

            for (int i = 0; i < numElements; ++ i) {

                assertThat(map.get(i)).isEqualTo(String.valueOf(i));

                assertThat(map.isEmpty()).isFalse();
                assertThat(map.getNumElements()).isEqualTo(numElements);
            }

            checkGetKeys(map, numElements);

            map.clear();

            assertThat(map.isEmpty()).isTrue();
            assertThat(map.getNumElements()).isEqualTo(0);
        }
    }

    private static void checkGetKeys(LongToObjectMap<String> map, int numElements) {

        final long[] keys = map.keys();

        assertThat(keys.length).isEqualTo(numElements);

        final LongSet values = new LongSet(keys);

        for (int i = 0; i < numElements; ++ i) {

            assertThat(values.contains(i)).isTrue();
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmpty() {

        final LongToObjectMap<String> map = createMap(0);

        assertThat(map.isEmpty()).isTrue();

        put(map, 1);

        assertThat(map.isEmpty()).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final LongToObjectMap<String> map = createMap(0);

        assertThat(map.getNumElements()).isEqualTo(0);

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            put(map, i);

            assertThat(map.getNumElements()).isEqualTo(i + 1);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testClear() {

        final LongToObjectMap<String> map = createMap(0);

        assertThat(map.isEmpty()).isTrue();

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            put(map, i);
        }

        assertThat(map.isEmpty()).isFalse();

        map.clear();

        assertThat(map.isEmpty()).isTrue();
    }

    private static LongToObjectMap<String> createMap(int initialCapacity) {

        return new LongToObjectMap<>(initialCapacity, String[]::new);
    }

    private static void put(LongToObjectMap<String> map, int value) {

        map.put(value, String.valueOf(value));
    }
}
