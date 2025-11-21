package dev.jdata.db.utils.adt.elements;

public interface IObjectElementsToString<T> extends IElementsToString {

    @FunctionalInterface
    interface IElementsToStringAdder<T, P> {

        void addString(T element, StringBuilder sb, P parameter);
    }

    <P> void toString(StringBuilder sb, P parameter, IElementsToStringAdder<T, P> consumer);
}
