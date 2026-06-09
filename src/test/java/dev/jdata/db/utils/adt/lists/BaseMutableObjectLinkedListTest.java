package dev.jdata.db.utils.adt.lists;

abstract class BaseMutableObjectLinkedListTest<T extends ILinkedListView<Integer>, U extends ILinkedListView<String>> extends BaseMutableObjectListTest<T, U> {

    @Override
    protected final boolean hasCapacity() {

        return false;
    }
}
