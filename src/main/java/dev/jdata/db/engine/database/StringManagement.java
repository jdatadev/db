package dev.jdata.db.engine.database;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.engine.database.strings.IStringCache;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public final class StringManagement extends ObjectCacheNode implements IResettable {

    private DatabaseStringManagement databaseStringManagement;
    private StringResolver parserStringResolver;
    private IStringCache stringCache;

    public StringManagement(AllocationType allocationType) {
        super(allocationType);
    }

    public void initialize(DatabaseStringManagement databaseStringManagement, StringResolver parserStringResolver, IStringCache stringCache) {

        this.databaseStringManagement = Initializable.checkNotYetInitialized(this.databaseStringManagement, databaseStringManagement);
        this.parserStringResolver = Initializable.checkNotYetInitialized(this.parserStringResolver, parserStringResolver);
        this.stringCache = Initializable.checkNotYetInitialized(this.stringCache, stringCache);
    }

    @Override
    public void reset() {

        this.databaseStringManagement = Initializable.checkResettable(databaseStringManagement);
        this.parserStringResolver = Initializable.checkResettable(parserStringResolver);
        this.stringCache = Initializable.checkResettable(stringCache);
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
