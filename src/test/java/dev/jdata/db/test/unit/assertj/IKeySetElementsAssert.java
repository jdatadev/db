package dev.jdata.db.test.unit.assertj;

public interface IKeySetElementsAssert<S extends IKeySetElementsAssert<S, K>, K> extends IKeyElementsAssert<S> {

    S hasUnmodifiableKeySet(@SuppressWarnings("unchecked") K ... keys);
}
