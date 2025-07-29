package dev.jdata.db.utils.adt.collections;

import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.jdk.adt.lists.Lists;

public final class CollectionUtilTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testToArrayByIndex() {

        assertThatThrownBy(() -> CollectionUtil.toArray(null, 1, null, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> CollectionUtil.toArray((List<Integer>)null, 1, Integer[]::new, List::get)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> CollectionUtil.toArray(Lists.unmodifiableOf(1), 1, null, List::get)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> CollectionUtil.toArray(Lists.unmodifiableOf(1), 1, Integer[]::new, null)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> CollectionUtil.toArray(Lists.unmodifiableOf(1), -1, Integer[]::new, List::get)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CollectionUtil.toArray(Lists.empty(), 0, Integer[]::new, List::get)).isEmpty();
        assertThat(CollectionUtil.toArray(Lists.unmodifiableOf(1), 1, Integer[]::new, List::get)).containsExactly(1);
        assertThat(CollectionUtil.toArray(Lists.unmodifiableOf(1, 2, 3), 1, Integer[]::new, List::get)).containsExactly(1);
        assertThat(CollectionUtil.toArray(Lists.unmodifiableOf(1, 2, 3), 2, Integer[]::new, List::get)).containsExactly(1, 2);
        assertThat(CollectionUtil.toArray(Lists.unmodifiableOf(1, 2, 3), 3, Integer[]::new, List::get)).containsExactly(1, 2, 3);
    }
}
