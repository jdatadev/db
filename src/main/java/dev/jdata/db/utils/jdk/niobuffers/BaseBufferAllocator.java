package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.Buffer;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.BaseIntCapacityInstanceAllocator;

abstract class BaseBufferAllocator<T extends Buffer> extends BaseIntCapacityInstanceAllocator<T> {

    BaseBufferAllocator(IntFunction<T> createArray) {
        super(createArray, (create, capacity) -> create.apply(capacity), Buffer::capacity);
    }
}
