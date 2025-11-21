package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.marker.IIterable;

interface INodeListIterable extends IIterable {

    long getNextNode(long node);
}
