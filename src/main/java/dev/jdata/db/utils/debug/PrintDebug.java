package dev.jdata.db.utils.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.jdk.adt.strings.Strings;

public abstract class PrintDebug {

    protected static final class NameValue {

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

    protected static String getMethodName() {

        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        String methodName = null;

        final int numStackTraceElements = stackTraceElements.length;

        final String printDebugClassName = PrintDebug.class.getName();
        final String iprintDebugClassName = IPrintDebug.class.getName();

        for (int i = 1; i < numStackTraceElements; ++ i) {

            final StackTraceElement stackTraceElement = stackTraceElements[i];

            final String className = stackTraceElement.getClassName();

            if (!className.equals(printDebugClassName) && !className.equals(iprintDebugClassName)) {

                methodName = stackTraceElement.getMethodName();
                break;
            }
        }

        return methodName;
    }

    protected final PrintDebug enter() {

        enter(getClass());

        return this;
    }

    protected final PrintDebug enter(String message) {

        enter(getClass(), message);

        return this;
    }

    protected final PrintDebug enter(Consumer<NameValueBuilder> consumer) {

        enter(getClass(), consumer);

        return this;
    }

    protected final PrintDebug exit() {

        exit(getClass());

        return this;
    }

    protected final PrintDebug exit(Object result) {

        exit(getClass(), result);

        return this;
    }

    protected final PrintDebug exitWithBinary(byte result) {

        exitWithBinary(getClass(), result);

        return this;
    }

    protected final PrintDebug exitWithBinary(short result) {

        exitWithBinary(getClass(), result);

        return this;
    }

    protected final PrintDebug exitWithBinary(int result) {

        exitWithBinary(getClass(), result);

        return this;
    }

    protected final PrintDebug exitWithBinary(long result) {

        exitWithBinary(getClass(), result);

        return this;
    }

    protected final PrintDebug exitWithHex(long result) {

        exitWithHex(getClass(), result);

        return this;
    }

    protected final PrintDebug exit(Consumer<NameValueBuilder> consumer) {

        exit(getClass(), consumer);

        return this;
    }

    protected final PrintDebug exit(Object result, Consumer<NameValueBuilder> consumer) {

        exit(getClass(), result, consumer);

        return this;
    }

    protected final PrintDebug exitWithBinary(byte result, Consumer<NameValueBuilder> consumer) {

        exitWithBinary(getClass(), result, consumer);

        return this;
    }

    protected final PrintDebug exitWithBinary(short result, Consumer<NameValueBuilder> consumer) {

        exitWithBinary(getClass(), result, consumer);

        return this;
    }

    protected final PrintDebug exitWithBinary(int result, Consumer<NameValueBuilder> consumer) {

        exitWithBinary(getClass(), result, consumer);

        return this;
    }

    protected final PrintDebug exitWithBinary(long result, Consumer<NameValueBuilder> consumer) {

        exitWithBinary(getClass(), result, consumer);

        return this;
    }

    protected final PrintDebug exitWithHex(byte result, Consumer<NameValueBuilder> consumer) {

        exitWithHex(getClass(), result, consumer);

        return this;
    }

    protected final PrintDebug exitWithHex(short result, Consumer<NameValueBuilder> consumer) {

        exitWithHex(getClass(), result, consumer);

        return this;
    }

    protected final PrintDebug exitWithHex(int result, Consumer<NameValueBuilder> consumer) {

        exitWithHex(getClass(), result, consumer);

        return this;
    }

    protected final PrintDebug exitWithHex(long result, Consumer<NameValueBuilder> consumer) {

        exitWithHex(getClass(), result, consumer);

        return this;
    }

    protected final PrintDebug debug(String message) {

        debug(getClass(), message);

        return this;
    }

    protected final PrintDebug debugFormatln(String format, Object ... parameters) {

        debugFormatln(getClass(), format, parameters);

        return this;
    }

    protected final PrintDebug debug(Consumer<NameValueBuilder> consumer) {

        debug(getClass(), consumer);

        return this;
    }

    protected final PrintDebug debug(String message, Consumer<NameValueBuilder> consumer) {

        debug(getClass(), message, consumer);

        return this;
    }

    protected final PrintDebug println(String message) {

        println(getClass(), message);

        return this;
    }

    protected final PrintDebug formatln(String format, Object ... parameters) {

        formatln(getClass(), format, parameters);

        return this;
    }

