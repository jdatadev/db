package dev.jdata.db;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;
import org.jutils.io.strings.StringResolver.ToStringWithStringResolver;

import dev.jdata.db.utils.debug.ToStringable;

public abstract class DBNamedObject extends ToStringable implements ToStringWithStringResolver {

    private final long storedSQLName;
    private final long hashName;

    protected DBNamedObject(long storedSQLName, long hashName) {

        this.storedSQLName = StringRef.checkIsString(storedSQLName);
        this.hashName = StringRef.checkIsString(hashName);
    }

    protected DBNamedObject(DBNamedObject toCopy) {

        this.storedSQLName = toCopy.storedSQLName;
        this.hashName = toCopy.hashName;
    }

    public final long getStoredSQLName() {
        return storedSQLName;
    }

    public final long getHashKeyName() {
        return hashName;
    }

    public final long getStoredName() {
        return hashName;
    }

    public final long getFileSystemName() {
        return hashName;
    }

    public final boolean equalsName(StringResolver thisStringResolver, DBNamedObject other, StringResolver otherStringResolver, boolean caseSensitive) {

        Objects.requireNonNull(thisStringResolver);
        Objects.requireNonNull(other);
        Objects.requireNonNull(otherStringResolver);

        final long thisName;
        final long otherName;

        if (caseSensitive) {

            thisName = storedSQLName;
            otherName = other.storedSQLName;
        }
        else {
            thisName = hashName;
            otherName = other.hashName;
        }

        return thisStringResolver.equals(thisName, otherStringResolver, otherName);
    }

    public final boolean equals(StringResolver thisStringResolver, DBNamedObject other, StringResolver otherStringResolver, boolean caseSensitive) {

        Objects.requireNonNull(thisStringResolver);
        Objects.requireNonNull(other);
        Objects.requireNonNull(otherStringResolver);

        return equalsName(thisStringResolver, other, otherStringResolver, caseSensitive);
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
            final DBNamedObject other = (DBNamedObject)object;

            result = hashName == other.hashName && storedSQLName == other.storedSQLName;
        }

        return result;
    }

    @Override
    public void toString(StringResolver stringResolver, StringBuilder sb) {

        Objects.requireNonNull(stringResolver);
        Objects.requireNonNull(sb);

        sb.append("[parsedname=");
        stringResolver.appendString(storedSQLName, sb);

        sb.append(", hashName=");
        stringResolver.appendString(hashName, sb);

        sb.append(']');
    }
}
