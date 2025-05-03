package dev.jdata.db.engine.database;

import org.jutils.io.strings.StringResolver.CharacterBuffer;

@Deprecated // currently not in use?
public interface IStringCache {

    String getString(CharSequence charSequence);

    String getString(int i);

    String getLowerCaseString(CharSequence charSequence);

    String makeString(CharacterBuffer[] characterBuffers, int numCharacterBuffers);
}
