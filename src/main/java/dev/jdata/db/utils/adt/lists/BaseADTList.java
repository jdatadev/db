package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.BaseADTElements;

abstract class BaseADTList<T, U, V> extends BaseADTElements<T, U, V> {

    BaseADTList(AllocationType allocationType) {
        super(allocationType);
    }
}
