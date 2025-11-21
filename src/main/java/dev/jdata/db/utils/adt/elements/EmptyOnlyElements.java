package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.EmptyContains;

public abstract class EmptyOnlyElements extends EmptyContains implements IOnlyElementsView {

    @Override
    public final long getNumElements() {

        return 0L;
    }
}
