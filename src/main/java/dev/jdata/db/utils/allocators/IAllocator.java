package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.BiConsumer;

public interface IAllocator {

    public static <T extends IAllocator, U> void safeFree(U instance, T allocator, BiConsumer<T, U> consumer) {

        Objects.requireNonNull(allocator);
        Objects.requireNonNull(consumer);

        if (instance != null) {

            consumer.accept(allocator, instance);
        }
    }
}
