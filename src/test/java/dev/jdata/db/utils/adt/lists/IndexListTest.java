package dev.jdata.db.utils.adt.lists;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class IndexListTest extends BaseArrayListTest<IIndexList<Integer>, IndexList<String>> {

    @Test
    @Category(UnitTest.class)
    public void testConstructorArguments() {

        final AllocationType allocationType = AllocationType.HEAP;

        assertThatThrownBy(() -> new IndexList<>(null, String[]::new)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new IndexList<>(allocationType, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new IndexList<>(allocationType, null, 1)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new IndexList<>(allocationType, String[]::new, -1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new IndexList<>(allocationType, String[]::new, 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testOfSingleton() {

        final IndexList<String> list = IndexList.of("0");

        checkAddTailMany(list, (l, i) -> {

            l.addTail(i);

            return true;
        });
    }

    @Override
    protected IIndexList<Integer> createTestElements(Integer[] elementsToAdd) {

        final IndexList.Builder<Integer> builder = IndexList.createBuilder(Integer[]::new);

        switch (elementsToAdd.length) {

        case 0:
            break;

        case 1:

            builder.addTail(elementsToAdd[0]);
            break;

        default:

            builder.addTail(elementsToAdd);
            break;
        }

        return builder.build();
    }

    @Override
    protected <P> long count(IIndexList<Integer> elements, P parameter, BiPredicate<Integer, P> predicate) {

        return elements.count(parameter, predicate);
    }

    @Override
    protected long countWithClosure(IIndexList<Integer> elements, Predicate<Integer> predicate) {

        return elements.countWithClosure(predicate);
    }

    @Override
    protected int maxInt(IIndexList<Integer> elements, int defaultValue, ToIntFunction<Integer> mapper) {

        return elements.maxInt(defaultValue, mapper);
    }

    @Override
    protected long maxLong(IIndexList<Integer> elements, long defaultValue, ToLongFunction<Integer> mapper) {

        return elements.maxLong(defaultValue, mapper);
    }

    @Override
    IndexList<String> createStringList() {

        return new IndexList<>(AllocationType.HEAP, String[]::new);
    }

    @Override
    IndexList<String> createStringList(int initialCapacity) {

        return new IndexList<>(AllocationType.HEAP, String[]::new, initialCapacity);
    }

    @Override
    void add(IndexList<String> list, String string) {

        list.addTail(string);
    }
}
