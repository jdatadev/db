package dev.jdata.db.test.unit;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import dev.jdata.db.test.unit.assertj.Assertions;

public abstract class BaseTest extends Assertions {

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
}
