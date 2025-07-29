package dev.jdata.db.utils.allocators;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public final class NodeObjectCacheTest extends BaseAllocatorTest<NodeObjectCacheTest.TestObject, NodeObjectCache<NodeObjectCacheTest.TestObject>> {

    static final class TestObject extends ObjectCacheNode {

    }

    @Test
    @Category(UnitTest.class)
    public void testConstructorArguments() {

        assertThatThrownBy(() -> new NodeObjectCache<>(null)).isInstanceOf(NullPointerException.class);
    }

    @Override
    protected NodeObjectCache<TestObject> createAllocator() {

        return new NodeObjectCache<>(TestObject::new);
    }

    @Override
    TestObject allocate(NodeObjectCache<TestObject> allocator) {

        return allocator.allocate();
    }

    @Override
    protected TestObject[] allocateArray(int length) {

        return new TestObject[length];
    }

    @Override
    protected void free(NodeObjectCache<TestObject> allocator, TestObject instance) {

        allocator.free(instance);
    }

    @Override
    boolean freesInSameOrder() {

        return false;
    }
}
