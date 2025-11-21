package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntElementsRandomAccessRemovalMutable;
import dev.jdata.db.utils.adt.elements.IIntOrderedAddTailElementsMutators;

interface IIntSingleHeadNodeListMutators extends INodeListMutatorsMarker, IIntHeadListMutators, IIntElementsRandomAccessRemovalMutable, IIntOrderedAddTailElementsMutators {

    long addHeadAndReturnNode(int value);

    long addTailAndReturnNode(int value);
}
