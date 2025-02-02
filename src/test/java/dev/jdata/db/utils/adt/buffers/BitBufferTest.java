package dev.jdata.db.utils.adt.buffers;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.scalars.Integers;

public final class BitBufferTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testBitBufferAddLongToCapacityExponent3() {

        final BitBuffer buffer = new BitBuffer(3);

        buffer.addUnsignedLong(123L);
        assertThat(buffer.getBitOffset()).isEqualTo(8L * 8L);

        buffer.addUnsignedLong(234L);
        assertThat(buffer.getBitOffset()).isEqualTo(16L * 8L);

        assertThat(buffer.getUnsignedLong(0L)).isEqualTo((short)123);
        assertThat(buffer.getUnsignedLong(8L * 8L)).isEqualTo(234);
    }

    @Test
    @Category(UnitTest.class)
    public void testBitBufferAddLargeNumberToSameInnerArray() {

        final int innerBytesCapacityExponent = 20;

        final BitBuffer buffer = new BitBuffer(innerBytesCapacityExponent);

        final int innerBytesCapacity = CapacityExponents.computeCapacity(innerBytesCapacityExponent);

        final int numLongs = innerBytesCapacity / Long.BYTES;

        for (int i = 0; i < numLongs; ++ i) {

            buffer.addUnsignedLong(numLongs + i);

            assertThat(buffer.getBitOffset()).isEqualTo((i + 1) * Long.SIZE);
        }

        for (int i = 0; i < numLongs; ++ i) {

            assertThat(buffer.getUnsignedLong(i * Long.SIZE)).isEqualTo(numLongs + i);
        }
    }


    @Test
    @Category(UnitTest.class)
    public void testBitBufferAddAndGetScalars() {

        for (int capacityExponent = 3; capacityExponent <= 20; ++ capacityExponent) {

            for (int numValues = 1; numValues < 10; ++ numValues) {

                checkBitBufferAddAndGetScalars(capacityExponent, numValues);
            }
        }
    }

    private void checkBitBufferAddAndGetScalars(int capacityExponent, int numValues) {

        final BitBuffer buffer = new BitBuffer(capacityExponent);

        long bitOffset = 0L;

        for (int i = 0; i < numValues; ++ i) {

            final short shortValue = Integers.checkUnsignedIntToUnsignedShort(123 + i);

            buffer.addUnsignedShort(shortValue);
            assertThat(buffer.getUnsignedShort(bitOffset)).isEqualTo(shortValue);

            bitOffset += 2L * 8L;
            assertThat(buffer.getBitOffset()).isEqualTo(bitOffset);

            final long longValue = 234L + i;

            buffer.addUnsignedLong(longValue);
            assertThat(buffer.getUnsignedLong(bitOffset)).isEqualTo(longValue);

            bitOffset += 8L * 8L;
            assertThat(buffer.getBitOffset()).isEqualTo(bitOffset);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testBitBufferAddBytesAndGetScalars() {

        for (int capacityExponent = 3; capacityExponent <= 20; ++ capacityExponent) {

            for (int numValues = 1; numValues < 10; ++ numValues) {

                checkBitBufferAddBytesAndGetScalars(capacityExponent, numValues);
            }
        }
    }

    private void checkBitBufferAddBytesAndGetScalars(int capacityExponent, int numValues) {

        final BitBuffer buffer = new BitBuffer(capacityExponent);

        long bitOffset = 0L;

        for (int i = 0; i < numValues; ++ i) {

            final short shortValue = Integers.checkUnsignedIntToUnsignedShort(123 + i);

            final byte[] byteArray = new byte[10];

            BitBufferUtil.setShortValue(byteArray, shortValue, false, 0L, Short.SIZE);

            buffer.addBytes(byteArray, 0L, Short.SIZE);

            final long longValue = 234L + i;

            BitBufferUtil.setLongValue(byteArray, longValue, false, 0L, Long.SIZE);

            buffer.addBytes(byteArray, 0L, Long.SIZE);

            assertThat(buffer.getUnsignedShort(bitOffset)).isEqualTo(shortValue);
            assertThat(buffer.getUnsignedLong(bitOffset + (2L * 8L))).isEqualTo(longValue);

            bitOffset += 10L * 8L;
        }
    }
}
