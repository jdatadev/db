package dev.jdata.db.engine.descriptorables;

import java.util.function.IntFunction;

import dev.jdata.db.utils.State;
import dev.jdata.db.utils.adt.lists.MultiTypeFreeList;

@Deprecated // currently not in use
public abstract class BaseMultiTypeDescriptorables<T extends Enum<T> & State, U extends BaseDescriptorable<T>> extends BaseDescriptorables<T, U, MultiTypeFreeList<U>> {

    @SafeVarargs
    protected BaseMultiTypeDescriptorables(IntFunction<U[]> createArray, Class<? extends U> ... typesToAllocate) {
        super(new MultiTypeFreeList<>(createArray, typesToAllocate), createArray);
    }

    protected final <F> U addDescriptorable(Class<? extends U> typeToAllocate, F factoryParameter, DescriptorableFactory<F, U> descriptorableFactory) {

        return super.addDescriptorable(factoryParameter, typeToAllocate, descriptorableFactory, (l, t) -> l.allocate(t));
    }
}
