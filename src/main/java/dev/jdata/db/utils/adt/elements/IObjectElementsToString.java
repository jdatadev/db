package dev.jdata.db.utils.adt.elements;

public interface IObjectElementsToString<T> {

    public static final char ELEMENTS_TO_STRING_PREFIX = '[';
    public static final char ELEMENTS_TO_STRING_SUFFIX = ']';
    public static final char ELEMENTS_TO_STRING_SEPARATOR = ',';

    @FunctionalInterface
    interface ElementsToStringAdder<T, P> {

        void addString(T element, StringBuilder sb, P parameter);
    }

    <P> void toString(StringBuilder sb, P parameter, ElementsToStringAdder<T, P> consumer);
}
