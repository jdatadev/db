package dev.jdata.db.utils.adt.contains;

import dev.jdata.db.utils.adt.IView;

public interface IContainsView extends IView, IContainsGetters {

    public static boolean isNullOrEmpty(IContainsView contains) {

        return contains == null || contains.isEmpty();
    }

    public static <T extends IContainsView> boolean isNullOrEmpty(T[] containsArray) {

        boolean nullOrEmpty = true;

        for (T contains : containsArray) {

            if (contains != null && !contains.isEmpty()) {

                nullOrEmpty = false;
                break;
            }
        }

        return nullOrEmpty;
    }
}
