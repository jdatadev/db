package dev.jdata.db.utils.adt.elements;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface IObjectElementPredicate<T, P> extends IElementPredicateMarker, BiPredicate<T, P> {

}
