package dev.jdata.db.utils.bits;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class BitsUtilTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testMask() {

        assertThat(BitsUtil.mask(1)).isEqualTo((byte)0x01);
        assertThat(BitsUtil.mask(2)).isEqualTo((byte)0x03);
        assertThat(BitsUtil.mask(3)).isEqualTo((byte)0x07);
        assertThat(BitsUtil.mask(4)).isEqualTo((byte)0x0F);
        assertThat(BitsUtil.mask(5)).isEqualTo((byte)0x1F);
        assertThat(BitsUtil.mask(6)).isEqualTo((byte)0x3F);
        assertThat(BitsUtil.mask(7)).isEqualTo((byte)0x7F);
        assertThat(BitsUtil.mask(8)).isEqualTo((byte)0xFF);
    }

    @Test
    @Category(UnitTest.class)
    public void testMaskIntWithShift() {

        assertThat(BitsUtil.maskInt(1, 0)).isEqualTo(0x01);
        assertThat(BitsUtil.maskInt(2, 0)).isEqualTo(0x03);
        assertThat(BitsUtil.maskInt(3, 0)).isEqualTo(0x07);
        assertThat(BitsUtil.maskInt(4, 0)).isEqualTo(0x0F);
        assertThat(BitsUtil.maskInt(5, 0)).isEqualTo(0x1F);
        assertThat(BitsUtil.maskInt(6, 0)).isEqualTo(0x3F);
        assertThat(BitsUtil.maskInt(7, 0)).isEqualTo(0x7F);
        assertThat(BitsUtil.maskInt(8, 0)).isEqualTo(0xFF);

        assertThat(BitsUtil.maskInt(1, 1)).isEqualTo(0x002);
        assertThat(BitsUtil.maskInt(2, 1)).isEqualTo(0x006);
        assertThat(BitsUtil.maskInt(3, 1)).isEqualTo(0x00E);
        assertThat(BitsUtil.maskInt(4, 1)).isEqualTo(0x01E);
        assertThat(BitsUtil.maskInt(5, 1)).isEqualTo(0x03E);
        assertThat(BitsUtil.maskInt(6, 1)).isEqualTo(0x07E);
        assertThat(BitsUtil.maskInt(7, 1)).isEqualTo(0x0FE);
        assertThat(BitsUtil.maskInt(8, 1)).isEqualTo(0x1FE);
    }

    @Test
    @Category(UnitTest.class)
    public void testMaskLong() {

        assertThat(BitsUtil.maskLong(1)).isEqualTo(0x01L);
        assertThat(BitsUtil.maskLong(2)).isEqualTo(0x03L);
        assertThat(BitsUtil.maskLong(3)).isEqualTo(0x07L);
        assertThat(BitsUtil.maskLong(4)).isEqualTo(0x0FL);
        assertThat(BitsUtil.maskLong(5)).isEqualTo(0x1FL);
        assertThat(BitsUtil.maskLong(6)).isEqualTo(0x3FL);
        assertThat(BitsUtil.maskLong(7)).isEqualTo(0x7FL);
        assertThat(BitsUtil.maskLong(8)).isEqualTo(0xFFL);
    }

    @Test
    @Category(UnitTest.class)
    public void testMaskLongWithShift() {

        assertThat(BitsUtil.maskLong(1, 0)).isEqualTo(0x01L);
        assertThat(BitsUtil.maskLong(2, 0)).isEqualTo(0x03L);
        assertThat(BitsUtil.maskLong(3, 0)).isEqualTo(0x07L);
        assertThat(BitsUtil.maskLong(4, 0)).isEqualTo(0x0FL);
        assertThat(BitsUtil.maskLong(5, 0)).isEqualTo(0x1FL);
        assertThat(BitsUtil.maskLong(6, 0)).isEqualTo(0x3FL);
        assertThat(BitsUtil.maskLong(7, 0)).isEqualTo(0x7FL);
        assertThat(BitsUtil.maskLong(8, 0)).isEqualTo(0xFFL);

        assertThat(BitsUtil.maskLong(1, 1)).isEqualTo(0x002L);
        assertThat(BitsUtil.maskLong(2, 1)).isEqualTo(0x006L);
        assertThat(BitsUtil.maskLong(3, 1)).isEqualTo(0x00EL);
        assertThat(BitsUtil.maskLong(4, 1)).isEqualTo(0x01EL);
        assertThat(BitsUtil.maskLong(5, 1)).isEqualTo(0x03EL);
        assertThat(BitsUtil.maskLong(6, 1)).isEqualTo(0x07EL);
        assertThat(BitsUtil.maskLong(7, 1)).isEqualTo(0x0FEL);
        assertThat(BitsUtil.maskLong(8, 1)).isEqualTo(0x1FEL);
    }

    @Test
    @Category(UnitTest.class)
    public void testApplyShiftedMask() {

        final long value = 0b111000110010L;

        assertThat(BitsUtil.applyShiftedMask(value, 0b11111L,               0)).isEqualTo(0b10010L);
        assertThat(BitsUtil.applyShiftedMask(value, 0b111110L,              1)).isEqualTo(0b11001L);
        assertThat(BitsUtil.applyShiftedMask(value, 0b1111100L,             2)).isEqualTo(0b01100L);
        assertThat(BitsUtil.applyShiftedMask(value, 0b11111000L,            3)).isEqualTo(0b00110L);
        assertThat(BitsUtil.applyShiftedMask(value, 0b111110000L,           4)).isEqualTo(0b00011L);
        assertThat(BitsUtil.applyShiftedMask(value, 0b1111100000L,          5)).isEqualTo(0b10001L);
        assertThat(BitsUtil.applyShiftedMask(value, 0b11111000000L,         6)).isEqualTo(0b11000L);
        assertThat(BitsUtil.applyShiftedMask(value, 0b111110000000L,        7)).isEqualTo(0b11100L);
        assertThat(BitsUtil.applyShiftedMask(value, 0b1111100000000L,       8)).isEqualTo(0b01110L);
        assertThat(BitsUtil.applyShiftedMask(value, 0b11111000000000L,      9)).isEqualTo(0b00111L);
        assertThat(BitsUtil.applyShiftedMask(value, 0b111110000000000L,     10)).isEqualTo(0b00011L);
        assertThat(BitsUtil.applyShiftedMask(value, 0b1111100000000000L,    11)).isEqualTo(0b00001L);
        assertThat(BitsUtil.applyShiftedMask(value, 0b11111000000000000L,   12)).isEqualTo(0b00000L);
    }

    @Test
    @Category(UnitTest.class)
    public void testSetWithShiftedMask() {

        final long value = 0b11001L;
        final long existingValue = 0b111000110010L;

        assertThat(BitsUtil.setWithShiftedMask(existingValue, value, 0b11111L,              0)).isEqualTo(0b111000111001L);
        assertThat(BitsUtil.setWithShiftedMask(existingValue, value, 0b111110L,             1)).isEqualTo(0b111000110010L);
        assertThat(BitsUtil.setWithShiftedMask(existingValue, value, 0b1111100L,            2)).isEqualTo(0b111001100110L);
        assertThat(BitsUtil.setWithShiftedMask(existingValue, value, 0b11111000L,           3)).isEqualTo(0b111011001010L);
        assertThat(BitsUtil.setWithShiftedMask(existingValue, value, 0b111110000L,          4)).isEqualTo(0b111110010010L);
        assertThat(BitsUtil.setWithShiftedMask(existingValue, value, 0b1111100000L,         5)).isEqualTo(0b111100110010L);
        assertThat(BitsUtil.setWithShiftedMask(existingValue, value, 0b11111000000L,        6)).isEqualTo(0b111001110010L);
        assertThat(BitsUtil.setWithShiftedMask(existingValue, value, 0b111110000000L,       7)).isEqualTo(0b110010110010L);
    }

    @Test
    @Category(UnitTest.class)
    public void testMerge() {

        assertThat(BitsUtil.merge((byte)0b10101010, (byte)0b01010101, 0)).isEqualTo((byte)0b01010101);
        assertThat(BitsUtil.merge((byte)0b10101010, (byte)0b01010101, 1)).isEqualTo((byte)0b11010101);
        assertThat(BitsUtil.merge((byte)0b10101010, (byte)0b01010101, 2)).isEqualTo((byte)0b10010101);
        assertThat(BitsUtil.merge((byte)0b10101010, (byte)0b01010101, 3)).isEqualTo((byte)0b10110101);
        assertThat(BitsUtil.merge((byte)0b10101010, (byte)0b01010101, 4)).isEqualTo((byte)0b10100101);
        assertThat(BitsUtil.merge((byte)0b10101010, (byte)0b01010101, 5)).isEqualTo((byte)0b10101101);
        assertThat(BitsUtil.merge((byte)0b10101010, (byte)0b01010101, 6)).isEqualTo((byte)0b10101001);
        assertThat(BitsUtil.merge((byte)0b10101010, (byte)0b01010101, 7)).isEqualTo((byte)0b10101011);
        assertThat(BitsUtil.merge((byte)0b10101010, (byte)0b01010101, 8)).isEqualTo((byte)0b10101010);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetMaxLong() {

        assertThatThrownBy(()-> BitsUtil.getMaxLong(-1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(()-> BitsUtil.getMaxLong(0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(()-> BitsUtil.getMaxLong(64)).isInstanceOf(IllegalArgumentException.class);

        assertThat(BitsUtil.getMaxLong(1)).isEqualTo(1);
        assertThat(BitsUtil.getMaxLong(2)).isEqualTo(3);
        assertThat(BitsUtil.getMaxLong(3)).isEqualTo(7);
        assertThat(BitsUtil.getMaxLong(31)).isEqualTo(Integer.MAX_VALUE);
        assertThat(BitsUtil.getMaxLong(63)).isEqualTo(Long.MAX_VALUE);
    }

    private static enum TestEnum1 {

        ONE
    }

    private static enum TestEnum2 {

        ONE,
        THREE
    }

    private static enum TestEnum3 {

        ONE,
        TWO,
        THREE
    }

    private static enum TestEnum4 {

        ONE,
        TWO,
        THREE,
        FOUR
    }

    private static enum TestEnum5 {

        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE
    }

    @Test
    @Category(UnitTest.class)
    public void testGetNumEnumBitsInt() {

        assertThatThrownBy(() -> BitsUtil.getNumEnumBits(null)).isInstanceOf(NullPointerException.class);

        assertThat(BitsUtil.getNumEnumBits(TestEnum1.class)).isEqualTo(1);
        assertThat(BitsUtil.getNumEnumBits(TestEnum2.class)).isEqualTo(1);
        assertThat(BitsUtil.getNumEnumBits(TestEnum3.class)).isEqualTo(2);
        assertThat(BitsUtil.getNumEnumBits(TestEnum4.class)).isEqualTo(2);
        assertThat(BitsUtil.getNumEnumBits(TestEnum5.class)).isEqualTo(3);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetNumUnsignedBitsInt() {

        assertThatThrownBy(() -> BitsUtil.getNumUnsignedBits(-1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(BitsUtil.getNumUnsignedBits(0)).isEqualTo(1);

        for (int i = 0; i < 31; ++ i) {

            int testValue = 1 << i;

            if (i > 0) {

                testValue |= 1 << (i - 1);
            }

            assertThat(BitsUtil.getNumUnsignedBits(testValue)).isEqualTo(i + 1);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testGetNumStorageBitsInt() {

        assertThat(BitsUtil.getNumStorageBits(0, false)).isEqualTo(1);
        assertThat(BitsUtil.getNumStorageBits(0, true)).isEqualTo(2);

        for (int i = 0; i < 32; ++ i) {

            int testValue = 1 << i;

            if (i > 0) {

                testValue |= 1 << (i - 1);
            }

            assertThat(BitsUtil.getNumStorageBits(testValue, false)).isEqualTo(i + 1);
            assertThat(BitsUtil.getNumStorageBits(testValue, true)).isEqualTo(i + (i < 31 ? 2 : 1));
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testGetNumStorageBitsLong() {

        assertThat(BitsUtil.getNumStorageBits(0L, false)).isEqualTo(1);
        assertThat(BitsUtil.getNumStorageBits(0L, true)).isEqualTo(2);

        for (int i = 0; i < 64; ++ i) {

            long testValue = 1L << i;

            if (i > 0) {

                testValue |= 1L << (i - 1);
            }

            assertThat(BitsUtil.getNumStorageBits(testValue, false)).isEqualTo(i + 1);
            assertThat(BitsUtil.getNumStorageBits(testValue, true)).isEqualTo(i + (i < 63 ? 2 : 1));
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testGetRange() {

        final long bits = 0b111000110010L;

        assertThat(BitsUtil.getRange(bits, 0, 1)).isEqualTo(0b0L);

        assertThat(BitsUtil.getRange(bits, 1, 1)).isEqualTo(0b1L);
        assertThat(BitsUtil.getRange(bits, 1, 2)).isEqualTo(0b10L);

        assertThat(BitsUtil.getRange(bits, 2, 1)).isEqualTo(0b0L);
        assertThat(BitsUtil.getRange(bits, 2, 2)).isEqualTo(0b01L);
        assertThat(BitsUtil.getRange(bits, 2, 3)).isEqualTo(0b010L);

        assertThat(BitsUtil.getRange(bits, 3, 1)).isEqualTo(0b0);
        assertThat(BitsUtil.getRange(bits, 3, 2)).isEqualTo(0b00L);
        assertThat(BitsUtil.getRange(bits, 3, 3)).isEqualTo(0b001L);
        assertThat(BitsUtil.getRange(bits, 3, 4)).isEqualTo(0b0010L);

        assertThat(BitsUtil.getRange(bits, 4, 1)).isEqualTo(0b1);
        assertThat(BitsUtil.getRange(bits, 4, 2)).isEqualTo(0b10L);
        assertThat(BitsUtil.getRange(bits, 4, 3)).isEqualTo(0b100L);
        assertThat(BitsUtil.getRange(bits, 4, 4)).isEqualTo(0b1001L);
        assertThat(BitsUtil.getRange(bits, 4, 5)).isEqualTo(0b10010L);

        assertThat(BitsUtil.getRange(bits, 5, 1)).isEqualTo(0b1);
        assertThat(BitsUtil.getRange(bits, 5, 2)).isEqualTo(0b11L);
        assertThat(BitsUtil.getRange(bits, 5, 3)).isEqualTo(0b110L);
        assertThat(BitsUtil.getRange(bits, 5, 4)).isEqualTo(0b1100L);
        assertThat(BitsUtil.getRange(bits, 5, 5)).isEqualTo(0b11001L);
        assertThat(BitsUtil.getRange(bits, 5, 6)).isEqualTo(0b0110010L);

        assertThat(BitsUtil.getRange(bits, 6, 1)).isEqualTo(0b0L);
        assertThat(BitsUtil.getRange(bits, 6, 2)).isEqualTo(0b01L);
        assertThat(BitsUtil.getRange(bits, 6, 3)).isEqualTo(0b011L);
        assertThat(BitsUtil.getRange(bits, 6, 4)).isEqualTo(0b0110L);
        assertThat(BitsUtil.getRange(bits, 6, 5)).isEqualTo(0b01100L);
        assertThat(BitsUtil.getRange(bits, 6, 6)).isEqualTo(0b011001L);
        assertThat(BitsUtil.getRange(bits, 6, 7)).isEqualTo(0b0110010L);

        assertThat(BitsUtil.getRange(bits, 7, 1)).isEqualTo(0b0);
        assertThat(BitsUtil.getRange(bits, 7, 2)).isEqualTo(0b00L);
        assertThat(BitsUtil.getRange(bits, 7, 3)).isEqualTo(0b001L);
        assertThat(BitsUtil.getRange(bits, 7, 4)).isEqualTo(0b0011L);
        assertThat(BitsUtil.getRange(bits, 7, 5)).isEqualTo(0b00110L);
        assertThat(BitsUtil.getRange(bits, 7, 6)).isEqualTo(0b001100L);
        assertThat(BitsUtil.getRange(bits, 7, 7)).isEqualTo(0b0011001L);
        assertThat(BitsUtil.getRange(bits, 7, 8)).isEqualTo(0b00110010L);

        assertThat(BitsUtil.getRange(bits, 8, 1)).isEqualTo(0b0);
        assertThat(BitsUtil.getRange(bits, 8, 2)).isEqualTo(0b00L);
        assertThat(BitsUtil.getRange(bits, 8, 3)).isEqualTo(0b000L);
        assertThat(BitsUtil.getRange(bits, 8, 4)).isEqualTo(0b0001L);
        assertThat(BitsUtil.getRange(bits, 8, 5)).isEqualTo(0b00011L);
        assertThat(BitsUtil.getRange(bits, 8, 6)).isEqualTo(0b000110L);
        assertThat(BitsUtil.getRange(bits, 8, 7)).isEqualTo(0b0001100L);
        assertThat(BitsUtil.getRange(bits, 8, 8)).isEqualTo(0b00011001L);
        assertThat(BitsUtil.getRange(bits, 8, 9)).isEqualTo(0b000110010L);

        assertThat(BitsUtil.getRange(bits, 9, 1)).isEqualTo(0b1);
        assertThat(BitsUtil.getRange(bits, 9, 2)).isEqualTo(0b10L);
        assertThat(BitsUtil.getRange(bits, 9, 3)).isEqualTo(0b100L);
        assertThat(BitsUtil.getRange(bits, 9, 4)).isEqualTo(0b1000L);
        assertThat(BitsUtil.getRange(bits, 9, 5)).isEqualTo(0b10001L);
        assertThat(BitsUtil.getRange(bits, 9, 6)).isEqualTo(0b100011L);
        assertThat(BitsUtil.getRange(bits, 9, 7)).isEqualTo(0b1000110L);
        assertThat(BitsUtil.getRange(bits, 9, 8)).isEqualTo(0b10001100L);
        assertThat(BitsUtil.getRange(bits, 9, 9)).isEqualTo(0b100011001L);
        assertThat(BitsUtil.getRange(bits, 9, 10)).isEqualTo(0b1000110010L);

        assertThat(BitsUtil.getRange(bits, 10, 1)).isEqualTo(0b1);
        assertThat(BitsUtil.getRange(bits, 10, 2)).isEqualTo(0b11L);
        assertThat(BitsUtil.getRange(bits, 10, 3)).isEqualTo(0b110L);
        assertThat(BitsUtil.getRange(bits, 10, 4)).isEqualTo(0b1100L);
        assertThat(BitsUtil.getRange(bits, 10, 5)).isEqualTo(0b11000L);
        assertThat(BitsUtil.getRange(bits, 10, 6)).isEqualTo(0b110001L);
        assertThat(BitsUtil.getRange(bits, 10, 7)).isEqualTo(0b1100011L);
        assertThat(BitsUtil.getRange(bits, 10, 8)).isEqualTo(0b11000110L);
        assertThat(BitsUtil.getRange(bits, 10, 9)).isEqualTo(0b110001100L);
        assertThat(BitsUtil.getRange(bits, 10, 10)).isEqualTo(0b1100011001L);
        assertThat(BitsUtil.getRange(bits, 10, 11)).isEqualTo(0b11000110010L);

        assertThat(BitsUtil.getRange(bits, 11, 1)).isEqualTo(0b1);
        assertThat(BitsUtil.getRange(bits, 11, 2)).isEqualTo(0b11L);
        assertThat(BitsUtil.getRange(bits, 11, 3)).isEqualTo(0b111L);
        assertThat(BitsUtil.getRange(bits, 11, 4)).isEqualTo(0b1110L);
        assertThat(BitsUtil.getRange(bits, 11, 5)).isEqualTo(0b11100L);
        assertThat(BitsUtil.getRange(bits, 11, 6)).isEqualTo(0b111000L);
        assertThat(BitsUtil.getRange(bits, 11, 7)).isEqualTo(0b1110001L);
        assertThat(BitsUtil.getRange(bits, 11, 8)).isEqualTo(0b11100011L);
        assertThat(BitsUtil.getRange(bits, 11, 9)).isEqualTo(0b111000110L);
        assertThat(BitsUtil.getRange(bits, 11, 10)).isEqualTo(0b1110001100L);
        assertThat(BitsUtil.getRange(bits, 11, 11)).isEqualTo(0b11100011001L);
        assertThat(BitsUtil.getRange(bits, 11, 12)).isEqualTo(0b111000110010L);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetIndexOfHighestSetBitInt() {

        assertThat(BitsUtil.getIndexOfHighestSetBit(0)).isEqualTo(-1);

        for (int i = 0; i < 32; ++ i) {

            int testValue = 1 << i;

            if (i > 0) {

                testValue |= 1 << (i - 1);
            }

            assertThat(BitsUtil.getIndexOfHighestSetBit(testValue)).isEqualTo(i);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testGetIndexOfHighestSetBitLong() {

        assertThat(BitsUtil.getIndexOfHighestSetBit(0L)).isEqualTo(-1);

        for (int i = 0; i < 64; ++ i) {

            long testValue = 1L << i;

            if (i > 0) {

                testValue |= 1L << (i - 1);
            }

            assertThat(BitsUtil.getIndexOfHighestSetBit(testValue)).isEqualTo(i);
        }
    }
}
