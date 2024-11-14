package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public final class NumStorageBitsParameters {

    private final int booleanMinNumBits;
    private final int shortMinNumBits;
    private final int intMinNumBits;
    private final int longMinNumBits;
    private final StorageMode stringStorageMode;
    private final Integer numBitsPerStringCharacter;

    private final StorageMode blobStorageMode;
    private final StorageMode textStorageMode;

    public NumStorageBitsParameters(int booleaNMinNumBits, int shortMinNumBits, int intMinNumBits, int longMinNumBits, StorageMode stringStorageMode,
            Integer numBitsPerStringCharacter, StorageMode blobStorageMode, StorageMode textStorageMode) {

        this.booleanMinNumBits = Checks.isNumBits(booleaNMinNumBits);
        this.shortMinNumBits = Checks.isNumBits(shortMinNumBits);
        this.intMinNumBits = Checks.isNumBits(intMinNumBits);
        this.longMinNumBits = Checks.isNumBits(longMinNumBits);
        this.stringStorageMode = Objects.requireNonNull(stringStorageMode);
        this.numBitsPerStringCharacter = numBitsPerStringCharacter != null ? Checks.isNumBits(numBitsPerStringCharacter) : null;
        this.blobStorageMode = Objects.requireNonNull(blobStorageMode);
        this.textStorageMode = Objects.requireNonNull(textStorageMode);
    }

    int getBooleanMinNumBits() {
        return booleanMinNumBits;
    }

    int getShortMinNumBits() {
        return shortMinNumBits;
    }

    int getIntMinNumBits() {
        return intMinNumBits;
    }

    int getLongMinNumBits() {
        return longMinNumBits;
    }

    StorageMode getStringStorageMode() {
        return stringStorageMode;
    }

    Integer getNumBitsPerStringCharacter() {
        return numBitsPerStringCharacter;
    }

    StorageMode getBlobStorageMode() {
        return blobStorageMode;
    }

    StorageMode getTextStorageMode() {
        return textStorageMode;
    }
}
