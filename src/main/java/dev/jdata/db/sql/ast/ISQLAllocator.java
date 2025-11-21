package dev.jdata.db.sql.ast;

import dev.jdata.db.sql.parse.IParserAllocator;
import dev.jdata.db.utils.adt.decimals.IMutableDecimalAllocator;
import dev.jdata.db.utils.allocators.ILargeIntegerAllocator;

public interface ISQLAllocator extends IParserAllocator, IMutableDecimalAllocator, ILargeIntegerAllocator {

}
