package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.mutability.IImmutable;

public interface IImmutableInstanceFreer<T extends IImmutable> {

    void freeImmutable(T immutable);
}
