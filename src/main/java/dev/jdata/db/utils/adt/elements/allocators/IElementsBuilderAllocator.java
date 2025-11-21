package dev.jdata.db.utils.adt.elements.allocators;

import dev.jdata.db.utils.adt.elements.builders.IElementsBuilder;
import dev.jdata.db.utils.allocators.IBuilderAllocator;

public interface IElementsBuilderAllocator<T, U extends IElementsBuilder<T>> extends IBuilderAllocator<U> {

}
