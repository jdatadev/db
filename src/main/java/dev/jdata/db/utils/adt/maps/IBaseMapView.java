package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

public interface IBaseMapView<T extends IAnyOrderAddable> extends IOnlyElementsView, IBaseMapGetters<T> {

    @Override
    default long getNumKeys() {

        return getNumElements();
    }
}
