package dev.jdata.db.engine.database;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.utils.allocators.CharacterBuffersAllocator;
import dev.jdata.db.utils.debug.ToStringable;

public final class DatabaseStringManagement extends ToStringable {

    private final IStringStorer stringStorer;
    private final CharacterBuffersAllocator characterBuffersAllocator;

    public DatabaseStringManagement(CharacterBuffersAllocator characterBuffersAllocator) {
        this(characterBuffersAllocator, IStringStorer.create(1, 10));
    }

    public DatabaseStringManagement(CharacterBuffersAllocator characterBuffersAllocator, IStringStorer stringStorer) {

        this.characterBuffersAllocator = Objects.requireNonNull(characterBuffersAllocator);
        this.stringStorer = Objects.requireNonNull(stringStorer);
    }

    public long storeParsedStringRef(StringResolver parserStringResolver, long stringRef) {

        Objects.requireNonNull(parserStringResolver);
        StringRef.checkIsString(stringRef);

        return parserStringResolver.makeStringRef(stringRef, this, characterBuffersAllocator, (b, n, i) -> i.stringStorer.getOrAddStringRef(b, n));
    }

    public long storeHashStringRefFromStoredSQLStringRef(long storedSQLStringRef) {

        StringRef.checkIsString(storedSQLStringRef);

        return stringStorer.containsOnly(storedSQLStringRef, c -> !Character.isUpperCase(c))
                ? storedSQLStringRef
                : stringStorer.toLowerCase(storedSQLStringRef);
    }

    public String getLowerCaseString(long stringRef) {

        final long lowerCaseStringRef = stringStorer.getOrAddLowerCaseStringRef(stringRef);

        return stringStorer.asString(lowerCaseStringRef);
    }

    public StringResolver getStoredStringResolver() {

        return stringStorer;
    }
}
