package dev.jdata.db.utils.adt.contains.builders;

import dev.jdata.db.utils.adt.contains.IContains;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.contains.IMutableContains;
import dev.jdata.db.utils.builders.BaseMutableBuilder;
import dev.jdata.db.utils.builders.IBuilder;

public abstract class ContainsBuilder<T extends IContains, U extends IContains & IHeapContainsMarker, V extends IMutableContains>

        extends BaseMutableBuilder<T, U, V>
        implements IContainsView, IContainsBuildable<T, U>, IBuilder {

    protected <P> ContainsBuilder(AllocationType allocationType, long minimumCapacity, P parameter, IBuilderMutableAllocator<V, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    public final boolean isEmpty() {

        return getMutable().isEmpty();
    }
}
