package dev.jdata.db.utils.checks;

import java.util.Collection;

public class Checks {

    public static int isNotNegative(int value) {

        if (value < 0) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    public static long isNotNegative(long value) {

        if (value < 0L) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    public static int isAboveZero(int value) {

        if (value < 1) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    public static void areEqual(int value1, int value2) {

        if (value1 != value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void areEqual(long value1, long value2) {

        if (value1 != value2) {

            throw new IllegalArgumentException();
        }
    }

    public static int isDecimalPrecision(int value) {

        return isAboveZero(value);
    }

    public static int isDecimalScale(int value) {

        return isAboveZero(value);
    }

    public static float isLoadFactor(float value) {

        if (value < 0.01) {

            throw new IllegalArgumentException();
        }
        else if (value >= 1.0f) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    public static boolean isFileName(String fileName) {

        return isASCIIAlphaNumeric(fileName, c -> c == '_' || c == '.');
    }

    public static boolean isFileNamePrefix(String fileNamePrefix) {

        return stringContainsOnly(fileNamePrefix, c -> c >= 'a' && c <= 'z');
    }

    public static String isTableName(String schemaName) {

        return isSchemaName(schemaName);
    }

    public static String isColumnName(String schemaName) {

        return isSchemaName(schemaName);
    }

    public static String isSchemaName(String schemaName) {

        return isDBName(schemaName);
    }

    public static String isDBName(String schemaName) {

        if (schemaName.isEmpty()) {

            throw new IllegalArgumentException();
        }

        if (!isASCIIAlphaNumeric(schemaName, c -> c == '_')) {

            throw new IllegalArgumentException();
        }

        return schemaName;
    }

    @FunctionalInterface
    private interface CharPredicate {

        boolean test(char c);
    }

    private static boolean isASCIIAlphaNumeric(String string) {

        return isASCIIAlphaNumeric(string, c -> true);
    }

    private static boolean isASCIIAlphaNumeric(String string, CharPredicate additionalPredicate) {

        return stringContainsOnly(string, c ->
                   (c >= '0' && c <= '9')
                || (c >= 'A' && c <= 'Z')
                || (c >= 'a' && c <= 'z')
                || additionalPredicate.test(c));
    }

    private static boolean stringContainsOnly(String string, CharPredicate predicate) {

        final int length = string.length();

        boolean containsOnly = true;

        for (int i = 0; i < length; ++ i) {

            if (!containsOnly) {

                throw new IllegalArgumentException();
            }
        }

        return containsOnly;
    }

    public static <T, C extends Collection<T>> C isNotEmpty(C collection) {

        if (collection.isEmpty()) {

            throw new IllegalArgumentException();
        }

        return collection;
    }

    public static <T> T[] isNotEmpty(T[] array) {

        if (array.length == 0) {

            throw new IllegalArgumentException();
        }

        return array;
    }

    public static int isIndex(int index) {

        if (index < 0) {

            throw new IllegalArgumentException();
        }

        return index;
    }

    public static int isNumElements(int numElements) {

        if (numElements < 0) {

            throw new IllegalArgumentException();
        }

        return numElements;
    }

    public static int isInitialCapacity(int initialCapacity) {

        if (initialCapacity < 1) {

            throw new IllegalArgumentException();
        }

        return initialCapacity;
    }

    public static int isCapacity(int capacity) {

        if (capacity < 0) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    private static final int MAX_CAPACITY_EXPONENT = Integer.SIZE - 1;

    public static int isCapacityExponent(int capacityExponent) {

        if (capacityExponent < 0) {

            throw new IllegalArgumentException();
        }
        else if (capacityExponent > MAX_CAPACITY_EXPONENT) {

            throw new IllegalArgumentException();
        }

        return capacityExponent;
    }

    public static int isOffset(int offset) {

        if (offset < 0) {

            throw new IllegalArgumentException();
        }

        return offset;
    }

    public static long isOffset(long offset) {

        if (offset < 0) {

            throw new IllegalArgumentException();
        }

        return offset;
    }

    public static int isLength(int length) {

        if (length < 1) {

            throw new IllegalArgumentException();
        }

        return length;
    }

    public static <T> T[] atMost(T[] array, int numElements) {

        isNumElements(numElements);

        if (numElements > array.length) {

            throw new IllegalArgumentException();
        }

        return array;
    }

    public static int isSequenceNo(int sequenceNo) {

        if (sequenceNo < 0) {

            throw new IllegalArgumentException();
        }

        return sequenceNo;
    }

    public static long isSequenceNo(long sequenceNo) {

        if (sequenceNo < 0) {

            throw new IllegalArgumentException();
        }

        return sequenceNo;
    }
/*
    public static int isBufferBitsOffset(int offset) {

        if (offset < 0) {

            throw new IllegalArgumentException();
        }

        return offset;
    }
*/
    public static long isBufferBitsOffset(long offset) {

        if (offset < 0) {

            throw new IllegalArgumentException();
        }

        return offset;
    }

    public static int isNumBits(int numBits) {

        if (numBits < 1) {

            throw new IllegalArgumentException();
        }

        return numBits;
    }

    public static int isNumByteBitsOrZero(int numByteBits) {

        if (numByteBits < 0) {

            throw new IllegalArgumentException();
        }

        if (numByteBits >= 8) {

            throw new IllegalArgumentException();
        }

        return numByteBits;
    }

    public static int isNumBytes(int numBytes) {

        if (numBytes < 1) {

            throw new IllegalArgumentException();
        }

        return numBytes;
    }

    public static int isNumColumnBits(int numColumnBits) {

        if (numColumnBits < 1) {

            throw new IllegalArgumentException();
        }

        if (numColumnBits > 100) {

            throw new IllegalArgumentException();
        }

        return numColumnBits;
    }

    public static void verifyOffsetAndLength(byte[] buffer, int offset, int length) {

        isOffset(offset);
        isLength(length);

        if (offset + length > buffer.length) {

            throw new IllegalArgumentException();
        }
    }

    public static int isTableId(int tableId) {

        return isSchemaId(tableId);
    }

    public static int isSchemaId(int schemaId) {

        if (schemaId < 0) {

            throw new IllegalArgumentException();
        }

        return schemaId;
    }

    public static long isRowId(long rowId) {

        if (rowId < 0) {

            throw new IllegalArgumentException();
        }

        return rowId;
    }

    public static long isTransactionId(long transactionId) {

        if (transactionId < 0) {

            throw new IllegalArgumentException();
        }

        return transactionId;
    }

    public static int isDatabaseSchemaVersionNumber(int versionNumber) {

        if (versionNumber < 0) {

            throw new IllegalArgumentException();
        }

        return versionNumber;
    }

    public static int isNumColumns(int numColumns) {

        if (numColumns < 1) {

            throw new IllegalArgumentException();
        }

        return numColumns;
    }

    public static int isColumnIndex(int index) {

        if (index < 0) {

            throw new IllegalArgumentException();
        }

        return index;
    }
}
