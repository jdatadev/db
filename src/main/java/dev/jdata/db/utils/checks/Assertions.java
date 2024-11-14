package dev.jdata.db.utils.checks;

public class Assertions {

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

    public static void areEqual(int value1, int value2) {

        if (value1 != value2) {

            throwAssertion();
        }
    }

    public static void areEqual(long value1, long value2) {

        if (value1 != value2) {

            throwAssertion();
        }
    }

    public static void isAboveZero(long value) {

        if (value < 1L) {

            throwAssertion();
        }
    }

    private static void throwAssertion() {

        throw new AssertionError();
    }
}
