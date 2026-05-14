package dev.jdata.db.utils.adt.maps;

interface IBaseLongToObjectDynamicMapAllocator<

                VALUE,
                IMMUTABLE extends IBaseLongToObjectDynamicMap<VALUE>,
                MUTABLE extends IBaseMutableLongToObjectDynamicMap<VALUE>,
                BUILDER extends IBaseLongToObjectDynamicMapBuilder<VALUE, IMMUTABLE, ?>>

        extends IMapAllocator<IMMUTABLE, MUTABLE, BUILDER> {

}
