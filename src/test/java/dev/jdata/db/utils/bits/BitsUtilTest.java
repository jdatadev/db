package dev.jdata.db.utils.bits;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.bits.BitsUtil;

public final class BitsUtilTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testMask() {
/*
        assertThatThrownBy(() -> BitsUtil.mask(-1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitsUtil.mask(0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitsUtil.mask(9)).isInstanceOf(IllegalArgumentException.class);
*/
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
    public void testGetNumBitsInt() {

        assertThat(BitsUtil.getNumBits(0, false)).isEqualTo(1);
        assertThat(BitsUtil.getNumBits(0, true)).isEqualTo(2);

        for (int i = 0; i < 32; ++ i) {

System.out.println("num bits " + i);

            int testValue = 1 << i;

            if (i > 0) {

                testValue |= 1 << (i - 1);
            }

            assertThat(BitsUtil.getNumBits(testValue, false)).isEqualTo(i + 1);
            assertThat(BitsUtil.getNumBits(testValue, true)).isEqualTo(i + 2);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testGetNumBitsLong() {

        assertThat(BitsUtil.getNumBits(0L, false)).isEqualTo(0L);
        assertThat(BitsUtil.getNumBits(0L, true)).isEqualTo(0L);

        for (int i = 0; i < 64; ++ i) {

            assertThat(BitsUtil.getNumBits(1L << i, false)).isEqualTo(i + 1);
            assertThat(BitsUtil.getNumBits(1L << i, true)).isEqualTo(i + 2);
        }
    }
}
