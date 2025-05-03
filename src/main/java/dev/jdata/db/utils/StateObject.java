package dev.jdata.db.utils;

import java.util.Objects;

import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

public abstract class StateObject<T extends Enum<T> & State> extends ObjectCacheNode implements PrintDebug {

    public static enum NoStatesEnum {

    }

    private final boolean debug;

    private T state;

    protected StateObject(T initialState, boolean debug) {

        this.state = Objects.requireNonNull(initialState);
        this.debug = debug;
    }

    protected final T getState() {
        return state;
    }

    protected final void setState(T nextState) {

        Objects.requireNonNull(nextState);
        Checks.areNotEqual(nextState, state);

        if (debug) {

            enter(b -> b.add("nextState", nextState.name()).add("this.state", state));
        }

        this.state = nextState;

        if (debug) {

            exit();
        }
    }

    protected final void checkIsInitializable() {

        if (!getState().isInitializable()) {

            throw new IllegalStateException();
        }
    }
}
