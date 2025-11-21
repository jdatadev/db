package dev.jdata.db.utils.adt.hashed;

import java.util.function.IntConsumer;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.ADTConstants;

public abstract class BaseHashedTest extends BaseTest {

    protected static final int MAX_ELEMENTS = 10 * 1000 * 1000;

    protected static void checkInitialCapacities(IntConsumer check) {

        final int max = ADTConstants.DEFAULT_HASHED_INITIAL_CAPACITY;
        final int step = max / 3;

        int i;

        for (i = 0; i <= max; i += step) {

            check.accept(i);
        }

        if (i != max) {

            check.accept(max);
        }
    }

    protected static void checkNumElements(IntConsumer check) {

        for (int numElements = 1; numElements < MAX_ELEMENTS; numElements *= 10) {

            check.accept(numElements);
        }
    }
}
