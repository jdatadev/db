package dev.jdata.db.engine.database;

import java.util.Objects;

import org.jutils.io.strings.StringResolver.CharacterBuffer;

@Deprecated
public final class StringCache implements IStringCache {

    @Override
    public String getString(CharSequence charSequence) {

        return new StringBuilder(charSequence).toString();
    }

    @Override
    public String getString(int i) {

        throw new UnsupportedOperationException();
    }

    @Override
    public String getLowerCaseString(CharSequence charSequence) {

        Objects.requireNonNull(charSequence);

        return getString(charSequence).toLowerCase();
    }

    @Override
    public String makeString(CharacterBuffer[] characterBuffers, int numCharacterBuffers) {

        int totalNumCharacters = 0;

        for (int i = 0; i < numCharacterBuffers; ++ i) {

            totalNumCharacters += characterBuffers[i].getLength();
        }

        final StringBuilder sb = new StringBuilder(totalNumCharacters);

        for (int i = 0; i < numCharacterBuffers; ++ i) {

            final CharacterBuffer characterBuffer = characterBuffers[i];

            sb.append(characterBuffer.getCharArray(), characterBuffer.getOffset(), characterBuffer.getLength());
        }

        return sb.toString();
    }
}
