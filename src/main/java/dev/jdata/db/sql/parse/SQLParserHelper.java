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
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListBuilder;
import dev.jdata.db.utils.allocators.IAddableListAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.scalars.Integers;

public final class SQLParserHelper<

                T extends IndexList<BaseSQLStatement>,
                U extends IndexListBuilder<BaseSQLStatement, T, U>,
                V extends IndexListAllocator<BaseSQLStatement, T, U, ?>> {

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
                    INDEX_LIST extends IndexList<BaseSQLStatement>,
                    INDEX_LIST_BUILDER extends IndexListBuilder<BaseSQLStatement, INDEX_LIST, INDEX_LIST_BUILDER>,
                    INDEX_LIST_ALLOCATOR extends IndexListAllocator<BaseSQLStatement, INDEX_LIST, INDEX_LIST_BUILDER, ?>>

        INDEX_LIST parse(SQLParser sqlParser, BUFFER buffer, Function<String, E> createEOFException, SQLScratchExpressionValues sqlScratchExpressionValues,
                ISQLAllocator sqlAllocator, IAddableListAllocator addableListAllocator, INDEX_LIST_ALLOCATOR indexListAllocator) throws ParserException, E {

        final INDEX_LIST_BUILDER sqlStatementsBuilder = IndexList.createBuilder(indexListAllocator);

        final INDEX_LIST result;

        try {
            parse(sqlParser, buffer, createEOFException, sqlScratchExpressionValues, sqlAllocator, addableListAllocator, sqlStatementsBuilder, null);

            result = sqlStatementsBuilder.build();
        }
        finally {

            indexListAllocator.freeIndexListBuilder(sqlStatementsBuilder);
        }

        return result;
    }

    private static <E extends Exception, BUFFER extends BaseStringBuffers<E>> void parse(SQLParser sqlParser, BUFFER buffer, Function<String, E> createEOFException,
            SQLScratchExpressionValues sqlScratchExpressionValues, ISQLAllocator sqlAllocator, IAddableListAllocator addableListAllocator,
            IndexListBuilder<BaseSQLStatement, ?, ?> sqlStatementsDst, IndexListBuilder<ISQLString, ?, ?> sqlStringsDst) throws ParserException, E {

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

        indexListAllocator.freeIndexList(sqlStatements);
    }

    private static <T> void toIndexList(IAddableList<T> addableList, IndexListBuilder<T, ?, ?> indexListBuilder) {

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(addableList.getNumElements());

        toIndexList(addableList, numElements, indexListBuilder);
    }

    private static <T> void toIndexList(IAddableList<T> addableList, int numElements, IndexListBuilder<T, ?, ?> indexListBuilder) {

        for (int i = 0; i < numElements; ++ i) {

            indexListBuilder.addTail(addableList.get(i));
        }
    }
}
