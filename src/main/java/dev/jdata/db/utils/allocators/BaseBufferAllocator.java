package dev.jdata.db.utils.allocators;

import java.nio.Buffer;
import java.util.function.IntFunction;

abstract class BaseBufferAllocator<T extends Buffer> extends BaseArrayAllocator<T> {

    BaseBufferAllocator(IntFunction<T> createArray) {
        super(createArray, Buffer::capacity);
    }
}
