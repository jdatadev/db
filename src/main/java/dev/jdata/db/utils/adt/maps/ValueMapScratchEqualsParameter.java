package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IEqualityTester;
import dev.jdata.db.utils.adt.IResettable;

abstract class ValueMapScratchEqualsParameter<M extends IKeyMap<?> & IToValueMapGetters, P1, P2, E extends IEqualityTester<P1, P2>> implements IResettable {

    private P1 thisParameter;
    private M other;
    private P2 otherParameter;
    private E equalityTester;

    ValueMapScratchEqualsParameter() {

    }

    ValueMapScratchEqualsParameter(P1 thisParameter, M other, P2 otherParameter, E equalityTester) {
        this();

        initialize(thisParameter, other, otherParameter, equalityTester);
    }

    final void initialize(P1 thisParameter, M other, P2 otherParameter, E equalityTester) {

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

    final E getEqualityTester() {
        return equalityTester;
    }

    final boolean isInitialized() {

        return other != null;
    }
}
