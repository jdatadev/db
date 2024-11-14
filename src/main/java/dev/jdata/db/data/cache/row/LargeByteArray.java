package dev.jdata.db.data.cache.row;

import java.util.Objects;

import dev.jdata.db.data.RowDataNumBitsGetter;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.checks.Checks;

public final class LargeByteArray {

    private final byte[][] bytes;

    LargeByteArray(int initialOuterCapacity) {

        this.bytes = new byte[initialOuterCapacity][];
    }

    public void retrieveRow(long index, byte[] outputBuffer, RowDataNumBitsGetter inputRowDataNumBitsGetter, long outputBufferStartBitOffset, int[] columnIndices,
            RowDataNumBitsGetter outputRowDataNumBitsGetter) {

        Objects.requireNonNull(outputBuffer);
        Objects.requireNonNull(inputRowDataNumBitsGetter);
        Checks.isBufferBitsOffset(outputBufferStartBitOffset);
        Objects.requireNonNull(columnIndices);
        Objects.requireNonNull(outputRowDataNumBitsGetter);

        final byte[] cacheBuffer = bytes[getOuterIndex(index)];

        long inputBitOffset = getInnerBitOffset(index);
        long outputBitOffset = outputBufferStartBitOffset;

        for (int columnIndex : columnIndices) {

            BitBufferUtil.copyBits(cacheBuffer, inputBitOffset, inputRowDataNumBitsGetter.getNumBits(columnIndex), outputBuffer, outputBitOffset,
                    outputRowDataNumBitsGetter.getNumBits(columnIndex));

            inputBitOffset += inputRowDataNumBitsGetter.getNumBits(columnIndex);
            outputBitOffset += outputRowDataNumBitsGetter.getNumBits(columnIndex);
        }
    }

    private static int getOuterIndex(long index) {

        return (int)(index >>> 40);
    }

    private static long getInnerBitOffset(long index) {

        return (int)(index & 0xFFFFFFFFFL);
    }
}
