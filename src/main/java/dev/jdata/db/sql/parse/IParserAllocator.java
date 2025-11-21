package dev.jdata.db.sql.parse;

import org.jutils.language.common.names.IArrayOfLongsAllocator;

import dev.jdata.db.utils.adt.lists.ICachedLongIndexList;
import dev.jdata.db.utils.adt.lists.ICachedLongIndexListBuilder;
import dev.jdata.db.utils.allocators.IAddableListAllocator;

public interface IParserAllocator extends IArrayOfLongsAllocator, IAddableListAllocator {

    ICachedLongIndexListBuilder createLongIndexListBuilder(int minimumCapacity);
    void freeLongIndexListBuilder(ICachedLongIndexListBuilder builder);

    void freeLongIndexList(ICachedLongIndexList list);
}
