package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntElementsMutators;

public interface IIntSetMutators extends IIntElementsMutators, ISetMutators {

    boolean addToSet(int value);
}
