package dev.jdata.db.utils.adt.maps;

public interface ILongToObjectDynamicMapAllocator<

                VALUE,
                IMMUTABLE extends IBaseLongToObjectDynamicMap<VALUE>,
                MUTABLE extends IBaseMutableLongToObjectDynamicMap<VALUE>,
                BUILDER extends IBaseLongToObjectDynamicMapBuilder<VALUE, IMMUTABLE, ?>>

        extends IBaseLongToObjectDynamicMapAllocator<VALUE, IMMUTABLE, MUTABLE, BUILDER> {

}

