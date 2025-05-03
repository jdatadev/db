package dev.jdata.db.engine.database;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.utils.adt.strings.Strings;
import dev.jdata.db.utils.allocators.CharacterBuffersAllocator;
import dev.jdata.db.utils.checks.Checks;

public final class StringManagement {

    public static String getHashNameString(String parsedName) {

        Checks.isDBName(parsedName);

        return Strings.isAllLowerCase(parsedName) ? parsedName : parsedName.toLowerCase();
    }

    private final StringStorer stringStorer;
    private final CharacterBuffersAllocator characterBuffersAllocator;

    public StringManagement(CharacterBuffersAllocator characterBuffersAllocator) {

        this.stringStorer = new StringStorer(1, 10);
        this.characterBuffersAllocator = Objects.requireNonNull(characterBuffersAllocator);
    }

    public long resolveParsedStringRef(StringResolver parserStringResolver, long stringRef) {

        Objects.requireNonNull(parserStringResolver);
        StringRef.checkIsString(stringRef);

        return parserStringResolver.makeStringRef(stringRef, this, characterBuffersAllocator, (b, n, i) -> i.stringStorer.getOrAddStringRef(b, n));
    }

    public long getHashStringRef(long parsedStringRef) {

        StringRef.checkIsString(parsedStringRef);

        return stringStorer.containsOnly(parsedStringRef, Character::isLowerCase)
                ? parsedStringRef
                : stringStorer.toLowerCase(parsedStringRef);
    }

    public String getLowerCaseString(long stringRef) {

        throw new UnsupportedOperationException();
    }
}
