package dev.jdata.db.utils.adt.elements;

public abstract class BaseElements {

    protected static int intIndex(long index) {

        return IElements.intIndex(index);
    }

    protected static int intNumElements(long numElements) {

        return IElements.intNumElements(numElements);
    }
}
