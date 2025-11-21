package dev.jdata.db.engine.database;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.utils.allocators.CharacterBuffersAllocator;

public final class DatabaseStringManagement {

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

    public long getHashStringRef(long parsedStringRef) {

        StringRef.checkIsString(parsedStringRef);

        return stringStorer.containsOnly(parsedStringRef, c -> !Character.isUpperCase(c))
                ? parsedStringRef
                : stringStorer.toLowerCase(parsedStringRef);
    }

    public String getLowerCaseString(long stringRef) {

        final long lowerCaseStringRef = stringStorer.getOrAddLowerCaseStringRef(stringRef);

        return stringStorer.asString(lowerCaseStringRef);
    }

    public StringResolver getStringResolver() {

        return stringStorer;
    }
}
