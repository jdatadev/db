package dev.jdata.db.utils.adt;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class CapacityTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testComputeArrayOuterCapacity() {

        assertThatThrownBy(() -> Capacity.computeArrayOuterCapacity(-1, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Capacity.computeArrayOuterCapacity(1, -1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(Capacity.computeArrayOuterCapacity(0, 1)).isEqualTo(0);
        assertThat(Capacity.computeArrayOuterCapacity(1, 1)).isEqualTo(1);
        assertThat(Capacity.computeArrayOuterCapacity(2, 1)).isEqualTo(2);
        assertThat(Capacity.computeArrayOuterCapacity(3, 1)).isEqualTo(3);

        assertThat(Capacity.computeArrayOuterCapacity(0, 2)).isEqualTo(0);
        assertThat(Capacity.computeArrayOuterCapacity(1, 2)).isEqualTo(1);
        assertThat(Capacity.computeArrayOuterCapacity(2, 2)).isEqualTo(1);
        assertThat(Capacity.computeArrayOuterCapacity(3, 2)).isEqualTo(2);
        assertThat(Capacity.computeArrayOuterCapacity(4, 2)).isEqualTo(2);
        assertThat(Capacity.computeArrayOuterCapacity(5, 2)).isEqualTo(3);

        assertThat(Capacity.computeArrayOuterCapacity(0, 3)).isEqualTo(0);
        assertThat(Capacity.computeArrayOuterCapacity(1, 3)).isEqualTo(1);
        assertThat(Capacity.computeArrayOuterCapacity(2, 3)).isEqualTo(1);
        assertThat(Capacity.computeArrayOuterCapacity(3, 3)).isEqualTo(1);
        assertThat(Capacity.computeArrayOuterCapacity(4, 3)).isEqualTo(2);
        assertThat(Capacity.computeArrayOuterCapacity(5, 3)).isEqualTo(2);
        assertThat(Capacity.computeArrayOuterCapacity(6, 3)).isEqualTo(2);
        assertThat(Capacity.computeArrayOuterCapacity(7, 3)).isEqualTo(3);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetRemainderOfLastInnerArrayWithLimit() {

        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArrayWithLimit(2L, 1L, 2, -1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArrayWithLimit(-1L, 1L, 2, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArrayWithLimit(1L, 1L, 2, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArrayWithLimit(3L, 1L, 2, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArrayWithLimit(2L, 1L, -1, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArrayWithLimit(2L, -1L, 2, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArrayWithLimit(2L, 0L, 2, 1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(0L, 1L, 0, 0)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(1L, 1L, 1, 0)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(2L, 1L, 2, 1)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(3L, 1L, 3, 2)).isEqualTo(0L);

        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(0L, 2L, 0, 0)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(0L, 2L, 1, 0)).isEqualTo(2L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(1L, 2L, 1, 0)).isEqualTo(1L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(2L, 2L, 1, 0)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(3L, 2L, 2, 1)).isEqualTo(1L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(4L, 2L, 2, 1)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(5L, 2L, 3, 2)).isEqualTo(1L);

        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(0L, 3L, 0, 0)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(1L, 3L, 1, 0)).isEqualTo(2L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(2L, 3L, 1, 0)).isEqualTo(1L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(3L, 3L, 1, 0)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(4L, 3L, 2, 1)).isEqualTo(2L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(5L, 3L, 2, 1)).isEqualTo(1L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(6L, 3L, 2, 1)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArrayWithLimit(7L, 3L, 3, 2)).isEqualTo(2L);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetRemainderOfLastInnerArray() {

        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArray(1L, 1L, 2, -1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArray(1L, 1L, 1, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArray(1L, 1L, 3, 1)).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArray(1L, -1L, 1, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArray(1L, 0L, 1, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArray(-1L, 1L, 1, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Capacity.getRemainderOfLastInnerArray(2L, 1L, 1, 1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(Capacity.getRemainderOfLastInnerArray(0L, 1L, 0, 0)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArray(1L, 1L, 1, 0)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArray(1L, 1L, 2, 1)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArray(0L, 1L, 2, 1)).isEqualTo(1L);
        assertThat(Capacity.getRemainderOfLastInnerArray(1L, 1L, 2, 1)).isEqualTo(0L);

        assertThat(Capacity.getRemainderOfLastInnerArray(0L, 2L, 0, 0)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArray(1L, 2L, 1, 0)).isEqualTo(1L);
        assertThat(Capacity.getRemainderOfLastInnerArray(0L, 2L, 2, 1)).isEqualTo(2L);
        assertThat(Capacity.getRemainderOfLastInnerArray(1L, 2L, 2, 1)).isEqualTo(1L);
        assertThat(Capacity.getRemainderOfLastInnerArray(2L, 2L, 2, 1)).isEqualTo(0L);

        assertThat(Capacity.getRemainderOfLastInnerArray(0L, 3L, 0, 0)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArray(1L, 3L, 1, 0)).isEqualTo(2L);
        assertThat(Capacity.getRemainderOfLastInnerArray(2L, 3L, 1, 0)).isEqualTo(1L);
        assertThat(Capacity.getRemainderOfLastInnerArray(3L, 3L, 1, 0)).isEqualTo(0L);
        assertThat(Capacity.getRemainderOfLastInnerArray(0L, 3L, 2, 1)).isEqualTo(3L);
        assertThat(Capacity.getRemainderOfLastInnerArray(1L, 3L, 2, 1)).isEqualTo(2L);
        assertThat(Capacity.getRemainderOfLastInnerArray(2L, 3L, 2, 1)).isEqualTo(1L);
        assertThat(Capacity.getRemainderOfLastInnerArray(3L, 3L, 2, 1)).isEqualTo(0L);
    }
}
