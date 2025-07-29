package dev.jdata.db.engine.database;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ObjLongConsumer;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

class StringStoreTestUtil extends BaseTest {

    @FunctionalInterface
    interface AdditionalTestsRunnable<T> {

        void run(T stringStore, String string, long expectedCharArrayIndex);
    }

    static <T, R> void checkStore(BiIntToObjectFunction<T> createStringStore, BiPredicate<T, String> containsPredicate, BiFunction<T, String, R> getOrAddString,
            ObjLongConsumer<R> checkGetOrAddResult, AdditionalTestsRunnable<T> additionalTestsRunnable) {

        Objects.requireNonNull(createStringStore);
        Objects.requireNonNull(containsPredicate);
        Objects.requireNonNull(getOrAddString);

        for (int initialCapacityExponent = 0; initialCapacityExponent < 8; ++ initialCapacityExponent) {

            final T stringStore = createStringStore.apply(1, initialCapacityExponent);

            final int numToAdd = 1000;

            long expectedCharArrayIndex = 0L;

            for (int i = 0; i < numToAdd; ++ i) {

                final String stringToAdd = "toAdd" + i;

                assertThat(containsPredicate.test(stringStore, stringToAdd)).isFalse();

                getOrAdd(stringStore, stringToAdd, expectedCharArrayIndex, getOrAddString, checkGetOrAddResult);
                getOrAdd(stringStore, stringToAdd, expectedCharArrayIndex, getOrAddString, checkGetOrAddResult);
                getOrAdd(stringStore, stringToAdd, expectedCharArrayIndex, getOrAddString, checkGetOrAddResult);

                if (checkGetOrAddResult != null) {

                    getOrAdd(stringStore, stringToAdd, expectedCharArrayIndex, getOrAddString, checkGetOrAddResult);

                    checkGetOrAddResult.accept(getOrAddString.apply(stringStore, stringToAdd), expectedCharArrayIndex);
                    checkGetOrAddResult.accept(getOrAddString.apply(stringStore, stringToAdd), expectedCharArrayIndex);
                    checkGetOrAddResult.accept(getOrAddString.apply(stringStore, stringToAdd), expectedCharArrayIndex);
                }

                assertThat(getOrAddString.apply(stringStore, stringToAdd)).isEqualTo(expectedCharArrayIndex);
                assertThat(getOrAddString.apply(stringStore, stringToAdd)).isEqualTo(expectedCharArrayIndex);
                assertThat(getOrAddString.apply(stringStore, stringToAdd)).isEqualTo(expectedCharArrayIndex);

                assertThat(containsPredicate.test(stringStore, stringToAdd)).isTrue();

                if (additionalTestsRunnable != null) {

                    additionalTestsRunnable.run(stringStore, stringToAdd, expectedCharArrayIndex);
                }

                expectedCharArrayIndex += stringToAdd.length() + 1;
            }
        }
    }

    private static <T, R> void getOrAdd(T stringStore, String stringToAdd, long expectedCharArrayIndex, BiFunction<T, String, R> getOrAddString,
            ObjLongConsumer<R> checkGetOrAddResult) {

        final R getOrAddResult = getOrAddString.apply(stringStore, stringToAdd);

        if (checkGetOrAddResult != null) {

            checkGetOrAddResult.accept(getOrAddResult, expectedCharArrayIndex);
        }
    }
}
