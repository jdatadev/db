package dev.jdata.db.utils.adt.elements;

public interface IMutableIntElements extends IIntElementsMutators, IIntElements, IMutableElements {

    @Override
    default void addAll(IIntElements intElements) {

        intElements.forEach(this, (e, i) -> i.add(e));
    }
}
