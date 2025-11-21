package dev.jdata.db.utils.adt.lists;

interface IBaseIndexList<T> extends IList<T>, IObjectIndexListCommon<T> {

    @SuppressWarnings("unchecked")
    public static <T> IIndexList<T> empty() {

        return (IIndexList<T>)ObjectEmptyIndexList.INSTANCE;
    }

    IMutableIndexList<T> copyToMutable(IBaseIndexListAllocator<T, ? extends IBaseIndexList<T>, ?> indexListAllocator);
}
