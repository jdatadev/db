package dev.jdata.db.test.unit.assertj;

import java.util.Collection;

import dev.jdata.db.utils.adt.Elements;
import dev.jdata.db.utils.adt.KeyElements;
import dev.jdata.db.utils.adt.KeySetElements;
import dev.jdata.db.utils.adt.maps.IMapOfCollection;

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
}
