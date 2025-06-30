package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.scalars.Integers;

public final class MutableIndexListTest extends BaseMutableObjectArrayListTest<IMutableIndexList<Integer>, MutableIndexList<String>> {

    @Test
    @Category(UnitTest.class)
    public void testOfSingleton() {

        final MutableIndexList<String> list = MutableIndexList.of("0");

        checkAddTailMany(list, (l, i) -> {

            l.addTail(i);

            return true;
        });
    }

    @Test
    @Category(UnitTest.class)
    public void testRemoveAtMostOneInstance() {

        checkArguments(MutableIndexList::removeAtMostOneInstance);

        final Integer integer = 123;

        assertThat(makeList().removeAtMostOneInstance(integer)).isFalse();
        assertThat(makeList(0, 1).removeAtMostOneInstance(integer)).isFalse();

        checkRemoveInstance(MutableIndexList::removeAtMostOneInstance);
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
    protected IMutableIndexList<Integer> createTestElements(Integer[] elementsToAdd) {

        final MutableIndexList<Integer> result = HeapMutableIndexList.from(Integer[]::new);

        result.addTail(elementsToAdd);

        return result;
    }

    @Override
    protected <P> long count(IMutableIndexList<Integer> elements, P parameter, BiPredicate<Integer, P> predicate) {

        return elements.count(parameter, predicate);
    }

    @Override
    protected long countWithClosure(IMutableIndexList<Integer> elements, Predicate<Integer> predicate) {

        return elements.closureOrConstantCount(predicate);
    }

    @Override
    protected int maxInt(IMutableIndexList<Integer> elements, int defaultValue, ToIntFunction<Integer> mapper) {

        return elements.maxInt(defaultValue, mapper);
    }

    @Override
    protected long maxLong(IMutableIndexList<Integer> elements, long defaultValue, ToLongFunction<Integer> mapper) {

        return elements.maxLong(defaultValue, mapper);
    }

    @Override
    int getCapacity(MutableIndexList<String> list) {

        return Integers.checkUnsignedLongToUnsignedInt(list.getCapacity());
    }

    @Override
    void addHead(MutableIndexList<String> list, String string) {

        list.addHead(string);
    }

    @Override
    void addTail(MutableIndexList<String> list, String string) {

        list.addTail(string);
    }

    @Override
    void addTail(MutableIndexList<String> list, String... strings) {

        list.addTail(strings);
    }

    @Override
    MutableIndexList<String> createStringList() {

        return HeapMutableIndexList.from(String[]::new);
    }

    @Override
    MutableIndexList<String> createStringList(int initialCapacity) {

        return HeapMutableIndexList.from(String[]::new, initialCapacity);
    }

    @Override
    void add(MutableIndexList<String> list, String string) {

        list.addTailElement(string);
    }

    @FunctionalInterface
    private interface InstanceRemover<T> {

        boolean remove(MutableIndexList<T> list, T instance);
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

    private static MutableIndexList<Integer> makeList(int ... elements) {

        final MutableIndexList<Integer> list = HeapMutableIndexList.from(Integer[]::new, 10);

        for (int element : elements) {

            list.addTail(element);
        }

        return list;
    }
}
