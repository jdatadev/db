package dev.jdata.db.utils.adt;

public abstract class BaseElements implements Elements {

    private int numElements;

    @Override
    public final int getNumElements() {
        return numElements;
    }

    protected final void increaseNumElements() {

        ++ numElements;
    }

    protected final void decreaseNumElements() {

        -- numElements;
    }
}
