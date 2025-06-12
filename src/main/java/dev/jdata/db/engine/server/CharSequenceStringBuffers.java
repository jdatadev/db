package dev.jdata.db.engine.server;

import org.jutils.io.buffers.BaseStringBuffers;

public final class CharSequenceStringBuffers extends BaseStringBuffers<RuntimeException> {

    public CharSequenceStringBuffers(CharSequence charSequence) {
        super(new CharSequenceLoadStream(charSequence));
    }
}
