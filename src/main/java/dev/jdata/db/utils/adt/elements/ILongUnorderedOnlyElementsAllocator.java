package dev.jdata.db.utils.adt.elements;

public interface ILongUnorderedOnlyElementsAllocator<

                T extends IElements & IOnlyElementsView & ILongUnorderedElementsView,
                U extends IMutableElements & IOnlyElementsView & ILongUnorderedElementsView,
                V extends ILongUnorderedOnlyElementsBuilder<T, ?>>

        extends ILongElementsAllocator<T, U, V> {

}
