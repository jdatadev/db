package dev.jdata.db.engine.database;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public final class StringManagement extends ObjectCacheNode implements IResettable {

    private DatabaseStringManagement databaseStringManagement;
    private StringResolver parserStringResolver;
    private IStringCache stringCache;

    public StringManagement() {

    }

    public void initialize(DatabaseStringManagement databaseStringManagement, StringResolver parserStringResolver, IStringCache stringCache) {

        if (this.databaseStringManagement != null) {

            throw new IllegalStateException();
        }

        this.databaseStringManagement = Objects.requireNonNull(databaseStringManagement);
        this.parserStringResolver = Objects.requireNonNull(parserStringResolver);
        this.stringCache = Objects.requireNonNull(stringCache);
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

    public long storeParsedStringRef(long stringRef) {

        StringRef.checkIsString(stringRef);

        return databaseStringManagement.storeParsedStringRef(parserStringResolver, stringRef);
    }

    public long getHashStringRef(long stringRef) {

        StringRef.checkIsString(stringRef);

        return databaseStringManagement.getHashStringRef(stringRef);
    }

    public String getLowerCaseString(long stringRef) {

        StringRef.checkIsString(stringRef);

        return databaseStringManagement.getLowerCaseString(stringRef);
    }

    public IStringCache getStringCache() {
        return stringCache;
    }
}
