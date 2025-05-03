package dev.jdata.db.sql.parse;

import org.jutils.language.common.names.IArrayOfLongsAllocator;

import dev.jdata.db.utils.allocators.IAddableListAllocator;
import dev.jdata.db.utils.allocators.ILongArrayAllocator;

public interface IParserAllocator extends IArrayOfLongsAllocator, ILongArrayAllocator, IAddableListAllocator {

}
