package dev.jdata.db.sql.ast;

import dev.jdata.db.sql.parse.IParserAllocator;
import dev.jdata.db.utils.adt.numbers.decimals.ICachedMutableDecimalAllocator;
import dev.jdata.db.utils.adt.numbers.integers.ICachedMutableLargeIntegerAllocator;

public interface ISQLAllocator extends IParserAllocator, ICachedMutableDecimalAllocator, ICachedMutableLargeIntegerAllocator {

}
