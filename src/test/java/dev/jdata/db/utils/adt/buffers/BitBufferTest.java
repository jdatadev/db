package dev.jdata.db.utils.adt.buffers;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.math.Sign;
import dev.jdata.db.utils.scalars.Integers;

public final class BitBufferTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testBitBufferAddUnsignedLongToCapacityExponent3() {

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

        final int innerBytesCapacity = CapacityExponents.computeIntCapacityFromExponent(innerBytesCapacityExponent);

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

                checkBitBufferAddAndGetScalars(capacityExponent, numValues, Sign.PLUS);
                checkBitBufferAddAndGetScalars(capacityExponent, numValues, Sign.MINUS);
            }
        }
    }

    private void checkBitBufferAddAndGetScalars(int capacityExponent, int numValues, Sign sign) {

        final BitBuffer buffer = new BitBuffer(capacityExponent);

        long bitOffset = 0L;

        final int numBitsPerByte = Byte.SIZE;

        for (int i = 0; i < numValues; ++ i) {

            final byte signedByteValue = Integers.checkIntToByte(sign.apply(12 + i));

            buffer.addSignedByte(signedByteValue);
            assertThat(buffer.getSignedByte(bitOffset)).isEqualTo(signedByteValue);

            bitOffset += 1L * numBitsPerByte;
            assertThat(buffer.getBitOffset()).isEqualTo(bitOffset);

            final short unsignedByteValue = Integers.checkUnsignedIntToUnsignedByteAsShort(234 + i);

            buffer.addUnsignedByte(unsignedByteValue);
            assertThat(buffer.getUnsignedByte(bitOffset)).isEqualTo(unsignedByteValue);

            bitOffset += 1L * numBitsPerByte;
            assertThat(buffer.getBitOffset()).isEqualTo(bitOffset);

            final short signedShortValue = Integers.checkIntToShort(sign.apply(345 + i));

            buffer.addSignedShort(signedShortValue);
            assertThat(buffer.getSignedShort(bitOffset)).isEqualTo(signedShortValue);

            bitOffset += 2L * numBitsPerByte;
            assertThat(buffer.getBitOffset()).isEqualTo(bitOffset);

            final int unsignedShortValue = Integers.checkUnsignedIntToUnsignedShort(456 + i);

            buffer.addUnsignedShort(unsignedShortValue);
            assertThat(buffer.getUnsignedShort(bitOffset)).isEqualTo(unsignedShortValue);

            bitOffset += 2L * numBitsPerByte;
            assertThat(buffer.getBitOffset()).isEqualTo(bitOffset);

            final int signedIntValue = sign.apply(567 + i);

            buffer.addSignedInt(signedIntValue);
            assertThat(buffer.getSignedInt(bitOffset)).isEqualTo(signedIntValue);

            bitOffset += 4L * numBitsPerByte;
            assertThat(buffer.getBitOffset()).isEqualTo(bitOffset);

            final long unsignedIntValue = 678 + i;

            buffer.addUnsignedInt(unsignedIntValue);
            assertThat(buffer.getUnsignedInt(bitOffset)).isEqualTo(unsignedIntValue);

            bitOffset += 4L * numBitsPerByte;
            assertThat(buffer.getBitOffset()).isEqualTo(bitOffset);

            final long signedLongValue = sign.apply(789L + i);

            buffer.addSignedLong(signedLongValue);
            assertThat(buffer.getSignedLong(bitOffset)).isEqualTo(signedLongValue);

            bitOffset += 8L * numBitsPerByte;
            assertThat(buffer.getBitOffset()).isEqualTo(bitOffset);

            final long unsignedLongValue = 890L + i;

            buffer.addUnsignedLong(unsignedLongValue);
            assertThat(buffer.getUnsignedLong(bitOffset)).isEqualTo(unsignedLongValue);

            bitOffset += 8L * numBitsPerByte;
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
