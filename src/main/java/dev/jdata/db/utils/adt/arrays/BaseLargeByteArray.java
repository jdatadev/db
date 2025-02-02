package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;

import dev.jdata.db.utils.adt.Clearable;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseLargeByteArray extends LargeExponentArray implements Clearable {

    protected static final int NUM_INNER_ELEMENTS_NUM_BYTES = 4;

    private byte[][] buffers;

    protected BaseLargeByteArray(int innerCapacityExponent) {
        this(0, innerCapacityExponent);
    }

    protected BaseLargeByteArray(int initialOuterCapacity, int innerCapacityExponent) {
        super(innerCapacityExponent, initialOuterCapacity, NUM_INNER_ELEMENTS_NUM_BYTES);

        Checks.isCapacity(initialOuterCapacity);

        if (initialOuterCapacity > 0) {

            this.buffers = new byte[1][];

            final byte[] buffer = buffers[0] = new byte[getInnerNumAllocateElements()];

            setNumElements(buffer, 0);
        }
        else {
            this.buffers = null;
        }
    }

    @Override
    public void clear() {

        if (buffers != null) {

            final int numOuter = getNumOuterEntries();

            for (int i = 0; i < numOuter; ++ i) {

                setNumElements(buffers[i], 0);
            }
        }

        super.clear();
    }

    protected final byte[] getBuffer(long byteOffset) {

        Checks.isNotNegative(byteOffset);

        return buffers[getOuterIndex(byteOffset)];
    }

    protected final byte[] getLastBufferOrAllocate() {

        return getNumOuterEntries() != 0 ? getLastBuffer() : allocate();
    }

    private byte[] getLastBuffer() {

        return buffers[getNumOuterEntries() - 1];
    }

    final byte[] checkCapacity() {

        final int numOuterAllocatedEntries = getNumOuterAllocatedEntries();
        final int numOuterUtilizedEntries = getNumOuterEntries();

        final byte[] result;

        if (numOuterAllocatedEntries == 0) {

            result = initializeBuffers();
        }
        else if (numOuterUtilizedEntries == 0) {

            result = reallocateFirstOuterBuffer();
        }
        else {
            final byte[] buffer = buffers[numOuterUtilizedEntries - 1];

            final int numInnerElements = getInnerArrayNumElements(buffer);

            if (numInnerElements == buffer.length - NUM_INNER_ELEMENTS_NUM_BYTES) {

                if (numOuterUtilizedEntries < numOuterAllocatedEntries) {

                    result = buffers[numOuterUtilizedEntries];

                    setNumElements(result, 0);

                    incrementNumOuterEntries();
                }
                else {
                    result = allocateNewOuter(numOuterUtilizedEntries);
                }
            }
            else {
                result = buffer;
            }
        }

        return result;
    }

    protected final byte[] allocate() {

        final int numOuterAllocatedEntries = getNumOuterAllocatedEntries();
        final int numOuterUtilizedEntries = getNumOuterEntries();

        final byte[] result;

        if (numOuterAllocatedEntries == 0) {

            result = initializeBuffers();
        }
        else if (numOuterUtilizedEntries == 0) {

            result = reallocateFirstOuterBuffer();
        }
        else {
            result = allocateNewOuter(numOuterUtilizedEntries);
        }

        return result;
    }

    private byte[] initializeBuffers() {

        this.buffers = new byte[1][];

        final byte[] result = buffers[0] = new byte[getInnerNumAllocateElements()];

        setNumElements(result, 0);

        incrementNumOuterAllocatedEntries();
        incrementNumOuterEntries();

        return result;
    }

    private byte[] reallocateFirstOuterBuffer() {

        final byte[] result = buffers[0];

        setNumElements(result, 0);

        incrementNumOuterEntries();

        return result;
    }

    private byte[] allocateNewOuter(int numOuterUtilizedEntries) {

        final int outerArrayLength = buffers.length;

        final byte[][] updateBuffers;

        if (numOuterUtilizedEntries == outerArrayLength) {

            final byte[][] newBuffers = Arrays.copyOf(buffers, outerArrayLength << 2);

            this.buffers = newBuffers;

            updateBuffers = newBuffers;
        }
        else {
            updateBuffers = buffers;
        }

        final byte[] result = updateBuffers[numOuterUtilizedEntries] = new byte[getInnerNumAllocateElements()];

        setNumElements(result, 0);

        incrementNumOuterAllocatedEntries();
        incrementNumOuterEntries();

        return result;
    }

    protected static int getInnerArrayNumElements(byte[] innerArray) {

        return    (Byte.toUnsignedInt(innerArray[0]) << 24)
                | (Byte.toUnsignedInt(innerArray[1]) << 16)
                | (Byte.toUnsignedInt(innerArray[2]) << 8)
                | Byte.toUnsignedInt(innerArray[3]);
    }

    static void incrementNumElements(byte[] innerArray) {

        setNumElements(innerArray, getInnerArrayNumElements(innerArray) + 1);
    }

    protected static void setNumElements(byte[] innerArray, int numElements) {

        innerArray[0] = (byte)(numElements >>> 24);
        innerArray[1] = (byte)((numElements & 0x00FF0000) >>> 16);
        innerArray[2] = (byte)((numElements & 0x0000FF00) >>> 8);
        innerArray[3] = (byte)(numElements & 0x000000FF);
    }
}
