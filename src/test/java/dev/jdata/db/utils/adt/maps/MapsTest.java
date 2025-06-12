package dev.jdata.db.utils.adt.maps;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseDBTest;
import dev.jdata.db.utils.adt.maps.Maps.IIntToObjectMapForEachCaller;
import dev.jdata.db.utils.adt.maps.Maps.ILongAppendEachValue;
import dev.jdata.db.utils.adt.maps.Maps.ILongForEachAppendCaller;
import dev.jdata.db.utils.adt.maps.Maps.ILongToObjectMapForEachCaller;

public final class MapsTest extends BaseDBTest {

    @Test
    @Category(UnitTest.class)
    public void testIntToObjectMapToString() {

        final Integer parameter = 1;

        final String prefix = getClass().getSimpleName();

        final IIntToObjectMapForEachCaller<String, Integer> intToObjectMapForEachCaller = (b, p, f) -> {

            assertThat(p).isSameAs(parameter);

            f.each(123, "abc", b, p);
            f.each(234, "bcd", b, p);
            f.each(345, "cde", b, p);
        };

        final String string = Maps.intToObjectMapToString(prefix, 3L, parameter, intToObjectMapForEachCaller);

        assertThat(string).isEqualTo(prefix + " {123=abc,234=bcd,345=cde}");
    }

    @Test
    @Category(UnitTest.class)
    public void testLongToObjectMapToString() {

        final Integer parameter = 1;

        final String prefix = getClass().getSimpleName();

        final ILongToObjectMapForEachCaller<String, Integer> longToObjectMapForEachCaller = (b, p, f) -> {

            assertThat(p).isSameAs(parameter);

            f.each(123, "abc", b, p);
            f.each(234, "bcd", b, p);
            f.each(345, "cde", b, p);
        };

        final String string = Maps.longToObjectMapToString(prefix, 3L, parameter, longToObjectMapForEachCaller);

        assertThat(string).isEqualTo(prefix + " {123=abc,234=bcd,345=cde}");
    }

    @Test
    @Category(UnitTest.class)
    public void testLongAppendToString() {

        final Integer parameter = 1;

        final String prefix = getClass().getSimpleName();

        final ILongForEachAppendCaller<String, Integer> longForEachAppendCaller = (b, p, f) -> {

            assertThat(p).isSameAs(parameter);

            f.each(123L, "abc", b, p);
            f.each(234L, "bcd", b, p);
            f.each(345L, "cde", b, p);
        };

        final ILongAppendEachValue<String, Integer> longAppendEachValue = (k, v, b, p) -> b.append(v);

        final String string = Maps.longAppendToString(prefix, 3L, parameter, longForEachAppendCaller, longAppendEachValue);

        assertThat(string).isEqualTo(prefix + " {123=abc,234=bcd,345=cde}");
    }
}
