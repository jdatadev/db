package dev.jdata.db.utils.instances;

import java.util.Objects;
import java.util.function.Supplier;

public class Instances {

    public static boolean areAnyNotNull(Object instance1, Object instance2) {

        return instance1 != null || instance2 != null;
    }

    public static boolean areBothNotNullOrBothNullOrThrowException(Object instance1, Object instance2) {

        return areBothNotNullOrBothNullOrThrowException(instance1, instance2, Instances::makeException);
    }

    public static <E extends Exception> boolean areBothNotNullOrBothNullOrThrowException(Object instance1, Object instance2, Supplier<E> exceptionSupplier) throws E {

        Objects.requireNonNull(exceptionSupplier);

        final boolean result;

        if (instance1 != null) {

            if (instance2 != null) {

                result = true;
            }
            else {
                throw exceptionSupplier.get();
            }
        }
        else {
            if (instance2 == null) {

                result = false;
            }
            else {
                throw exceptionSupplier.get();
            }
        }

        return result;
    }

    public static <E extends Exception> boolean areAllNotNullOrAllNullOrThrowException(Object instance1, Object instance2, Object instance3) throws E {

        return areAllNotNullOrAllNullOrThrowException(instance1, instance2, instance3, Instances::makeException);
    }

    public static <E extends Exception> boolean areAllNotNullOrAllNullOrThrowException(Object instance1, Object instance2, Object instance3, Supplier<E> exceptionSupplier)
            throws E {

        Objects.requireNonNull(exceptionSupplier);

        final boolean result;

        if (instance1 != null) {

            if (instance2 != null) {

                if (instance3 != null) {

                    result = true;
                }
                else {
                    throw exceptionSupplier.get();
                }
            }
            else {
                throw exceptionSupplier.get();
            }
        }
        else {
            if (instance2 == null) {

                if (instance3 == null) {

                    result = false;
                }
                else {
                    throw exceptionSupplier.get();
                }
            }
            else {
                throw exceptionSupplier.get();
            }
        }

        return result;
    }

    private static IllegalStateException makeException() {

        return new IllegalStateException();
    }
}
