package dev.jdata.db.utils.allocators;

import org.jutils.io.strings.StringResolver.CharacterBuffer;
import org.jutils.io.strings.StringResolver.ICharactersBufferAllocator;

import dev.jdata.db.utils.adt.arrays.InheritableArrayAllocator;
import dev.jdata.db.utils.checks.Checks;

public final class CharacterBuffersAllocator extends InheritableArrayAllocator<CharacterBuffer[]> implements ICharactersBufferAllocator {

    public CharacterBuffersAllocator() {
        super(CharacterBuffer[]::new, a -> a.length);
    }

    @Override
    public CharacterBuffer[] allocateCharacterBuffers(int minimumCapacity) {

        final CharacterBuffer[] characterBuffers = allocateFromFreeListOrCreateCapacityInstance(minimumCapacity);

        final int arrayLength = characterBuffers.length;

        for (int i = 0; i < arrayLength; ++ i) {

            if (characterBuffers[i] == null) {

                characterBuffers[i] = new CharacterBuffer();
            }
        }

        return characterBuffers;
    }

    @Override
    public CharacterBuffer[] reallocateCharacterBuffers(CharacterBuffer[] characterBuffers, int minimumCapacity) {

        final int characterBuffersLength = characterBuffers.length;

        Checks.isGreaterThan(minimumCapacity, characterBuffersLength);

        final CharacterBuffer[] newCharacterBuffers = allocateCharacterBuffers(minimumCapacity);

        for (int i = 0; i < characterBuffersLength; ++ i) {

            final CharacterBuffer oldCharacterBuffer = characterBuffers[i];

            newCharacterBuffers[i].initialize(oldCharacterBuffer);
        }

        freeCharacterBuffers(characterBuffers);

        return newCharacterBuffers;
    }

    @Override
    public void freeCharacterBuffers(CharacterBuffer[] characterBuffers) {

        freeArrayInstance(characterBuffers);
    }
}
