package dev.jdata.db.utils.adt.decimals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class MutableDecimalTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testConstructor() {

        checkConstructor(new BigDecimal("123.45"));
        checkConstructor(new BigDecimal("-123.45"));
    }

    private void checkConstructor(BigDecimal bigDecimal) {

        final MutableDecimal decimal = MutableDecimal.valueOf(bigDecimal);

        assertThat(decimal.getValue()).isEqualTo(bigDecimal);
    }

    @Test
    @Category(UnitTest.class)
    public void testCompare() {

        checkCompare(new BigDecimal("123.45"), new BigDecimal("123.45"), 0);
        checkCompare(new BigDecimal("123.450"), new BigDecimal("123.45"), 0);
        checkCompare(new BigDecimal("123.45000"), new BigDecimal("123.45"), 0);
        checkCompare(new BigDecimal("123.45"), new BigDecimal("123.45000"), 0);
        checkCompare(new BigDecimal("123.45001"), new BigDecimal("123.45"), 1);
        checkCompare(new BigDecimal("123.45"), new BigDecimal("123.45001"), -1);
        checkCompare(new BigDecimal("12.345"), new BigDecimal("123.45"), -1);
        checkCompare(new BigDecimal("123.45"), new BigDecimal("12.345"), 1);

        checkCompare(new BigDecimal("-123.45"), new BigDecimal("123.45"), -1);
        checkCompare(new BigDecimal("-123.45"), new BigDecimal("-123.45"), 0);
        checkCompare(new BigDecimal("123.45"), new BigDecimal("-123.45"), 1);
    }

    private void checkCompare(BigDecimal bigDecimal1, BigDecimal bigDecimal2, int expectedResult) {

        final MutableDecimal decimal1 = MutableDecimal.valueOf(bigDecimal1);
        final MutableDecimal decimal2 = MutableDecimal.valueOf(bigDecimal2);

        assertThat(decimal1.compareTo(decimal2)).isEqualTo(expectedResult);
    }
}
