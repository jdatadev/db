package dev.jdata.db.test.unit.assertj;

interface IKeyElementsAssert<S extends IKeyElementsAssert<S>> {

    S hasNumKeys(int expectedNumKeys);
}
