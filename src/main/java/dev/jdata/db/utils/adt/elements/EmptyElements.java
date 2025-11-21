package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.EmptyContains;

abstract class EmptyElements extends EmptyContains implements IOnlyElementsView {

    @Override
    public final long getNumElements() {

        return 0L;
    }
}
