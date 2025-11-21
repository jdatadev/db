package dev.jdata.db.utils.adt.maps;

import java.util.Collection;

import dev.jdata.db.utils.adt.KeySetElements;
import dev.jdata.db.utils.adt.elements.IOnlyElementsMutable;

public interface IMapOfCollection<K, V, C extends Collection<V>> extends IOnlyElementsMutable, KeySetElements<K> {

    void add(K key, V value);

    C getUnmodifiable(K key);
}
