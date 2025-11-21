package dev.jdata.db.utils.adt.lists;

@FunctionalInterface
public interface ILargeNodeListValuesFactory<

                TO_ARRAY,
                VALUES_LIST extends IInnerOuterNodeListInternal<TO_ARRAY>,
                VALUES extends BaseInnerOuterNodeListValues<TO_ARRAY, VALUES_LIST>>

        extends INodeListValuesFactory<TO_ARRAY, VALUES_LIST, VALUES> {

}
