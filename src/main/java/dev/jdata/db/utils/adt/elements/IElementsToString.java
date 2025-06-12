package dev.jdata.db.utils.adt.elements;

public interface IElementsToString<T> {

    @FunctionalInterface
    interface ElementsToStringAdder<T, P> {

        void addString(T element, StringBuilder sb, P parameter);
    }

    <P> void toString(StringBuilder sb, P parameter, ElementsToStringAdder<T, P> consumer);
}
