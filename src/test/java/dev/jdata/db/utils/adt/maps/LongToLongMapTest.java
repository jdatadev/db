package dev.jdata.db.utils.adt.maps;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class LongToLongMapTest extends BaseLongToIntegerOrObjectTest<LongToLongMap> {

    @Test
    @Category(UnitTest.class)
    public final void testKeysAndValues() {

        final LongToLongMap map = createMap(0);

        assertThat(map).isEmpty();

        put(map, 123, 234);
        put(map, 345, 456);
        put(map, 567, 678);

        final int numElements = 3;

        assertThatThrownBy(() -> {

            final long[] keysDst = new long[numElements + 1];
            final long[] valuesDst = new long[numElements];

            map.keysAndValues(keysDst, valuesDst);
        })
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {

            final long[] keysDst = new long[numElements];
            final long[] valuesDst = new long[numElements + 1];

            map.keysAndValues(keysDst, valuesDst);
        })
        .isInstanceOf(IllegalArgumentException.class);

        final long[] keysDst = new long[numElements];
        final long[] valuesDst = new long[numElements];

        map.keysAndValues(keysDst, valuesDst);

        for (int i = 0; i < numElements; ++ i) {

            final long expectedValue;

            switch ((int)keysDst[i]) {

            case 123:

                expectedValue = 234L;
                break;

            case 345:

                expectedValue = 456L;
                break;

            case 567:

                expectedValue = 678L;
                break;

            default:
                throw new UnsupportedOperationException();
            }

            assertThat(valuesDst[i]).isEqualTo(expectedValue);
        }
    }

    @Override
    LongToLongMap createMap(int initialCapacity) {

        return new LongToLongMap(initialCapacity);
    }

    @Override
    int get(LongToLongMap map, long key) {

        return (int)map.get(key);
    }

    @Override
    void put(LongToLongMap map, long key, int value) {

        map.put(key, value);
    }
}
