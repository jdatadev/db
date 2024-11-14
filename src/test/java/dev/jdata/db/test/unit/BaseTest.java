package dev.jdata.db.test.unit;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import dev.jdata.db.test.unit.assertj.CustomAssertJAssertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.file.access.RelativeFilePath;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseTest extends CustomAssertJAssertions {

    public interface UnitTest {

    }

    protected static <T, C extends Collection<T>> C checkUnmodifiable(C collection, T element) {

        Objects.requireNonNull(element);

        assertThatThrownBy(() -> collection.add(element)).isInstanceOf(UnsupportedOperationException.class);

        return collection;
    }

    protected static <T, C extends Collection<T>> C checkUnmodifiableEmptyIsEmptyList(C collection, T element) {

        Objects.requireNonNull(element);

        if (collection.isEmpty()) {

            assertThat(collection).isSameAs(Collections.emptyList());
        }
        else {
            assertThatThrownBy(() -> collection.add(element)).isInstanceOf(UnsupportedOperationException.class);
        }

        return collection;
    }

    protected static <T, C extends Collection<T>> C checkUnmodifiableEmptyIsEmptySet(C collection, T element) {

        Objects.requireNonNull(element);

        if (collection.isEmpty()) {

            assertThat(collection).isSameAs(Collections.emptySet());
        }
        else {
            assertThatThrownBy(() -> collection.add(element)).isInstanceOf(UnsupportedOperationException.class);
        }

        return collection;
    }

    protected static <T> void checkModifiable(Collection<T> collection, T value) {

        Objects.requireNonNull(collection);
        Objects.requireNonNull(value);

        assertThat(collection).doesNotContain(value);

        collection.add(value);

        assertThat(collection).contains(value);
    }

    protected static byte[] byteArray(int ... values) {

        final int length = values.length;

        final byte[] result = new byte[length];

        for (int i = 0; i < length; ++ i) {

            result[i] = Integers.checkUnsignedIntToUnsignedByte(values[i]);
        }

        return result;
    }

    protected static RelativeFilePath makeTestFilePath(String fileName) {

        Checks.isFileName(fileName);

        return RelativeFilePath.of(fileName);
    }
}
