package dev.jdata.db.utils.scalars;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class IntegersTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testCheckUnsignedLongToUnsignedInt() {

        assertThat(Integers.checkUnsignedLongToUnsignedInt(0L)).isEqualTo(0);

        for (int i = 0; i < 31; ++ i) {

            assertThat(Integers.checkUnsignedLongToUnsignedInt(1L << i)).isEqualTo(1 << i);
        }

        for (int i = 31; i < 64; ++ i) {

            final int closureI = i;

            assertThatThrownBy(() -> Integers.checkUnsignedLongToUnsignedInt(1L << closureI)).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
