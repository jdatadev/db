package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.mutability.IImmutable;
import dev.jdata.db.utils.adt.mutability.IImmutableBuilder;

public interface IImmutableInstanceBuilderAllocator<T extends IImmutable, U extends IImmutableBuilder<T, ?>> extends IInstanceBuilderAllocator<U>, IImmutableInstanceFreer<T> {

}
