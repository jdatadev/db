package dev.jdata.db.sql.parse;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import org.jutils.ast.objects.list.IAddableList;
import org.jutils.ast.objects.list.IIndexListView;
import org.jutils.io.buffers.BaseStringBuffers;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.expression.SQLScratchExpressionValues;
import dev.jdata.db.utils.adt.elements.ILongOrderedAddable;
import dev.jdata.db.utils.adt.elements.IObjectOrderedAddable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.lists.ICachedLongIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.lists.ILongIndexListView;
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

        final U sqlStatementsBuilder = indexListAllocator.createBuilder();

        try {
            parse(buffer, sqlAllocator, sqlStatementsBuilder, null, createEOFException);

            result = sqlStatementsBuilder.buildOrEmpty();
        }
        finally {

            indexListAllocator.freeBuilder(sqlStatementsBuilder);
        }

        return result;
    }

    public <E extends Exception, BUFFER extends BaseStringBuffers<E>> void parse(BUFFER buffer, ISQLAllocator sqlAllocator,
            IObjectOrderedAddable<BaseSQLStatement> sqlStatementsDst, ILongOrderedAddable sqlStringsDst, Function<String, E> createEOFException)
                    throws ParserException, E {

        final SQLScratchExpressionValues scratchExpressionValues = scratchExpressionValuesCache.allocate();

        try {
            parse(sqlParser, buffer, scratchExpressionValues, sqlAllocator, sqlAllocator, sqlStatementsDst, sqlStringsDst, createEOFException);
        }
        finally {

            scratchExpressionValuesCache.free(scratchExpressionValues);
        }
    }

    @Deprecated // necessary?
    public static <
                    E extends Exception,
                    BUFFER extends BaseStringBuffers<E>,
                    INDEX_LIST extends IIndexList<BaseSQLStatement>,
                    INDEX_LIST_BUILDER extends IIndexListBuilder<BaseSQLStatement, INDEX_LIST, ?>,
                    INDEX_LIST_ALLOCATOR extends IIndexListAllocator<BaseSQLStatement, INDEX_LIST, ?, INDEX_LIST_BUILDER>>

        INDEX_LIST parse(SQLParser sqlParser, BUFFER buffer, SQLScratchExpressionValues sqlScratchExpressionValues, ISQLAllocator sqlAllocator,
                IAddableListAllocator addableListAllocator, INDEX_LIST_ALLOCATOR indexListAllocator, Function<String, E> createEOFException) throws ParserException, E {

        final INDEX_LIST result;

        final INDEX_LIST_BUILDER sqlStatementsBuilder = indexListAllocator.createBuilder();

        try {
            parse(sqlParser, buffer, sqlScratchExpressionValues, sqlAllocator, addableListAllocator, sqlStatementsBuilder, null, createEOFException);

            result = sqlStatementsBuilder.buildOrEmpty();
        }
        finally {

            indexListAllocator.freeBuilder(sqlStatementsBuilder);
        }

        return result;
    }

    private static <E extends Exception, BUFFER extends BaseStringBuffers<E>> void parse(SQLParser sqlParser, BUFFER buffer,
            SQLScratchExpressionValues sqlScratchExpressionValues, ISQLAllocator sqlAllocator, IAddableListAllocator addableListAllocator,
            IObjectOrderedAddable<BaseSQLStatement> sqlStatementsDst, ILongOrderedAddable sqlStringsDst, Function<String, E> createEOFException) throws ParserException, E {

        final IAddableList<BaseSQLStatement> sqlStatementsAddableList = addableListAllocator.allocateList(1);

        try {
            sqlParser.parse(buffer, createEOFException, sqlAllocator, sqlScratchExpressionValues, sqlStatementsAddableList, sqlStringsDst);

            toIndexList(sqlStatementsAddableList, sqlStatementsDst);
        }
        finally {

            addableListAllocator.freeList(sqlStatementsAddableList);
        }
    }

    @FunctionalInterface
    public interface IParsedSQLStatementsFunction<P, E extends Exception> {

        long apply(int databaseId, int sessionId, IIndexListView<BaseSQLStatement> sqlStatements, ILongIndexListView sqlStrings, StringResolver stringResolver, P parameter)
                throws E;
    }

    public <P, E extends Exception, BUFFER extends BaseStringBuffers<E>, PARSED_E extends Exception> long parseSQLStatements(BUFFER buffer,
            Function<String, E> createEOFException, int databaseId, int sessionId, ISQLAllocator sqlAllocator, P parameter,
            IParsedSQLStatementsFunction<P, PARSED_E> onParsedSQLStatements)
                    throws ParserException, E, PARSED_E {

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

    private static <P, E extends Exception, BUFFER extends BaseStringBuffers<E>, PARSED_E extends Exception> long parseSQLStatements(BUFFER buffer,
            Function<String, E> createEOException, int databaseId, int sessionId, SQLParser sqlParser, ISQLAllocator sqlAllocator,
            SQLScratchExpressionValues scratchExpressionValues, P parameter, IParsedSQLStatementsFunction<P, PARSED_E> onParsedSQLStatements)
                    throws ParserException, E, PARSED_E {

        Objects.requireNonNull(buffer);
        Objects.requireNonNull(sqlParser);
        Objects.requireNonNull(sqlAllocator);
        Objects.requireNonNull(scratchExpressionValues);
        Objects.requireNonNull(onParsedSQLStatements);

        final long result;

        final int initialCapacity = 1;

        final IAddableList<BaseSQLStatement> sqlStatementsDst = sqlAllocator.allocateList(initialCapacity);
        final ICachedLongIndexListBuilder sqlStringsDst = sqlAllocator.createLongIndexListBuilder(initialCapacity);

        try {
            sqlParser.parse(buffer, createEOException, sqlAllocator, scratchExpressionValues, sqlStatementsDst, sqlStringsDst);

            result = onParsedSQLStatements.apply(databaseId, sessionId, sqlStatementsDst, sqlStringsDst.buildOrEmpty(), buffer, parameter);
        }
        finally {

            sqlAllocator.freeList(sqlStatementsDst);

            sqlAllocator.freeLongIndexListBuilder(sqlStringsDst);
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
