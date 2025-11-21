package dev.jdata.db.sql.parse;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import org.jutils.ast.objects.list.IAddableList;
import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.io.buffers.BaseStringBuffers;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.expression.SQLScratchExpressionValues;
import dev.jdata.db.utils.adt.elements.IObjectOrderedAddable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.allocators.IAddableListAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public final class SQLParserHelper<

                T extends IIndexList<BaseSQLStatement>,
                U extends IIndexListBuilder<BaseSQLStatement, T, ?>,
                V extends IIndexListAllocator<BaseSQLStatement, T, ?, U>> {

    private final SQLParser sqlParser;

    private final V indexListAllocator;

    private final NodeObjectCache<SQLScratchExpressionValues> scratchExpressionValuesCache;

    public SQLParserHelper(SQLParser sqlParser, Function<IntFunction<BaseSQLStatement[]>, V> createIndexListAllocator) {

        Objects.requireNonNull(sqlParser);
        Objects.requireNonNull(createIndexListAllocator);

        this.sqlParser = sqlParser;

        this.indexListAllocator = createIndexListAllocator.apply(BaseSQLStatement[]::new);
        this.scratchExpressionValuesCache = new NodeObjectCache<>(SQLScratchExpressionValues::new);
    }

    public <E extends Exception, BUFFER extends BaseStringBuffers<E>> T parse(BUFFER buffer, ISQLAllocator sqlAllocator, Function<String, E> createEOFException)
            throws ParserException, E {

        final T result;

        final SQLScratchExpressionValues scratchExpressionValues = scratchExpressionValuesCache.allocate();

        try {
            result = parse(sqlParser, buffer, createEOFException, scratchExpressionValues, sqlAllocator, sqlAllocator, indexListAllocator);
        }
        finally {

            scratchExpressionValuesCache.free(scratchExpressionValues);
        }

        return result;
    }

    public static <
                    E extends Exception,
                    BUFFER extends BaseStringBuffers<E>,
                    INDEX_LIST extends IIndexList<BaseSQLStatement>,
                    INDEX_LIST_BUILDER extends IIndexListBuilder<BaseSQLStatement, INDEX_LIST, ?>,
                    INDEX_LIST_ALLOCATOR extends IIndexListAllocator<BaseSQLStatement, INDEX_LIST, ?, INDEX_LIST_BUILDER>>

        INDEX_LIST parse(SQLParser sqlParser, BUFFER buffer, Function<String, E> createEOFException, SQLScratchExpressionValues sqlScratchExpressionValues,
                ISQLAllocator sqlAllocator, IAddableListAllocator addableListAllocator, INDEX_LIST_ALLOCATOR indexListAllocator) throws ParserException, E {

        final INDEX_LIST_BUILDER sqlStatementsBuilder = indexListAllocator.createBuilder();

        final INDEX_LIST result;

        try {
            parse(sqlParser, buffer, createEOFException, sqlScratchExpressionValues, sqlAllocator, addableListAllocator, sqlStatementsBuilder, null);

            result = sqlStatementsBuilder.buildOrEmpty();
        }
        finally {

            indexListAllocator.freeBuilder(sqlStatementsBuilder);
        }

        return result;
    }

    private static <E extends Exception, BUFFER extends BaseStringBuffers<E>> void parse(SQLParser sqlParser, BUFFER buffer, Function<String, E> createEOFException,
            SQLScratchExpressionValues sqlScratchExpressionValues, ISQLAllocator sqlAllocator, IAddableListAllocator addableListAllocator,
            IObjectOrderedAddable<BaseSQLStatement> sqlStatementsDst, IObjectOrderedAddable<ISQLString> sqlStringsDst) throws ParserException, E {

        final IAddableList<BaseSQLStatement> sqlStatementsAddableList = addableListAllocator.allocateList(1);
        final IAddableList<ISQLString> sqlStringsAddableList = addableListAllocator.allocateList(1);

        try {
            sqlParser.parse(buffer, createEOFException, sqlAllocator, sqlScratchExpressionValues, sqlStatementsAddableList, sqlStringsAddableList);

            toIndexList(sqlStatementsAddableList, sqlStatementsDst);

            if (sqlStringsDst != null) {

                toIndexList(sqlStringsAddableList, sqlStringsDst);
            }
        }
        finally {

            addableListAllocator.freeList(sqlStatementsAddableList);
            addableListAllocator.freeList(sqlStringsAddableList);
        }
    }

    @FunctionalInterface
    public interface ParsedSQLStatementsFunction<P, E extends Exception> {

        long apply(int databaseId, int sessionId, IIndexListGetters<BaseSQLStatement> sqlStatements, IIndexListGetters<ISQLString> sqlStrings, P parameter) throws E;
    }

    public <P, E extends Exception, BUFFER extends BaseStringBuffers<E>, PARSEDE extends Exception> long parseSQLStatements(BUFFER buffer,
            Function<String, E> createEOFException, int databaseId, int sessionId, ISQLAllocator sqlAllocator, P parameter,
            ParsedSQLStatementsFunction<P, PARSEDE> onParsedSQLStatements)
                    throws ParserException, E, PARSEDE {

        final long result;

        final SQLScratchExpressionValues scratchExpressionValues = scratchExpressionValuesCache.allocate();

        try {
            result = parseSQLStatements(buffer, createEOFException, databaseId, sessionId, sqlParser, sqlAllocator, scratchExpressionValues, parameter,
                    onParsedSQLStatements);
        }
        finally {

            scratchExpressionValuesCache.free(scratchExpressionValues);
        }

        return result;
    }

    private static <P, E extends Exception, BUFFER extends BaseStringBuffers<E>, PARSEDE extends Exception> long parseSQLStatements(BUFFER buffer,
            Function<String, E> createEOException, int databaseId, int sessionId, SQLParser sqlParser, ISQLAllocator sqlAllocator,
            SQLScratchExpressionValues scratchExpressionValues, P parameter, ParsedSQLStatementsFunction<P, PARSEDE> onParsedSQLStatements)
                    throws ParserException, E, PARSEDE {

        Objects.requireNonNull(buffer);
        Objects.requireNonNull(sqlParser);
        Objects.requireNonNull(sqlAllocator);
        Objects.requireNonNull(scratchExpressionValues);
        Objects.requireNonNull(onParsedSQLStatements);

        final long result;

        final int initialCapacity = 1;

        final IAddableList<BaseSQLStatement> sqlStatements = sqlAllocator.allocateList(initialCapacity);
        final IAddableList<ISQLString> sqlStrings = sqlAllocator.allocateList(initialCapacity);

        try {
            sqlParser.parse(buffer, createEOException, sqlAllocator, scratchExpressionValues, sqlStatements, sqlStrings);

            result = onParsedSQLStatements.apply(databaseId, sessionId, sqlStatements, sqlStrings, parameter);
        }
        finally {

            sqlAllocator.freeList(sqlStatements);
            sqlAllocator.freeList(sqlStrings);
        }

        return result;
    }

    public void freeSQLStatements(T sqlStatements) {

        Objects.requireNonNull(sqlStatements);

        indexListAllocator.freeImmutable(sqlStatements);
    }

    private static <T> void toIndexList(IAddableList<T> addableList, IObjectOrderedAddable<T> dst) {

        final int numElements = IOnlyElementsView.intNumElementsRenamed(addableList.getNumElements());

        toIndexList(addableList, numElements, dst);
    }

    private static <T> void toIndexList(IAddableList<T> addableList, int numElements, IObjectOrderedAddable<T> dst) {

        for (int i = 0; i < numElements; ++ i) {

            dst.addTail(addableList.get(i));
        }
    }
}
