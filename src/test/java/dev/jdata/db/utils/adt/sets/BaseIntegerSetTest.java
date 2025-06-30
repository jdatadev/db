package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntIterableElements.IForEach;

abstract class BaseIntegerSetTest<T extends ISet> extends BaseSetTest<T> {

    abstract <P> void forEach(T set, P parameter, IForEach<P, RuntimeException> forEach);

    abstract boolean contains(T set, int value);
}
