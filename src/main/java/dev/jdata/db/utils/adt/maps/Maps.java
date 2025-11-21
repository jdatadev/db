package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.adt.elements.IElementsView;
import dev.jdata.db.utils.checks.Checks;

public class Maps {

    @FunctionalInterface
    public interface IIntForEachAppend<T, P> {

        void each(int key, T value, StringBuilder sb, P parameter);
    }

    @FunctionalInterface
    public interface IIntToObjectMapForEachCaller<T, P> {

        void allForEach(StringBuilder sb, P parameter, IIntForEachAppend<T, P> forEachAppend);
    }

    @FunctionalInterface
    public interface ILongToObjectMapForEachCaller<T, P> {

        void allForEach(StringBuilder sb, P parameter, ILongForEachAppend<T, P> forEachAppend);
    }

    @FunctionalInterface
    public interface IObjectToObjectMapForEachCaller<K, V, P> {

        void allForEach(StringBuilder sb, P parameter, IObjectForEachAppend<K, V, P> forEachAppend);
    }

    public static <T, P> String intToObjectMapToString(String prefix, long numElements, P parameter, IIntToObjectMapForEachCaller<T, P> forEachCaller) {

        Checks.isNumElements(numElements);
        Objects.requireNonNull(forEachCaller);

        final StringBuilder sb = createStringBuilder(numElements);

        intToObjectMapToString(prefix, sb, parameter, forEachCaller);

        return sb.toString();
    }

    private static <T, P> void intToObjectMapToString(String prefix, StringBuilder sb, P parameter, IIntToObjectMapForEachCaller<T, P> forEachCaller) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(forEachCaller);

        final ILongToObjectMapForEachCaller<T, P> longToObjectMapForEachCaller = (b, p, f)
                -> forEachCaller.allForEach(b, p, (key, value, forEachStringBuilder, forEachParameter) -> f.each(key, value, forEachStringBuilder, forEachParameter));