    protected static void enter(Class<?> javaClass) {

        enter(javaClass, null, null);
    }

    protected static void enter(Class<?> javaClass, String message) {

        enter(javaClass, message, null);
    }

    protected static void enter(Class<?> javaClass, Consumer<NameValueBuilder> consumer) {

        enter(javaClass, null, consumer);
    }

    protected static void enter(Class<?> javaClass, String message, Consumer<NameValueBuilder> consumer) {

        println(javaClass, "ENTER " + getMethodName() + (message != null ? ' ' + message : "") + (consumer != null ? ' ' + NameValueBuilder.build(consumer) : ""));
    }

    protected static void exit(Class<?> javaClass) {

        exit(javaClass, null, false, null);
    }

    protected static void exit(Class<?> javaClass, Object result) {

        exit(javaClass, result, true, null);
    }

    protected static void exitWithBinary(Class<?> javaClass, byte result) {

        exit(javaClass, binaryString(result));
    }

    protected static void exitWithBinary(Class<?> javaClass, short result) {

        exit(javaClass, binaryString(result));
    }

    protected static void exitWithBinary(Class<?> javaClass, int result) {

        exit(javaClass, binaryString(result));
    }

    protected static void exitWithBinary(Class<?> javaClass, long result) {

        exit(javaClass, binaryString(result));
    }

    protected static void exitWithHex(Class<?> javaClass, long result) {

        exit(javaClass, hexString(result));
    }

    protected static void exit(Class<?> javaClass, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, null, false, consumer);
    }

    protected static void exit(Class<?> javaClass, Object result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, result, true, consumer);
    }

    protected static void exit(Class<?> javaClass, Object result, boolean hasResult, Consumer<NameValueBuilder> consumer) {

        println(javaClass, "EXIT " + getMethodName() + (hasResult ? "=" + result : "") + (consumer != null ? ' '+ NameValueBuilder.build(consumer) : ""));
    }

    protected static void exitWithBinary(Class<?> javaClass, byte result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, binaryString(result), consumer);
    }

    protected static void exitWithBinary(Class<?> javaClass, short result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, binaryString(result), consumer);
    }

    protected static void exitWithBinary(Class<?> javaClass, int result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, binaryString(result), consumer);
    }

    protected static void exitWithBinary(Class<?> javaClass, long result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, binaryString(result), consumer);
    }

    protected static void exitWithHex(Class<?> javaClass, byte result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, hexString(result), consumer);
    }

    protected static void exitWithHex(Class<?> javaClass, short result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, hexString(result), consumer);
    }

    protected static void exitWithHex(Class<?> javaClass, int result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, hexString(result), consumer);
    }

    protected static void exitWithHex(Class<?> javaClass, long result, Consumer<NameValueBuilder> consumer) {

        exit(javaClass, hexString(result), consumer);
    }

    protected static void debug(Class<?> javaClass, String message) {

        println(javaClass, "DEBUG " + getMethodName() + ' ' + message);
    }

    protected static void debugFormatln(Class<?> javaClass, String format, Object ... parameters) {

        debug(javaClass, String.format(format, parameters));
    }

    protected static void debug(Class<?> javaClass, Consumer<NameValueBuilder> consumer) {

        debug(javaClass, NameValueBuilder.build(consumer));
    }

    protected static void debug(Class<?> javaClass, String message, Consumer<NameValueBuilder> consumer) {

        debug(javaClass, message + ' ' + NameValueBuilder.build(consumer));
    }

    protected static void println(Class<?> javaClass, String message) {

        Objects.requireNonNull(javaClass);
        Objects.requireNonNull(message);

        System.out.println(javaClass.getSimpleName() + ' ' + message);
    }

    protected static void formatln(Class<?> javaClass, String format, Object ... parameters) {

        Objects.requireNonNull(javaClass);
        Objects.requireNonNull(format);

        println(javaClass, String.format(format, parameters));
    }

    protected static String binaryString(byte value) {

        return Strings.binaryString(value, true);
    }

    protected static String binaryString(short value) {

        return Strings.binaryString(value, true);
    }

    protected static String binaryString(int value) {

        return Strings.binaryString(value, true);
    }

    protected static String binaryString(long value) {

        return Strings.binaryString(value, true);
    }

    protected static String hexString(long value) {

        return Strings.hexString(value, true);
    }
}
