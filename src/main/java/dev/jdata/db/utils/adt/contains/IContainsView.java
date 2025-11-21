package dev.jdata.db.utils.adt.contains;

import dev.jdata.db.utils.adt.IView;

public interface IContainsView extends IView, IContainsGetters {

    public static boolean isNullOrEmpty(IContainsView contains) {

        return contains == null || contains.isEmpty();
    }
}
