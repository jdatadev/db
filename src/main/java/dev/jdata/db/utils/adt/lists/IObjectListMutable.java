package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IObjectOrderedOnlyElementsMutable;

public interface IObjectListMutable<T> extends IObjectOrderedOnlyElementsMutable<T>, IObjectTailListRemovalMutators<T> {

    @Override
    default T removeTailAndReturnValue() {
        // TODO Auto-generated method stub
        return null;
    }
}
