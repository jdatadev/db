package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

interface IKeyValueMapGettersHelper<KEYS_ADDABLE extends IAnyOrderAddable, VALUES_ADDER extends IAnyOrderAddable> extends IKeyValueMapGettersMarker {

    long keysAndValues(KEYS_ADDABLE keysDst, VALUES_ADDER valuesDst);
}
