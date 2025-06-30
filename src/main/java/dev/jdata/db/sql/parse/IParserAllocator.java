package dev.jdata.db.sql.parse;

import org.jutils.language.common.names.IArrayOfLongsAllocator;

import dev.jdata.db.utils.adt.lists.CachedLongIndexList;
import dev.jdata.db.utils.adt.lists.CachedLongIndexList.CachedLongIndexListBuilder;
import dev.jdata.db.utils.adt.lists.LongIndexList.ILongIndexListAllocator;
import dev.jdata.db.utils.allocators.IAddableListAllocator;

public interface IParserAllocator extends IArrayOfLongsAllocator, ILongIndexListAllocator<CachedLongIndexList, CachedLongIndexListBuilder>, IAddableListAllocator {

}
