package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.review.IDeprecatedInstanceAllocator;

interface IBaseLongSetAllocator<T extends IBaseLongSet, U extends IBaseMutableLongSet<IBaseLongSet, IBaseLongSetAllocator<T>>> extends IDeprecatedInstanceAllocator<T> {

}
