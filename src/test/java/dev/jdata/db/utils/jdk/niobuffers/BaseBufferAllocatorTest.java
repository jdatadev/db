package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.Buffer;

import dev.jdata.db.utils.allocators.BaseArrayMinimumCapacityAllocatorTest;

abstract class BaseBufferAllocatorTest<T extends Buffer, A extends BaseBufferAllocator<T>> extends BaseArrayMinimumCapacityAllocatorTest<T, A> {

}
