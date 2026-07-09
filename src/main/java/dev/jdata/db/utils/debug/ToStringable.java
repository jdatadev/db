package dev.jdata.db.utils.debug;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.ByIndex;
import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.adt.elements.IElementsToString;
import dev.jdata.db.utils.jdk.adt.strings.StringBuilders;
import dev.jdata.db.utils.jdk.adt.strings.Strings;

public abstract class ToStringable {

    private static final boolean DEBUG = Boolean.FALSE;

    protected @interface StringNullOrNonNull {

    }

    static String getClassName(Class<?> javaClass ) {

        Objects.requireNonNull(javaClass);

        return javaClass.getSimpleName();
    }

    protected final String getClassName() {

        return getClassName(getClass());
    }

    protected void checkToString() {

    }

    protected String toStringSub() {

        return null;
    }

    protected void toStringBuilder(StringBuilder sb) {

    }

    protected final void appendHeader(StringBuilder sb) {

        Objects.requireNonNull(sb);

        sb.append(getClassName(getClass())).append(' ');
    }

    @Override
    public final String toString() {

        if (DEBUG) {

            System.out.println("toString " + getClassName());
        }

        checkToString();

        String result;

        result = toStringSub();

        if (result == null) {

            final StringBuilder sb = new StringBuilder(1000);

            toStringBuilder(sb);

            if (StringBuilders.isEmpty(sb)) {

                final Class<?> javaClass = getClass();

                appendHeader(sb);

                sb.append(IElementsToString.ELEMENTS_TO_STRING_PREFIX);

                try {
                    addFields(javaClass, this, sb, false);
                }
                catch (IllegalAccessException ex) {

                    throw new RuntimeException(ex);
                }

                sb.append(IElementsToString.ELEMENTS_TO_STRING_SUFFIX);
            }

            result = sb.toString();
        }

        return result;
    }

    private static boolean addFields(Class<?> javaClass, Object object, StringBuilder sb, boolean prefixWithComma) throws IllegalAccessException {

        final Class<?> superClass = javaClass.getSuperclass();

        if (DEBUG) {

            System.out.println("classes " + javaClass + ' ' + superClass);
        }

        if (countToStringStackframes() > 10) {

            throw new IllegalStateException();
        }

        boolean addComma;

        if (!superClass.equals(Object.class) && !superClass.isPrimitive()) {

            addComma = addFields(superClass, object, sb, prefixWithComma);
        }
        else {
            addComma = prefixWithComma;
        }

        for (Field field : javaClass.getDeclaredFields()) {

            if (Modifier.isStatic(field.getModifiers())) {

                continue;
            }

            final boolean wasAccessible = field.isAccessible();

            if (!wasAccessible) {

                field.setAccessible(true);
            }

            try {
                final Object fieldValue = field.get(object);

                if (fieldValue != null) {

                    if (DEBUG) {

                        System.out.println("field value of " + getClassName(fieldValue.getClass()) + '.' + field.getName());
                    }
                }

                final String fieldValueString = makeFieldValueString(field, fieldValue);

                if (addComma) {

                    sb.append(IElementsToString.ELEMENTS_TO_STRING_SEPARATOR);
                }
                else {
                    addComma = true;
                }

                sb.append(field.getName()).append('=').append(fieldValueString);
            }
            finally {

                if (!wasAccessible) {

                    field.setAccessible(false);
                }
            }
        }

        return addComma;
    }

    private static String makeFieldValueString(Field field, Object fieldValue) {

        final String result;

        if (field.getAnnotation(StringNullOrNonNull.class) != null) {

            result = Strings.nullOrNonNullString(fieldValue);
        }
        else {
            if (fieldValue == null) {

                result = Objects.toString(fieldValue);
            }
            else {
                final Class<?> javaClass = fieldValue.getClass();

                if (javaClass.isArray()) {

                    final StringBuilder sb = new StringBuilder(1000);

                    ByIndex.toString(fieldValue, 0L, Array.getLength(fieldValue), sb, null, (a, i, b, p) -> {

                        final Object arrayElement = Array.get(a, IByIndexView.intIndex(i));

                        sb.append(arrayElement);
                    });

                    result = sb.toString();
                }
                else {
                    result = Objects.toString(fieldValue);
                }
            }
        }

        return result;
    }

    private static int countToStringStackframes() {

        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        final int numStackTraceElements = stackTraceElements.length;

        final String toStringClassName = ToStringable.class.getName();

        int toStringStackframeCounter = 0;

        for (int i = 1; i < numStackTraceElements; ++ i) {

            final StackTraceElement stackTraceElement = stackTraceElements[i];

            if (stackTraceElement.getClassName().equals(toStringClassName) && stackTraceElement.getMethodName().equals("toString")) {

                ++ toStringStackframeCounter;
            }
        }

        return toStringStackframeCounter;
    }
}
