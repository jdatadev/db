package dev.jdata.db.utils.instances;

import java.io.IOException;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class InstancesTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testAreAnyNotNull() {

        assertThat(Instances.areAnyNotNull(null, null)).isFalse();
        assertThat(Instances.areAnyNotNull(null, new Object())).isTrue();
        assertThat(Instances.areAnyNotNull(new Object(), null)).isTrue();
        assertThat(Instances.areAnyNotNull(new Object(), new Object())).isTrue();
    }

    @Test
    @Category(UnitTest.class)
    public void testAreBothNotNullOrBothNullOrThrowException() {

        checkAreBothNotNullOrBothNullOrThrowException(IllegalStateException.class,
                (o1, o2) -> Instances.areBothNotNullOrBothNullOrThrowException(o1, o2));
    }

    @Test
    @Category(UnitTest.class)
    public void testAreBothNotNullOrBothNullOrThrowExceptionWithSupplier() throws IOException {

        assertThatThrownBy(() -> Instances.areBothNotNullOrBothNullOrThrowException(null, null, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Instances.areBothNotNullOrBothNullOrThrowException(null, new Object(), null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Instances.areBothNotNullOrBothNullOrThrowException(new Object(), null, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Instances.areBothNotNullOrBothNullOrThrowException(new Object(), new Object(), null)).isInstanceOf(NullPointerException.class);

        checkAreBothNotNullOrBothNullOrThrowException(IllegalArgumentException.class,
                (o1, o2) -> Instances.areBothNotNullOrBothNullOrThrowException(o1, o2, IllegalArgumentException::new));

        checkAreBothNotNullOrBothNullOrThrowException(IOException.class,
                (o1, o2) -> Instances.areBothNotNullOrBothNullOrThrowException(o1, o2, IOException::new));
    }

    @FunctionalInterface
    private interface CheckAreBothNotNullOrBothNull<E extends Exception> {

        boolean check(Object instance1, Object instance2) throws E;
    }

    private static <E extends Exception> void checkAreBothNotNullOrBothNullOrThrowException(Class<E> exceptionClass, CheckAreBothNotNullOrBothNull<E> delegate) throws E {

        assertThat(delegate.check(new Object(), new Object())).isTrue();
        assertThatThrownBy(() -> delegate.check(new Object(), null)).isInstanceOf(exceptionClass);
        assertThatThrownBy(() -> delegate.check(null, new Object())).isInstanceOf(exceptionClass);
        assertThat(delegate.check(null, null)).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public void testAreAllNotNullOrAllNullOrThrowException() {

        checkAreAllNotNullOrAllNullOrThrowException(IllegalStateException.class, (o1, o2, o3) -> Instances.areAllNotNullOrAllNullOrThrowException(o1, o2, o3));
    }

    @Test
    @Category(UnitTest.class)
    public void testAreAllNotNullOrAllNullOrThrowExceptionWithSupplier() throws IOException {

        assertThatThrownBy(() -> Instances.areBothNotNullOrBothNullOrThrowException(null, null, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Instances.areBothNotNullOrBothNullOrThrowException(null, new Object(), null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Instances.areBothNotNullOrBothNullOrThrowException(new Object(), null, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Instances.areBothNotNullOrBothNullOrThrowException(new Object(), new Object(), null)).isInstanceOf(NullPointerException.class);

        checkAreAllNotNullOrAllNullOrThrowException(IllegalArgumentException.class,
                (o1, o2, o3) -> Instances.areAllNotNullOrAllNullOrThrowException(o1, o2, o3, IllegalArgumentException::new));

        checkAreAllNotNullOrAllNullOrThrowException(IOException.class,
                (o1, o2, o3) -> Instances.areAllNotNullOrAllNullOrThrowException(o1, o2, o3, IOException::new));
    }

    @FunctionalInterface
    private interface CheckAreAllNotNullOrAllNull<E extends Exception> {

        boolean check(Object instance1, Object instance2, Object instance3) throws E;
    }

    private static <E extends Exception> void checkAreAllNotNullOrAllNullOrThrowException(Class<E> exceptionClass, CheckAreAllNotNullOrAllNull<E> delegate) throws E {

        assertThat(delegate.check(new Object(), new Object(), new Object())).isTrue();

        assertThatThrownBy(() -> delegate.check(new Object(), null, null)).isInstanceOf(exceptionClass);
        assertThatThrownBy(() -> delegate.check(null, new Object(), null)).isInstanceOf(exceptionClass);
        assertThatThrownBy(() -> delegate.check(null, null, new Object())).isInstanceOf(exceptionClass);

        assertThatThrownBy(() -> delegate.check(new Object(), new Object(), null)).isInstanceOf(exceptionClass);
        assertThatThrownBy(() -> delegate.check(null, new Object(), new Object())).isInstanceOf(exceptionClass);
        assertThatThrownBy(() -> delegate.check(new Object(), null, new Object())).isInstanceOf(exceptionClass);

        assertThat(delegate.check(null, null, null)).isFalse();
    }
}
