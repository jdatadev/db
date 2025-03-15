package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IElements;

public interface LargeListIterable extends IElements {

    long getNextNode(long node);
}
