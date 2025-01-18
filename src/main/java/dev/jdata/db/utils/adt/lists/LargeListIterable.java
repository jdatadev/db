package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.Elements;

public interface LargeListIterable extends Elements {

    long getNextNode(long node);
}
