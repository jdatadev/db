package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.builders.IBuilder;

public interface IInstanceBuilderAllocator<T extends IBuilder> extends IBuilderAllocator<T> {

    T createBuilder();
}
