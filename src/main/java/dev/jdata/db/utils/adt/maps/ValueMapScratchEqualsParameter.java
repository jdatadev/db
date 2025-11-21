package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.marker.IEqualityTesterMarker;

abstract class ValueMapScratchEqualsParameter<M extends IBaseMapGetters<?> & IToValueEqualsGettersMarker, P1, P2, T extends IEqualityTesterMarker<P1, P2, E>, E extends Exception>

        implements IResettable {

    private P1 thisParameter;
    private M other;
    private P2 otherParameter;
    private T equalityTester;

    ValueMapScratchEqualsParameter() {

    }

    ValueMapScratchEqualsParameter(P1 thisParameter, M other, P2 otherParameter, T equalityTester) {
        this();

        initialize(thisParameter, other, otherParameter, equalityTester);
    }

    final void initialize(P1 thisParameter, M other, P2 otherParameter, T equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        this.thisParameter = Initializable.checkNotYetInitialized(this.thisParameter, thisParameter);
        this.other = Initializable.checkNotYetInitialized(this.other, other);
        this.otherParameter = Initializable.checkNotYetInitialized(this.otherParameter, otherParameter);
        this.equalityTester = Initializable.checkNotYetInitialized(this.equalityTester, equalityTester);
    }

    @Override
    public void reset() {

        this.other = Initializable.checkResettable(other);
        this.equalityTester = Initializable.checkResettable(equalityTester);
        this.thisParameter = null;
        this.otherParameter = null;
    }

    final P1 getThisParameter() {
        return thisParameter;
    }

    final M getOther() {
        return other;
    }

    final P2 getOtherParameter() {
        return otherParameter;
    }

    final T getEqualityTester() {
        return equalityTester;
    }

    final boolean isInitialized() {

        return other != null;
    }
}
