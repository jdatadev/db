package dev.jdata.db.test.unit.assertj;

import java.util.Collection;

import dev.jdata.db.utils.adt.arrays.IOneDimensionalArrayView;
import dev.jdata.db.utils.adt.elements.IObjectIterableOnlyElementsView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.maps.IBaseMapView;
import dev.jdata.db.utils.adt.maps.ILongContainsKeyMapView;
import dev.jdata.db.utils.adt.maps.IMapOfCollection;
import dev.jdata.db.utils.adt.maps.IObjectKeyMapView;
import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;
import dev.jdata.db.utils.adt.sets.IIntSetView;
import dev.jdata.db.utils.adt.sets.ILongSetView;

public class CustomAssertJAssertions extends org.assertj.core.api.Assertions {

    public static <T extends CharSequence> CharSequenceAssert<T> assertThatCharSeq(T actual) {

        return new CharSequenceAssert<>(actual);
    }

    public static OnlyElementsAssert assertThat(IOnlyElementsView actual) {

        return new OnlyElementsAssert(actual);
    }

    public static <T, U extends IObjectIterableOnlyElementsView<T>> ObjectIterableOnlyElementsAssert<T, U> assertThat(U actual) {

        return new ObjectIterableOnlyElementsAssert<>(actual);
    }

    public static OneDimensionalArrayAssert assertThat(IOneDimensionalArrayView actual) {

        return new OneDimensionalArrayAssert(actual);
    }

    public static <KEYS, KEYS_ADDABLE extends IAnyOrderAddable, ACTUAL extends IBaseMapView<KEYS_ADDABLE>> KeyMapAssert<KEYS, KEYS_ADDABLE, ACTUAL> assertThat(ACTUAL actual) {

        return new KeyMapAssert<>(actual);
    }

    public static <K, A extends IObjectKeyMapView<K>> ObjectKeyMapAssert<K, A> assertThat(A actual) {

        return new ObjectKeyMapAssert<>(actual);
    }

    public static <K, V, C extends Collection<V>> MapOfCollectionAssert<K, V, C> assertThat(IMapOfCollection<K, V, C> actual) {

        return new MapOfCollectionAssert<>(actual);
    }

    public static IntSetViewAssert assertThat(IIntSetView actual) {

        return new IntSetViewAssert(actual);
    }

    public static LongSetViewAssert assertThat(ILongSetView actual) {

        return new LongSetViewAssert(actual);
    }

    public static LongKeyMapAssert assertThat(ILongContainsKeyMapView actual) {

        return new LongKeyMapAssert(actual);
    }
}
