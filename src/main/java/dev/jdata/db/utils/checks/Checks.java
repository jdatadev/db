package dev.jdata.db.utils.checks;

import java.math.BigDecimal;
import java.nio.Buffer;
import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dev.jdata.db.DBConstants;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.utils.adt.IContains;
import dev.jdata.db.utils.adt.arrays.IOneDimensionalArrayGetters;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.strings.CharSequences;
import dev.jdata.db.utils.adt.strings.Characters;
import dev.jdata.db.utils.adt.strings.Strings;
import dev.jdata.db.utils.function.CharPredicate;

public class Checks {

    public static boolean isTrue(boolean value) {

        if (!value) {

            throw new IllegalArgumentException();
        }

        return value;
    }

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

    public static boolean checkIsNotNull(Object object) {

        return object != null;
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

    public static long isNotZero(long value) {

        if (value == 0L) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    public static long isExactlyZero(long value) {

        if (value != 0L) {

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

    public static int isExactlyZero(int value) {

        if (value != 0) {

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

    public static void checkFromIndexSize(int fromIndex, int size, int length) {

        if (fromIndex < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (size < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (fromIndex + size > length) {

            throw new IndexOutOfBoundsException();
        }
        else if (length < 0) {

            throw new IndexOutOfBoundsException();
        }
    }

    public static void checkFromIndexSize(long fromIndex, long size, long length) {

        if (fromIndex < 0L) {

            throw new IndexOutOfBoundsException();
        }
        else if (size < 0L) {

            throw new IndexOutOfBoundsException();
        }
        else if (fromIndex + size > length) {

            throw new IndexOutOfBoundsException();
        }
        else if (length < 0L) {

            throw new IndexOutOfBoundsException();
        }
    }

    public static void checkFromToIndex(int fromIndex, int toIndex, int length) {

        if (fromIndex < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (fromIndex > toIndex) {

            throw new IndexOutOfBoundsException();
        }
        else if (toIndex > length) {

            throw new IndexOutOfBoundsException();
        }
        else if (length < 0) {

            throw new IndexOutOfBoundsException();
        }
    }

    public static void checkFromIndexNum(int fromIndex, int num, int limit) {

        if (fromIndex < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (num < 0) {

            throw new IllegalArgumentException();
        }
        else if (limit < 0) {

            throw new IllegalArgumentException();
        }
        else if (num == 0) {

            if (limit == 0) {

                if (fromIndex != 0) {

                    throw new IndexOutOfBoundsException();
                }
            }
            else if (fromIndex >= limit) {

                throw new IndexOutOfBoundsException();
            }
        }
        else {
            if (fromIndex >= limit) {

                throw new IndexOutOfBoundsException();
            }
            else if (fromIndex + num > limit) {

                throw new IllegalArgumentException();
            }
        }
    }

    public static void checkIndex(int index, int length) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= length) {

            throw new IndexOutOfBoundsException();
        }
        else if (length < 0) {

            throw new IndexOutOfBoundsException();
        }
    }

    public static void checkIndex(long index, long length) {

        if (index < 0L) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= length) {

            throw new IndexOutOfBoundsException();
        }
        else if (length < 0L) {

            throw new IndexOutOfBoundsException();
        }
    }

    public static <T> void checkArrayIndex(int[] array, int index) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= array.length) {

            throw new IndexOutOfBoundsException();
        }
    }

    public static <T> void checkArrayIndex(long[] array, int index) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= array.length) {

            throw new IndexOutOfBoundsException();
        }
    }

    public static <T> void checkArrayIndex(T[] array, int index) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= array.length) {

            throw new IndexOutOfBoundsException();
        }
    }

    public static <T> void checkArrayLength(T[] array, int length) {

        if (length < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (length > array.length) {

            throw new IndexOutOfBoundsException();
        }
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

    public static <T> void areNotSame(T object1, T object2) {

        if (object1 == object2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isLessThan(int value1, int value2) {

        if (value1 >= value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isLessThanOrEqualTo(int value1, int value2) {

        if (value1 > value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isGreaterThan(int value1, int value2) {

        if (value1 <= value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isGreaterThanOrEqualTo(int value1, int value2) {

        if (value1 < value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void areEqual(int value1, int value2) {

        if (value1 != value2) {

            throw new IllegalArgumentException();
        }
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

    public static void isGreaterThan(long value1, long value2) {

        if (value1 <= value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isGreaterThanOrEqualTo(long value1, long value2) {

        if (value1 < value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void areEqual(long value1, long value2) {

        if (value1 != value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isLessThan(float value1, float value2) {

        if (value1 >= value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isLessThanOrEqualTo(float value1, float value2) {

        if (value1 > value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isGreaterThan(float value1, float value2) {

        if (value1 <= value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isGreaterThanOrEqualTo(float value1, float value2) {

        if (value1 < value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void areEqual(float value1, float value2) {

        if (value1 != value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isLessThan(double value1, double value2) {

        if (value1 >= value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isLessThanOrEqualTo(double value1, double value2) {

        if (value1 > value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isGreaterThan(double value1, double value2) {

        if (value1 <= value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void isGreaterThanOrEqualTo(double value1, double value2) {

        if (value1 < value2) {

            throw new IllegalArgumentException();
        }
    }

    public static void areEqual(double value1, double value2) {

        if (value1 != value2) {

            throw new IllegalArgumentException();
        }
    }

    public static <T> void areEqual(T object1, T object2) {

        if (!object1.equals(object2)) {

            throw new IllegalArgumentException();
        }
    }

    public static <T> void areNotEqual(T object1, T object2) {

        if (object1.equals(object2)) {

            throw new IllegalArgumentException();
        }
    }

    public static int isIntegerPrecision(int value) {

        return isAboveZero(value);
    }

    public static int isDecimalPrecision(int value) {

        return isAboveZero(value);
    }

    public static int isDecimalScale(int value) {

        return isAboveZero(value);
    }

    public static float isLoadFactor(float value) {

        final float margin = 0.01f;

        if (value < margin) {

            throw new IllegalArgumentException();
        }
        else if (value > 1.0f - margin) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    public static <T extends CharSequence> T isFileName(T fileName) {

        if (!checkIsFileName(fileName)) {

            throw new IllegalArgumentException();
        }

        return fileName;
    }

    public static String isFileNamePrefix(String fileNamePrefix) {

        if (!checkIsFileNamePrefix(fileNamePrefix)) {

            throw new IllegalArgumentException();
        }

        return fileNamePrefix;
    }

    public static boolean checkIsFileName(CharSequence fileName) {

        return checkIsPathName(fileName);
    }

    public static <T extends CharSequence> T isDirectoryName(T directoryName) {

        if (!checkIsDirectoryName(directoryName)) {

            throw new IllegalArgumentException();
        }

        return directoryName;
    }

    public static boolean checkIsDirectoryName(CharSequence directoryName) {

        return checkIsPathName(directoryName);
    }

    public static boolean checkIsFileNamePrefix(String fileNamePrefix) {

        return Strings.containsOnly(fileNamePrefix, c -> c >= 'a' && c <= 'z');
    }

    public static <T extends CharSequence> T isPathName(T pathName) {

        if (checkIsPathName(pathName)) {

            throw new IllegalArgumentException();
        }

        return pathName;
    }

    public static boolean checkIsPathName(CharSequence pathName) {

        return CharSequences.isASCIIAlphaNumeric(pathName, c -> c == '_' || c == '.');
    }

    public static String isTableName(String tableName) {

        return isSchemaObjectName(tableName);
    }

    public static String isSchemaObjectName(String tableName) {

        return isSchemaName(tableName);
    }

    public static String isColumnName(String columnName) {

        return isSchemaName(columnName);
    }

    public static String isSchemaName(String schemaName) {

        return isDBName(schemaName);
    }

    public static String isDatabaseName(String databaseName) {

        return isDBName(databaseName);
    }

    public static <T extends CharSequence> T isDBName(T dbName) {

        if (!checkIsDBName(dbName)) {

            throw new IllegalArgumentException();
        }

        return dbName;
    }

    public static boolean checkIsDBName(CharSequence dbName) {

        final boolean result;

        if (dbName.length() == 0) {

            result = false;
        }
        else if (!CharSequences.hasFirstCharacterAndRemaining(dbName, Characters::isASCIIAlpha, c -> Characters.isASCIIAlphaNumeric(c) || c == '_')) {

            result = false;
        }
        else {
            result = true;
        }

        return result;
    }

    public static String isNotEmpty(String string) {

        if (string.isEmpty()) {

            throw new IllegalArgumentException();
        }

        return string;
    }

    public static CharSequence isNotEmpty(CharSequence charSequence) {

        if (charSequence.length() == 0) {

            throw new IllegalArgumentException();
        }

        return charSequence;
    }

    public static String isNotEmptyWithNoBlanks(String string) {

        if (string.isEmpty() || Strings.stringContainsAny(string, Character::isWhitespace)) {

            throw new IllegalArgumentException();
        }

        return string;
    }

    public static <T extends CharSequence> T startsWith(T charSequence, String prefix) {

        if (prefix == null) {

            throw new NullPointerException();
        }
        else if (!CharSequences.startsWith(charSequence, prefix)) {

            throw new IllegalArgumentException();
        }

        return charSequence;
    }

    public static <T extends CharSequence> T containsOnly(T charSequence, int startIndex, int numCharacters, CharPredicate predicate) {

        if (!CharSequences.containsOnly(charSequence, startIndex, numCharacters, predicate)) {

            throw new IllegalArgumentException();
        }

        return charSequence;
    }

    public static String isAlphabetic(String string) {

        if (!Strings.containsOnly(string, Character::isAlphabetic)) {

            throw new IllegalArgumentException();
        }

        return string;
    }

    public static String isJavaIdentifier(String string) {

        return isJavaIdentifier(string, string.length());
    }

    private static String isJavaIdentifier(String string, int numCharacters) {

        if (!Strings.hasFirstCharacterAndRemaining(string, numCharacters, Character::isJavaIdentifierStart, Character::isJavaIdentifierPart)) {

            throw new IllegalArgumentException();
        }

        return string;
    }

    public static String isJavaVariableOrMethodWithoutParameters(String string) {

        final String noParameters = "()";

        final String result;

        if (string.endsWith(noParameters)) {

            result = isJavaIdentifier(string, string.length() - noParameters.length());
        }
        else {
            result = isJavaVariable(string);
        }

        return result;
    }

    public static String isJavaVariable(String string) {

        return isJavaVariable(string, string.length());
    }

    private static String isJavaVariable(String string, int numCharacters) {

        if (!Strings.hasFirstCharacterAndRemaining(string, numCharacters, Character::isJavaIdentifierStart, c -> Character.isJavaIdentifierPart(c) || c == '.')) {

            throw new IllegalArgumentException();
        }

        return string;
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

    public static long[] isNotEmpty(long[] array) {

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

    public static <T extends IContains> T isEmpty(T contains) {

        if (contains.isEmpty()) {

            throw new IllegalArgumentException();
        }

        return contains;
    }

    public static <T extends IContains> T isNotEmpty(T contains) {

        if (contains.isEmpty()) {

            throw new IllegalArgumentException();
        }

        return contains;
    }

    public static <T> void checkNumElements(int numElements, int length) {

        if (numElements < 0) {

            throw new IllegalArgumentException();
        }
        else if (numElements > length) {

            throw new IllegalArgumentException();
        }
    }

    public static <T> void checkNumElements(long[] array, int numElements) {

        if (numElements < 0) {

            throw new IllegalArgumentException();
        }
        else if (numElements > array.length) {

            throw new IllegalArgumentException();
        }
    }

    public static <T> void checkNumElements(T[] array, int numElements) {

        if (numElements < 0) {

            throw new IllegalArgumentException();
        }
        else if (numElements > array.length) {

            throw new IllegalArgumentException();
        }
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

    public static <T, U extends IIndexList<T>, P> U areElements(U list, P parameter, BiPredicate<T, P> predicate) {

        final long numElements = list.getNumElements();

        for (long i = 0L; i < numElements; ++ i) {

            if (!predicate.test(list.get(i), parameter)) {

                throw new IllegalArgumentException();
            }
        }

        return list;
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

    public static long isIndexNotOutOfBounds(long index) {

        if (index < 0L) {

            throw new IndexOutOfBoundsException();
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

    public static long isNumElementsAboveZero(long numElements) {

        if (numElements < 1L) {

            throw new IllegalArgumentException();
        }

        return numElements;
    }

    public static int isArrayLimit(int limit) {

        if (limit < 0) {

            throw new IllegalArgumentException();
        }

        return limit;
    }

    public static long isArrayLimit(long limit) {

        if (limit < 0L) {

            throw new IllegalArgumentException();
        }

        return limit;
    }

    public static int isArrayCapacity(int capacity) {

        if (capacity < 0) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    public static void areSameNumElements(IElements elements1, IElements elements2) {

        if (elements1.getNumElements() != elements2.getNumElements()) {

            throw new IllegalArgumentException();
        }
    }

    public static void areSameLength(int[] array1, int[] array2) {

        if (array1.length != array2.length) {

            throw new IllegalArgumentException();
        }
    }

    public static void isSameLimit(IOneDimensionalArrayGetters array1, IOneDimensionalArrayGetters array2) {

        if (array1.getLimit() != array2.getLimit()) {

            throw new IllegalArgumentException();
        }
    }

    public static int isInitialCapacity(int initialCapacity) {

        if (initialCapacity < 1) {

            throw new IllegalArgumentException();
        }

        return initialCapacity;
    }

    public static long isInitialCapacity(long initialCapacity) {

        if (initialCapacity < 1L) {

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

    public static long isCapacity(long capacity) {

        if (capacity < 0L) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    public static int isCapacityAboveZero(int capacity) {

        if (capacity < 1) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    public static long isCapacityAboveZero(long capacity) {

        if (capacity < 1L) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    public static final int MAX_INT_CAPACITY_EXPONENT = Integer.SIZE - 2;

    public static final int MAX_LONG_CAPACITY_EXPONENT = Long.SIZE - 2;

    public static int isInitialIntCapacityExponent(int initialCapacityExponent) {

        return isIntCapacityExponent(initialCapacityExponent);
    }

    public static int isIntCapacityExponent(int capacityExponent) {

        if (capacityExponent < 0) {

            throw new IllegalArgumentException();
        }
        else if (capacityExponent > MAX_INT_CAPACITY_EXPONENT) {

            throw new IllegalArgumentException();
        }

        return capacityExponent;
    }

    public static int isLongCapacityExponent(int capacityExponent) {

        if (capacityExponent < 0) {

            throw new IllegalArgumentException();
        }
        else if (capacityExponent > MAX_LONG_CAPACITY_EXPONENT) {

            throw new IllegalArgumentException();
        }

        return capacityExponent;
    }

    public static int isIntCapacityExponentIncrease(int capacityExponentIncrease) {

        if (capacityExponentIncrease < 1) {

            throw new IllegalArgumentException();
        }
        else if (capacityExponentIncrease > MAX_INT_CAPACITY_EXPONENT) {

            throw new IllegalArgumentException();
        }

        return capacityExponentIncrease;
    }

    public static int isLongCapacityExponentIncrease(int capacityExponentIncrease) {

        if (capacityExponentIncrease < 1) {

            throw new IllegalArgumentException();
        }
        else if (capacityExponentIncrease > MAX_LONG_CAPACITY_EXPONENT) {

            throw new IllegalArgumentException();
        }

        return capacityExponentIncrease;
    }

    public static int isCapacityExponentIncrease(int capacityExponentIncrease, boolean longCapacity) {

        return longCapacity ? isLongCapacityExponentIncrease(capacityExponentIncrease) : isIntCapacityExponentIncrease(capacityExponentIncrease);
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

    public static long isLengthAboveOrAtZero(long length) {

        if (length < 0L) {

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

    public static long isLengthAboveZero(long length) {

        if (length < 1L) {

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

    public static long isNumBytes(long numBytes) {

        if (numBytes < 1) {

            throw new IllegalArgumentException();
        }

        return numBytes;
    }

    public static long isNumCharacters(long numCharacters) {

        if (numCharacters < 1) {

            throw new IllegalArgumentException();
        }

        return numCharacters;
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

    public static void checkBuffer(Buffer buffer, int offset, int length) {

        checkFromIndexSize(offset, length, buffer.remaining());
    }

    public static <T extends Buffer> T isEmpty(T buffer) {

        if (buffer.position() != 0) {

            throw new IllegalArgumentException();
        }

        return buffer;
    }

    public static int isTableId(int tableId) {

        return isSchemaObjectId(tableId);
    }

    public static int isViewId(int viewId) {

        return isSchemaObjectId(viewId);
    }

    public static int isIndexId(int indexId) {

        return isSchemaObjectId(indexId);
    }

    public static int isTriggerId(int triggerId) {

        return isSchemaObjectId(triggerId);
    }

    public static int isDBFunctionId(int dbFunctionId) {

        return isSchemaObjectId(dbFunctionId);
    }

    public static int isProcedureId(int procedureId) {

        return isSchemaObjectId(procedureId);
    }

    public static int isSchemaObjectId(int schemaObjectId) {

        return isDBNamedIdentifiableId(schemaObjectId);
    }

    public static int isColumnId(int columnId) {

        return isDBNamedIdentifiableId(columnId);
    }

    private static int isDBNamedIdentifiableId(int dbNamedIdentifiableId) {

        if (dbNamedIdentifiableId < 0) {

            throw new IllegalArgumentException();
        }

        return dbNamedIdentifiableId;
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

        if (!checkIsDescriptor(descriptor)) {

            throw new IllegalArgumentException();
        }

        return descriptor;
    }

    public static boolean checkIsDescriptor(int descriptor) {

        return descriptor >= 0;
    }

    public static int isDatabaseId(int databaseId) {

        return isDescriptor(databaseId);
    }

    public static boolean checkIsDatabaseId(int databaseId) {

        return checkIsDescriptor(databaseId);
    }

    public static int isSessionDescriptor(int sessionDescriptor) {

        return isDescriptor(sessionDescriptor);
    }

    public static int isPreparedStatementId(int preparedStatementId) {

        return isDescriptor(preparedStatementId);
    }

    public static int isTransactionDescriptor(int transactionDescriptor) {

        return isDescriptor(transactionDescriptor);
    }

    public static int isStatementId(int statementId) {

        if (statementId < 0) {

            throw new IllegalArgumentException();
        }

        return statementId;
    }

    public static long isLargeObjectRef(long largeObjectRed) {

        if (largeObjectRed < 0L) {

            throw new IllegalArgumentException();
        }

        return largeObjectRed;
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
