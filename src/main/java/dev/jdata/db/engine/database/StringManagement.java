package dev.jdata.db.engine.database;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public final class StringManagement extends ObjectCacheNode implements IResettable {

    private DatabaseStringManagement databaseStringManagement;
    private StringResolver parserStringResolver;

    public StringManagement() {

    }

    public void initialize(DatabaseStringManagement databaseStringManagement, StringResolver parserStringResolver) {

        if (this.databaseStringManagement != null) {

            throw new IllegalStateException();
        }

        this.databaseStringManagement = Objects.requireNonNull(databaseStringManagement);
        this.parserStringResolver = Objects.requireNonNull(parserStringResolver);
    }

    @Override
    public void reset() {

        if (databaseStringManagement == null) {

            throw new IllegalStateException();
        }

        this.databaseStringManagement = null;
        this.parserStringResolver = null;
    }

    public boolean parsedEqualsStored(long parsedStringRef, long storedStringRef, boolean caseSensitive) {

        StringRef.checkIsString(parsedStringRef);
        StringRef.checkIsString(storedStringRef);

        return databaseStringManagement.getStringResolver().equals(storedStringRef, parserStringResolver, parsedStringRef);
    }

    public long resolveParsedStringRef(long stringRef) {

        StringRef.checkIsString(stringRef);

        return databaseStringManagement.resolveParsedStringRef(parserStringResolver, stringRef);
    }

    public long getHashStringRef(long parsedStringRef) {

        StringRef.checkIsString(parsedStringRef);

        return databaseStringManagement.getHashStringRef(parsedStringRef);
    }

    public String getLowerCaseString(long stringRef) {

        return databaseStringManagement.getLowerCaseString(stringRef);
    }

    private StringResolver getStringResolver() {

        return databaseStringManagement.getStringResolver();
    }
}
