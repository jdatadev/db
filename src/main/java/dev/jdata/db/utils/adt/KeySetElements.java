package dev.jdata.db.utils.adt;

import java.util.Set;

public interface KeySetElements<K> extends KeyElements {

    Set<K> unmdifiableKeySet();
}
