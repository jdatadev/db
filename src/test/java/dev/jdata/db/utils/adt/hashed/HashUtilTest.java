package dev.jdata.db.utils.adt.hashed;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.checks.Checks;

public final class HashUtilTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testComputeHashCapacityExponent() {

        checkComputeCapacityExponent(HashUtil::computeHashCapacityExponent);
    }

    @Test
    @Category(UnitTest.class)
    public void testComputeRehashCapacityExponent() {

        checkComputeCapacityExponent(HashUtil::computeRehashCapacityExponent);
    }

    @FunctionalInterface
    private interface CapacityExponentComputer {

        int compute(long numElements, float loadFactor);
    }

    private static void checkComputeCapacityExponent(CapacityExponentComputer capacityExponentComputer) {

        assertThatThrownBy(() -> capacityExponentComputer.compute(-1, 0.75f)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> capacityExponentComputer.compute(1, -0.001f)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> capacityExponentComputer.compute(1, 0.0f)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> capacityExponentComputer.compute(1, 1.001f)).isInstanceOf(IllegalArgumentException.class);

        assertThat(capacityExponentComputer.compute(0, 0.1f)).isEqualTo(0);
        assertThat(capacityExponentComputer.compute(0, 0.9f)).isEqualTo(0);

        assertThat(capacityExponentComputer.compute(10, 0.5f)).isEqualTo(5);
        assertThat(capacityExponentComputer.compute(50, 0.5f)).isEqualTo(7);
        assertThat(capacityExponentComputer.compute(100, 0.5f)).isEqualTo(8);

        assertThat(capacityExponentComputer.compute(10, 0.9f)).isEqualTo(4);
        assertThat(capacityExponentComputer.compute(50, 0.9f)).isEqualTo(6);
        assertThat(capacityExponentComputer.compute(100, 0.9f)).isEqualTo(7);

        assertThat(capacityExponentComputer.compute(4, 0.5f)).isEqualTo(3);
        assertThat(capacityExponentComputer.compute(5, 0.5f)).isEqualTo(4);

        assertThat(capacityExponentComputer.compute(1024, 0.5f)).isEqualTo(11);
        assertThat(capacityExponentComputer.compute(1025, 0.5f)).isEqualTo(12);
    }

    @Test
    @Category(UnitTest.class)
    public void testShouldRehash() {

        assertThatThrownBy(() -> HashUtil.shouldRehash(-1, 1L, 0.1f)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.shouldRehash(1L, -1L, 0.1f)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.shouldRehash(1L, Double.valueOf(Double.MAX_VALUE).longValue() + 1, 0.1f)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.shouldRehash(1L, 1L, -0.001f)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.shouldRehash(1L, 1L, 0.0f)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.shouldRehash(1L, 1L, 1.001f)).isInstanceOf(IllegalArgumentException.class);

        assertThat(HashUtil.shouldRehash(0L, 0L, 0.9f)).isFalse();
        assertThat(HashUtil.shouldRehash(1L, 0L, 0.9f)).isTrue();
        assertThat(HashUtil.shouldRehash(1L, 1L, 0.9f)).isTrue();

        assertThat(HashUtil.shouldRehash(1L, Double.valueOf(Double.MAX_VALUE).longValue(), 0.9f)).isFalse();

        assertThat(HashUtil.shouldRehash(10L, 100L, 0.1f)).isFalse();
        assertThat(HashUtil.shouldRehash(11L, 100L, 0.1f)).isTrue();
    }

    @Test
    @Category(UnitTest.class)
    public void testComputeRequiredCapacity() {

        assertThatThrownBy(() -> HashUtil.computeRequiredCapacity(-1L, 0L, 0.5f, 1, false)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.computeRequiredCapacity(0L, 0L, 0.5f, 1, false)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.computeRequiredCapacity(1L, -1L, 0.5f, 1, false)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.computeRequiredCapacity(1L, 0L, -0.1f, 1, false)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.computeRequiredCapacity(1L, 0L, 1.01f, 1, false)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.computeRequiredCapacity(1L, 0L, 0.5f, 0, false)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.computeRequiredCapacity(1L, 0L, 0.5f, Checks.MAX_INT_CAPACITY_EXPONENT + 1, false)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.computeRequiredCapacity(1L, 0L, 0.5f, Checks.MAX_LONG_CAPACITY_EXPONENT + 1, true)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> HashUtil.computeRequiredCapacity(1L, 2L, 0.5f, 1, false)).isInstanceOf(IllegalArgumentException.class);

        assertThat(HashUtil.computeRequiredCapacity(1L, 0L, 0.5f, 1, false)).isEqualTo(2);
        assertThat(HashUtil.computeRequiredCapacity(1L, 1L, 0.5f, 1, false)).isEqualTo(2);

        assertThat(HashUtil.computeRequiredCapacity(1L, 0L, 0.5f, 2, false)).isEqualTo(4);
        assertThat(HashUtil.computeRequiredCapacity(1L, 1L, 0.5f, 2, false)).isEqualTo(4);

        assertThat(HashUtil.computeRequiredCapacity(1L, 0L, 0.5f, 3, false)).isEqualTo(8);
        assertThat(HashUtil.computeRequiredCapacity(1L, 1L, 0.5f, 3, false)).isEqualTo(8);

        assertThat(HashUtil.computeRequiredCapacity(3L, 1L, 0.5f, 1, false)).isEqualTo(8);
        assertThat(HashUtil.computeRequiredCapacity(4L, 1L, 0.5f, 1, false)).isEqualTo(8);
        assertThat(HashUtil.computeRequiredCapacity(5L, 1L, 0.5f, 1, false)).isEqualTo(16);
        assertThat(HashUtil.computeRequiredCapacity(6L, 1L, 0.5f, 1, false)).isEqualTo(16);
        assertThat(HashUtil.computeRequiredCapacity(7L, 0L, 0.5f, 1, false)).isEqualTo(16);
        assertThat(HashUtil.computeRequiredCapacity(8L, 1L, 0.5f, 1, false)).isEqualTo(16);
        assertThat(HashUtil.computeRequiredCapacity(9L, 1L, 0.5f, 1, false)).isEqualTo(32);

        assertThat(HashUtil.computeRequiredCapacity(3L, 1L, 0.75f, 1, false)).isEqualTo(4);
        assertThat(HashUtil.computeRequiredCapacity(4L, 1L, 0.75f, 1, false)).isEqualTo(8);
        assertThat(HashUtil.computeRequiredCapacity(5L, 1L, 0.75f, 1, false)).isEqualTo(8);
        assertThat(HashUtil.computeRequiredCapacity(6L, 1L, 0.75f, 1, false)).isEqualTo(8);
        assertThat(HashUtil.computeRequiredCapacity(7L, 0L, 0.75f, 1, false)).isEqualTo(16);
    }
}
