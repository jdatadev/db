package dev.jdata.db.test.unit.assertj;

import java.util.Collection;

import dev.jdata.db.utils.adt.KeyElements;
import dev.jdata.db.utils.adt.KeySetElements;
import dev.jdata.db.utils.adt.arrays.IOneDimensionalArrayCommon;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.maps.ILongContainsKeyMap;
import dev.jdata.db.utils.adt.maps.IMapOfCollection;
import dev.jdata.db.utils.adt.sets.MutableIntBucketSet;
import dev.jdata.db.utils.adt.sets.MutableLongBucketSet;

public class CustomAssertJAssertions extends org.assertj.core.api.Assertions {

    public static ElementsAssert assertThat(IElements actual) {

        return new ElementsAssert(actual);
    }

    public static OneDimensionalArrayAssert assertThat(IOneDimensionalArrayCommon actual) {

        return new OneDimensionalArrayAssert(actual);
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

    public static IntBucketSetAssert assertThat(MutableIntBucketSet actual) {

        return new IntBucketSetAssert(actual);
    }

    public static LongBucketSetAssert assertThat(MutableLongBucketSet actual) {

        return new LongBucketSetAssert(actual);
    }

    public static LongKeyMapAssert assertThat(ILongContainsKeyMap actual) {

        return new LongKeyMapAssert(actual);
    }
}
