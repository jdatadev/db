package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableObjectIndexListTest extends BaseMutableObjectArrayListTest<IMutableIndexList<Integer>, MutableObjectIndexList<String>> {

    @Test
    @Category(UnitTest.class)
    public void testOfSingleton() {

        final MutableObjectIndexList<String> list = HeapMutableObjectIndexList.of(AllocationType.HEAP, new String[] { "0" }, String[]::new);

        checkArrayListAddTailMany(list, (l, i) -> {

            l.addTail(i);

            return true;
        });
    }

    @Override
    protected void clear(MutableObjectIndexList<String> list) {

        list.clear();
    }

    @Override
    protected IMutableIndexList<Integer> createTestElements(Integer[] elementsToAdd) {

        final MutableObjectIndexList<Integer> result = HeapMutableObjectIndexList.create(AllocationType.HEAP, Integer[]::new);

        if (elementsToAdd.length != 0) {

            result.addTail(elementsToAdd);
        }

        return result;
    }

    @Override
    protected long getCapacity(MutableObjectIndexList<String> list) {

        return list.getCapacity();
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
