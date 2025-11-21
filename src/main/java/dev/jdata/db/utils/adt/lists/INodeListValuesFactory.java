package dev.jdata.db.utils.adt.lists;

interface INodeListValuesFactory<

                TO_ARRAY,
                VALUES_LIST extends INodeListInternal<TO_ARRAY>,
                VALUES extends BaseNodeListValues<TO_ARRAY, VALUES_LIST>> {

    VALUES create(int initialOuterCapacity);
}
