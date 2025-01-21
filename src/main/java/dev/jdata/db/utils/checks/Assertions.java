package dev.jdata.db.utils.checks;

import dev.jdata.db.utils.adt.Contains;
import dev.jdata.db.utils.adt.elements.Elements;

public class Assertions {

    public static void isTrue(boolean value) {

        if (!value) {

            throwAssertion();
        }
    }

    public static void isFalse(boolean value) {

        if (value) {

            throwAssertion();
        }
    }

    public static void isNotNull(Object object) {

        if (object == null) {

            throwAssertion();
        }
    }

    public static void isLessThan(int value1, int value2) {

        if (value1 >= value2) {

            throwAssertion();
        }
    }

    public static void isLessThanOrEqualTo(int value1, int value2) {

        if (value1 > value2) {

            throwAssertion();
        }
    }

    public static void areEqual(long value1, long value2) {

        if (value1 != value2) {

            throwAssertion();
        }
    }

    public static void areNotEqual(long value1, long value2) {

        if (value1 == value2) {

            throwAssertion();
        }
    }

    public static void isAboveZero(long value) {

        if (value < 1L) {

            throwAssertion();
        }
    }

    public static void isEmpty(Contains contains) {

        if (!contains.isEmpty()) {

            throwAssertion();
        }
    }

    public static void areSameNumElements(Elements elements1, Elements elements2) {

        if (elements1.getNumElements() != elements2.getNumElements()) {

            throwAssertion();
        }
    }

    private static void throwAssertion() {

        throw new AssertionError();
    }
}
