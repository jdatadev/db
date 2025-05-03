package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.ILongElementsMutators;

public interface ILongSetMutators extends ILongElementsMutators {

    boolean addToSet(long value);
}
