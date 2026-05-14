package dev.jdata.db.engine.database.allocators;

import java.util.Objects;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.engine.sessions.DMLUpdatingEvaluatorParameter;
import dev.jdata.db.engine.sessions.DMLUpdatingPreparedEvaluatorParameter;
import dev.jdata.db.schema.allocators.databases.schemamanagement.CacheDatabaseSchemaManagementAllocators;
import dev.jdata.db.schema.model.diff.dropped.CachedSchemaDroppedElementsAllocators;
import dev.jdata.db.utils.adt.arrays.ICachedMutableLongLargeArray;
import dev.jdata.db.utils.adt.maps.ICachedMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.maps.ICachedMutableIntToObjectWithRemoveStaticMapAllocator;
import dev.jdata.db.utils.adt.sets.ICachedMutableIntSet;
import dev.jdata.db.utils.adt.sets.ICachedMutableIntSetAllocator;
import dev.jdata.db.utils.adt.sets.ICachedMutableLongLargeSet;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public final class CachedDatabasesAllocators extends DatabasesAllocators<ICachedMutableLongLargeArray, ICachedMutableLongLargeSet> {

    private final CachedUtilAllocators utilAllocators;

    private final NodeObjectCache<DMLUpdatingEvaluatorParameter<ICachedMutableLongLargeArray, ICachedMutableLongLargeSet>> evaluatorParameterCache;
    private final NodeObjectCache<DMLUpdatingPreparedEvaluatorParameter<ICachedMutableLongLargeArray>> preparedStatementevaluatorParameterCache;

    public CachedDatabasesAllocators(INumStorageBitsGetter numStorageBitsGetter, ICachedMutableIntSetAllocator mutableIntSetAllocator,
            ICachedMutableIntToObjectWithRemoveStaticMapAllocator<ICachedMutableIntSet> mutableIntToObjectWithRemoveStaticMapAllocator) {
        super(createDatabaseSchemaManagementAllocators(mutableIntSetAllocator, mutableIntToObjectWithRemoveStaticMapAllocator));

        Objects.requireNonNull(numStorageBitsGetter);

        this.utilAllocators = new CachedUtilAllocators();

        this.evaluatorParameterCache = new NodeObjectCache<>(a -> new DMLUpdatingEvaluatorParameter<>(a, utilAllocators.expressionEvaluatorArrayAllocator,
                numStorageBitsGetter, utilAllocators.byteArrayByteBufferAllocator, utilAllocators.mutableLongLargeArrayAllocator, utilAllocators.transactionSelectAllocator,
                utilAllocators.mutableLongLargeSetAllocator));

        this.preparedStatementevaluatorParameterCache = new NodeObjectCache<>(a -> new DMLUpdatingPreparedEvaluatorParameter<>(a,
                utilAllocators.expressionEvaluatorArrayAllocator, numStorageBitsGetter, utilAllocators.byteArrayByteBufferAllocator,
                utilAllocators.mutableLongLargeArrayAllocator, utilAllocators.insertRowArrayAllocator));
    }

    private static CacheDatabaseSchemaManagementAllocators<ICachedMutableIntSet, ICachedMutableIntToObjectWithRemoveStaticMap<ICachedMutableIntSet>>
    createDatabaseSchemaManagementAllocators(ICachedMutableIntSetAllocator mutableIntSetAllocator,
            ICachedMutableIntToObjectWithRemoveStaticMapAllocator<ICachedMutableIntSet> mutableIntToObjectWithRemoveStaticMapAllocator) {

        return new CacheDatabaseSchemaManagementAllocators<>(new CachedSchemaDroppedElementsAllocators<>(mutableIntSetAllocator, mutableIntToObjectWithRemoveStaticMapAllocator));
    }

    @Override
    public DMLUpdatingEvaluatorParameter<ICachedMutableLongLargeArray, ICachedMutableLongLargeSet> allocateDMLEvaluatorParameter() {

        return evaluatorParameterCache.allocate();
    }

    @Override
    public void freeDMLEvaluatorParameter(DMLUpdatingEvaluatorParameter<ICachedMutableLongLargeArray, ICachedMutableLongLargeSet> evaluatorParameter) {

        Objects.requireNonNull(evaluatorParameter);

        evaluatorParameterCache.free(evaluatorParameter);
    }

    @Override
    public DMLUpdatingPreparedEvaluatorParameter<ICachedMutableLongLargeArray> allocateDMLPreparedStatementEvaluatorParameter() {

        return preparedStatementevaluatorParameterCache.allocate();
    }

    @Override
    public void freeDMLPreparedStatementEvaluatorParameter(DMLUpdatingPreparedEvaluatorParameter<ICachedMutableLongLargeArray> evaluatorParameter) {

        Objects.requireNonNull(evaluatorParameter);

        preparedStatementevaluatorParameterCache.free(evaluatorParameter);
    }
}
