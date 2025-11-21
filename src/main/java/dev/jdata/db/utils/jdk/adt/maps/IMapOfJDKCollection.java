package dev.jdata.db.utils.jdk.adt.maps;

import java.util.Collection;
import java.util.Set;

import dev.jdata.db.utils.adt.maps.IMapOfCollection;

public interface IMapOfJDKCollection<K, V, C extends Collection<V>> extends IMapOfCollection<K, V, C> {

    Set<K> unmdifiableKeySet();
}
