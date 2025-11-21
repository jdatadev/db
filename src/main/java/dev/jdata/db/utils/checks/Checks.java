package dev.jdata.db.utils.checks;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.nio.Buffer;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dev.jdata.db.DBConstants;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.elements.IScatteredElementsView;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.function.CharPredicate;
import dev.jdata.db.utils.instances.Instances;
import dev.jdata.db.utils.jdk.adt.strings.CharSequences;
import dev.jdata.db.utils.jdk.adt.strings.Characters;
import dev.jdata.db.utils.jdk.adt.strings.Strings;

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

    public static void areAnyNotNull(Object instance1, Object instance2) {

        if (!Instances.areAnyNotNull(instance1, instance2)) {

            throw new IllegalArgumentException();
        }
    }

    public static void areBothNotNullOrBothNull(Object instance1, Object instance2) {

        Instances.areBothNotNullOrBothNullOrThrowException(instance1, instance2, IllegalArgumentException::new);
    }

    public static void areAllNotNullOrAllNull(Object instance1, Object instance2, Object instance3) {

        Instances.areAllNotNullOrAllNullOrThrowException(instance1, instance2, instance3, IllegalArgumentException::new);
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

    public static void checkArrayFromIndexSize(Object array, int fromIndex, int size) {

        checkFromIndexSize(fromIndex, size, Array.getLength(array));
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

    public static void checkFromIndexNum(long fromIndex, long num, long limit) {

        if (fromIndex < 0L) {

            throw new IndexOutOfBoundsException();
        }
        else if (num < 0L) {

            throw new IllegalArgumentException();
        }
        else if (limit < 0L) {

            throw new IllegalArgumentException();
        }
        else if (num == 0L) {

            if (limit == 0L) {

                if (fromIndex != 0L) {

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

    public static void checkLongIndex(long index, long length) {

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

    public static void checkIntIndexAndNumElements(int index, int numElements) {

        Checks.isIntIndex(index);
        Checks.isIntNumElements(numElements);
        Checks.checkIndex(index, numElements);
    }

    public static void checkIntIndexAndNumElements(long index, long numElements) {

        Checks.isIntIndex(index);
        Checks.isIntNumElements(numElements);
        Checks.checkLongIndex(index, numElements);
    }

    public static void checkLongIndexAndNumElements(long index, long numElements) {

        Checks.isLongIndex(index);
        Checks.isLongNumElements(numElements);
        Checks.checkLongIndex(index, numElements);
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

    public static void checkArrayLength(int[] array, int length) {

        if (length < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (length > array.length) {

            throw new IndexOutOfBoundsException();
        }
    }

    public static void checkArrayLength(long[] array, int length) {

        if (length < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (length > array.length) {

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

    public static <T> void areSame(T object1, T object2) {

        if (object1 != object2) {

            throw new IllegalArgumentException();
        }
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

    public static void areNotEqual(int value1, int value2) {

        if (value1 == value2) {

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

    public static void areNotEqual(long value1, long value2) {

        if (value1 == value2) {

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

    public static void areNotEqual(float value1, float value2) {

        if (value1 == value2) {

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

    public static void areNotEqual(double value1, double value2) {

        if (value1 == value2) {

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

    public static Path isAbsolutePath(Path path) {

        if (!path.isAbsolute()) {

            throw new IllegalArgumentException();
        }

        return path;
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

        if (!checkIsPathName(pathName)) {

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

    public static <T extends IContainsView> T isEmpty(T contains) {

        if (contains.isEmpty()) {

            throw new IllegalArgumentException();
        }

        return contains;
    }

    public static <T extends IContainsView> T isNotEmpty(T contains) {

        if (contains.isEmpty()) {

            throw new IllegalArgumentException();
        }

        return contains;
    }

    public static <T, U extends IObjectIterableElementsView<T>> U isNotEmpty(U elements) {

        if (IObjectIterableElementsView.isEmpty(elements)) {

            throw new IllegalArgumentException();
        }

        return elements;
    }

    public static void checkIntNumElements(int numElements, int length) {

        if (numElements < 0) {

            throw new IllegalArgumentException();
        }
        else if (numElements > length) {

            throw new IllegalArgumentException();
        }
    }

    public static void checkIntNumElements(long numElements, long length) {

        if (numElements < 0L) {

            throw new IllegalArgumentException();
        }
        else if (numElements > Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }
        else if (numElements > length) {

            throw new IllegalArgumentException();
        }
    }

    public static void checkLongNumElements(long numElements, long length) {

        if (numElements < 0L) {

            throw new IllegalArgumentException();
        }
        else if (numElements > length) {

            throw new IllegalArgumentException();
        }
    }

    public static void checkArrayNumElements(Object array, int numElements) {

        if (numElements < 0) {

            throw new IllegalArgumentException();
        }
        else if (numElements > Array.getLength(array)) {

            throw new IllegalArgumentException();
        }
    }

    public static void checkNumElements(int[] array, int numElements) {

        if (numElements < 0) {

            throw new IllegalArgumentException();
        }
        else if (numElements > array.length) {

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

        return areElements(array, 0, array.length, predicate);
    }

    public static <T> T[] areElements(T[] array, int startIndex, int numElements, Predicate<T> predicate) {

        final int arrayLength = array.length;

        for (int i = 0; i < arrayLength; ++ i) {

            if (!predicate.test(array[i])) {

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

    public static int isIntIndex(int index) {

        if (index < 0) {

            throw new IllegalArgumentException();
        }

        return index;
    }

    public static long isIntIndex(long index) {

        if (index < 0L) {

            throw new IllegalArgumentException();
        }

        if (index >= Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return index;
    }

    public static long isLongIndex(long index) {

        if (index < 0L) {

            throw new IllegalArgumentException();
        }

        return index;
    }

    public static long isIntIndexNotOutOfBounds(long index) {

        if (index < 0L) {

            throw new IndexOutOfBoundsException();
        }

        if (index >= Integer.MAX_VALUE) {

            throw new IndexOutOfBoundsException();
        }

        return index;
    }

    public static long isLongIndexNotOutOfBounds(long index) {

        if (index < 0L) {

            throw new IndexOutOfBoundsException();
        }

        return index;
    }

    public static int isIntNumElements(int numElements) {

        if (numElements < 0) {

            throw new IllegalArgumentException();
        }

        return numElements;
    }

    public static long isIntNumElements(long numElements) {

        if (numElements < 0) {

            throw new IllegalArgumentException();
        }

        return numElements;
    }

    public static long isLongNumElements(long numElements) {

        if (numElements < 0L) {

            throw new IllegalArgumentException();
        }

        return numElements;
    }

    public static long isIntOrLongNumElements(long numElements) {

        if (numElements < 0L) {

            throw new IllegalArgumentException();
        }

        return numElements;
    }

    public static long isIntNumElementsAboveZero(int numElements) {

        if (numElements < 1) {

            throw new IllegalArgumentException();
        }

        return numElements;
    }

    public static long isIntNumElementsAboveZero(long numElements) {

        if (numElements < 1L) {

            throw new IllegalArgumentException();
        }

        if (numElements > Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return numElements;
    }

    public static long isLongNumElementsAboveZero(long numElements) {

        if (numElements < 1L) {

            throw new IllegalArgumentException();
        }

        return numElements;
    }

    public static <T> void checkIntAddFromArray(int[] array, int startIndex, int numElements) {

        checkIntAddFromArray(startIndex, numElements, array.length);
    }

    public static <T> void checkIntAddFromArray(long[] array, int startIndex, int numElements) {

        checkIntAddFromArray(startIndex, numElements, array.length);
    }

    public static <T> void checkIntAddFromArray(T[] array, int startIndex, int numElements) {

        Checks.areElements(array, startIndex, numElements, Checks::checkIsNotNull);

        checkIntAddFromArray(startIndex, numElements, array.length);
    }

    public static <T> void checkIntAddFromArray(int[] array, long startIndex, long numElements) {

        checkIntAddFromArray(startIndex, numElements, array.length);
    }

    public static <T> void checkIntAddFromArray(long[] array, long startIndex, long numElements) {

        checkIntAddFromArray(startIndex, numElements, array.length);
    }

    public static <T> void checkIntAddFromArray(T[] array, long startIndex, long numElements) {

        Checks.areElements(array, (int)isIntIndex(startIndex), (int)isIntNumElements(numElements), Checks::checkIsNotNull);

        checkIntAddFromArray(startIndex, numElements, array.length);
    }

    private static <T> void checkIntAddFromArray(long startIndex, long numElements, long arrayLength) {

        Checks.isIntNumElementsAboveZero(numElements);
        Checks.checkFromIndexSize(startIndex, numElements, arrayLength);
    }

    public static <T> T isArray(T array) {

        if (!array.getClass().isArray()) {

            throw new IllegalArgumentException();
        }

        return array;
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

    public static int isArrayLength(int length) {

        if (length < 0) {

            throw new IllegalArgumentException();
        }

        return length;
    }

    public static void areSameNumElements(IOnlyElementsView elements1, IOnlyElementsView elements2) {

        if (elements1.getNumElements() != elements2.getNumElements()) {

            throw new IllegalArgumentException();
        }
    }

    public static void areSameLength(int[] array1, int[] array2) {

        if (array1.length != array2.length) {

            throw new IllegalArgumentException();
        }
    }

    public static void isSameLimit(IScatteredElementsView scatteredElementsView1, IScatteredElementsView scatteredElementsView2) {

        if (scatteredElementsView1.getLimit() != scatteredElementsView2.getLimit()) {

            throw new IllegalArgumentException();
        }
    }

    public static int isIntInitialCapacity(int initialCapacity) {

        if (initialCapacity < 1) {

            throw new IllegalArgumentException();
        }

        return initialCapacity;
    }

    public static long isIntInitialCapacity(long initialCapacity) {

        if (initialCapacity < 1L) {

            throw new IllegalArgumentException();
        }

        if (initialCapacity > Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return initialCapacity;
    }

    public static long isLongInitialCapacity(long initialCapacity) {

        if (initialCapacity < 1L) {

            throw new IllegalArgumentException();
        }

        return initialCapacity;
    }

    public static int isIntMinimumCapacity(int minimumCapacity) {

        return isIntInitialCapacity(minimumCapacity);
    }

    public static long isIntMinimumCapacity(long minimumCapacity) {

        return isIntInitialCapacity(minimumCapacity);
    }

    public static long isLongMinimumCapacity(long minimumCapacity) {

        return isLongInitialCapacity(minimumCapacity);
    }

    public static int isIntInitialOuterCapacity(int initialOuterCapacity) {

        if (initialOuterCapacity < 1) {

            throw new IllegalArgumentException();
        }

        return initialOuterCapacity;
    }

    public static int isIntCapacity(int capacity) {

        if (capacity < 0) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    public static long isIntCapacity(long capacity) {

        if (capacity < 0L) {

            throw new IllegalArgumentException();
        }

        if (capacity > Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    public static long isLongCapacity(long capacity) {

        if (capacity < 0L) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    public static long isIntOrLongCapacity(long capacity) {

        if (capacity < 0L) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    public static long isCapacity(long capacity, boolean longCapacity) {

        return longCapacity ? isLongCapacity(capacity) : isIntCapacity(capacity);
    }

    public static int isIntCapacityAboveZero(int capacity) {

        if (capacity < 1) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    public static long isLongCapacityAboveZero(long capacity) {

        if (capacity < 1L) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    public static long isIntOrLongCapacityAboveZero(long capacity) {

        if (capacity < 1L) {

            throw new IllegalArgumentException();
        }

        return capacity;
    }

    public static int isOuterIndex(int outerIndex) {

        if (outerIndex < 0) {

            throw new IllegalArgumentException();
        }

        return outerIndex;
    }

    public static int isInitialOuterCapacity(int outerCapacity) {

        if (outerCapacity < 1) {

            throw new IllegalArgumentException();
        }

        return outerCapacity;
    }

    public static int isOuterCapacity(int outerCapacity) {

        if (outerCapacity < 1) {

            throw new IllegalArgumentException();
        }

        return outerCapacity;
    }

    public static int isInnerCapacity(int innerCapacity) {

        if (innerCapacity < 1) {

            throw new IllegalArgumentException();
        }

        return innerCapacity;
    }

    public static long isIntInnerElementCapacity(long innerElementCapacity) {

        if (innerElementCapacity < 1L) {

            throw new IllegalArgumentException();
        }

        return innerElementCapacity;
    }

    private static long isLongInnerElementCapacity(long innerElementCapacity) {

        if (innerElementCapacity < 1L) {

            throw new IllegalArgumentException();
        }

        return innerElementCapacity;
    }

    public static long isIntOrLongInnerElementCapacity(long innerElementCapacity) {

        if (innerElementCapacity < 1L) {

            throw new IllegalArgumentException();
        }

        return innerElementCapacity;
    }

    public static final int MAX_INT_CAPACITY_EXPONENT = Integer.SIZE - 2;

    public static final int MAX_LONG_CAPACITY_EXPONENT = Long.SIZE - 2;

    public static int isIntInitialCapacityExponent(int initialCapacityExponent) {

        return isIntCapacityExponent(initialCapacityExponent);
    }

    public static int isIntInitialOuterCapacityExponent(int initialOuterCapacityExponent) {

        return isIntCapacityExponent(initialOuterCapacityExponent);
    }

    public static int isIntInnerCapacityExponent(int innerCapacityExponent) {

        return isIntCapacityExponent(innerCapacityExponent);
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

    public static long isIntCapacityExponent(long capacityExponent) {

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

    public static int isIntMinimumCapacityExponent(int capacityExponent) {

        return Checks.isIntCapacityExponent(capacityExponent);
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

    public static int isIntOrLongCapacityExponentIncrease(int capacityExponentIncrease) {

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

    public static int isIntOffset(int offset) {

        if (offset < 0) {

            throw new IllegalArgumentException();
        }

        return offset;
    }

    public static long isLongOffset(long offset) {

        if (offset < 0) {

            throw new IllegalArgumentException();
        }

        return offset;
    }

    public static int isIntLengthAboveOrAtZero(int length) {

        if (length < 0) {

            throw new IllegalArgumentException();
        }

        return length;
    }

    public static long isIntLengthAboveOrAtZero(long length) {

        if (length < 0L) {

            throw new IllegalArgumentException();
        }
        else if (length > Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return length;
    }

    public static long isLongLengthAboveOrAtZero(long length) {

        if (length < 0L) {

            throw new IllegalArgumentException();
        }

        return length;
    }

    public static int isIntLengthAboveZero(int length) {

        if (length < 1) {

            throw new IllegalArgumentException();
        }

        return length;
    }

    public static long isIntLengthAboveZero(long length) {

        if (length < 1L) {

            throw new IllegalArgumentException();
        }
        else if (length > Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return length;
    }

    public static long isLongLengthAboveZero(long length) {

        if (length < 1L) {

            throw new IllegalArgumentException();
        }

        return length;
    }

    public static long isIntOrLongLengthAboveZero(long length) {

        if (length < 1L) {

            throw new IllegalArgumentException();
        }

        return length;
    }

    public static <T> T[] atMost(T[] array, int numElements) {

        isIntNumElements(numElements);

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

        if (offset < 0L) {

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

        isIntOffset(offset);
        isIntLengthAboveZero(length);

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
