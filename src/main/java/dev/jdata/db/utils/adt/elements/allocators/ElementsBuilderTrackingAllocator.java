package dev.jdata.db.utils.adt.elements.allocators;

import dev.jdata.db.utils.adt.elements.builders.IElementsBuilder;
import dev.jdata.db.utils.allocators.BuilderTrackingAllocator;

public abstract class ElementsBuilderTrackingAllocator<T, U extends IElementsBuilder<T>> extends BuilderTrackingAllocator<U> implements IElementsBuilderAllocator<T, U> {

}
