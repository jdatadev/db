package dev.jdata.db.utils.adt.byindex;

interface IByIndexToStringGetters extends IByIndexGettersMarker {

    void toString(long index, StringBuilder sb);

    void toHexString(long index, StringBuilder sb);
}
