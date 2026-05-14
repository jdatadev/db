package dev.jdata.db.utils.adt.lists;

public interface ICachedIntIndexListAllocator extends IIntIndexListAllocator<ICachedIntIndexList, ICachedMutableIntIndexList, ICachedIntIndexListBuilder> {

    public static ICachedIntIndexListAllocator create() {

        return new CachedIntIndexListAllocator(new CachedMutableIntIndexListAllocator());
    }
}
