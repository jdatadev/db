package dev.jdata.db.utils.adt.hashed.helpers;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.scalars.Integers;

public final class MaxDistanceTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testComputeMaxDistance() {

        assertThatThrownBy(() -> MaxDistance.computeDistance(-1, 0, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> MaxDistance.computeDistance(0, -1, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> MaxDistance.computeDistance(0, 0, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> MaxDistance.computeDistance(1, 0, 1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> MaxDistance.computeDistance(0, 1, 1)).isInstanceOf(IndexOutOfBoundsException.class);

        checkComputeDistance(0, 0, 1, 0);
        checkComputeDistance(0, 0, 1, 0);

        checkComputeDistance(0, 0, 2, 0);
        checkComputeDistance(0, 1, 2, 1);
        checkComputeDistance(1, 0, 2, 1);
        checkComputeDistance(1, 1, 2, 0);

        checkComputeDistance(0, 0, 3, 0);
        checkComputeDistance(0, 1, 3, 2);
        checkComputeDistance(0, 2, 3, 1);
        checkComputeDistance(1, 0, 3, 1);
        checkComputeDistance(1, 1, 3, 0);
        checkComputeDistance(1, 2, 3, 2);
        checkComputeDistance(2, 0, 3, 2);
        checkComputeDistance(2, 1, 3, 1);
        checkComputeDistance(2, 2, 3, 0);
    }

    private static void checkComputeDistance(int putIndex, int hashArrayIndex, int capacity, int expectedResult) {

        assertThat(MaxDistance.computeDistance(putIndex, hashArrayIndex, capacity)).isEqualTo(expectedResult);
    }

    @Test
    @Category(UnitTest.class)
    public void testCopyMaxDistances() {

        assertThatThrownBy(() -> MaxDistance.copyMaxDistances(null)).isInstanceOf(NullPointerException.class);

        checkCopyMaxDistances();
        checkCopyMaxDistances(12);
        checkCopyMaxDistances(12, 23);
        checkCopyMaxDistances(12, 23, 34);
    }

    @Deprecated // test LargeMaxDistance class
    private void checkCopyMaxDistances(int ... values) {

        final int length = values.length;

        final byte[] maxDistances = new byte[length];

        for (int i = 0; i < length; ++ i) {

            maxDistances[i] = Integers.checkIntToByte(values[i]);
        }

        final byte[] copied = MaxDistance.copyMaxDistances(maxDistances);

        assertThat(copied).isNotSameAs(maxDistances);
        assertThat(copied).isEqualTo(maxDistances);
    }
}
