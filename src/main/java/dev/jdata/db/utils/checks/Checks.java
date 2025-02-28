package dev.jdata.db.utils.checks;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dev.jdata.db.DBConstants;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.utils.adt.Contains;
import dev.jdata.db.utils.adt.elements.Elements;
import dev.jdata.db.utils.function.CharPredicate;

public class Checks {

    public static <T> T isNull(T value, boolean expectNonNull) {

        if (expectNonNull) {

            if (value == null) {

                throw new NullPointerException();
            }
        }
        else {
            if (value != null) {

                throw new IllegalArgumentException();
            }
        }

        return value;
    }

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

    public static BigDecimal isNotNegative(BigDecimal value) {

        if (value.signum() < 0) {

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

    public static long isAboveZero(long value) {

        if (value < 1L) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    public static long isExactlyOne(long value) {

        if (value != 1L) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    public static long isWithinRangeInclusive(long value, long lower, long upper) {

        if (lower >= upper) {

            throw new IllegalArgumentException();
        }
        else if (value < lower) {

            throw new IllegalArgumentException();
        }
        else if (value > upper) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    public static long isWithinRangeUpperExclusive(long value, long lower, long upper) {

        if (lower >= upper) {

            throw new IllegalArgumentException();
        }
        else if (value < lower) {

            throw new IllegalArgumentException();
        }
        else if (value >= upper) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    public static void isLessThan(long value1, long value2) {

        if (value1 >= value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isLessThanOrEqualTo(long value1, long value2) {

        if (value1 > value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isGreaterThanOrEqualTo(long value1, long value2) {

        if (value1 <= value2) {

            throw new IllegalArgumentException();
        }
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

    public static <T> void areNotEqual(T object1, T object2) {

        if (object1.equals(object2)) {

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

        return isPathName(fileName);
    }

    public static boolean isDirectoryName(String directoryName) {

        return isPathName(directoryName);
    }

    public static boolean isFileNamePrefix(String fileNamePrefix) {

        return stringContainsOnly(fileNamePrefix, c -> c >= 'a' && c <= 'z');
    }

    public static boolean isPathName(String pathName) {

        return isASCIIAlphaNumeric(pathName, c -> c == '_' || c == '.');
    }

    public static String isTableName(String tableName) {

        return isSchemaName(tableName);
    }

    public static String isColumnName(String columnName) {

        return isSchemaName(columnName);
    }

    public static String isSchemaName(String schemaName) {

        return isDBName(schemaName);
    }

    public static String isDBName(String dbName) {

        if (!checkIsDBName(dbName)) {

            throw new IllegalArgumentException();
        }

        return dbName;
    }

    public static boolean checkIsDBName(CharSequence dbName) {

        final boolean result;

        if (dbName.isEmpty()) {

            result = false;
        }
        else if (!hasFirstCharacterAndRemaining(dbName, Character::isAlphabetic, c -> isASCIIAlphaNumeric(c) || c == '_')) {

            result = false;
        }
        else {
            result = true;
        }

        return result;
    }

    public static String startsWith(String string, String prefix) {

        if (prefix == null) {

            throw new NullPointerException();
        }

        if (!string.startsWith(prefix)) {

            throw new IllegalArgumentException();
        }

        return string;
    }

    public static String isAlphabetic(String string) {

        if (!stringContainsOnly(string, Character::isAlphabetic)) {

            throw new IllegalArgumentException();
        }

        return string;
    }

    public static String isJavaIdentifier(String string) {

        if (!hasFirstCharacterAndRemaining(string, Character::isJavaIdentifierStart, Character::isJavaIdentifierPart)) {

            throw new IllegalArgumentException();
        }

        return string;
    }

    public static String isJavaVariable(String string) {

        if (!hasFirstCharacterAndRemaining(string, Character::isJavaIdentifierStart, c -> Character.isJavaIdentifierPart(c) || c == '.')) {

            throw new IllegalArgumentException();
        }

        return string;
    }

    public static String containsOnly(String string, int startIndex, int numCharacters, CharPredicate predicate) {

        if (!stringContainsOnly(string, startIndex, numCharacters, predicate)) {

            throw new IllegalArgumentException();
        }

        return string;
    }

    private static boolean isASCIIAlphaNumeric(String string) {

        return isASCIIAlphaNumeric(string, c -> true);
    }

    private static boolean isASCIIAlphaNumeric(String string, CharPredicate additionalPredicate) {

        return stringContainsOnly(string, c ->
                   isASCIIAlphaNumeric(c)
                || additionalPredicate.test(c));
    }

    private static boolean isASCIIAlphaNumeric(char c) {

        return
                   (c >= '0' && c <= '9')
                || (c >= 'A' && c <= 'Z')
                || (c >= 'a' && c <= 'z');
    }

    private static boolean stringContainsOnly(String string, CharPredicate predicate) {

        return stringContainsOnly(string, 0, string.length(), predicate);
    }

    private static boolean stringContainsOnly(String string, int startIndex, int numCharacters, CharPredicate predicate) {

        return charSequenceContainsOnly(string, startIndex, numCharacters, predicate);
    }

    private static boolean charSequenceContainsOnly(CharSequence charSequence, int startIndex, int numCharacters, CharPredicate predicate) {

        boolean containsOnly = true;

        for (int i = 0; i < numCharacters; ++ i) {

            if (!predicate.test(charSequence.charAt(startIndex + i))) {

                containsOnly = false;
                break;
            }
        }

        return containsOnly;
    }

    private static boolean hasFirstCharacterAndRemaining(CharSequence charSequence, CharPredicate firstCharacterPredicate, CharPredicate remainingCharacterspredicate) {

        final int length = charSequence.length();

        final boolean result;

        if (length == 0) {

            result = false;
        }
        else {
            if (!firstCharacterPredicate.test(charSequence.charAt(0))) {

                result = false;
            }
            else {
                result = length > 1
                        ? charSequenceContainsOnly(charSequence, 0, length - 1, remainingCharacterspredicate)
                        : true;
            }
        }

        return result;
    }

    public static <T, C extends Collection<T>> C isEmpty(C collection) {

        if (!collection.isEmpty()) {

            throw new IllegalArgumentException();
        }

        return collection;
    }

    public static <T, C extends Collection<T>> C isNotEmpty(C collection) {

        if (collection.isEmpty()) {

            throw new IllegalArgumentException();
        }

        return collection;
    }

    public static int[] isNotEmpty(int[] array) {

        if (array.length == 0) {

            throw new IllegalArgumentException();
        }

        return array;
    }

    public static <T> T[] isNotEmpty(T[] array) {

        if (array.length == 0) {

            throw new IllegalArgumentException();
        }

        return array;
    }

    public static <T extends Contains> T isNotEmpty(T contains) {

        if (contains.isEmpty()) {

            throw new IllegalArgumentException();
        }

        return contains;
    }

    public static <T> T[] checkElements(T[] array, Consumer<T> check) {

        for (T element : array) {

            check.accept(element);
        }

        return array;
    }

    public static <T> T[] areElements(T[] array, Predicate<T> predicate) {

        for (T element : array) {

            if (!predicate.test(element)) {

                throw new IllegalArgumentException();
            }
        }

        return array;
    }

    public static int isIndex(int index) {

        if (index < 0) {

            throw new IllegalArgumentException();
        }

        return index;
    }

    public static long isIndex(long index) {

        if (index < 0L) {

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

    public static long isNumElements(long numElements) {

        if (numElements < 0L) {

            throw new IllegalArgumentException();
        }

        return numElements;
    }

    public static void areSameNumElements(Elements elements1, Elements elements2) {

        if (elements1.getNumElements() != elements2.getNumElements()) {

            throw new IllegalArgumentException();
        }
    }

    public static void areSameLength(int[] array1, int[] array2) {

        if (array1.length != array2.length) {

            throw new IllegalArgumentException();
        }
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

    public static int isLengthAboveOrAtZero(int length) {

        if (length < 0) {

            throw new IllegalArgumentException();
        }

        return length;
    }

    public static int isLengthAboveZero(int length) {

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

    public static long isString(long string) {

        if (string < 0) {

            throw new IllegalArgumentException();
        }

        return string;
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

    public static Integer isNumBits(Integer numBits) {

        isNumBits(numBits.intValue());

        return numBits;
    }

    public static int isShortNumBits(int numBits, boolean signed) {

        return isNumBits(numBits, signed, 16);
    }

    public static int isIntNumBits(int numBits, boolean signed) {

        return isNumBits(numBits, signed, 32);
    }

    public static int isLongNumBits(int numBits, boolean signed) {

        return isNumBits(numBits, signed, 64);
    }

    private static int isNumBits(int numBits, boolean signed, int maxBits) {

        if (numBits < 1) {

            throw new IllegalArgumentException();
        }
        else if (numBits >= maxBits) {

            if (signed) {

                throw new IllegalArgumentException();
            }
            else if (numBits > maxBits) {

                throw new IllegalArgumentException();
            }
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
        isLengthAboveZero(length);

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

    public static int isDescriptor(int descriptor) {

        if (descriptor < 0) {

            throw new IllegalArgumentException();
        }

        return descriptor;
    }

    public static int isSessionDescriptor(int sessionDescriptor) {

        return Checks.isDescriptor(sessionDescriptor);
    }

    public static int isTransactionDescriptor(int transactionDescriptor) {

        return Checks.isDescriptor(transactionDescriptor);
    }

    public static int isStatementId(int statementId) {

        if (statementId < 0) {

            throw new IllegalArgumentException();
        }

        return statementId;
    }

    public static int isDatabaseSchemaVersionNumber(int versionNumber) {

        if (versionNumber < DatabaseSchemaVersion.INITIAL_VERSION) {

            throw new IllegalArgumentException();
        }

        return versionNumber;
    }

    public static int isNumColumns(int numColumns) {

        if (numColumns < 1) {

            throw new IllegalArgumentException();
        }
        else if (numColumns > DBConstants.MAX_COLUMNS) {

            throw new IllegalArgumentException();
        }

        return numColumns;
    }

    public static int isColumnIndex(int index) {

        if (index < 0) {

            throw new IllegalArgumentException();
        }
        else if (index >= DBConstants.MAX_COLUMN_INDEX) {

            throw new IllegalArgumentException();
        }

        return index;
    }
}
