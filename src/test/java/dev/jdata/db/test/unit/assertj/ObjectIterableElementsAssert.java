package dev.jdata.db.test.unit.assertj;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;

import dev.jdata.db.utils.adt.elements.IElementsView;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;

public final class ObjectIterableElementsAssert<T> extends BaseIterableElementsAssert<ObjectIterableElementsAssert<T>, IObjectIterableElementsView<T>> {

    ObjectIterableElementsAssert(IObjectIterableElementsView<T> actual) {
        super(actual, castAssertClass(ObjectIterableElementsAssert.class));
    }

    @SafeVarargs
    public final ObjectIterableElementsAssert<T> containsExactly(T ... expectedElements) {

        isNotNull();

        hasNumElements(expectedElements.length);

        final int numElements = IElementsView.intNumElements(actual.getNumElements());

        final List<T> list = new ArrayList<>(numElements);

        actual.forEach(list, (e, l) -> l.add(e));

        Assertions.assertThat(list).containsExactly(expectedElements);

        return getThis();
    }
}
