package dev.jdata.db.utils.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public interface PrintDebug {

    public static final class NameValue {

        private final String name;
        private final Object value;

        NameValue(String name, Object parameterValue) {

            this.name = Objects.requireNonNull(name);
            this.value = Objects.requireNonNull(parameterValue);
        }
    }

    public static final class NameValueBuilder {

        private final List<NameValue> nameValues;

        NameValueBuilder() {

            this.nameValues = new ArrayList<>();
        }

        public NameValueBuilder add(String name, Object value) {

            nameValues.add(new NameValue(name, value));

            return this;
        }

        void build(StringBuilder sb) {

            for (NameValue nameValue : nameValues) {

                sb.append(' ').append(nameValue.name).append('=').append(nameValue.value);
            }
        }

        static String build(Consumer<NameValueBuilder> consumer) {

            final NameValueBuilder parameterBuilder = new NameValueBuilder();

            consumer.accept(parameterBuilder);

            final StringBuilder sb = new StringBuilder();

            parameterBuilder.build(sb);

            return sb.toString();
        }
    }

    public static String getMethodName() {

        return Thread.currentThread().getStackTrace()[4].getMethodName();
    }

    default PrintDebug enter() {

        enter(getClass());

        return this;
    }

    default PrintDebug enter(String message) {

        enter(getClass(), message);

        return this;
    }

    default PrintDebug enter(Consumer<NameValueBuilder> consumer) {

        enter(getClass(), consumer);

        return this;
    }

    default PrintDebug exit() {

        exit(getClass());

        return this;
    }

    default PrintDebug exit(Object result) {

        exit(getClass(), result);

        return this;
    }

    default PrintDebug exit(Consumer<NameValueBuilder> consumer) {

        exit(getClass(), consumer);

        return this;
    }

    default PrintDebug exit(Object result, Consumer<NameValueBuilder> consumer) {

        exit(getClass(), result, consumer);

        return this;
    }

    default PrintDebug debug(String message) {

        debug(getClass(), message);

        return this;
    }

    default PrintDebug debugFormatln(String format, Object ... parameters) {

        debugFormatln(getClass(), format, parameters);

        return this;
    }

    default PrintDebug debug(Consumer<NameValueBuilder> consumer) {

        debug(getClass(), consumer);

        return this;
    }

    default PrintDebug debug(String message, Consumer<NameValueBuilder> consumer) {

        debug(getClass(), message, consumer);

        return this;
    }

    default PrintDebug println(String message) {

        println(getClass(), message);

        return this;
    }

    default PrintDebug formatln(String format, Object ... parameters) {

        formatln(getClass(), format, parameters);

        return this;
    }

    public static void enter(Class<?> javaClass) {

        println(javaClass, "ENTER " + getMethodName());
    }

    public static void enter(Class<?> javaClass, String message) {

        println(javaClass, "ENTER " + getMethodName() + ' ' + message);
    }

    public static void enter(Class<?> javaClass, Consumer<NameValueBuilder> consumer) {

        println(javaClass, "ENTER " + getMethodName() + NameValueBuilder.build(consumer));
    }

    public static void exit(Class<?> javaClass) {

        println(javaClass, "EXIT " + getMethodName());
    }

    public static void exit(Class<?> javaClass, Object result) {

        println(javaClass, "EXIT " + getMethodName() + '=' + result);
    }

    public static void exit(Class<?> javaClass, Consumer<NameValueBuilder> consumer) {

        println(javaClass, "EXIT " + getMethodName() + NameValueBuilder.build(consumer));
    }

    public static void exit(Class<?> javaClass, Object result, Consumer<NameValueBuilder> consumer) {

        println(javaClass, "EXIT " + getMethodName() + '=' + result + NameValueBuilder.build(consumer));
    }

    public static void debug(Class<?> javaClass, String message) {

        println(javaClass, "DEBUG " + getMethodName() + ' ' + message);
    }

    public static void debugFormatln(Class<?> javaClass, String format, Object ... parameters) {

        println(javaClass, "DEBUG " + getMethodName() + ' ' + String.format(format, parameters));
    }

    public static void debug(Class<?> javaClass, Consumer<NameValueBuilder> consumer) {

        println(javaClass, "DEBUG " + getMethodName() + NameValueBuilder.build(consumer));
    }

    public static void debug(Class<?> javaClass, String message, Consumer<NameValueBuilder> consumer) {

        println(javaClass, "DEBUG " + getMethodName() + message + NameValueBuilder.build(consumer));
    }

    public static void println(Class<?> javaClass, String message) {

        Objects.requireNonNull(javaClass);
        Objects.requireNonNull(message);

        System.out.println(javaClass.getSimpleName() + ' ' + message);
    }

    public static void formatln(Class<?> javaClass, String format, Object ... parameters) {

        Objects.requireNonNull(javaClass);
        Objects.requireNonNull(format);

        println(javaClass, String.format(format, parameters));
    }
}
