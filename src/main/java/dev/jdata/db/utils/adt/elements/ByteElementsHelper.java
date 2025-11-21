package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

class ByteElementsHelper extends BaseElementsHelper {

    static byte[] toArray(IByteIterable longIterable, int arrayLength) {

        Objects.requireNonNull(longIterable);
        Checks.isArrayLength(arrayLength);

        final ToArrayParameter<byte[]> toArrayParameter = new ToArrayParameter<>(arrayLength, byte[]::new);

        longIterable.forEach(toArrayParameter, (e, p) -> p.result[p.index ++] = e);

        return toArrayParameter.result;
    }
}
