package dev.jdata.db.schema.storage.sqloutputter;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.util.Objects;

import org.jutils.io.strings.StringResolver.ICharactersBufferAllocator;

import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.jdk.adt.strings.CharacterEncodingUtil;

public final class ByteSQLOutputter<P, E extends Exception> extends ExceptionAppendableSQLOutputter<ByteSQLOutputter<P, E>, E> implements IResettable {

    @FunctionalInterface
    public interface ByteOutputter<P, E extends Exception> {

        void output(byte b, P parameter) throws E;
    }

    private TextToByteOutputPrerequisites textToByteOutputPrerequisites;
    private P parameter;
    private ByteOutputter<P, E> byteOutputter;

    private CharsetEncoder charsetEncoder;
    private CharBuffer charBuffer;
    private ByteBuffer byteBuffer;

    public void initialize(ICharactersBufferAllocator charactersBufferAllocator, TextToByteOutputPrerequisites textOutputPrerequisites, P parameter,
            ByteOutputter<P, E> byteOutputter) {

        if (this.textToByteOutputPrerequisites != null) {

            throw new IllegalStateException();
        }

        this.textToByteOutputPrerequisites = Objects.requireNonNull(textOutputPrerequisites);
        this.parameter = parameter;
        this.byteOutputter = Objects.requireNonNull(byteOutputter);

        this.charsetEncoder = textOutputPrerequisites.getCharsetEncoder();

        final int numCharacters = 1000;
        final int numBytes = CharacterEncodingUtil.calculateNumEncodedBytes(charsetEncoder, numCharacters);

        this.charBuffer = textOutputPrerequisites.getCharBufferAllocator().allocateForEncodeCharacters(numCharacters);
        this.byteBuffer = textOutputPrerequisites.getByteBufferAllocator().allocateByteArrayByteBuffer(numBytes);

        final ExceptionAppendable<ByteSQLOutputter<P, E>, E> appendable = (c, i) -> {

            final CharBuffer charBuffer = i.charBuffer;

            charBuffer.reset();
            charBuffer.append(c);

            final ByteBuffer byteBuffer = i.byteBuffer;

            byteBuffer.reset();

            if (i.charsetEncoder.encode(charBuffer, byteBuffer, true).isError()) {

                throw new IllegalStateException();
            }

            final int numByteBufferBytes = byteBuffer.limit();

            final P byteOutputterParameter = i.parameter;

            for (int byteBufferIndex = 0; byteBufferIndex < numByteBufferBytes; ++ byteBufferIndex) {

                i.byteOutputter.output(byteBuffer.get(byteBufferIndex), byteOutputterParameter);
            }
        };

        initialize(charactersBufferAllocator, this, appendable);
    }

    @Override
    public void reset() {

        super.reset();

        textToByteOutputPrerequisites.getCharBufferAllocator().freeCharBuffer(charBuffer);
        textToByteOutputPrerequisites.getByteBufferAllocator().freeByteBuffer(byteBuffer);

        this.textToByteOutputPrerequisites = null;
        this.parameter = null;
        this.byteOutputter = null;
        this.charBuffer = null;
        this.byteBuffer = null;
    }
}
