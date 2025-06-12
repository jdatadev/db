package dev.jdata.db.utils.adt;

public interface IContains {

    public static boolean isNullOrEmpty(IContains contains) {

        return contains == null || contains.isEmpty();
    }

    boolean isEmpty();
}
