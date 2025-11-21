package dev.jdata.db.utils.scalars.mutable;

public final class MutableInt extends BaseMutableInteger {

    public static MutableInt undefined() {

        return new MutableInt();
    }

    public static MutableInt ofZero() {

        return new MutableInt(0);
    }

    public static MutableInt of(int value) {

        return new MutableInt(value);
    }

    private int value;

    private MutableInt() {

    }

    private MutableInt(int value) {

        set(value);
    }

    public void set(int value) {

        markAsSet();

        this.value = value;
    }

    public int get() {

        checkIsSet();

        return value;
    }

    public int getAndIncrement() {

        checkIsSet();

        return value ++;
    }
}
