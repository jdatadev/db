package dev.jdata.db.utils.bits;

import java.util.Arrays;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.checks.Checks;

public final class BitBufferUtilTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testIsNumBitsOnByteBoundary() {

        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(0)).isTrue();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(1)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(2)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(3)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(4)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(5)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(6)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(7)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(8)).isTrue();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(9)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(10)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(11)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(12)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(13)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(14)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(15)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(16)).isTrue();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(17)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(18)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(19)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(20)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(21)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(22)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(23)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(24)).isTrue();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(25)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(26)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(27)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(28)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(29)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(30)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(31)).isFalse();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(32)).isTrue();
        assertThat(BitBufferUtil.isNumBitsOnByteBoundary(33)).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public void testNumBytes() {

        checkNumBytes(0, 0);
        checkNumBytes(1, 1);
        checkNumBytes(2, 1);
        checkNumBytes(3, 1);
        checkNumBytes(4, 1);
        checkNumBytes(5, 1);
        checkNumBytes(6, 1);
        checkNumBytes(7, 1);
        checkNumBytes(8, 1);
        checkNumBytes(9, 2);
        checkNumBytes(10, 2);
        checkNumBytes(11, 2);
        checkNumBytes(12, 2);
        checkNumBytes(13, 2);
        checkNumBytes(14, 2);
        checkNumBytes(15, 2);
        checkNumBytes(16, 2);
        checkNumBytes(17, 3);
        checkNumBytes(18, 3);
        checkNumBytes(19, 3);
        checkNumBytes(20, 3);
        checkNumBytes(21, 3);
        checkNumBytes(22, 3);
        checkNumBytes(23, 3);
        checkNumBytes(24, 3);
        checkNumBytes(25, 4);
        checkNumBytes(26, 4);
        checkNumBytes(27, 4);
        checkNumBytes(28, 4);
        checkNumBytes(29, 4);
        checkNumBytes(30, 4);
        checkNumBytes(31, 4);
        checkNumBytes(32, 4);
        checkNumBytes(33, 5);
   }

    private void checkNumBytes(int numBits, int numBytes) {

        Checks.isNotNegative(numBits);
        Checks.isNotNegative(numBytes);

        assertThat(BitBufferUtil.numBytes(numBits)).isEqualTo(numBytes);
    }

    @Test
    @Category(UnitTest.class)
    public void testNumBytesExact() {

        checkNumBytesExact(0, 0);
        checkNumBytesExact(1, -1);
        checkNumBytesExact(2, -1);
        checkNumBytesExact(3, -1);
        checkNumBytesExact(4, -1);
        checkNumBytesExact(5, -1);
        checkNumBytesExact(6, -1);
        checkNumBytesExact(7, -1);
        checkNumBytesExact(8, 1);
        checkNumBytesExact(9, -1);
        checkNumBytesExact(10, -1);
        checkNumBytesExact(11, -1);
        checkNumBytesExact(12, -1);
        checkNumBytesExact(13, -1);
        checkNumBytesExact(14, -1);
        checkNumBytesExact(15, -1);
        checkNumBytesExact(16, 2);
        checkNumBytesExact(17, -1);
        checkNumBytesExact(18, -1);
        checkNumBytesExact(19, -1);
        checkNumBytesExact(20, -1);
        checkNumBytesExact(21, -1);
        checkNumBytesExact(22, -1);
        checkNumBytesExact(23, -1);
        checkNumBytesExact(24, 3);
        checkNumBytesExact(25, -1);
        checkNumBytesExact(26, -1);
        checkNumBytesExact(27, -1);
        checkNumBytesExact(28, -1);
        checkNumBytesExact(29, -1);
        checkNumBytesExact(30, -1);
        checkNumBytesExact(31, -1);
        checkNumBytesExact(32, 4);
        checkNumBytesExact(33, -1);
   }

    private void checkNumBytesExact(int numBits, int numBytes) {

        Checks.isNotNegative(numBits);

        if (numBytes == -1) {

            assertThatThrownBy(() -> BitBufferUtil.numBytesExact(numBits)).isInstanceOf(IllegalArgumentException.class);
        }
        else if (numBytes >= 0) {

            assertThat(BitBufferUtil.numBytesExact(numBits)).isEqualTo(numBytes);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testNumLeftOverBits() {

        assertThat(BitBufferUtil.numLeftoverBits(0)).isEqualTo(0);
        assertThat(BitBufferUtil.numLeftoverBits(1)).isEqualTo(7);
        assertThat(BitBufferUtil.numLeftoverBits(2)).isEqualTo(6);
        assertThat(BitBufferUtil.numLeftoverBits(3)).isEqualTo(5);
        assertThat(BitBufferUtil.numLeftoverBits(4)).isEqualTo(4);
        assertThat(BitBufferUtil.numLeftoverBits(5)).isEqualTo(3);
        assertThat(BitBufferUtil.numLeftoverBits(6)).isEqualTo(2);
        assertThat(BitBufferUtil.numLeftoverBits(7)).isEqualTo(1);
        assertThat(BitBufferUtil.numLeftoverBits(8)).isEqualTo(0);
        assertThat(BitBufferUtil.numLeftoverBits(9)).isEqualTo(7);
        assertThat(BitBufferUtil.numLeftoverBits(10)).isEqualTo(6);
        assertThat(BitBufferUtil.numLeftoverBits(11)).isEqualTo(5);
        assertThat(BitBufferUtil.numLeftoverBits(12)).isEqualTo(4);
        assertThat(BitBufferUtil.numLeftoverBits(13)).isEqualTo(3);
        assertThat(BitBufferUtil.numLeftoverBits(14)).isEqualTo(2);
        assertThat(BitBufferUtil.numLeftoverBits(15)).isEqualTo(1);
        assertThat(BitBufferUtil.numLeftoverBits(16)).isEqualTo(0);
        assertThat(BitBufferUtil.numLeftoverBits(17)).isEqualTo(7);
        assertThat(BitBufferUtil.numLeftoverBits(18)).isEqualTo(6);
        assertThat(BitBufferUtil.numLeftoverBits(19)).isEqualTo(5);
        assertThat(BitBufferUtil.numLeftoverBits(20)).isEqualTo(4);
        assertThat(BitBufferUtil.numLeftoverBits(21)).isEqualTo(3);
        assertThat(BitBufferUtil.numLeftoverBits(22)).isEqualTo(2);
        assertThat(BitBufferUtil.numLeftoverBits(23)).isEqualTo(1);
        assertThat(BitBufferUtil.numLeftoverBits(24)).isEqualTo(0);
        assertThat(BitBufferUtil.numLeftoverBits(25)).isEqualTo(7);
        assertThat(BitBufferUtil.numLeftoverBits(26)).isEqualTo(6);
        assertThat(BitBufferUtil.numLeftoverBits(27)).isEqualTo(5);
        assertThat(BitBufferUtil.numLeftoverBits(28)).isEqualTo(4);
        assertThat(BitBufferUtil.numLeftoverBits(29)).isEqualTo(3);
        assertThat(BitBufferUtil.numLeftoverBits(30)).isEqualTo(2);
        assertThat(BitBufferUtil.numLeftoverBits(31)).isEqualTo(1);
        assertThat(BitBufferUtil.numLeftoverBits(32)).isEqualTo(0);
        assertThat(BitBufferUtil.numLeftoverBits(33)).isEqualTo(7);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetInt() {

/*
        final byte testByte = (byte)0b11100110;
        final byte testByte = (byte)0b11010101;
*/
        final byte[] testBytes = new byte[] {

                (byte)0b11100011,
                (byte)0b00101110,
                (byte)0b00110010,
                (byte)0b11100011,
                (byte)0b00101110,
                (byte)0b00110010,
                (byte)0b11100011,
                (byte)0b00101110
        };

        assertThatThrownBy(() -> BitBufferUtil.getIntValue(testBytes, false, 0, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitBufferUtil.getIntValue(testBytes, true, 0, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitBufferUtil.getIntValue(testBytes, true, 0, (testBytes.length * 8) + 1)).isInstanceOf(IllegalArgumentException.class);

        checkGetInt(testBytes, false, 0, 1, 0b1);
        checkGetInt(testBytes, false, 0, 2, 0b11);
        checkGetInt(testBytes, false, 0, 3, 0b111);
        checkGetInt(testBytes, false, 0, 4, 0b1110);
        checkGetInt(testBytes, false, 0, 5, 0b11100);
        checkGetInt(testBytes, false, 0, 6, 0b111000);
        checkGetInt(testBytes, false, 0, 7, 0b1110001);
        checkGetInt(testBytes, false, 0, 8, 0b11100011);
        checkGetInt(testBytes, false, 0, 9, 0b111000110);
        checkGetInt(testBytes, false, 0, 10, 0b1110001100);
        checkGetInt(testBytes, false, 0, 11, 0b11100011001);
        checkGetInt(testBytes, false, 0, 12, 0b111000110010);
        checkGetInt(testBytes, false, 0, 13, 0b1110001100101);
        checkGetInt(testBytes, false, 0, 14, 0b11100011001011);
        checkGetInt(testBytes, false, 0, 15, 0b111000110010111);
        checkGetInt(testBytes, false, 0, 16, 0b1110001100101110);
        checkGetInt(testBytes, false, 0, 17, 0b11100011001011100);
        checkGetInt(testBytes, false, 0, 18, 0b111000110010111000);
        checkGetInt(testBytes, false, 0, 19, 0b1110001100101110001);
        checkGetInt(testBytes, false, 0, 20, 0b11100011001011100011);
        checkGetInt(testBytes, false, 0, 21, 0b111000110010111000110);
        checkGetInt(testBytes, false, 0, 22, 0b1110001100101110001100);
        checkGetInt(testBytes, false, 0, 23, 0b11100011001011100011001);
        checkGetInt(testBytes, false, 0, 24, 0b111000110010111000110010);
        checkGetInt(testBytes, false, 0, 25, 0b1110001100101110001100101);
        checkGetInt(testBytes, false, 0, 26, 0b11100011001011100011001011);
        checkGetInt(testBytes, false, 0, 27, 0b111000110010111000110010111);
        checkGetInt(testBytes, false, 0, 28, 0b1110001100101110001100101110);
        checkGetInt(testBytes, false, 0, 29, 0b11100011001011100011001011100);
        checkGetInt(testBytes, false, 0, 30, 0b111000110010111000110010111000);
        checkGetInt(testBytes, false, 0, 31, 0b1110001100101110001100101110001);
        checkGetInt(testBytes, false, 0, 32, 0b11100011001011100011001011100011);

        checkGetInt(testBytes, true, 0, 2, - 0b1);
        checkGetInt(testBytes, true, 0, 3, - 0b11);
        checkGetInt(testBytes, true, 0, 4, - 0b110);
        checkGetInt(testBytes, true, 0, 5, - 0b1100);
        checkGetInt(testBytes, true, 0, 6, - 0b11000);
        checkGetInt(testBytes, true, 0, 7, - 0b110001);
        checkGetInt(testBytes, true, 0, 8, - 0b1100011);
        checkGetInt(testBytes, true, 0, 9, - 0b11000110);
        checkGetInt(testBytes, true, 0, 10, - 0b110001100);
        checkGetInt(testBytes, true, 0, 11, - 0b1100011001);
        checkGetInt(testBytes, true, 0, 12, - 0b11000110010);
        checkGetInt(testBytes, true, 0, 13, - 0b110001100101);
        checkGetInt(testBytes, true, 0, 14, - 0b1100011001011);
        checkGetInt(testBytes, true, 0, 15, - 0b11000110010111);
        checkGetInt(testBytes, true, 0, 16, - 0b110001100101110);
        checkGetInt(testBytes, true, 0, 17, - 0b1100011001011100);
        checkGetInt(testBytes, true, 0, 18, - 0b11000110010111000);
        checkGetInt(testBytes, true, 0, 19, - 0b110001100101110001);
        checkGetInt(testBytes, true, 0, 20, - 0b1100011001011100011);
        checkGetInt(testBytes, true, 0, 21, - 0b11000110010111000110);
        checkGetInt(testBytes, true, 0, 22, - 0b110001100101110001100);
        checkGetInt(testBytes, true, 0, 23, - 0b1100011001011100011001);
        checkGetInt(testBytes, true, 0, 24, - 0b11000110010111000110010);
        checkGetInt(testBytes, true, 0, 25, - 0b110001100101110001100101);
        checkGetInt(testBytes, true, 0, 26, - 0b1100011001011100011001011);
        checkGetInt(testBytes, true, 0, 27, - 0b11000110010111000110010111);
        checkGetInt(testBytes, true, 0, 28, - 0b110001100101110001100101110);
        checkGetInt(testBytes, true, 0, 29, - 0b1100011001011100011001011100);
        checkGetInt(testBytes, true, 0, 30, - 0b11000110010111000110010111000);
        checkGetInt(testBytes, true, 0, 31, - 0b110001100101110001100101110001);
        checkGetInt(testBytes, true, 0, 32, - 0b1100011001011100011001011100011);

        checkGetInt(testBytes, false, 3, 1, 0b0);
        checkGetInt(testBytes, false, 3, 2, 0b00);
        checkGetInt(testBytes, false, 3, 3, 0b000);
        checkGetInt(testBytes, false, 3, 4, 0b0001);
        checkGetInt(testBytes, false, 3, 5, 0b00011);
        checkGetInt(testBytes, false, 3, 6, 0b000110);
        checkGetInt(testBytes, false, 3, 7, 0b0001100);
        checkGetInt(testBytes, false, 3, 8, 0b00011001);
        checkGetInt(testBytes, false, 3, 9, 0b000110010);
        checkGetInt(testBytes, false, 3, 10, 0b0001100101);
        checkGetInt(testBytes, false, 3, 11, 0b00011001011);
        checkGetInt(testBytes, false, 3, 12, 0b000110010111);
        checkGetInt(testBytes, false, 3, 13, 0b0001100101110);
        checkGetInt(testBytes, false, 3, 14, 0b00011001011100);
        checkGetInt(testBytes, false, 3, 15, 0b000110010111000);
        checkGetInt(testBytes, false, 3, 16, 0b0001100101110001);
        checkGetInt(testBytes, false, 3, 17, 0b00011001011100011);
        checkGetInt(testBytes, false, 3, 18, 0b000110010111000110);
        checkGetInt(testBytes, false, 3, 19, 0b0001100101110001100);
        checkGetInt(testBytes, false, 3, 20, 0b00011001011100011001);
        checkGetInt(testBytes, false, 3, 21, 0b000110010111000110010);
        checkGetInt(testBytes, false, 3, 22, 0b0001100101110001100101);
        checkGetInt(testBytes, false, 3, 23, 0b00011001011100011001011);
        checkGetInt(testBytes, false, 3, 24, 0b000110010111000110010111);
        checkGetInt(testBytes, false, 3, 25, 0b0001100101110001100101110);
        checkGetInt(testBytes, false, 3, 26, 0b00011001011100011001011100);
        checkGetInt(testBytes, false, 3, 27, 0b000110010111000110010111000);
        checkGetInt(testBytes, false, 3, 28, 0b0001100101110001100101110001);
        checkGetInt(testBytes, false, 3, 29, 0b00011001011100011001011100011);
        checkGetInt(testBytes, false, 3, 30, 0b000110010111000110010111000110);
        checkGetInt(testBytes, false, 3, 31, 0b0001100101110001100101110001100);
        checkGetInt(testBytes, false, 3, 32, 0b00011001011100011001011100011001);

        checkGetInt(testBytes, true, 3, 2, 0b0);
        checkGetInt(testBytes, true, 3, 3, 0b00);
        checkGetInt(testBytes, true, 3, 4, 0b001);
        checkGetInt(testBytes, true, 3, 5, 0b0011);
        checkGetInt(testBytes, true, 3, 6, 0b00110);
        checkGetInt(testBytes, true, 3, 7, 0b001100);
        checkGetInt(testBytes, true, 3, 8, 0b0011001);
        checkGetInt(testBytes, true, 3, 9, 0b00110010);
        checkGetInt(testBytes, true, 3, 10, 0b001100101);
        checkGetInt(testBytes, true, 3, 11, 0b0011001011);
        checkGetInt(testBytes, true, 3, 12, 0b00110010111);
        checkGetInt(testBytes, true, 3, 13, 0b001100101110);
        checkGetInt(testBytes, true, 3, 14, 0b0011001011100);
        checkGetInt(testBytes, true, 3, 15, 0b00110010111000);
        checkGetInt(testBytes, true, 3, 16, 0b001100101110001);
        checkGetInt(testBytes, true, 3, 17, 0b0011001011100011);
        checkGetInt(testBytes, true, 3, 18, 0b00110010111000110);
        checkGetInt(testBytes, true, 3, 19, 0b001100101110001100);
        checkGetInt(testBytes, true, 3, 20, 0b0011001011100011001);
        checkGetInt(testBytes, true, 3, 21, 0b00110010111000110010);
        checkGetInt(testBytes, true, 3, 22, 0b001100101110001100101);
        checkGetInt(testBytes, true, 3, 23, 0b0011001011100011001011);
        checkGetInt(testBytes, true, 3, 24, 0b00110010111000110010111);
        checkGetInt(testBytes, true, 3, 25, 0b001100101110001100101110);
        checkGetInt(testBytes, true, 3, 26, 0b0011001011100011001011100);
        checkGetInt(testBytes, true, 3, 27, 0b00110010111000110010111000);
        checkGetInt(testBytes, true, 3, 28, 0b001100101110001100101110001);
        checkGetInt(testBytes, true, 3, 29, 0b0011001011100011001011100011);
        checkGetInt(testBytes, true, 3, 30, 0b00110010111000110010111000110);
        checkGetInt(testBytes, true, 3, 31, 0b001100101110001100101110001100);
        checkGetInt(testBytes, true, 3, 32, 0b0011001011100011001011100011001);
    }

    private void checkGetInt(byte[] buffer, boolean signed, int bitOffset, int numBits, int expectedValue) {

        assertThat(BitBufferUtil.getIntValue(buffer, signed, bitOffset, numBits)).isEqualTo(expectedValue);
    }

    @Test
    @Category(UnitTest.class)
    public void testSetIntArguments() {

        assertThatThrownBy(() -> BitBufferUtil.setIntValue(new byte[1], 0, false, 0, 9)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testSetIntOneByte() {

        checkSetIntOneByte(0b10101010, 0b0101, 0, 4, 0b01011010);
        checkSetIntOneByte(0b10101010, 0b1010, 1, 4, 0b11010010);
        checkSetIntOneByte(0b10101010, 0b0101, 2, 4, 0b10010110);
        checkSetIntOneByte(0b10101010, 0b1010, 3, 4, 0b10110100);
        checkSetIntOneByte(0b10101010, 0b0101, 4, 4, 0b10100101);
    }

    private void checkSetIntOneByte(int existingByte, int value, int bitOffset, int numBits, int expectedByte) {

        final byte[] buffer = new byte[1];

        buffer[0] = (byte)existingByte;

        BitBufferUtil.setIntValue(buffer, value, false, bitOffset, numBits);

        assertThat(buffer[0]).isEqualTo((byte)expectedByte);
    }

    @Test
    @Category(UnitTest.class)
    public void testSetIntOffset() {

        final byte[] buffer = new byte[4];

        final int value = 0b111000110010;

        final int offset = 0;

        BitBufferUtil.setIntValue(buffer, value, false, 0, 32);

        checkSetIntStartsAtMSB(value, offset, 12, 0b11100011, 0b00100000, 0b00000000, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 13, 0b01110001, 0b10010000, 0b00000000, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 14, 0b00111000, 0b11001000, 0b00000000, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 15, 0b00011100, 0b01100100, 0b00000000, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 16, 0b00001110, 0b00110010, 0b00000000, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 17, 0b00000111, 0b00011001, 0b00000000, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 18, 0b00000011, 0b10001100, 0b10000000, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 19, 0b00000001, 0b11000110, 0b01000000, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 20, 0b00000000, 0b11100011, 0b00100000, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 21, 0b00000000, 0b01110001, 0b10010000, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 22, 0b00000000, 0b00111000, 0b11001000, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 23, 0b00000000, 0b00011100, 0b01100100, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 24, 0b00000000, 0b00001110, 0b00110010, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 25, 0b00000000, 0b00000111, 0b00011001, 0b00000000);
        checkSetIntStartsAtMSB(value, offset, 26, 0b00000000, 0b00000011, 0b10001100, 0b10000000);
        checkSetIntStartsAtMSB(value, offset, 27, 0b00000000, 0b00000001, 0b11000110, 0b01000000);
        checkSetIntStartsAtMSB(value, offset, 28, 0b00000000, 0b00000000, 0b11100011, 0b00100000);
        checkSetIntStartsAtMSB(value, offset, 29, 0b00000000, 0b00000000, 0b01110001, 0b10010000);
        checkSetIntStartsAtMSB(value, offset, 30, 0b00000000, 0b00000000, 0b00111000, 0b11001000);
        checkSetIntStartsAtMSB(value, offset, 31, 0b00000000, 0b00000000, 0b00011100, 0b01100100);
        checkSetIntStartsAtMSB(value, offset, 32, 0b00000000, 0b00000000, 0b00001110, 0b00110010);
    }

    @Test
    @Category(UnitTest.class)
    public void testSetIntNumBits() {

        final byte[] buffer = new byte[4];

        final int value = 0b111000110010;

        final int numBits = 12;

        BitBufferUtil.setIntValue(buffer, value, false, 0, 32);

        checkSetIntStartsAtMSB(value, 0,  numBits, 0b11100011, 0b00100000, 0b00000000, 0b00000000);
        checkSetIntStartsAtMSB(value, 1,  numBits, 0b01110001, 0b10010000, 0b00000000, 0b00000000);
        checkSetIntStartsAtMSB(value, 2,  numBits, 0b00111000, 0b11001000, 0b00000000, 0b00000000);
        checkSetIntStartsAtMSB(value, 3,  numBits, 0b00011100, 0b01100100, 0b00000000, 0b00000000);
        checkSetIntStartsAtMSB(value, 4,  numBits, 0b00001110, 0b00110010, 0b00000000, 0b00000000);
        checkSetIntStartsAtMSB(value, 5,  numBits, 0b00000111, 0b00011001, 0b00000000, 0b00000000);
        checkSetIntStartsAtMSB(value, 6,  numBits, 0b00000011, 0b10001100, 0b10000000, 0b00000000);
        checkSetIntStartsAtMSB(value, 7,  numBits, 0b00000001, 0b11000110, 0b01000000, 0b00000000);
        checkSetIntStartsAtMSB(value, 8,  numBits, 0b00000000, 0b11100011, 0b00100000, 0b00000000);
        checkSetIntStartsAtMSB(value, 9,  numBits, 0b00000000, 0b01110001, 0b10010000, 0b00000000);
        checkSetIntStartsAtMSB(value, 10, numBits, 0b00000000, 0b00111000, 0b11001000, 0b00000000);
        checkSetIntStartsAtMSB(value, 11, numBits, 0b00000000, 0b00011100, 0b01100100, 0b00000000);
        checkSetIntStartsAtMSB(value, 12, numBits, 0b00000000, 0b00001110, 0b00110010, 0b00000000);
        checkSetIntStartsAtMSB(value, 13, numBits, 0b00000000, 0b00000111, 0b00011001, 0b00000000);
        checkSetIntStartsAtMSB(value, 14, numBits, 0b00000000, 0b00000011, 0b10001100, 0b10000000);
        checkSetIntStartsAtMSB(value, 15, numBits, 0b00000000, 0b00000001, 0b11000110, 0b01000000);
        checkSetIntStartsAtMSB(value, 16, numBits, 0b00000000, 0b00000000, 0b11100011, 0b00100000);
        checkSetIntStartsAtMSB(value, 17, numBits, 0b00000000, 0b00000000, 0b01110001, 0b10010000);
        checkSetIntStartsAtMSB(value, 18, numBits, 0b00000000, 0b00000000, 0b00111000, 0b11001000);
        checkSetIntStartsAtMSB(value, 19, numBits, 0b00000000, 0b00000000, 0b00011100, 0b01100100);
        checkSetIntStartsAtMSB(value, 20, numBits, 0b00000000, 0b00000000, 0b00001110, 0b00110010);
    }

    private void checkSetIntStartsAtMSB(int value, int bitOffset, int numBits, int expectedByte1, int expectedByte2, int expectedByte3, int expectedByte4) {

        final byte[] buffer = new byte[4];

        final int maxBit = BitsUtil.getIndexOfHighestSetBit(value);

        if (maxBit >= numBits) {

            throw new IllegalArgumentException();
        }

        for (int i = maxBit + 1; i <= numBits; ++ i) {

            BitBufferUtil.setIntValue(buffer, value, false, bitOffset, numBits);

            assertThat(buffer[0]).isEqualTo((byte)expectedByte1);
            assertThat(buffer[1]).isEqualTo((byte)expectedByte2);
            assertThat(buffer[2]).isEqualTo((byte)expectedByte3);
            assertThat(buffer[3]).isEqualTo((byte)expectedByte4);

            Arrays.fill(buffer, (byte)0);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testGetLong() {

        final byte[] testBytes = new byte[] {

                (byte)0b11100011,
                (byte)0b00101110,
                (byte)0b00110010,
                (byte)0b11100011,
                (byte)0b00101110,
                (byte)0b00110010,
                (byte)0b11100011,
                (byte)0b00101110,
                (byte)0b00110010,
                (byte)0b11100011,
                (byte)0b00101110,
                (byte)0b00110010,
                (byte)0b11100011,
                (byte)0b00101110,
                (byte)0b00110010,
                (byte)0b11100011
        };

        assertThatThrownBy(() -> BitBufferUtil.getLongValue(testBytes, false, 0, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitBufferUtil.getLongValue(testBytes, true, 0, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitBufferUtil.getLongValue(testBytes, true, 0, (testBytes.length * 8) + 1)).isInstanceOf(IllegalArgumentException.class);

        checkGetLong(testBytes, false, 0, 1, 0b1L);
        checkGetLong(testBytes, false, 0, 2, 0b11L);
        checkGetLong(testBytes, false, 0, 3, 0b111L);
        checkGetLong(testBytes, false, 0, 4, 0b1110L);
        checkGetLong(testBytes, false, 0, 5, 0b11100L);
        checkGetLong(testBytes, false, 0, 6, 0b111000L);
        checkGetLong(testBytes, false, 0, 7, 0b1110001L);
        checkGetLong(testBytes, false, 0, 8, 0b11100011L);
        checkGetLong(testBytes, false, 0, 9, 0b111000110L);
        checkGetLong(testBytes, false, 0, 10, 0b1110001100L);
        checkGetLong(testBytes, false, 0, 11, 0b11100011001L);
        checkGetLong(testBytes, false, 0, 12, 0b111000110010L);
        checkGetLong(testBytes, false, 0, 13, 0b1110001100101L);
        checkGetLong(testBytes, false, 0, 14, 0b11100011001011L);
        checkGetLong(testBytes, false, 0, 15, 0b111000110010111L);
        checkGetLong(testBytes, false, 0, 16, 0b1110001100101110L);
        checkGetLong(testBytes, false, 0, 17, 0b11100011001011100L);
        checkGetLong(testBytes, false, 0, 18, 0b111000110010111000L);
        checkGetLong(testBytes, false, 0, 19, 0b1110001100101110001L);
        checkGetLong(testBytes, false, 0, 20, 0b11100011001011100011L);
        checkGetLong(testBytes, false, 0, 21, 0b111000110010111000110L);
        checkGetLong(testBytes, false, 0, 22, 0b1110001100101110001100L);
        checkGetLong(testBytes, false, 0, 23, 0b11100011001011100011001L);
        checkGetLong(testBytes, false, 0, 24, 0b111000110010111000110010L);
        checkGetLong(testBytes, false, 0, 25, 0b1110001100101110001100101L);
        checkGetLong(testBytes, false, 0, 26, 0b11100011001011100011001011L);
        checkGetLong(testBytes, false, 0, 27, 0b111000110010111000110010111L);
        checkGetLong(testBytes, false, 0, 28, 0b1110001100101110001100101110L);
        checkGetLong(testBytes, false, 0, 29, 0b11100011001011100011001011100L);
        checkGetLong(testBytes, false, 0, 30, 0b111000110010111000110010111000L);
        checkGetLong(testBytes, false, 0, 31, 0b1110001100101110001100101110001L);
        checkGetLong(testBytes, false, 0, 32, 0b11100011001011100011001011100011L);
        checkGetLong(testBytes, false, 0, 33, 0b111000110010111000110010111000110L);
        checkGetLong(testBytes, false, 0, 34, 0b1110001100101110001100101110001100L);
        checkGetLong(testBytes, false, 0, 35, 0b11100011001011100011001011100011001L);
        checkGetLong(testBytes, false, 0, 36, 0b111000110010111000110010111000110010L);
        checkGetLong(testBytes, false, 0, 37, 0b1110001100101110001100101110001100101L);
        checkGetLong(testBytes, false, 0, 38, 0b11100011001011100011001011100011001011L);
        checkGetLong(testBytes, false, 0, 39, 0b111000110010111000110010111000110010111L);
        checkGetLong(testBytes, false, 0, 40, 0b1110001100101110001100101110001100101110L);
        checkGetLong(testBytes, false, 0, 41, 0b11100011001011100011001011100011001011100L);
        checkGetLong(testBytes, false, 0, 42, 0b111000110010111000110010111000110010111000L);
        checkGetLong(testBytes, false, 0, 43, 0b1110001100101110001100101110001100101110001L);
        checkGetLong(testBytes, false, 0, 44, 0b11100011001011100011001011100011001011100011L);
        checkGetLong(testBytes, false, 0, 45, 0b111000110010111000110010111000110010111000110L);
        checkGetLong(testBytes, false, 0, 46, 0b1110001100101110001100101110001100101110001100L);
        checkGetLong(testBytes, false, 0, 47, 0b11100011001011100011001011100011001011100011001L);
        checkGetLong(testBytes, false, 0, 48, 0b111000110010111000110010111000110010111000110010L);
        checkGetLong(testBytes, false, 0, 49, 0b1110001100101110001100101110001100101110001100101L);
        checkGetLong(testBytes, false, 0, 50, 0b11100011001011100011001011100011001011100011001011L);
        checkGetLong(testBytes, false, 0, 51, 0b111000110010111000110010111000110010111000110010111L);
        checkGetLong(testBytes, false, 0, 52, 0b1110001100101110001100101110001100101110001100101110L);
        checkGetLong(testBytes, false, 0, 53, 0b11100011001011100011001011100011001011100011001011100L);
        checkGetLong(testBytes, false, 0, 54, 0b111000110010111000110010111000110010111000110010111000L);
        checkGetLong(testBytes, false, 0, 55, 0b1110001100101110001100101110001100101110001100101110001L);
        checkGetLong(testBytes, false, 0, 56, 0b11100011001011100011001011100011001011100011001011100011L);
        checkGetLong(testBytes, false, 0, 57, 0b111000110010111000110010111000110010111000110010111000110L);
        checkGetLong(testBytes, false, 0, 58, 0b1110001100101110001100101110001100101110001100101110001100L);
        checkGetLong(testBytes, false, 0, 59, 0b11100011001011100011001011100011001011100011001011100011001L);
        checkGetLong(testBytes, false, 0, 60, 0b111000110010111000110010111000110010111000110010111000110010L);
        checkGetLong(testBytes, false, 0, 61, 0b1110001100101110001100101110001100101110001100101110001100101L);
        checkGetLong(testBytes, false, 0, 62, 0b11100011001011100011001011100011001011100011001011100011001011L);
        checkGetLong(testBytes, false, 0, 63, 0b111000110010111000110010111000110010111000110010111000110010111L);
        checkGetLong(testBytes, false, 0, 64, 0b1110001100101110001100101110001100101110001100101110001100101110L);

        checkGetLong(testBytes, true, 0, 2, - 0b1L);
        checkGetLong(testBytes, true, 0, 3, - 0b11L);
        checkGetLong(testBytes, true, 0, 4, - 0b110L);
        checkGetLong(testBytes, true, 0, 5, - 0b1100L);
        checkGetLong(testBytes, true, 0, 6, - 0b11000L);
        checkGetLong(testBytes, true, 0, 7, - 0b110001L);
        checkGetLong(testBytes, true, 0, 8, - 0b1100011L);
        checkGetLong(testBytes, true, 0, 9, - 0b11000110L);
        checkGetLong(testBytes, true, 0, 10, - 0b110001100L);
        checkGetLong(testBytes, true, 0, 11, - 0b1100011001L);
        checkGetLong(testBytes, true, 0, 12, - 0b11000110010L);
        checkGetLong(testBytes, true, 0, 13, - 0b110001100101L);
        checkGetLong(testBytes, true, 0, 14, - 0b1100011001011L);
        checkGetLong(testBytes, true, 0, 15, - 0b11000110010111L);
        checkGetLong(testBytes, true, 0, 16, - 0b110001100101110L);
        checkGetLong(testBytes, true, 0, 17, - 0b1100011001011100L);
        checkGetLong(testBytes, true, 0, 18, - 0b11000110010111000L);
        checkGetLong(testBytes, true, 0, 19, - 0b110001100101110001L);
        checkGetLong(testBytes, true, 0, 20, - 0b1100011001011100011L);
        checkGetLong(testBytes, true, 0, 21, - 0b11000110010111000110L);
        checkGetLong(testBytes, true, 0, 22, - 0b110001100101110001100L);
        checkGetLong(testBytes, true, 0, 23, - 0b1100011001011100011001L);
        checkGetLong(testBytes, true, 0, 24, - 0b11000110010111000110010L);
        checkGetLong(testBytes, true, 0, 25, - 0b110001100101110001100101L);
        checkGetLong(testBytes, true, 0, 26, - 0b1100011001011100011001011L);
        checkGetLong(testBytes, true, 0, 27, - 0b11000110010111000110010111L);
        checkGetLong(testBytes, true, 0, 28, - 0b110001100101110001100101110L);
        checkGetLong(testBytes, true, 0, 29, - 0b1100011001011100011001011100L);
        checkGetLong(testBytes, true, 0, 30, - 0b11000110010111000110010111000L);
        checkGetLong(testBytes, true, 0, 31, - 0b110001100101110001100101110001L);
        checkGetLong(testBytes, true, 0, 32, - 0b1100011001011100011001011100011L);
        checkGetLong(testBytes, true, 0, 33, - 0b11000110010111000110010111000110L);
        checkGetLong(testBytes, true, 0, 34, - 0b110001100101110001100101110001100L);
        checkGetLong(testBytes, true, 0, 35, - 0b1100011001011100011001011100011001L);
        checkGetLong(testBytes, true, 0, 36, - 0b11000110010111000110010111000110010L);
        checkGetLong(testBytes, true, 0, 37, - 0b110001100101110001100101110001100101L);
        checkGetLong(testBytes, true, 0, 38, - 0b1100011001011100011001011100011001011L);
        checkGetLong(testBytes, true, 0, 39, - 0b11000110010111000110010111000110010111L);
        checkGetLong(testBytes, true, 0, 40, - 0b110001100101110001100101110001100101110L);
        checkGetLong(testBytes, true, 0, 41, - 0b1100011001011100011001011100011001011100L);
        checkGetLong(testBytes, true, 0, 42, - 0b11000110010111000110010111000110010111000L);
        checkGetLong(testBytes, true, 0, 43, - 0b110001100101110001100101110001100101110001L);
        checkGetLong(testBytes, true, 0, 44, - 0b1100011001011100011001011100011001011100011L);
        checkGetLong(testBytes, true, 0, 45, - 0b11000110010111000110010111000110010111000110L);
        checkGetLong(testBytes, true, 0, 46, - 0b110001100101110001100101110001100101110001100L);
        checkGetLong(testBytes, true, 0, 47, - 0b1100011001011100011001011100011001011100011001L);
        checkGetLong(testBytes, true, 0, 48, - 0b11000110010111000110010111000110010111000110010L);
        checkGetLong(testBytes, true, 0, 49, - 0b110001100101110001100101110001100101110001100101L);
        checkGetLong(testBytes, true, 0, 50, - 0b1100011001011100011001011100011001011100011001011L);
        checkGetLong(testBytes, true, 0, 51, - 0b11000110010111000110010111000110010111000110010111L);
        checkGetLong(testBytes, true, 0, 52, - 0b110001100101110001100101110001100101110001100101110L);
        checkGetLong(testBytes, true, 0, 53, - 0b1100011001011100011001011100011001011100011001011100L);
        checkGetLong(testBytes, true, 0, 54, - 0b11000110010111000110010111000110010111000110010111000L);
        checkGetLong(testBytes, true, 0, 55, - 0b110001100101110001100101110001100101110001100101110001L);
        checkGetLong(testBytes, true, 0, 56, - 0b1100011001011100011001011100011001011100011001011100011L);
        checkGetLong(testBytes, true, 0, 57, - 0b11000110010111000110010111000110010111000110010111000110L);
        checkGetLong(testBytes, true, 0, 58, - 0b110001100101110001100101110001100101110001100101110001100L);
        checkGetLong(testBytes, true, 0, 59, - 0b1100011001011100011001011100011001011100011001011100011001L);
        checkGetLong(testBytes, true, 0, 60, - 0b11000110010111000110010111000110010111000110010111000110010L);
        checkGetLong(testBytes, true, 0, 61, - 0b110001100101110001100101110001100101110001100101110001100101L);
        checkGetLong(testBytes, true, 0, 62, - 0b1100011001011100011001011100011001011100011001011100011001011L);
        checkGetLong(testBytes, true, 0, 63, - 0b11000110010111000110010111000110010111000110010111000110010111L);
        checkGetLong(testBytes, true, 0, 64, - 0b110001100101110001100101110001100101110001100101110001100101110L);

        checkGetLong(testBytes, false, 3, 1, 0b0L);
        checkGetLong(testBytes, false, 3, 2, 0b00L);
        checkGetLong(testBytes, false, 3, 3, 0b000L);
        checkGetLong(testBytes, false, 3, 4, 0b0001L);
        checkGetLong(testBytes, false, 3, 5, 0b00011L);
        checkGetLong(testBytes, false, 3, 6, 0b000110L);
        checkGetLong(testBytes, false, 3, 7, 0b0001100L);
        checkGetLong(testBytes, false, 3, 8, 0b00011001L);
        checkGetLong(testBytes, false, 3, 9, 0b000110010L);
        checkGetLong(testBytes, false, 3, 10, 0b0001100101L);
        checkGetLong(testBytes, false, 3, 11, 0b00011001011L);
        checkGetLong(testBytes, false, 3, 12, 0b000110010111L);
        checkGetLong(testBytes, false, 3, 13, 0b0001100101110L);
        checkGetLong(testBytes, false, 3, 14, 0b00011001011100L);
        checkGetLong(testBytes, false, 3, 15, 0b000110010111000L);
        checkGetLong(testBytes, false, 3, 16, 0b0001100101110001L);
        checkGetLong(testBytes, false, 3, 17, 0b00011001011100011L);
        checkGetLong(testBytes, false, 3, 18, 0b000110010111000110L);
        checkGetLong(testBytes, false, 3, 19, 0b0001100101110001100L);
        checkGetLong(testBytes, false, 3, 20, 0b00011001011100011001L);
        checkGetLong(testBytes, false, 3, 21, 0b000110010111000110010L);
        checkGetLong(testBytes, false, 3, 22, 0b0001100101110001100101L);
        checkGetLong(testBytes, false, 3, 23, 0b00011001011100011001011L);
        checkGetLong(testBytes, false, 3, 24, 0b000110010111000110010111L);
        checkGetLong(testBytes, false, 3, 25, 0b0001100101110001100101110L);
        checkGetLong(testBytes, false, 3, 26, 0b00011001011100011001011100L);
        checkGetLong(testBytes, false, 3, 27, 0b000110010111000110010111000L);
        checkGetLong(testBytes, false, 3, 28, 0b0001100101110001100101110001L);
        checkGetLong(testBytes, false, 3, 29, 0b00011001011100011001011100011L);
        checkGetLong(testBytes, false, 3, 30, 0b000110010111000110010111000110L);
        checkGetLong(testBytes, false, 3, 31, 0b0001100101110001100101110001100L);
        checkGetLong(testBytes, false, 3, 32, 0b00011001011100011001011100011001L);
        checkGetLong(testBytes, false, 3, 33, 0b000110010111000110010111000110010L);
        checkGetLong(testBytes, false, 3, 34, 0b0001100101110001100101110001100101L);
        checkGetLong(testBytes, false, 3, 35, 0b00011001011100011001011100011001011L);
        checkGetLong(testBytes, false, 3, 36, 0b000110010111000110010111000110010111L);
        checkGetLong(testBytes, false, 3, 37, 0b0001100101110001100101110001100101110L);
        checkGetLong(testBytes, false, 3, 38, 0b00011001011100011001011100011001011100L);
        checkGetLong(testBytes, false, 3, 39, 0b000110010111000110010111000110010111000L);
        checkGetLong(testBytes, false, 3, 40, 0b0001100101110001100101110001100101110001L);
        checkGetLong(testBytes, false, 3, 41, 0b00011001011100011001011100011001011100011L);
        checkGetLong(testBytes, false, 3, 42, 0b000110010111000110010111000110010111000110L);
        checkGetLong(testBytes, false, 3, 43, 0b0001100101110001100101110001100101110001100L);
        checkGetLong(testBytes, false, 3, 44, 0b00011001011100011001011100011001011100011001L);
        checkGetLong(testBytes, false, 3, 45, 0b000110010111000110010111000110010111000110010L);
        checkGetLong(testBytes, false, 3, 46, 0b0001100101110001100101110001100101110001100101L);
        checkGetLong(testBytes, false, 3, 47, 0b00011001011100011001011100011001011100011001011L);
        checkGetLong(testBytes, false, 3, 48, 0b000110010111000110010111000110010111000110010111L);
        checkGetLong(testBytes, false, 3, 49, 0b0001100101110001100101110001100101110001100101110L);
        checkGetLong(testBytes, false, 3, 50, 0b00011001011100011001011100011001011100011001011100L);
        checkGetLong(testBytes, false, 3, 51, 0b000110010111000110010111000110010111000110010111000L);
        checkGetLong(testBytes, false, 3, 52, 0b0001100101110001100101110001100101110001100101110001L);
        checkGetLong(testBytes, false, 3, 53, 0b00011001011100011001011100011001011100011001011100011L);
        checkGetLong(testBytes, false, 3, 54, 0b000110010111000110010111000110010111000110010111000110L);
        checkGetLong(testBytes, false, 3, 55, 0b0001100101110001100101110001100101110001100101110001100L);
        checkGetLong(testBytes, false, 3, 56, 0b00011001011100011001011100011001011100011001011100011001L);
        checkGetLong(testBytes, false, 3, 57, 0b000110010111000110010111000110010111000110010111000110010L);
        checkGetLong(testBytes, false, 3, 58, 0b0001100101110001100101110001100101110001100101110001100101L);
        checkGetLong(testBytes, false, 3, 59, 0b00011001011100011001011100011001011100011001011100011001011L);
        checkGetLong(testBytes, false, 3, 60, 0b000110010111000110010111000110010111000110010111000110010111L);
        checkGetLong(testBytes, false, 3, 61, 0b0001100101110001100101110001100101110001100101110001100101110L);
        checkGetLong(testBytes, false, 3, 62, 0b00011001011100011001011100011001011100011001011100011001011100L);
        checkGetLong(testBytes, false, 3, 63, 0b000110010111000110010111000110010111000110010111000110010111000L);
        checkGetLong(testBytes, false, 3, 64, 0b0001100101110001100101110001100101110001100101110001100101110001L);

        checkGetLong(testBytes, true, 3, 2, 0b0L);
        checkGetLong(testBytes, true, 3, 3, 0b00L);
        checkGetLong(testBytes, true, 3, 4, 0b001L);
        checkGetLong(testBytes, true, 3, 5, 0b0011L);
        checkGetLong(testBytes, true, 3, 6, 0b00110L);
        checkGetLong(testBytes, true, 3, 7, 0b001100L);
        checkGetLong(testBytes, true, 3, 8, 0b0011001L);
        checkGetLong(testBytes, true, 3, 9, 0b00110010L);
        checkGetLong(testBytes, true, 3, 10, 0b001100101L);
        checkGetLong(testBytes, true, 3, 11, 0b0011001011L);
        checkGetLong(testBytes, true, 3, 12, 0b00110010111L);
        checkGetLong(testBytes, true, 3, 13, 0b001100101110L);
        checkGetLong(testBytes, true, 3, 14, 0b0011001011100L);
        checkGetLong(testBytes, true, 3, 15, 0b00110010111000L);
        checkGetLong(testBytes, true, 3, 16, 0b001100101110001L);
        checkGetLong(testBytes, true, 3, 17, 0b0011001011100011L);
        checkGetLong(testBytes, true, 3, 18, 0b00110010111000110L);
        checkGetLong(testBytes, true, 3, 19, 0b001100101110001100L);
        checkGetLong(testBytes, true, 3, 20, 0b0011001011100011001L);
        checkGetLong(testBytes, true, 3, 21, 0b00110010111000110010L);
        checkGetLong(testBytes, true, 3, 22, 0b001100101110001100101L);
        checkGetLong(testBytes, true, 3, 23, 0b0011001011100011001011L);
        checkGetLong(testBytes, true, 3, 24, 0b00110010111000110010111L);
        checkGetLong(testBytes, true, 3, 25, 0b001100101110001100101110L);
        checkGetLong(testBytes, true, 3, 26, 0b0011001011100011001011100L);
        checkGetLong(testBytes, true, 3, 27, 0b00110010111000110010111000L);
        checkGetLong(testBytes, true, 3, 28, 0b001100101110001100101110001L);
        checkGetLong(testBytes, true, 3, 29, 0b0011001011100011001011100011L);
        checkGetLong(testBytes, true, 3, 30, 0b00110010111000110010111000110L);
        checkGetLong(testBytes, true, 3, 31, 0b001100101110001100101110001100L);
        checkGetLong(testBytes, true, 3, 32, 0b0011001011100011001011100011001L);
        checkGetLong(testBytes, true, 3, 33, 0b00110010111000110010111000110010L);
        checkGetLong(testBytes, true, 3, 34, 0b001100101110001100101110001100101L);
        checkGetLong(testBytes, true, 3, 35, 0b0011001011100011001011100011001011L);
        checkGetLong(testBytes, true, 3, 36, 0b00110010111000110010111000110010111L);
        checkGetLong(testBytes, true, 3, 37, 0b001100101110001100101110001100101110L);
        checkGetLong(testBytes, true, 3, 38, 0b0011001011100011001011100011001011100L);
        checkGetLong(testBytes, true, 3, 39, 0b00110010111000110010111000110010111000L);
        checkGetLong(testBytes, true, 3, 40, 0b001100101110001100101110001100101110001L);
        checkGetLong(testBytes, true, 3, 41, 0b0011001011100011001011100011001011100011L);
        checkGetLong(testBytes, true, 3, 42, 0b00110010111000110010111000110010111000110L);
        checkGetLong(testBytes, true, 3, 43, 0b001100101110001100101110001100101110001100L);
        checkGetLong(testBytes, true, 3, 44, 0b0011001011100011001011100011001011100011001L);
        checkGetLong(testBytes, true, 3, 45, 0b00110010111000110010111000110010111000110010L);
        checkGetLong(testBytes, true, 3, 46, 0b001100101110001100101110001100101110001100101L);
        checkGetLong(testBytes, true, 3, 47, 0b0011001011100011001011100011001011100011001011L);
        checkGetLong(testBytes, true, 3, 48, 0b00110010111000110010111000110010111000110010111L);
        checkGetLong(testBytes, true, 3, 49, 0b001100101110001100101110001100101110001100101110L);
        checkGetLong(testBytes, true, 3, 50, 0b0011001011100011001011100011001011100011001011100L);
        checkGetLong(testBytes, true, 3, 51, 0b00110010111000110010111000110010111000110010111000L);
        checkGetLong(testBytes, true, 3, 52, 0b001100101110001100101110001100101110001100101110001L);
        checkGetLong(testBytes, true, 3, 53, 0b0011001011100011001011100011001011100011001011100011L);
        checkGetLong(testBytes, true, 3, 54, 0b00110010111000110010111000110010111000110010111000110L);
        checkGetLong(testBytes, true, 3, 55, 0b001100101110001100101110001100101110001100101110001100L);
        checkGetLong(testBytes, true, 3, 56, 0b0011001011100011001011100011001011100011001011100011001L);
        checkGetLong(testBytes, true, 3, 57, 0b00110010111000110010111000110010111000110010111000110010L);
        checkGetLong(testBytes, true, 3, 58, 0b001100101110001100101110001100101110001100101110001100101L);
        checkGetLong(testBytes, true, 3, 59, 0b0011001011100011001011100011001011100011001011100011001011L);
        checkGetLong(testBytes, true, 3, 60, 0b00110010111000110010111000110010111000110010111000110010111L);
        checkGetLong(testBytes, true, 3, 61, 0b001100101110001100101110001100101110001100101110001100101110L);
        checkGetLong(testBytes, true, 3, 62, 0b0011001011100011001011100011001011100011001011100011001011100L);
        checkGetLong(testBytes, true, 3, 63, 0b00110010111000110010111000110010111000110010111000110010111000L);
        checkGetLong(testBytes, true, 3, 64, 0b001100101110001100101110001100101110001100101110001100101110001L);
    }

    private void checkGetLong(byte[] buffer, boolean signed, int bitOffset, int numBits, long expectedValue) {

        assertThat(BitBufferUtil.getLongValue(buffer, signed, bitOffset, numBits)).isEqualTo(expectedValue);
    }

    @Test
    @Category(UnitTest.class)
    public void testSetLongArguments() {

        assertThatThrownBy(() -> BitBufferUtil.setLongValue(new byte[1], 0, false, 0, 9)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testSetLongOneByte() {

        checkSetLongOneByte(0b10101010, 0b0101, 0, 4, 0b01011010);
        checkSetLongOneByte(0b10101010, 0b1010, 1, 4, 0b11010010);
        checkSetLongOneByte(0b10101010, 0b0101, 2, 4, 0b10010110);
        checkSetLongOneByte(0b10101010, 0b1010, 3, 4, 0b10110100);
        checkSetLongOneByte(0b10101010, 0b0101, 4, 4, 0b10100101);
    }

    private void checkSetLongOneByte(int existingByte, int value, int bitOffset, int numBits, int expectedByte) {

        final byte[] buffer = new byte[1];

        buffer[0] = (byte)existingByte;

        BitBufferUtil.setIntValue(buffer, value, false, bitOffset, numBits);

        assertThat(buffer[0]).isEqualTo((byte)expectedByte);
    }

    @Test
    @Category(UnitTest.class)
    public void testSetLongOffset() {

        final byte[] buffer = new byte[8];

        final int value = 0b111000110010;

        final int offset = 0;

        BitBufferUtil.setIntValue(buffer, value, false, 0, 64);

        checkSetLongStartsAtMSB(value, offset, 12, 0b11100011, 0b00100000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 13, 0b01110001, 0b10010000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 14, 0b00111000, 0b11001000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 15, 0b00011100, 0b01100100, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 16, 0b00001110, 0b00110010, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 17, 0b00000111, 0b00011001, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 18, 0b00000011, 0b10001100, 0b10000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 19, 0b00000001, 0b11000110, 0b01000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 20, 0b00000000, 0b11100011, 0b00100000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 21, 0b00000000, 0b01110001, 0b10010000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 22, 0b00000000, 0b00111000, 0b11001000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 23, 0b00000000, 0b00011100, 0b01100100, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 24, 0b00000000, 0b00001110, 0b00110010, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 25, 0b00000000, 0b00000111, 0b00011001, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 26, 0b00000000, 0b00000011, 0b10001100, 0b10000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 27, 0b00000000, 0b00000001, 0b11000110, 0b01000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 28, 0b00000000, 0b00000000, 0b11100011, 0b00100000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 29, 0b00000000, 0b00000000, 0b01110001, 0b10010000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 30, 0b00000000, 0b00000000, 0b00111000, 0b11001000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 31, 0b00000000, 0b00000000, 0b00011100, 0b01100100, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 32, 0b00000000, 0b00000000, 0b00001110, 0b00110010, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 33, 0b00000000, 0b00000000, 0b00000111, 0b00011001, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 34, 0b00000000, 0b00000000, 0b00000011, 0b10001100, 0b10000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 35, 0b00000000, 0b00000000, 0b00000001, 0b11000110, 0b01000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 36, 0b00000000, 0b00000000, 0b00000000, 0b11100011, 0b00100000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 37, 0b00000000, 0b00000000, 0b00000000, 0b01110001, 0b10010000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 38, 0b00000000, 0b00000000, 0b00000000, 0b00111000, 0b11001000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 39, 0b00000000, 0b00000000, 0b00000000, 0b00011100, 0b01100100, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 40, 0b00000000, 0b00000000, 0b00000000, 0b00001110, 0b00110010, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 41, 0b00000000, 0b00000000, 0b00000000, 0b00000111, 0b00011001, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 42, 0b00000000, 0b00000000, 0b00000000, 0b00000011, 0b10001100, 0b10000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 43, 0b00000000, 0b00000000, 0b00000000, 0b00000001, 0b11000110, 0b01000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 44, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b11100011, 0b00100000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 45, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b01110001, 0b10010000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 46, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00111000, 0b11001000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 47, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00011100, 0b01100100, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 48, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00001110, 0b00110010, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 49, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000111, 0b00011001, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 50, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000011, 0b10001100, 0b10000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 51, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000001, 0b11000110, 0b01000000, 0b00000000);
        checkSetLongStartsAtMSB(value, offset, 52, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b11100011, 0b00100000, 0b00000000);
    }

    @Test
    @Category(UnitTest.class)
    public void testSetLongNumBits() {

        final byte[] buffer = new byte[8];

        final int value = 0b111000110010;

        final int numBits = 12;

        BitBufferUtil.setIntValue(buffer, value, false, 0, 64);

        checkSetLongStartsAtMSB(value, 0,  numBits, 0b11100011, 0b00100000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 1,  numBits, 0b01110001, 0b10010000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 2,  numBits, 0b00111000, 0b11001000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 3,  numBits, 0b00011100, 0b01100100, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 4,  numBits, 0b00001110, 0b00110010, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 5,  numBits, 0b00000111, 0b00011001, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 6,  numBits, 0b00000011, 0b10001100, 0b10000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 7,  numBits, 0b00000001, 0b11000110, 0b01000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 8,  numBits, 0b00000000, 0b11100011, 0b00100000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 9,  numBits, 0b00000000, 0b01110001, 0b10010000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 10, numBits, 0b00000000, 0b00111000, 0b11001000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 11, numBits, 0b00000000, 0b00011100, 0b01100100, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 12, numBits, 0b00000000, 0b00001110, 0b00110010, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 13, numBits, 0b00000000, 0b00000111, 0b00011001, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 14, numBits, 0b00000000, 0b00000011, 0b10001100, 0b10000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 15, numBits, 0b00000000, 0b00000001, 0b11000110, 0b01000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 16, numBits, 0b00000000, 0b00000000, 0b11100011, 0b00100000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 17, numBits, 0b00000000, 0b00000000, 0b01110001, 0b10010000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 18, numBits, 0b00000000, 0b00000000, 0b00111000, 0b11001000, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 19, numBits, 0b00000000, 0b00000000, 0b00011100, 0b01100100, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 20, numBits, 0b00000000, 0b00000000, 0b00001110, 0b00110010, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 21, numBits, 0b00000000, 0b00000000, 0b00000111, 0b00011001, 0b00000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 22, numBits, 0b00000000, 0b00000000, 0b00000011, 0b10001100, 0b10000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 23, numBits, 0b00000000, 0b00000000, 0b00000001, 0b11000110, 0b01000000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 24, numBits, 0b00000000, 0b00000000, 0b00000000, 0b11100011, 0b00100000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 25, numBits, 0b00000000, 0b00000000, 0b00000000, 0b01110001, 0b10010000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 26, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00111000, 0b11001000, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 27, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00011100, 0b01100100, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 28, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00001110, 0b00110010, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 29, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000111, 0b00011001, 0b00000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 30, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000011, 0b10001100, 0b10000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 31, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000001, 0b11000110, 0b01000000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 32, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b11100011, 0b00100000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 33, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b01110001, 0b10010000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 34, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00111000, 0b11001000, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 35, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00011100, 0b01100100, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 36, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00001110, 0b00110010, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 37, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000111, 0b00011001, 0b00000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 38, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000011, 0b10001100, 0b10000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 39, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000001, 0b11000110, 0b01000000, 0b00000000);
        checkSetLongStartsAtMSB(value, 40, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b11100011, 0b00100000, 0b00000000);
        checkSetLongStartsAtMSB(value, 41, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b01110001, 0b10010000, 0b00000000);
        checkSetLongStartsAtMSB(value, 42, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00111000, 0b11001000, 0b00000000);
        checkSetLongStartsAtMSB(value, 43, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00011100, 0b01100100, 0b00000000);
        checkSetLongStartsAtMSB(value, 44, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00001110, 0b00110010, 0b00000000);
        checkSetLongStartsAtMSB(value, 45, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000111, 0b00011001, 0b00000000);
        checkSetLongStartsAtMSB(value, 46, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000011, 0b10001100, 0b10000000);
        checkSetLongStartsAtMSB(value, 47, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000001, 0b11000110, 0b01000000);
        checkSetLongStartsAtMSB(value, 48, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b11100011, 0b00100000);
        checkSetLongStartsAtMSB(value, 49, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b01110001, 0b10010000);
        checkSetLongStartsAtMSB(value, 50, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00111000, 0b11001000);
        checkSetLongStartsAtMSB(value, 51, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00011100, 0b01100100);
        checkSetLongStartsAtMSB(value, 52, numBits, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00000000, 0b00001110, 0b00110010);
    }

    private void checkSetLongStartsAtMSB(int value, int bitOffset, int numBits, int expectedByte1, int expectedByte2, int expectedByte3, int expectedByte4, int expectedByte5,
            int expectedByte6, int expectedByte7, int expectedByte8) {

        final byte[] buffer = new byte[8];

        final int maxBit = BitsUtil.getIndexOfHighestSetBit(value);

        if (maxBit >= numBits) {

            throw new IllegalArgumentException();
        }

        for (int i = maxBit + 1; i <= numBits; ++ i) {

            BitBufferUtil.setIntValue(buffer, value, false, bitOffset, numBits);

            assertThat(buffer[0]).isEqualTo((byte)expectedByte1);
            assertThat(buffer[1]).isEqualTo((byte)expectedByte2);
            assertThat(buffer[2]).isEqualTo((byte)expectedByte3);
            assertThat(buffer[3]).isEqualTo((byte)expectedByte4);
            assertThat(buffer[4]).isEqualTo((byte)expectedByte5);
            assertThat(buffer[5]).isEqualTo((byte)expectedByte6);
            assertThat(buffer[6]).isEqualTo((byte)expectedByte7);
            assertThat(buffer[7]).isEqualTo((byte)expectedByte8);

            Arrays.fill(buffer, (byte)0);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testIsBitSet() {

        final byte[] buffer = new byte[4];

        buffer[0] = (byte)0b10101010;
        buffer[1] = (byte)0b01010101;
        buffer[2] = (byte)0b11110000;
        buffer[3] = (byte)0b00001111;

        assertThat(BitBufferUtil.isBitSet(buffer, 0)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 1)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 2)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 3)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 4)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 5)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 6)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 7)).isFalse();

        assertThat(BitBufferUtil.isBitSet(buffer, 8)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 9)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 10)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 11)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 12)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 13)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 14)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 15)).isTrue();

        assertThat(BitBufferUtil.isBitSet(buffer, 16)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 17)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 18)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 19)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 20)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 21)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 22)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 23)).isFalse();

        assertThat(BitBufferUtil.isBitSet(buffer, 24)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 25)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 26)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 27)).isFalse();
        assertThat(BitBufferUtil.isBitSet(buffer, 28)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 29)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 30)).isTrue();
        assertThat(BitBufferUtil.isBitSet(buffer, 31)).isTrue();
    }

    @Test
    @Category(UnitTest.class)
    public void testSetBit() {

        final byte[] buffer = new byte[4];

        Arrays.fill(buffer, (byte)0);
        checkSetBit(buffer, true);

        final byte allOnes = (byte)0b11111111;
        Arrays.fill(buffer, allOnes);
        checkSetBit(buffer, false);
    }

    private static void checkSetBit(byte[] initialBuffer, boolean set) {

        if (initialBuffer.length != 4) {

            throw new IllegalArgumentException();
        }

        for (int i = 0; i < 32; ++ i) {

            final byte[] buffer = Arrays.copyOf(initialBuffer, initialBuffer.length);

            BitBufferUtil.setBitValue(buffer, i, set);

            for (int j = 0; j < 32; ++ j) {

                if (set) {

                    assertThat(BitBufferUtil.isBitSet(buffer, j)).isEqualTo(i == j);
                }
                else {
                    assertThat(BitBufferUtil.isBitSet(buffer, j)).isEqualTo(i != j);
                }
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testSetBits() {

        final byte[] buffer = new byte[4];

        Arrays.fill(buffer, (byte)0);

        final int numBits3 = 3;

        checkSetBits(buffer, true, 0,  numBits3, 0b11100000, 0b00000000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 1,  numBits3, 0b01110000, 0b00000000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 2,  numBits3, 0b00111000, 0b00000000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 3,  numBits3, 0b00011100, 0b00000000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 4,  numBits3, 0b00001110, 0b00000000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 5,  numBits3, 0b00000111, 0b00000000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 6,  numBits3, 0b00000011, 0b10000000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 7,  numBits3, 0b00000001, 0b11000000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 8,  numBits3, 0b00000000, 0b11100000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 9,  numBits3, 0b00000000, 0b01110000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 10, numBits3, 0b00000000, 0b00111000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 11, numBits3, 0b00000000, 0b00011100, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 12, numBits3, 0b00000000, 0b00001110, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 13, numBits3, 0b00000000, 0b00000111, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 14, numBits3, 0b00000000, 0b00000011, 0b10000000, 0b00000000);
        checkSetBits(buffer, true, 15, numBits3, 0b00000000, 0b00000001, 0b11000000, 0b00000000);
        checkSetBits(buffer, true, 16, numBits3, 0b00000000, 0b00000000, 0b11100000, 0b00000000);
        checkSetBits(buffer, true, 17, numBits3, 0b00000000, 0b00000000, 0b01110000, 0b00000000);
        checkSetBits(buffer, true, 18, numBits3, 0b00000000, 0b00000000, 0b00111000, 0b00000000);
        checkSetBits(buffer, true, 19, numBits3, 0b00000000, 0b00000000, 0b00011100, 0b00000000);
        checkSetBits(buffer, true, 20, numBits3, 0b00000000, 0b00000000, 0b00001110, 0b00000000);
        checkSetBits(buffer, true, 21, numBits3, 0b00000000, 0b00000000, 0b00000111, 0b00000000);
        checkSetBits(buffer, true, 22, numBits3, 0b00000000, 0b00000000, 0b00000011, 0b10000000);
        checkSetBits(buffer, true, 23, numBits3, 0b00000000, 0b00000000, 0b00000001, 0b11000000);
        checkSetBits(buffer, true, 24, numBits3, 0b00000000, 0b00000000, 0b00000000, 0b11100000);
        checkSetBits(buffer, true, 25, numBits3, 0b00000000, 0b00000000, 0b00000000, 0b01110000);
        checkSetBits(buffer, true, 26, numBits3, 0b00000000, 0b00000000, 0b00000000, 0b00111000);
        checkSetBits(buffer, true, 27, numBits3, 0b00000000, 0b00000000, 0b00000000, 0b00011100);
        checkSetBits(buffer, true, 28, numBits3, 0b00000000, 0b00000000, 0b00000000, 0b00001110);
        checkSetBits(buffer, true, 29, numBits3, 0b00000000, 0b00000000, 0b00000000, 0b00000111);

        final int numBits12 = 12;

        checkSetBits(buffer, true, 0,  numBits12, 0b11111111, 0b11110000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 1,  numBits12, 0b01111111, 0b11111000, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 2,  numBits12, 0b00111111, 0b11111100, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 3,  numBits12, 0b00011111, 0b11111110, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 4,  numBits12, 0b00001111, 0b11111111, 0b00000000, 0b00000000);
        checkSetBits(buffer, true, 5,  numBits12, 0b00000111, 0b11111111, 0b10000000, 0b00000000);
        checkSetBits(buffer, true, 6,  numBits12, 0b00000011, 0b11111111, 0b11000000, 0b00000000);
        checkSetBits(buffer, true, 7,  numBits12, 0b00000001, 0b11111111, 0b11100000, 0b00000000);
        checkSetBits(buffer, true, 8,  numBits12, 0b00000000, 0b11111111, 0b11110000, 0b00000000);
        checkSetBits(buffer, true, 9,  numBits12, 0b00000000, 0b01111111, 0b11111000, 0b00000000);
        checkSetBits(buffer, true, 10, numBits12, 0b00000000, 0b00111111, 0b11111100, 0b00000000);
        checkSetBits(buffer, true, 11, numBits12, 0b00000000, 0b00011111, 0b11111110, 0b00000000);
        checkSetBits(buffer, true, 12, numBits12, 0b00000000, 0b00001111, 0b11111111, 0b00000000);
        checkSetBits(buffer, true, 13, numBits12, 0b00000000, 0b00000111, 0b11111111, 0b10000000);
        checkSetBits(buffer, true, 14, numBits12, 0b00000000, 0b00000011, 0b11111111, 0b11000000);
        checkSetBits(buffer, true, 15, numBits12, 0b00000000, 0b00000001, 0b11111111, 0b11100000);
        checkSetBits(buffer, true, 16, numBits12, 0b00000000, 0b00000000, 0b11111111, 0b11110000);
        checkSetBits(buffer, true, 17, numBits12, 0b00000000, 0b00000000, 0b01111111, 0b11111000);
        checkSetBits(buffer, true, 18, numBits12, 0b00000000, 0b00000000, 0b00111111, 0b11111100);
        checkSetBits(buffer, true, 19, numBits12, 0b00000000, 0b00000000, 0b00011111, 0b11111110);
        checkSetBits(buffer, true, 20, numBits12, 0b00000000, 0b00000000, 0b00001111, 0b11111111);

        final byte allOnes = (byte)0b11111111;
        Arrays.fill(buffer, allOnes);

        checkSetBits(buffer, false, 0,  numBits3, 0b00011111, 0b11111111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 1,  numBits3, 0b10001111, 0b11111111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 2,  numBits3, 0b11000111, 0b11111111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 3,  numBits3, 0b11100011, 0b11111111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 4,  numBits3, 0b11110001, 0b11111111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 5,  numBits3, 0b11111000, 0b11111111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 6,  numBits3, 0b11111100, 0b01111111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 7,  numBits3, 0b11111110, 0b00111111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 8,  numBits3, 0b11111111, 0b00011111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 9,  numBits3, 0b11111111, 0b10001111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 10, numBits3, 0b11111111, 0b11000111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 11, numBits3, 0b11111111, 0b11100011, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 12, numBits3, 0b11111111, 0b11110001, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 13, numBits3, 0b11111111, 0b11111000, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 14, numBits3, 0b11111111, 0b11111100, 0b01111111, 0b11111111);
        checkSetBits(buffer, false, 15, numBits3, 0b11111111, 0b11111110, 0b00111111, 0b11111111);
        checkSetBits(buffer, false, 16, numBits3, 0b11111111, 0b11111111, 0b00011111, 0b11111111);
        checkSetBits(buffer, false, 17, numBits3, 0b11111111, 0b11111111, 0b10001111, 0b11111111);
        checkSetBits(buffer, false, 18, numBits3, 0b11111111, 0b11111111, 0b11000111, 0b11111111);
        checkSetBits(buffer, false, 19, numBits3, 0b11111111, 0b11111111, 0b11100011, 0b11111111);
        checkSetBits(buffer, false, 20, numBits3, 0b11111111, 0b11111111, 0b11110001, 0b11111111);
        checkSetBits(buffer, false, 21, numBits3, 0b11111111, 0b11111111, 0b11111000, 0b11111111);
        checkSetBits(buffer, false, 22, numBits3, 0b11111111, 0b11111111, 0b11111100, 0b01111111);
        checkSetBits(buffer, false, 23, numBits3, 0b11111111, 0b11111111, 0b11111110, 0b00111111);
        checkSetBits(buffer, false, 24, numBits3, 0b11111111, 0b11111111, 0b11111111, 0b00011111);
        checkSetBits(buffer, false, 25, numBits3, 0b11111111, 0b11111111, 0b11111111, 0b10001111);
        checkSetBits(buffer, false, 26, numBits3, 0b11111111, 0b11111111, 0b11111111, 0b11000111);
        checkSetBits(buffer, false, 27, numBits3, 0b11111111, 0b11111111, 0b11111111, 0b11100011);
        checkSetBits(buffer, false, 28, numBits3, 0b11111111, 0b11111111, 0b11111111, 0b11110001);
        checkSetBits(buffer, false, 29, numBits3, 0b11111111, 0b11111111, 0b11111111, 0b11111000);

        checkSetBits(buffer, false, 0,  numBits12, 0b00000000, 0b00001111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 1,  numBits12, 0b10000000, 0b00000111, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 2,  numBits12, 0b11000000, 0b00000011, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 3,  numBits12, 0b11100000, 0b00000001, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 4,  numBits12, 0b11110000, 0b00000000, 0b11111111, 0b11111111);
        checkSetBits(buffer, false, 5,  numBits12, 0b11111000, 0b00000000, 0b01111111, 0b11111111);
        checkSetBits(buffer, false, 6,  numBits12, 0b11111100, 0b00000000, 0b00111111, 0b11111111);
        checkSetBits(buffer, false, 7,  numBits12, 0b11111110, 0b00000000, 0b00011111, 0b11111111);
        checkSetBits(buffer, false, 8,  numBits12, 0b11111111, 0b00000000, 0b00001111, 0b11111111);
        checkSetBits(buffer, false, 9,  numBits12, 0b11111111, 0b10000000, 0b00000111, 0b11111111);
        checkSetBits(buffer, false, 10, numBits12, 0b11111111, 0b11000000, 0b00000011, 0b11111111);
        checkSetBits(buffer, false, 11, numBits12, 0b11111111, 0b11100000, 0b00000001, 0b11111111);
        checkSetBits(buffer, false, 12, numBits12, 0b11111111, 0b11110000, 0b00000000, 0b11111111);
        checkSetBits(buffer, false, 13, numBits12, 0b11111111, 0b11111000, 0b00000000, 0b01111111);
        checkSetBits(buffer, false, 14, numBits12, 0b11111111, 0b11111100, 0b00000000, 0b00111111);
        checkSetBits(buffer, false, 15, numBits12, 0b11111111, 0b11111110, 0b00000000, 0b00011111);
        checkSetBits(buffer, false, 16, numBits12, 0b11111111, 0b11111111, 0b00000000, 0b00001111);
        checkSetBits(buffer, false, 17, numBits12, 0b11111111, 0b11111111, 0b10000000, 0b00000111);
        checkSetBits(buffer, false, 18, numBits12, 0b11111111, 0b11111111, 0b11000000, 0b00000011);
        checkSetBits(buffer, false, 19, numBits12, 0b11111111, 0b11111111, 0b11100000, 0b00000001);
        checkSetBits(buffer, false, 20, numBits12, 0b11111111, 0b11111111, 0b11110000, 0b00000000);
    }

    private void checkSetBits(byte[] buffer, boolean set, int bitOffset, int numBits, int expectedByte1, int expectedByte2, int expectedByte3, int expectedByte4) {

        final int bufferLength = buffer.length;

        if (bufferLength != 4) {

            throw new IllegalArgumentException();
        }

        final byte[] bufferCopy = Arrays.copyOf(buffer, bufferLength);

        BitBufferUtil.setBits(bufferCopy, set, bitOffset, numBits);

        assertThat(bufferCopy[0]).isEqualTo((byte)expectedByte1);
        assertThat(bufferCopy[1]).isEqualTo((byte)expectedByte2);
        assertThat(bufferCopy[2]).isEqualTo((byte)expectedByte3);
        assertThat(bufferCopy[3]).isEqualTo((byte)expectedByte4);
    }

    @Test
    @Category(UnitTest.class)
    public void testClearBits() {

        final byte[] buffer = new byte[4];

        final byte allOnes = (byte)0b11111111;
        Arrays.fill(buffer, allOnes);

        final int numBits3 = 3;

        checkClearBits(buffer, 0,  numBits3, 0b00011111, 0b11111111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 1,  numBits3, 0b10001111, 0b11111111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 2,  numBits3, 0b11000111, 0b11111111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 3,  numBits3, 0b11100011, 0b11111111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 4,  numBits3, 0b11110001, 0b11111111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 5,  numBits3, 0b11111000, 0b11111111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 6,  numBits3, 0b11111100, 0b01111111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 7,  numBits3, 0b11111110, 0b00111111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 8,  numBits3, 0b11111111, 0b00011111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 9,  numBits3, 0b11111111, 0b10001111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 10, numBits3, 0b11111111, 0b11000111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 11, numBits3, 0b11111111, 0b11100011, 0b11111111, 0b11111111);
        checkClearBits(buffer, 12, numBits3, 0b11111111, 0b11110001, 0b11111111, 0b11111111);
        checkClearBits(buffer, 13, numBits3, 0b11111111, 0b11111000, 0b11111111, 0b11111111);
        checkClearBits(buffer, 14, numBits3, 0b11111111, 0b11111100, 0b01111111, 0b11111111);
        checkClearBits(buffer, 15, numBits3, 0b11111111, 0b11111110, 0b00111111, 0b11111111);
        checkClearBits(buffer, 16, numBits3, 0b11111111, 0b11111111, 0b00011111, 0b11111111);
        checkClearBits(buffer, 17, numBits3, 0b11111111, 0b11111111, 0b10001111, 0b11111111);
        checkClearBits(buffer, 18, numBits3, 0b11111111, 0b11111111, 0b11000111, 0b11111111);
        checkClearBits(buffer, 19, numBits3, 0b11111111, 0b11111111, 0b11100011, 0b11111111);
        checkClearBits(buffer, 20, numBits3, 0b11111111, 0b11111111, 0b11110001, 0b11111111);
        checkClearBits(buffer, 21, numBits3, 0b11111111, 0b11111111, 0b11111000, 0b11111111);
        checkClearBits(buffer, 22, numBits3, 0b11111111, 0b11111111, 0b11111100, 0b01111111);
        checkClearBits(buffer, 23, numBits3, 0b11111111, 0b11111111, 0b11111110, 0b00111111);
        checkClearBits(buffer, 24, numBits3, 0b11111111, 0b11111111, 0b11111111, 0b00011111);
        checkClearBits(buffer, 25, numBits3, 0b11111111, 0b11111111, 0b11111111, 0b10001111);
        checkClearBits(buffer, 26, numBits3, 0b11111111, 0b11111111, 0b11111111, 0b11000111);
        checkClearBits(buffer, 27, numBits3, 0b11111111, 0b11111111, 0b11111111, 0b11100011);
        checkClearBits(buffer, 28, numBits3, 0b11111111, 0b11111111, 0b11111111, 0b11110001);
        checkClearBits(buffer, 29, numBits3, 0b11111111, 0b11111111, 0b11111111, 0b11111000);

        final int numBits12 = 12;

        checkClearBits(buffer, 0,  numBits12, 0b00000000, 0b00001111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 1,  numBits12, 0b10000000, 0b00000111, 0b11111111, 0b11111111);
        checkClearBits(buffer, 2,  numBits12, 0b11000000, 0b00000011, 0b11111111, 0b11111111);
        checkClearBits(buffer, 3,  numBits12, 0b11100000, 0b00000001, 0b11111111, 0b11111111);
        checkClearBits(buffer, 4,  numBits12, 0b11110000, 0b00000000, 0b11111111, 0b11111111);
        checkClearBits(buffer, 5,  numBits12, 0b11111000, 0b00000000, 0b01111111, 0b11111111);
        checkClearBits(buffer, 6,  numBits12, 0b11111100, 0b00000000, 0b00111111, 0b11111111);
        checkClearBits(buffer, 7,  numBits12, 0b11111110, 0b00000000, 0b00011111, 0b11111111);
        checkClearBits(buffer, 8,  numBits12, 0b11111111, 0b00000000, 0b00001111, 0b11111111);
        checkClearBits(buffer, 9,  numBits12, 0b11111111, 0b10000000, 0b00000111, 0b11111111);
        checkClearBits(buffer, 10, numBits12, 0b11111111, 0b11000000, 0b00000011, 0b11111111);
        checkClearBits(buffer, 11, numBits12, 0b11111111, 0b11100000, 0b00000001, 0b11111111);
        checkClearBits(buffer, 12, numBits12, 0b11111111, 0b11110000, 0b00000000, 0b11111111);
        checkClearBits(buffer, 13, numBits12, 0b11111111, 0b11111000, 0b00000000, 0b01111111);
        checkClearBits(buffer, 14, numBits12, 0b11111111, 0b11111100, 0b00000000, 0b00111111);
        checkClearBits(buffer, 15, numBits12, 0b11111111, 0b11111110, 0b00000000, 0b00011111);
        checkClearBits(buffer, 16, numBits12, 0b11111111, 0b11111111, 0b00000000, 0b00001111);
        checkClearBits(buffer, 17, numBits12, 0b11111111, 0b11111111, 0b10000000, 0b00000111);
        checkClearBits(buffer, 18, numBits12, 0b11111111, 0b11111111, 0b11000000, 0b00000011);
        checkClearBits(buffer, 19, numBits12, 0b11111111, 0b11111111, 0b11100000, 0b00000001);
        checkClearBits(buffer, 20, numBits12, 0b11111111, 0b11111111, 0b11110000, 0b00000000);
    }

    private void checkClearBits(byte[] buffer, int bitOffset, int numBits, int expectedByte1, int expectedByte2, int expectedByte3, int expectedByte4) {

        final int bufferLength = buffer.length;

        if (bufferLength != 4) {

            throw new IllegalArgumentException();
        }

        final byte[] bufferCopy = Arrays.copyOf(buffer, bufferLength);

        BitBufferUtil.clearBits(bufferCopy, bitOffset, numBits);

        assertThat(bufferCopy[0]).isEqualTo((byte)expectedByte1);
        assertThat(bufferCopy[1]).isEqualTo((byte)expectedByte2);
        assertThat(bufferCopy[2]).isEqualTo((byte)expectedByte3);
        assertThat(bufferCopy[3]).isEqualTo((byte)expectedByte4);
    }

    @Test
    @Category(UnitTest.class)
    public void testCopyBits() {

        checkCopyBits((byte)0);
        checkCopyBits((byte)0b10101010);
        checkCopyBits((byte)1);
    }

    private void checkCopyBits(byte inputByte) {

        checkCopyBits(inputByte, false);
        checkCopyBits(inputByte, true);
    }

    private void checkCopyBits(byte inputByte, boolean defaultIsOutputBitSet) {

        final int maxNumInputBytes = 4;
        final int maxOutputBitPaddingBytes = 2;
        final int maxNumOutputSkewBytes = 2;
        final int maxNumOutputBytes = maxNumInputBytes + maxOutputBitPaddingBytes + maxNumOutputSkewBytes;

        final byte[] inputBuffer = new byte[maxNumInputBytes];

        Arrays.fill(inputBuffer, inputByte);

        final byte[] outputBuffer = new byte[maxNumOutputBytes];

        Arrays.fill(outputBuffer, (byte)(defaultIsOutputBitSet ? 0b11111111 : 0));

        final int maxNumInputBits = maxNumInputBytes * 8;
        final int maxOutputBitPaddingBits = maxOutputBitPaddingBytes * 8;
        final int maxNumOutputBits = maxNumOutputBytes * 8;
// try {
        for (int numInputBits = 0; numInputBits <= maxNumInputBits; ++ numInputBits) {

            final int maxInputBitOffset = maxNumInputBits - numInputBits - 1;

            for (int inputBitOffset = 0; inputBitOffset <= maxInputBitOffset; ++ inputBitOffset) {

                for (int numOutputBitStartPaddingBits = 0; numOutputBitStartPaddingBits <= maxOutputBitPaddingBits; ++ numOutputBitStartPaddingBits) {

                    final int maxOutputBitStartOffset = maxNumOutputBits - numInputBits - numOutputBitStartPaddingBits - 1;

                    for (int outputBitStartOffset = 0; outputBitStartOffset <= maxOutputBitStartOffset; ++ outputBitStartOffset) {

                        final int outputBitOffset = outputBitStartOffset;
                        final int numOutputBits = numInputBits + numOutputBitStartPaddingBits;

                        final int outputNonPaddedBitOffset = outputBitOffset + numOutputBitStartPaddingBits;

                        final byte[] outputBufferCopy = Arrays.copyOf(outputBuffer, outputBuffer.length);

                        BitBufferUtil.copyBits(inputBuffer, inputBitOffset, numInputBits, outputBufferCopy, outputBitOffset, numOutputBits);

                        for (int i = 0; i < maxNumOutputBits; ++ i) {

                            final boolean isWithinCopyOutputRange = i >= outputNonPaddedBitOffset && i < (outputNonPaddedBitOffset + numInputBits);

// System.out.println("is output bit set " + i + ' ' + inputBitOffset + ' ' + (i >= outputNonPaddedBitOffset) + ' ' + (i < (outputNonPaddedBitOffset + numInputBits)));

                            final boolean expectedIsOutputBitSet;

                            if (isWithinCopyOutputRange) {

                                final int inputBufferBitOffset = inputBitOffset < outputNonPaddedBitOffset
                                        ? i - (outputNonPaddedBitOffset - inputBitOffset)
                                        : i + (inputBitOffset - outputNonPaddedBitOffset);

                                expectedIsOutputBitSet = BitBufferUtil.isBitSet(inputBuffer, inputBufferBitOffset);

// System.out.println("inputBufferBitOffset " + inputBufferBitOffset + ' ' + expectedIsOutputBitSet);
                            }
                            else {
                                final boolean isWithinCopyOutputPaddingRange = i >= outputBitOffset && i < outputNonPaddedBitOffset;

                                expectedIsOutputBitSet = isWithinCopyOutputPaddingRange ? false : defaultIsOutputBitSet;
                            }

                            assertThat(BitBufferUtil.isBitSet(outputBufferCopy, i)).isEqualTo(expectedIsOutputBitSet);
                        }
                    }
                }
            }
        }
/*
}
finally {

    System.out.println("end of test");
}
*/
    }

    @Test
    @Category(UnitTest.class)
    public void testToBinaryString() {

        assertThat(BitBufferUtil.toBinaryString(new byte[0])).isEqualTo("[]");
        assertThat(BitBufferUtil.toBinaryString(byteArray(0b10101010))).isEqualTo("[10101010]");
        assertThat(BitBufferUtil.toBinaryString(byteArray(0b00000000, 0b10101010, 0b11111111))).isEqualTo("[00000000,10101010,11111111]");
    }
}
