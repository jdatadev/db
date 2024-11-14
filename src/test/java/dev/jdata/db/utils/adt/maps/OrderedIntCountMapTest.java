package dev.jdata.db.utils.adt.maps;

import java.util.LinkedHashMap;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class OrderedIntCountMapTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testAdd() {

        final OrderedIntCountMap countMap = new OrderedIntCountMap(10);

        assertThat(countMap).isEmpty();

        countMap.add(1);
        countMap.add(4);
        countMap.add(3);
        countMap.add(2);
        countMap.add(3);
        countMap.add(1);
        countMap.add(4);
        countMap.add(4);

        assertThat(countMap).hasNumElements(4);

        assertThat(countMap.getCount(-1)).isEqualTo(0);
        assertThat(countMap.getCount(0)).isEqualTo(0);

        assertThat(countMap.getCount(1)).isEqualTo(2);
        assertThat(countMap.getCount(2)).isEqualTo(1);
        assertThat(countMap.getCount(3)).isEqualTo(2);
        assertThat(countMap.getCount(4)).isEqualTo(3);

        final LinkedHashMap<Integer, Integer> values = new LinkedHashMap<>();

        countMap.forEach((v, c) -> values.put(v, c));

        assertThat(values).hasSize(4);

        assertThat(values.get(1)).isEqualTo(2);
        assertThat(values.get(2)).isEqualTo(1);
        assertThat(values.get(3)).isEqualTo(2);
        assertThat(values.get(4)).isEqualTo(3);
    }

    @Test
    @Category(UnitTest.class)
    public void testIsEmpty() {

        final OrderedIntCountMap countMap = new OrderedIntCountMap(10);

        assertThat(countMap).isEmpty();

        countMap.add(1);
        assertThat(countMap).isNotEmpty();

        countMap.add(4);
        assertThat(countMap).isNotEmpty();

        countMap.clear();
        assertThat(countMap).isEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public void testGetNumElements() {

        final OrderedIntCountMap countMap = new OrderedIntCountMap(10);

        assertThat(countMap).hasNumElements(0);

        countMap.add(1);
        assertThat(countMap).hasNumElements(1);

        countMap.add(4);
        assertThat(countMap).hasNumElements(2);

        countMap.add(1);
        assertThat(countMap).hasNumElements(2);

        countMap.clear();
        assertThat(countMap).hasNumElements(0);
    }

    @Test
    @Category(UnitTest.class)
    public void testClear() {

        final OrderedIntCountMap countMap = new OrderedIntCountMap(10);

        countMap.add(1);
        countMap.add(4);
        countMap.add(1);

        assertThat(countMap).isNotEmpty();
        assertThat(countMap).hasNumElements(2);

        countMap.clear();
        assertThat(countMap).isEmpty();
        assertThat(countMap).hasNumElements(0);
    }
}
