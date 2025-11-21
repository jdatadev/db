package dev.jdata.db.utils.scalars.mutable;

abstract class BaseMutable {

    enum State {

        UNDEFINED,
        NULL,
        SET;
    }

    private State state;

    BaseMutable() {

        this.state = State.UNDEFINED;
    }

    public final void setToNull() {

        this.state = State.NULL;
    }

    final void markAsSet() {

        this.state = State.SET;
    }

    final void checkIsSet() {

        if (state != State.SET) {

            throw new IllegalStateException();
        }
    }
}
