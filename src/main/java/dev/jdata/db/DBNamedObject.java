package dev.jdata.db;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

public abstract class DBNamedObject {

    private final long parsedName;
    private final long hashName;

    protected DBNamedObject(long parsedName, long hashName) {

        this.parsedName = StringRef.checkIsString(parsedName);
        this.hashName = StringRef.checkIsString(hashName);
    }

    protected DBNamedObject(DBNamedObject toCopy) {

        this.parsedName = toCopy.parsedName;
        this.hashName = toCopy.hashName;
    }

    public final long getParsedName() {
        return parsedName;
    }

    public final long getHashName() {
        return hashName;
    }

    public final long getFileSystemName() {
        return hashName;
    }

    public void toString(StringResolver stringResolver, StringBuilder sb) {

        Objects.requireNonNull(stringResolver);
        Objects.requireNonNull(sb);

        sb.append("[parsedname=");
        stringResolver.appendString(parsedName, sb);

        sb.append(", hashName=");
        stringResolver.appendString(hashName, sb);

        sb.append(']');
    }
}
