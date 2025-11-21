package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableObjectIndexListTest extends BaseMutableObjectArrayListTest<IBaseMutableObjectIndexList<Integer>, MutableObjectIndexList<String>> {

    @Test
    @Category(UnitTest.class)
    public void testOfSingleton() {

        final MutableObjectIndexList<String> list = HeapMutableObjectIndexList.of(AllocationType.HEAP, "0");

        checkAddTailMany(list, (l, i) -> {

            l.addTail(i);

            return true;
        });
    }

    @Test
    @Category(UnitTest.class)
    public void testRemoveAtMostOneInstance() {

        checkArguments(MutableObjectIndexList::removeAtMostOneInstance);

        final Integer integer = 123;

        assertThat(makeList().removeAtMostOneInstance(integer)).isFalse();
        assertThat(makeList(0, 1).removeAtMostOneInstance(integer)).isFalse();

        checkRemoveInstance(MutableObjectIndexList::removeAtMostOneInstance);
    }

    @Test
    @Category(UnitTest.class)
    public void testRemoveExactlyOneInstance() {

        checkArguments((l, i) -> { l.removeAtMostOneInstance(i); return true; });

        final Integer integer = 123;

        assertThatThrownBy(() -> makeList().removeExactlyOneInstance(integer)).isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(() -> makeList(0, 1).removeExactlyOneInstance(integer)).isInstanceOf(NoSuchElementException.class);

        checkRemoveInstance((l, i) -> { l.removeAtMostOneInstance(i); return true; });
    }

    @Override
    protected IBaseMutableObjectIndexList<Integer> createTestElements(Integer[] elementsToAdd) {

        final MutableObjectIndexList<Integer> result = new HeapMutableObjectIndexList<>(AllocationType.HEAP, Integer[]::new);

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
    protected void addHead(MutableObjectIndexList<String> list, String string) {

        list.addHead(string);
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

        return new HeapMutableObjectIndexList<>(AllocationType.HEAP, String[]::new);
    }

    @Override
    protected MutableObjectIndexList<String> createStringList(int initialCapacity) {

        return new HeapMutableObjectIndexList<>(AllocationType.HEAP, String[]::new, initialCapacity);
    }

    @Override
    protected void add(MutableObjectIndexList<String> list, String string) {

        list.addTailElement(string);
    }

    @FunctionalInterface
    private interface InstanceRemover<T> {

        boolean remove(MutableObjectIndexList<T> list, T instance);
    }

    private static void checkArguments(InstanceRemover<Integer> remover) {

        final Integer integer = 123;

        assertThatThrownBy(() -> remover.remove(makeList(0, integer, 0, integer), integer)).isInstanceOf(IllegalStateException.class);
    }

    private static void checkRemoveInstance(InstanceRemover<Integer> remover) {

        final Integer integer = 123;

        assertThat(remover.remove(makeList(integer), integer)).isTrue();
        assertThat(remover.remove(makeList(integer, 0, 1), integer)).isTrue();
        assertThat(remover.remove(makeList(0, integer, 1), integer)).isTrue();
        assertThat(remover.remove(makeList(0, 1, integer), integer)).isTrue();
    }

    private static MutableObjectIndexList<Integer> makeList(int ... elements) {

        final MutableObjectIndexList<Integer> list = new HeapMutableObjectIndexList<>(AllocationType.HEAP, Integer[]::new, elements.length);

        for (int element : elements) {

            list.addTail(element);
        }

        return list;
    }
}
