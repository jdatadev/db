package dev.jdata.db.engine.database;

import java.util.Objects;

import dev.jdata.db.utils.allocators.IArrayAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.allocators.ObjectCache;

public class SQLExpressionEvaluatorParameter extends ObjectCacheNode {

    public interface ElementAllocator<T> {

        T allocateElement();

        void freeElement(T element);
    }

    private final IArrayAllocator<SQLExpressionEvaluator> arrayAllocator;

    private final ElementAllocator<SQLExpressionEvaluator> expressionEvaluatorAllocator = new ElementAllocator<SQLExpressionEvaluator>() {

        @Override
        public SQLExpressionEvaluator allocateElement() {

            return SQLExpressionEvaluatorParameter.this.allocateEvaluator();
        }

        @Override
        public void freeElement(SQLExpressionEvaluator element) {

            SQLExpressionEvaluatorParameter.this.freeEvaluator(element);
        }
    };

    private final ObjectCache<SQLExpressionEvaluator> expressionEvaluatorCache;

    protected SQLExpressionEvaluatorParameter(IArrayAllocator<SQLExpressionEvaluator> arrayAllocator) {

        this.arrayAllocator = Objects.requireNonNull(arrayAllocator);

        this.expressionEvaluatorCache = new ObjectCache<>(SQLExpressionEvaluator::new, SQLExpressionEvaluator[]::new);
    }

    public final SQLExpressionEvaluator allocateEvaluator() {

        return expressionEvaluatorCache.allocate();
    }

    public final void freeEvaluator(SQLExpressionEvaluator expressionEvaluator) {

        expressionEvaluator.clear();

        expressionEvaluatorCache.free(expressionEvaluator);
    }

    public final SQLExpressionEvaluator[] allocateEvaluatorArray(int numEvaluators) {

        return arrayAllocator.allocateArray(numEvaluators, expressionEvaluatorAllocator);
    }

    public final void freeEvaluatorArray(SQLExpressionEvaluator[] expressionEvaluatorArray, int numElements) {

        arrayAllocator.freeArray(expressionEvaluatorArray, numElements, expressionEvaluatorAllocator);
    }
}
