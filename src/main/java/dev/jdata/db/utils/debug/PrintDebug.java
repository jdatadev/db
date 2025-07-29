package dev.jdata.db.utils.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import dev.jdata.db.utils.adt.strings.Strings;
import dev.jdata.db.utils.checks.Checks;

public interface PrintDebug {

    public static final class NameValue {

        private final String name;
        private final Object value;

        NameValue(String name) {
            this(name, "<null>");
        }

        NameValue(String name, Object parameterValue) {

            this.name = Checks.isJavaVariableOrMethodWithoutParameters(name);
            this.value = Objects.requireNonNull(parameterValue);
        }
    }

    public static final class NameValueBuilder {

        private final List<NameValue> nameValues;

        NameValueBuilder() {

            this.nameValues = new ArrayList<>();
        }

        public NameValueBuilder add(String name, Object value) {

            nameValues.add(value != null ? new NameValue(name, value) : new NameValue(name));

            return this;
        }

        public NameValueBuilder format(String name, String format, Object value) {

            nameValues.add(new NameValue(name, String.format(format, value)));

            return this;
        }

        public NameValueBuilder binary(String name, byte value) {

            nameValues.add(new NameValue(name, binaryString(value)));

            return this;
        }

        public NameValueBuilder binary(String name, short value) {

            nameValues.add(new NameValue(name, binaryString(value)));

            return this;
        }

        public NameValueBuilder binary(String name, int value) {

            nameValues.add(new NameValue(name, binaryString(value)));

            return this;
        }

        public NameValueBuilder binary(String name, long value) {

            nameValues.add(new NameValue(name, binaryString(value)));

            return this;
        }

        public NameValueBuilder hex(String name, long value) {

            nameValues.add(new NameValue(name, hexString(value)));

            return this;
        }

        void build(StringBuilder sb) {

            Strings.join(nameValues, ' ', sb, (v, b) -> b.append(v.name).append('=').append(v.value));
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

        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        String methodName = null;

        final int numStackTraceElements = stackTraceElements.length;

        for (int i = 1; i < numStackTraceElements; ++ i) {

            final StackTraceElement stackTraceElement = stackTraceElements[i];

            if (!stackTraceElement.getClassName().equals(PrintDebug.class.getName())) {

                methodName = stackTraceElement.getMethodName();
                break;
            }
        }

        return methodName;
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

    default PrintDebug exitWithBinary(byte result) {

        exitWithBinary(getClass(), result);

        return this;
    }

    default PrintDebug exitWithBinary(short result) {

        exitWithBinary(getClass(), result);

        return this;
    }

    default PrintDebug exitWithBinary(int result) {

        exitWithBinary(getClass(), result);

        return this;
    }

    default PrintDebug exitWithBinary(long result) {

        exitWithBinary(getClass(), result);

        return this;
    }

    default PrintDebug exitWithHex(long result) {

        exitWithHex(getClass(), result);

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

    default PrintDebug exitWithBinary(byte result, Consumer<NameValueBuilder> consumer) {

        exitWithBinary(getClass(), result, consumer);

        return this;
    }

    default PrintDebug exitWithBinary(short result, Consumer<NameValueBuilder> consumer) {

        exitWithBinary(getClass(), result, consumer);

        return this;
    }

    default PrintDebug exitWithBinary(int result, Consumer<NameValueBuilder> consumer) {

        exitWithBinary(getClass(), result, consumer);

        return this;
    }

    default PrintDebug exitWithBinary(long result, Consumer<NameValueBuilder> consumer) {

        exitWithBinary(getClass(), result, consumer);

        return this;
    }

    default PrintDebug exitWithHex(byte result, Consumer<NameValueBuilder> consumer) {

        exitWithHex(getClass(), result, consumer);

        return this;
    }

    default PrintDebug exitWithHex(short result, Consumer<NameValueBuilder> consumer) {

        exitWithHex(getClass(), result, consumer);

        return this;
    }

    default PrintDebug exitWithHex(int result, Consumer<NameValueBuilder> consumer) {

        exitWithHex(getClass(), result, consumer);

        return this;
    }

    default PrintDebug exitWithHex(long result, Consumer<NameValueBuilder> consumer) {

        exitWithHex(getClass(), result, consumer);

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

        enter(javaClass, null, null);
    }

    public static void enter(Class<?> javaClass, String message) {

        enter(javaClass, message, null);
    }

    public static void enter(Class<?> javaClass, Consumer<NameValueBuilder> consumer) {

        enter(javaClass, null, consumer);
    }

    public static void enter(Class<?> javaClass, String message, Consumer<NameValueBuilder> consumer) {

        println(javaClass, "ENTER " + getMethodName() + (message != null ? ' ' + message : "") + (consumer != null ? ' ' + NameValueBuilder.build(consumer) : ""));
    }

    public static void exit(Class<?> javaClass) {

        exit(javaClass, null, false, null);
    }

    public static void exit(Class<?> javaClass, Object result) {

        exit(javaClass, result, true, null);
    }

    public static void exitWithBinary(Class<?> javaClass, byte result) {

        exit(javaClass, binaryString(result));
    }

    public static void exitWithBinary(Class<?> javaClass, short result) {

        exit(javaClass, binaryString(result));
    }

    public static void exitWithBinary(Class<?> javaClass, int result) {

        exit(javaClass, binaryString(result));
    }

    public static void exitWithBinary(Class<?> javaClass, long result) {

        exit(javaClass, binaryString(result));
    }

    public static void exitWithHex(Class<?> javaClass, long result) {

        exit(javaClass, hexString(result));
    }

    public static void exit(Class<?> javaClass, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, null, false, consumer);
    }

    public static void exit(Class<?> javaClass, Object result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, result, true, consumer);
    }