        longToObjectMapToString(prefix, sb, parameter, longToObjectMapForEachCaller);
    }

    public static <T, P> String longToObjectMapToString(String prefix, long numElements, P parameter, ILongToObjectMapForEachCaller<T, P> forEachCaller) {

        Checks.isNumElements(numElements);
        Objects.requireNonNull(forEachCaller);

        final StringBuilder sb = createStringBuilder(numElements);

        longToObjectMapToStringEach(prefix, sb, parameter, forEachCaller);

        return sb.toString();
    }

    private static <T, P> void longToObjectMapToStringEach(String prefix, StringBuilder sb, P parameter, ILongToObjectMapForEachCaller<T, P> forEachCaller) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(forEachCaller);

        final ILongForEachAppendCaller<T, P> forEachAppendCaller = (StringBuilder b, P p, ILongForEachAppend<T, P> f) -> {

            forEachCaller.allForEach(sb, p, (long key, T value, StringBuilder forEachStringBuilder, P forEachParameter) -> {

                f.each(key, value, forEachStringBuilder, forEachParameter);
            });
        };

        final ILongAppendEachValue<T, P> appendEach = (k, v, b, p) -> b.append(v);

        longAppendToString(prefix, sb, parameter, forEachAppendCaller, appendEach);
    }

    private static <T, P> void longToObjectMapToString(String prefix, StringBuilder sb, P parameter, ILongToObjectMapForEachCaller<T, P> forEachCaller) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(forEachCaller);

        final ILongForEachAppendCaller<T, P> forEachAppendCaller = (StringBuilder b, P p, ILongForEachAppend<T, P> f) -> {

            forEachCaller.allForEach(sb, p, (long key, T value, StringBuilder forEachStringBuilder, P forEachParameter) -> {

                f.each(key, value, forEachStringBuilder, forEachParameter);
            });
        };

        final ILongAppendEachValue<T, P> appendEach = (k, v, b, p) -> b.append(v);

        longAppendToString(prefix, sb, parameter, forEachAppendCaller, appendEach);
    }

    public static <K, V, P> String objectToObjectMapToString(String prefix, long numElements, P parameter, IObjectToObjectMapForEachCaller<K, V, P> forEachCaller) {

        Checks.isNumElements(numElements);
        Objects.requireNonNull(forEachCaller);

        final StringBuilder sb = createStringBuilder(numElements);

        objectToObjectMapToString(prefix, sb, parameter, forEachCaller);

        return sb.toString();
    }

    private static <K, V, P> void objectToObjectMapToString(String prefix, StringBuilder sb, P parameter, IObjectToObjectMapForEachCaller<K, V, P> forEachCaller) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(forEachCaller);

        final IObjectForEachAppendCaller<K, V, P> forEachAppendCaller = (StringBuilder b, P p, IObjectForEachAppend<K, V, P> f) -> {

            forEachCaller.allForEach(sb, p, (K key, V value, StringBuilder forEachStringBuilder, P forEachParameter) -> {

                f.each(key, value, forEachStringBuilder, forEachParameter);
            });
        };

        final IObjectAppendEachValue<K, V, P> appendEach = (k, v, b, p) -> b.append(v);

        objectAppendToString(prefix, sb, parameter, forEachAppendCaller, appendEach);
    }

    @FunctionalInterface
    public interface ILongForEachAppend<T, P> {

        void each(long key, T value, StringBuilder sb, P parameter);
    }

    @FunctionalInterface
    public interface ILongForEachAppendCaller<T, P> {

        void allForEach(StringBuilder sb, P parameter, ILongForEachAppend<T, P> forEachAppend);
    }

    @FunctionalInterface
    public interface ILongAppendEachValue<T, P> {

        void append(long key, T value, StringBuilder sb, P parameter);
    }

    public static <T, P> String longAppendToString(String prefix, long numElements, P parameter, ILongForEachAppendCaller<T, P> forEachAppendCaller,
            ILongAppendEachValue<T, P> appendEachValue) {

        Checks.isNumElements(numElements);
        Objects.requireNonNull(forEachAppendCaller);
        Objects.requireNonNull(appendEachValue);

        final StringBuilder sb = createStringBuilder(numElements);

        longAppendToString(prefix, sb, parameter, forEachAppendCaller, appendEachValue);

        return sb.toString();
    }

    public static <T, P> void longAppendToString(String prefix, StringBuilder sb, P parameter, ILongForEachAppendCaller<T, P> forEachAppendCaller,
            ILongAppendEachValue<T, P> appendEachValue) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(forEachAppendCaller);
        Objects.requireNonNull(appendEachValue);

        if (prefix != null) {

            Checks.isNotEmpty(prefix);

            sb.append(prefix).append(' ');
        }

        sb.append('{');

        final int prefixLength = sb.length();

        forEachAppendCaller.allForEach(sb, parameter, (k, v, b, p) -> {

            if (b.length() > prefixLength) {

                b.append(',');
            }

            b.append(k).append('=');

            appendEachValue.append(k, v, b, p);
        });

        sb.append('}');
    }

    @FunctionalInterface
    public interface IObjectForEachAppend<K, V, P> {

        void each(K key, V value, StringBuilder sb, P parameter);
    }

    @FunctionalInterface
    public interface IObjectForEachAppendCaller<K, V, P> {

        void allForEach(StringBuilder sb, P parameter, IObjectForEachAppend<K, V, P> forEachAppend);
    }

    @FunctionalInterface
    public interface IObjectAppendEachValue<K, V, P> {

        void append(K key, V value, StringBuilder sb, P parameter);
    }

    public static <K, V, P> String objectAppendToString(String prefix, long numElements, P parameter, IObjectForEachAppendCaller<K, V, P> forEachAppendCaller,
            IObjectAppendEachValue<K, V, P> appendEachValue) {

        Checks.isNumElements(numElements);
        Objects.requireNonNull(forEachAppendCaller);
        Objects.requireNonNull(appendEachValue);

        final StringBuilder sb = createStringBuilder(numElements);

        objectAppendToString(prefix, sb, parameter, forEachAppendCaller, appendEachValue);

        return sb.toString();
    }

    private static <K, V, P> void objectAppendToString(String prefix, StringBuilder sb, P parameter, IObjectForEachAppendCaller<K, V, P> forEachAppendCaller,
            IObjectAppendEachValue<K, V, P> appendEachValue) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(forEachAppendCaller);
        Objects.requireNonNull(appendEachValue);

        if (prefix != null) {

            Checks.isNotEmpty(prefix);

            sb.append(prefix).append(' ');
        }

        sb.append('{');

        final int prefixLength = sb.length();

        forEachAppendCaller.allForEach(sb, parameter, (k, v, b, p) -> {

            if (b.length() > prefixLength) {

                b.append(',');
            }

            b.append(k).append('=');

            appendEachValue.append(k, v, b, p);
        });

        sb.append('}');
    }

    private static StringBuilder createStringBuilder(long numElements) {

        return new StringBuilder(IElementsView.intNumElements(numElements * 100));
    }
}
