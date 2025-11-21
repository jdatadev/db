package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntForEach;

abstract class BaseIntegerSetTest<T extends ISetTypeCommon> extends BaseSetTest<T> {

    abstract <P> void forEach(T set, P parameter, IIntForEach<P, RuntimeException> forEach);

    abstract boolean contains(T set, int value);
}
