package dev.jdata.db.utils.adt.contains;

public abstract class EmptyContains implements IContainsView {

    @Override
    public final boolean isEmpty() {

        return true;
    }
}
