package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.mutability.IMutableMarker;

interface ISingleHeadNodeListMutable extends IMutableMarker, IClearable, IHeadListMutators, ISingleHeadNodeListMutators {

}
