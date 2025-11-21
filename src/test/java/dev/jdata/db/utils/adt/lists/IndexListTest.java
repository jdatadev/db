package dev.jdata.db.utils.adt.lists;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class IndexListTest extends BaseImmutableObjectArrayListTest<IBaseIndexList<Integer>, IndexList<String>> {

    @Test
    @Category(UnitTest.class)
    public void testEmptyList() {

        final IndexList<String> emptyList = IndexList.empty();

        assertThat(emptyList).isEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public void testOfSingleton() {

        final String value = "abc";

        final IndexList<String> list = IndexList.of(value);

        checkElementsSameAs(list, value);
    }

    @Test
    @Category(UnitTest.class)
    public void testOfValues() {

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        final IndexList<String> emptyList = IndexList.of();
        assertThat(emptyList).isEmpty();

        final IndexList<String> oneElementList = IndexList.of(abc);
        checkElementsSameAs(oneElementList, abc);

        final IndexList<String> twoElementsList = IndexList.of(abc, bcd);
        checkElementsSameAs(twoElementsList, abc, bcd);

        final IndexList<String> threeElementList = IndexList.of(abc, bcd, cde);
        checkElementsSameAs(threeElementList, abc, bcd, cde);
    }

    @Test
    @Category(UnitTest.class)
    public void testSortedOfValues() {

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        final HeapIndexListAllocator<String> indexListAllocator = new HeapIndexListAllocator<>(String[]::new);

        final IndexList<String> emptyList = IndexList.sortedOf(IndexList.empty(), String::compareTo, indexListAllocator);
        assertThat(emptyList).isEmpty();

        final IndexList<String> oneElementList = IndexList.sortedOf(IndexList.of(abc), String::compareTo, indexListAllocator);
        checkElementsSameAs(oneElementList, abc);

        final IndexList<String> twoElementsList = IndexList.sortedOf(IndexList.of(bcd, abc), String::compareTo, indexListAllocator);
        checkElementsSameAs(twoElementsList, abc, bcd);

        final IndexList<String> threeElementList = IndexList.sortedOf(IndexList.of(bcd, cde, abc), String::compareTo, indexListAllocator);
        checkElementsSameAs(threeElementList, abc, bcd, cde);
    }

    @Override
    protected IBaseIndexList<Integer> createTestElements(Integer[] elementsToAdd) {

        final HeapIndexListBuilder<Integer> builder = HeapIndexList.createBuilder(Integer[]::new);

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
    protected <P> long count(IBaseIndexList<Integer> elements, P parameter, BiPredicate<Integer, P> predicate) {

        return elements.count(parameter, predicate);
    }

    @Override
    protected long countWithClosure(IBaseIndexList<Integer> elements, Predicate<Integer> predicate) {

        return elements.closureOrConstantCount(predicate);
    }

    @Override
    protected int maxInt(IBaseIndexList<Integer> elements, int defaultValue, ToIntFunction<Integer> mapper) {

        return elements.maxInt(defaultValue, mapper);
    }

    @Override
    protected long maxLong(IBaseIndexList<Integer> elements, long defaultValue, ToLongFunction<Integer> mapper) {

        return elements.maxLong(defaultValue, mapper);
    }

    @Override
    IndexList<String> createStringList(String ... values) {

        return IndexList.of(values);
    }
}
