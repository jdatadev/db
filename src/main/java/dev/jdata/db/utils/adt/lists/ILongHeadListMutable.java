package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.mutability.IMutableMarker;

interface ILongHeadListMutable extends IMutableMarker, ILongListView, ILongHeadListMutators {

    @Override
    default long removeHeadAndReturnValue() {

        final long result = getHead();

        removeHead();

        return result;
    }
}