    public static void exit(Class<?> javaClass, Object result, boolean hasResult, Consumer<NameValueBuilder> consumer) {

        println(javaClass, "EXIT " + getMethodName() + (hasResult ? "=" + result : "") + (consumer != null ? ' '+ NameValueBuilder.build(consumer) : ""));
    }

    public static void exitWithBinary(Class<?> javaClass, byte result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, binaryString(result), consumer);
    }

    public static void exitWithBinary(Class<?> javaClass, short result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, binaryString(result), consumer);
    }

    public static void exitWithBinary(Class<?> javaClass, int result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, binaryString(result), consumer);
    }

    public static void exitWithBinary(Class<?> javaClass, long result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, binaryString(result), consumer);
    }

    public static void exitWithHex(Class<?> javaClass, byte result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, hexString(result), consumer);
    }

    public static void exitWithHex(Class<?> javaClass, short result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, hexString(result), consumer);
    }

    public static void exitWithHex(Class<?> javaClass, int result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, hexString(result), consumer);
    }

    public static void exitWithHex(Class<?> javaClass, long result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, hexString(result), consumer);
    }

    public static void debug(Class<?> javaClass, String message) {

        println(javaClass, "DEBUG " + getMethodName() + ' ' + message);
    }

    public static void debugFormatln(Class<?> javaClass, String format, Object ... parameters) {

        debug(javaClass, String.format(format, parameters));
    }

    public static void debug(Class<?> javaClass, Consumer<NameValueBuilder> consumer) {

        debug(javaClass, NameValueBuilder.build(consumer));
    }

    public static void debug(Class<?> javaClass, String message, Consumer<NameValueBuilder> consumer) {

        debug(javaClass, message + ' ' + NameValueBuilder.build(consumer));
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

    public static String binaryString(byte value) {

        return Strings.binaryString(value, true);
    }

    public static String binaryString(short value) {

        return Strings.binaryString(value, true);
    }

    public static String binaryString(int value) {

        return Strings.binaryString(value, true);
    }

    public static String binaryString(long value) {

        return Strings.binaryString(value, true);
    }

    public static String hexString(long value) {

        return Strings.hexString(value, true);
    }
}
