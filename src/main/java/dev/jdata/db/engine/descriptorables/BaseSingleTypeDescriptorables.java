package dev.jdata.db.engine.descriptorables;

import java.util.function.IntFunction;

import dev.jdata.db.utils.State;
import dev.jdata.db.utils.adt.lists.FreeList;

public abstract class BaseSingleTypeDescriptorables<T extends Enum<T> & State, U extends BaseDescriptorable<T>> extends BaseDescriptorables<T, U, FreeList<U>> {

    protected BaseSingleTypeDescriptorables(IntFunction<U[]> createArray) {
        super(new FreeList<>(createArray), createArray);
    }

    protected final <F> U addDescriptorable(F factoryParameter, DescriptorableFactory<F, U> descriptorableFactory) {

        return super.addDescriptorable(factoryParameter, null, descriptorableFactory, (l, t) -> l.allocate());
    }
}
