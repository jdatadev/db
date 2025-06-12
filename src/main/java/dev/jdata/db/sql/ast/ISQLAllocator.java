package dev.jdata.db.sql.ast;

import dev.jdata.db.sql.parse.IParserAllocator;
import dev.jdata.db.utils.allocators.ILargeIntegerAllocator;
import dev.jdata.db.utils.allocators.IMutableDecimalAllocator;

public interface ISQLAllocator extends IParserAllocator, IMutableDecimalAllocator, ILargeIntegerAllocator {

}
