package dev.jdata.db.engine.database;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.utils.allocators.CharacterBuffersAllocator;

public final class StringManagement {

    private final IStringCache stringCache;
    private final CharacterBuffersAllocator characterBuffersAllocator;

    public StringManagement(IStringCache stringCache, CharacterBuffersAllocator characterBuffersAllocator) {

        this.stringCache = Objects.requireNonNull(stringCache);
        this.characterBuffersAllocator = Objects.requireNonNull(characterBuffersAllocator);
    }

    String getParsedString(StringResolver parserStringResolver, long stringRef) {

        Objects.requireNonNull(parserStringResolver);
        StringRef.checkIsString(stringRef);

        return parserStringResolver.makeString(stringRef, stringCache, characterBuffersAllocator, (b, n, p) -> p.makeString(b, n));
    }

    String getLowerCaseString(CharSequence charSequence) {

        Objects.requireNonNull(charSequence);

        return stringCache.getLowerCaseString(charSequence);
    }
}
