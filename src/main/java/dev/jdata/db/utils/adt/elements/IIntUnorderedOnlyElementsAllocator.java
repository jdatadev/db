package dev.jdata.db.utils.adt.elements;

public interface IIntUnorderedOnlyElementsAllocator<

                T extends IElements & IOnlyElementsView & IIntUnorderedElementsView,
                U extends IMutableElements & IOnlyElementsView & IIntUnorderedElementsView,
                V extends IIntUnorderedOnlyElementsBuilder<T, ?>>

        extends IIntElementsAllocator<T, U, V> {

}
