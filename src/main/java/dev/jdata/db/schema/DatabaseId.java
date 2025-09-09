package dev.jdata.db.schema;

import dev.jdata.db.utils.checks.Checks;

public final class DatabaseId {

    private final int id;
    private final String name;

    public DatabaseId(int id, String name) {

        this.id = Checks.isDatabaseId(id);
        this.name = Checks.isDatabaseName(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {

        return Integer.hashCode(id);
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
            final DatabaseId other = (DatabaseId)object;

            result = id == other.id && name.equals(other.name);
        }

        return result;
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [id=" + id + ", name=" + name + "]";
    }
}
