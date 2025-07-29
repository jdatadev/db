package dev.jdata.db.engine.database;

public interface IStringCache {

    boolean contains(CharSequence charSequence);

    String getOrAddString(CharSequence charSequence, int startIndex, int numCharacters);

    default String getOrAddString(CharSequence charSequence) {

        return getOrAddString(charSequence, 0, charSequence.length());
    }

    String getOrAddString(int i);
}
