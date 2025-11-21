package dev.jdata.db.engine.descriptorables;

import java.util.Objects;

import dev.jdata.db.utils.State;
import dev.jdata.db.utils.StateObject;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseDescriptorable<T extends Enum<T> & State> extends StateObject<T> {

    private final T initialState;

    private int descriptor;

    public BaseDescriptorable(AllocationType allocationType, T initialState, boolean debug) {
        super(allocationType, initialState, debug);

        this.initialState = Objects.requireNonNull(initialState);
    }

    protected final void initialize(int descriptor) {

        Checks.isDescriptor(descriptor);

        if (!getState().isInitializable()) {

            throw new IllegalStateException();
        }

        this.descriptor = descriptor;

        setState(initialState);
    }

    protected final int getDescriptor() {
        return descriptor;
    }

    @Override
    public final boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (object == null) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            final BaseDescriptorable<?> other = (BaseDescriptorable<?>)object;

            result = descriptor == other.descriptor;
        }

        return result;
    }
}
