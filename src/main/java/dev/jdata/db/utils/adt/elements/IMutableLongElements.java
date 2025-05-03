package dev.jdata.db.utils.adt.elements;

public interface IMutableLongElements extends ILongElementsMutators, ILongElements, IMutableElements {

    @Override
    default void addAll(ILongElements longElements) {

        longElements.forEach(this, (e, i) -> i.add(e));
    }
}
