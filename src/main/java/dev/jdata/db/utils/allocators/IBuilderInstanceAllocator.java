package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.builders.IObjectBuilder;

public interface IBuilderInstanceAllocator<T, U extends IObjectBuilder<T, U>> extends IBuilderAllocator<U> {

}
