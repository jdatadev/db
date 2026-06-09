package dev.jdata.db.engine.database;

import org.jutils.io.strings.StringResolver.CharacterBuffer;

import dev.jdata.db.utils.adt.marker.IMutators;

public interface IStringStorerGetOrAddMutators extends IMutators {

    long toLowerCase(long stringRef);
    long getOrAddLowerCaseStringRef(long stringRef);

    long getOrAddStringRef(CharSequence charSequence, int offset, int length);

    long getOrAddStringRef(CharacterBuffer[] characterBuffers, int numCharacterBuffers);

    default long getOrAddStringRef(CharSequence charSequence) {

        return getOrAddStringRef(charSequence, 0, charSequence.length());
    }
}
