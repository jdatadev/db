package dev.jdata.db.utils.allocators;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.jutils.io.strings.StringResolver.CharacterBuffer;

public final class CharacterBuffersAllocatorTest extends BaseArrayMinimumCapacityAllocatorTest<CharacterBuffer[], CharacterBuffersAllocator> {

    @Test
    @Category(UnitTest.class)
    public void testInitializes() {

        final CharacterBuffersAllocator allocator = new CharacterBuffersAllocator();

        final int capacity = 10;

        final CharacterBuffer[] characterBuffers = allocator.allocateCharacterBuffers(capacity);

        assertThat(characterBuffers.length).isEqualTo(capacity);

        for (int i = 0; i < capacity; ++ i) {

            assertThat(characterBuffers[i]).isNotNull();
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testReallocateArguments() {

        final CharacterBuffersAllocator allocator = new CharacterBuffersAllocator();

        final int capacity = 10;

        final CharacterBuffer[] characterBuffers = allocator.allocateCharacterBuffers(capacity);

        assertThatThrownBy(() -> allocator.reallocateCharacterBuffers(characterBuffers, capacity - 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> allocator.reallocateCharacterBuffers(characterBuffers, capacity)).isInstanceOf(IllegalArgumentException.class);

        final int newCapacity = capacity + 1;

        assertThat(allocator.reallocateCharacterBuffers(characterBuffers, newCapacity).length).isEqualTo(newCapacity);

        assertThat(allocator.allocateFromFreeListOrCreateCapacityInstance(capacity)).isSameAs(characterBuffers);
    }

    @Test
    @Category(UnitTest.class)
    public void testReallocate() {

        final CharacterBuffersAllocator allocator = new CharacterBuffersAllocator();

        final int capacity = 10;

        final CharacterBuffer[] characterBuffers = allocator.allocateCharacterBuffers(capacity);

        assertThat(characterBuffers.length).isEqualTo(capacity);

        for (int i = 0; i < capacity; ++ i) {

            assertThat(characterBuffers[i]).isNotNull();
        }

        final int newCapacity = capacity * 2;

        final CharacterBuffer[] newCharacterBuffers = allocator.reallocateCharacterBuffers(characterBuffers, newCapacity);

        for (int i = 0; i < capacity; ++ i) {

            assertThat(newCharacterBuffers[i]).isSameAs(characterBuffers[i]);
        }

        for (int i = capacity; i < newCapacity; ++ i) {

            assertThat(newCharacterBuffers[i]).isNull();
        }
    }

    @Override
    protected CharacterBuffer[] allocate(CharacterBuffersAllocator allocator, int minimumCapacity) {

        return allocator.allocateCharacterBuffers(minimumCapacity);
    }

    @Override
    protected CharacterBuffersAllocator createAllocator() {

        return new CharacterBuffersAllocator();
    }

    @Override
    protected CharacterBuffer[][] allocateArray(int length) {

        return new CharacterBuffer[length][];
    }

    @Override
    protected void free(CharacterBuffersAllocator allocator, CharacterBuffer[] instance) {

        allocator.freeCharacterBuffers(instance);
    }
}
