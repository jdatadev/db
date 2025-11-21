package dev.jdata.db.engine.descriptorables;

import java.util.function.IntFunction;

import dev.jdata.db.utils.State;
import dev.jdata.db.utils.adt.lists.FreeList;

public abstract class BaseSingleTypeDescriptorables<T extends Enum<T> & State, U extends BaseDescriptorable<T>> extends BaseDescriptorables<T, U, FreeList<U>> {

    protected BaseSingleTypeDescriptorables(AllocationType allocationType, IntFunction<U[]> createArray) {
        super(allocationType, new FreeList<>(createArray), createArray);
    }

    protected final <F> U addDescriptorable(F factoryParameter, IDescriptorableFactory<F, U> descriptorableFactory) {

        return addDescriptorable(factoryParameter, null, descriptorableFactory, (l, t) -> l.allocate());
    }
}
