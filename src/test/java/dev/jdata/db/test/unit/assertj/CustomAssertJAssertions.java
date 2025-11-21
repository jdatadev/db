package dev.jdata.db.test.unit.assertj;

import java.util.Collection;

import dev.jdata.db.utils.adt.KeyElements;
import dev.jdata.db.utils.adt.KeySetElements;
import dev.jdata.db.utils.adt.arrays.IOneDimensionalArrayView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.adt.maps.ILongContainsKeyMapView;
import dev.jdata.db.utils.adt.maps.IMapOfCollection;
import dev.jdata.db.utils.adt.sets.MutableIntBucketSet;
import dev.jdata.db.utils.adt.sets.MutableLongBucketSet;

public class CustomAssertJAssertions extends org.assertj.core.api.Assertions {

    public static <T extends CharSequence> CharSequenceAssert<T> assertThatCharSeq(T actual) {

        return new CharSequenceAssert<>(actual);
    }

    public static ElementsAssert assertThat(IOnlyElementsView actual) {

        return new ElementsAssert(actual);
    }

    public static <T> ObjectIterableElementsAssert<T> assertThat(IObjectIterableElementsView<T> actual) {

        return new ObjectIterableElementsAssert<>(actual);
    }

    public static OneDimensionalArrayAssert assertThat(IOneDimensionalArrayView actual) {

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

    public static LongKeyMapAssert assertThat(ILongContainsKeyMapView actual) {

        return new LongKeyMapAssert(actual);
    }
}
