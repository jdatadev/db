package dev.jdata.db.test.unit.assertj;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

import dev.jdata.db.utils.classes.Classes;

public abstract class BaseAssert<S extends BaseAssert<S, A>, A> extends AbstractAssert<S, A> {

    protected static <S extends BaseAssert<S, A>, A> Class<S> castAssertClass(Class<?> assertClass) {

        Objects.requireNonNull(assertClass);

        return Classes.genericClass(assertClass);
    }

    BaseAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }

    protected final <T> void failWithActualExpected(T actual, T expected) {

        failWithActualExpectedAndMessage(actual, expected, "");
    }

    protected final <T> void failWithContains(T value) {

        failWithActualExpected(false, true);
    }

    protected final <T> void failWithDoesNotContain(T value) {

        failWithActualExpected(true, false);
    }

    protected final void failWithMessage(String message) {

        Objects.requireNonNull(message);

        super.failWithMessage(message);
    }

    @SuppressWarnings("unchecked")
    protected final S getThis() {

        return (S)this;
    }
}
