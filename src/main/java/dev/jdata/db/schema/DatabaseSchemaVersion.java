package dev.jdata.db.schema;

import dev.jdata.db.utils.checks.Checks;

public final class DatabaseSchemaVersion implements Comparable<DatabaseSchemaVersion> {

    public static final int NO_VERSION = -1;
    public static final int INITIAL_VERSION = 1;

    public static final DatabaseSchemaVersion INITIAL = DatabaseSchemaVersion.of(INITIAL_VERSION);

    public static DatabaseSchemaVersion of(int versionNumber) {

        return new DatabaseSchemaVersion(versionNumber);
    }

    private final int versionNumber;

    private DatabaseSchemaVersion(int versionNumber) {

        this.versionNumber = Checks.isDatabaseSchemaVersionNumber(versionNumber);
    }

    @Override
    public int compareTo(DatabaseSchemaVersion other) {

        return Integer.compare(versionNumber, other.versionNumber);
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public boolean isInitialVersion() {

        return versionNumber == INITIAL_VERSION;
    }

    public DatabaseSchemaVersion next() {

        return new DatabaseSchemaVersion(versionNumber + 1);
    }

    @Override
    public int hashCode() {

        return Integer.hashCode(versionNumber);
    }

    @Override
    public boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (object == null) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            final DatabaseSchemaVersion other = (DatabaseSchemaVersion)object;

            result = versionNumber == other.versionNumber;
        }

        return result;
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [versionNumber=" + versionNumber + "]";
    }
}
