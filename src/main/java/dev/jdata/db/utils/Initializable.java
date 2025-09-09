package dev.jdata.db.utils;

import java.util.Objects;

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
}
