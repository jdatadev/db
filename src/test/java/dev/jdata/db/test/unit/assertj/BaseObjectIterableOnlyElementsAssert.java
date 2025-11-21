package dev.jdata.db.test.unit.assertj;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;

import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

abstract class BaseObjectIterableOnlyElementsAssert<T, S extends BaseObjectIterableOnlyElementsAssert<T, S, A>, A extends IOnlyElementsView & IObjectIterableElementsView<T>>

        extends BaseIterableOnlyElementsAssert<S, A> {

    BaseObjectIterableOnlyElementsAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }

    @SafeVarargs
    public final S containsExactly(T ... expectedElements) {

        isNotNull();

        hasNumElements(expectedElements.length);

        final int numElements = IOnlyElementsView.intNumElements(actual);

        final List<T> list = new ArrayList<>(numElements);

        actual.forEach(list, (e, l) -> l.add(e));

        Assertions.assertThat(list).containsExactly(expectedElements);

        return getThis();
    }
}
