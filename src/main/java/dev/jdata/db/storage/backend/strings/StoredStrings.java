package dev.jdata.db.storage.backend.strings;

public final class StoredStrings {

    private final static class StoredString {

        private long identifier;
        private String string;
    }

    private StoredString[] storedStrings;

    public int getNumStoredStrings() {

        return storedStrings.length;
    }

    public long getIdentifier(int index) {

        return storedStrings[index].identifier;
    }

    public String getString(int index) {

        return storedStrings[index].string;
    }
}
