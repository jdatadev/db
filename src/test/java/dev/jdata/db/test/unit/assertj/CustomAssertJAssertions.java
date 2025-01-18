package dev.jdata.db.test.unit.assertj;

import java.util.Collection;

import dev.jdata.db.utils.adt.KeyElements;
import dev.jdata.db.utils.adt.KeySetElements;
import dev.jdata.db.utils.adt.elements.Elements;
import dev.jdata.db.utils.adt.maps.IMapOfCollection;
import dev.jdata.db.utils.adt.maps.LongKeyMap;
import dev.jdata.db.utils.adt.sets.LongSet;

public class CustomAssertJAssertions extends org.assertj.core.api.Assertions {

    public static ElementsAssert assertThat(Elements actual) {

        return new ElementsAssert(actual);
    }

    public static KeyElementsAssert assertThat(KeyElements actual) {

        return new KeyElementsAssert(actual);
    }

    public static <K> KeySetElementsAssert<K> assertThat(KeySetElements<K> actual) {

        return new KeySetElementsAssert<>(actual);
    }

    public static <K, V, C extends Collection<V>> MapOfCollectionAssert<K, V, C> assertThat(IMapOfCollection<K, V, C> actual) {

        return new MapOfCollectionAssert<>(actual);
    }

    public static LongSetAssert assertThat(LongSet actual) {

        return new LongSetAssert(actual);
    }

    public static LongKeyMapAssert assertThat(LongKeyMap actual) {

        return new LongKeyMapAssert(actual);
    }
}
