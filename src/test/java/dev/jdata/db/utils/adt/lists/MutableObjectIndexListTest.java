package dev.jdata.db.utils.adt.lists;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.capacity.ICapacity;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableObjectIndexListTest extends BaseMutableObjectArrayListTest<IBaseMutableObjectIndexList<Integer>, MutableObjectIndexList<String>> {

    @Test
    @Category(UnitTest.class)
    public void testOfSingleton() {

        final MutableObjectIndexList<String> list = HeapMutableObjectIndexList.of(AllocationType.HEAP, new String[] { "0" });

        checkAddTailMany(list, (l, i) -> {

            l.addTail(i);

            return true;
        });
    }

    @Override
    protected void clear(MutableObjectIndexList<String> list) {

        list.clear();
    }

    @Override
    protected IBaseMutableObjectIndexList<Integer> createTestElements(Integer[] elementsToAdd) {

        final MutableObjectIndexList<Integer> result = HeapMutableObjectIndexList.create(AllocationType.HEAP, Integer[]::new);

        result.addTail(elementsToAdd);

        return result;
    }

    @Override
    protected <P> long count(IBaseMutableObjectIndexList<Integer> elements, P parameter, BiPredicate<Integer, P> predicate) {

        return elements.count(parameter, predicate);
    }

    @Override
    protected long countWithClosure(IBaseMutableObjectIndexList<Integer> elements, Predicate<Integer> predicate) {

        return elements.closureOrConstantCount(predicate);
    }

    @Override
    protected int maxInt(IBaseMutableObjectIndexList<Integer> elements, int defaultValue, ToIntFunction<Integer> mapper) {

        return elements.maxInt(defaultValue, mapper);
    }

    @Override
    protected long maxLong(IBaseMutableObjectIndexList<Integer> elements, long defaultValue, ToLongFunction<Integer> mapper) {

        return elements.maxLong(defaultValue, mapper);
    }

    @Override
    protected int getCapacity(MutableObjectIndexList<String> list) {

        return ICapacity.intCapacity(list);
    }

    @Override
    protected void addTail(MutableObjectIndexList<String> list, String string) {

        list.addTail(string);
    }

    @Override
    protected void addTail(MutableObjectIndexList<String> list, String... strings) {

        list.addTail(strings);
    }

    @Override
    protected MutableObjectIndexList<String> createStringList() {

        return HeapMutableObjectIndexList.create(AllocationType.HEAP, String[]::new);
    }

    @Override
    protected MutableObjectIndexList<String> createStringList(int initialCapacity) {

        return HeapMutableObjectIndexList.create(AllocationType.HEAP, initialCapacity, String[]::new);
    }

    @Override
    protected void add(MutableObjectIndexList<String> list, String string) {

        list.addTailElement(string);
    }
}
