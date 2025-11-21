package dev.jdata.db.utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntPredicate;

public class Initializable {

    public static boolean checkNotYetInitialized(boolean initialized) {

        if (initialized) {

            throw new IllegalStateException();
        }

        return true;
    }

    public static void checkIsInitialized(boolean initialized) {

        if (!initialized) {

            throw new IllegalStateException();
        }
    }

    public static boolean checkResettable(boolean initialized) {

        checkIsInitialized(initialized);

        return false;
    }

    public static <T> T checkNotYetInitialized(T existing, T value) {

        if (existing != null) {

            throw new IllegalStateException();
        }

        return Objects.requireNonNull(value);
    }

    public static <T> T checkNotYetInitializedToNull(T existing) {

        if (existing != null) {

            throw new IllegalStateException();
        }

        return null;
    }

    public static <T> T checkIsInitialized(T existing) {

        if (existing == null) {

            throw new IllegalStateException();
        }

        return existing;
    }

    public static <T> T checkResettable(T existing) {

        if (existing == null) {

            throw new IllegalStateException();
        }

        return null;
    }

    public static int checkNotYetInitialized(int existing, int value, int resetValue) {

        if (value == resetValue) {

            throw new IllegalArgumentException();
        }

        if (existing != resetValue) {

            throw new IllegalStateException();
        }

        return value;
    }

    public static int checkResettable(int existing, int resetValue) {

        if (existing == resetValue) {

            throw new IllegalStateException();
        }

        return resetValue;
    }

    public static int clearToResetValue(int resetValue) {

        return resetValue;
    }

    private static void checkNotYetInitialized(int[] array, int initialValue, int resetValue) {

        checkNotYetInitialized(array, resetValue);

        Arrays.fill(array, resetValue);
    }

    public static void checkNotYetInitialized(int[] array, int resetValue) {

        final int arrayLength = array.length;

        for (int i = 0; i < arrayLength; ++ i) {

            checkNotYetInitialized(array[i] != resetValue);
        }
    }

    public static void checkResettable(int[] array, int resetValue, IntPredicate checkInitializedPredicate) {

        final int arrayLength = array.length;

        for (int i = 0; i < arrayLength; ++ i) {

            checkIsInitialized(checkInitializedPredicate.test(array[i]));
        }

        clearToResetValue(array, resetValue);
    }

    public static void clearToResetValue(int[] array, int resetValue) {

        Arrays.fill(array, resetValue);
    }
}
