package dev.jdata.db.utils.checks;

import dev.jdata.db.utils.adt.arrays.IOneDimensionalArrayView;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

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

    public static void areEqual(boolean value1, boolean value2) {

        if (value1 != value2) {

            throwAssertion();
        }
    }

    public static void isNull(Object object) {

        if (object != null) {

            throwAssertion();
        }
    }

    public static void isNotNull(Object object) {

        if (object == null) {

            throwAssertion();
        }
    }

    public static void isLessThan(long value1, long value2) {

        if (value1 >= value2) {

            throwAssertion();
        }
    }

    public static void isLessThanOrEqualTo(long value1, long value2) {

        if (value1 > value2) {

            throwAssertion();
        }
    }

    public static void isGreaterThan(long value1, long value2) {

        if (value1 <= value2) {

            throwAssertion();
        }
    }

    public static void isGreaterThanOrEqualTo(long value1, long value2) {

        if (value1 < value2) {

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

    public static void isEmpty(IContainsView contains) {

        if (!contains.isEmpty()) {

            throwAssertion();
        }
    }

    public static void areSameNumElements(IOnlyElementsView elements1, IOnlyElementsView elements2) {

        if (elements1.getNumElements() != elements2.getNumElements()) {

            throwAssertion();
        }
    }

    public static void isSameLimit(IOneDimensionalArrayView array1, IOneDimensionalArrayView array2) {

        if (array1.getLimit() != array2.getLimit()) {

            throwAssertion();
        }
    }

    private static void throwAssertion() {

        throw new AssertionError();
    }
}
