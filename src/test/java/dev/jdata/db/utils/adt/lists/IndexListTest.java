package dev.jdata.db.utils.adt.lists;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class IndexListTest extends BaseImmutableObjectArrayListTest<IBaseObjectIndexList<Integer>, ObjectIndexList<String>> {

    @Test
    @Category(UnitTest.class)
    public void testEmptyList() {

        final ObjectIndexList<String> emptyList = HeapObjectIndexList.empty();

        assertThat(emptyList).isEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public void testOfSingleton() {

        final String value = "abc";

        final ObjectIndexList<String> list = HeapObjectIndexList.of(AllocationType.HEAP, value);

        checkElementsSameAs(list, value);
    }

    @Test
    @Category(UnitTest.class)
    public void testOfValues() {

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        final ObjectIndexList<String> emptyList = HeapObjectIndexList.of(AllocationType.HEAP);
        assertThat(emptyList).isEmpty();

        final ObjectIndexList<String> oneElementList = HeapObjectIndexList.of(AllocationType.HEAP, new String[] { abc });
        checkElementsSameAs(oneElementList, abc);

        final ObjectIndexList<String> twoElementsList = HeapObjectIndexList.of(AllocationType.HEAP, abc, bcd);
        checkElementsSameAs(twoElementsList, abc, bcd);

        final ObjectIndexList<String> threeElementList = HeapObjectIndexList.of(AllocationType.HEAP, abc, bcd, cde);
        checkElementsSameAs(threeElementList, abc, bcd, cde);
    }

    @Test
    @Category(UnitTest.class)
    public void testSortedOfValues() {

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        final HeapObjectIndexListAllocator<String> indexListAllocator = new HeapObjectIndexListAllocator<>(String[]::new);

        final IHeapIndexList<String> emptyList = indexListAllocator.sortedOf(HeapObjectIndexList.empty(), String::compareTo);
        assertThat(emptyList).isEmpty();

        final IHeapIndexList<String> oneElementList = indexListAllocator.sortedOf(HeapObjectIndexList.of(AllocationType.HEAP, abc), String::compareTo);
        checkElementsSameAs(oneElementList, abc);

        final IHeapIndexList<String> twoElementsList = indexListAllocator.sortedOf(HeapObjectIndexList.of(AllocationType.HEAP, bcd, abc), String::compareTo);
        checkElementsSameAs(twoElementsList, abc, bcd);

        final IHeapIndexList<String> threeElementList = indexListAllocator.sortedOf(HeapObjectIndexList.of(AllocationType.HEAP, bcd, cde, abc), String::compareTo);
        checkElementsSameAs(threeElementList, abc, bcd, cde);
    }

    @Override
    protected IBaseObjectIndexList<Integer> createTestElements(Integer[] elementsToAdd) {

        final HeapObjectIndexListBuilder<Integer> builder = new HeapObjectIndexListBuilder<>(AllocationType.HEAP, Integer[]::new);

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

        return builder.buildOrEmpty();
    }

    @Override
    protected <P> long count(IBaseObjectIndexList<Integer> elements, P parameter, BiPredicate<Integer, P> predicate) {

        return elements.count(parameter, predicate);
    }

    @Override
    protected long countWithClosure(IBaseObjectIndexList<Integer> elements, Predicate<Integer> predicate) {

        return elements.closureOrConstantCount(predicate);
    }

    @Override
    protected int maxInt(IBaseObjectIndexList<Integer> elements, int defaultValue, ToIntFunction<Integer> mapper) {

        return elements.maxInt(defaultValue, mapper);
    }

    @Override
    protected long maxLong(IBaseObjectIndexList<Integer> elements, long defaultValue, ToLongFunction<Integer> mapper) {

        return elements.maxLong(defaultValue, mapper);
    }

    @Override
    ObjectIndexList<String> createStringList(String ... values) {

        return HeapObjectIndexList.of(AllocationType.HEAP, values);
    }

    @SafeVarargs
    static <E, L extends IIndexList<E>> void checkElementsSameAs(L list, E ... expectedElements) {

        checkElementsSameAs(list, IIndexList::get, IIndexList::isEmpty, IIndexList::getNumElements, expectedElements);
    }
}
