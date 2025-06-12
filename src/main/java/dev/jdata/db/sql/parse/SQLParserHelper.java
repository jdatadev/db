package dev.jdata.db.sql.parse;

import java.util.Objects;
import java.util.function.Function;

import org.jutils.ast.objects.list.IAddableList;
import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.io.buffers.BaseStringBuffers;
import org.jutils.parse.ParserException;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.expression.SQLScratchExpressionValues;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.CacheIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.allocators.IAddableListAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.scalars.Integers;

public final class SQLParserHelper {

    private final SQLParser sqlParser;

    private final IndexListAllocator<BaseSQLStatement> indexListAllocator;

    private final NodeObjectCache<SQLScratchExpressionValues> scratchExpressionValuesCache;

    public SQLParserHelper(SQLParser sqlParser) {

        this.sqlParser = Objects.requireNonNull(sqlParser);

        this.indexListAllocator = new CacheIndexListAllocator<>(BaseSQLStatement[]::new);
        this.scratchExpressionValuesCache = new NodeObjectCache<>(SQLScratchExpressionValues::new);
    }

    public <E extends Exception, BUFFER extends BaseStringBuffers<E>> IIndexList<BaseSQLStatement> parse(BUFFER buffer, ISQLAllocator sqlAllocator,
            Function<String, E> createEOFException) throws ParserException, E {

        final IIndexList<BaseSQLStatement> result;

        final SQLScratchExpressionValues scratchExpressionValues = scratchExpressionValuesCache.allocate();

        try {
            result = parse(sqlParser, buffer, createEOFException, scratchExpressionValues, sqlAllocator, sqlAllocator, indexListAllocator);
        }
        finally {

            scratchExpressionValuesCache.free(scratchExpressionValues);
        }

        return result;
    }

    public static <E extends Exception, BUFFER extends BaseStringBuffers<E>> IIndexList<BaseSQLStatement> parse(SQLParser sqlParser, BUFFER buffer,
            Function<String, E> createEOFException, SQLScratchExpressionValues sqlScratchExpressionValues, ISQLAllocator sqlAllocator,
            IAddableListAllocator addableListAllocator, IndexListAllocator<BaseSQLStatement> indexListAllocator) throws ParserException, E {

        final IndexList.Builder<BaseSQLStatement> sqlStatementsBuilder = IndexList.createBuilder(indexListAllocator);

        parse(sqlParser, buffer, createEOFException, sqlScratchExpressionValues, sqlAllocator, addableListAllocator, sqlStatementsBuilder, null);

        return sqlStatementsBuilder.build();
    }

    private static <E extends Exception, BUFFER extends BaseStringBuffers<E>> void parse(SQLParser sqlParser, BUFFER buffer, Function<String, E> createEOFException,
            SQLScratchExpressionValues sqlScratchExpressionValues, ISQLAllocator sqlAllocator, IAddableListAllocator addableListAllocator,
            IndexList.Builder<BaseSQLStatement> sqlStatementsDst,  IndexList.Builder<SQLString> sqlStringsDst) throws ParserException, E {

        final IAddableList<BaseSQLStatement> sqlStatementsAddableList = addableListAllocator.allocateList(1);
        final IAddableList<SQLString> sqlStringsAddableList = addableListAllocator.allocateList(1);

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

        long apply(int databaseId, int sessionId, IIndexListGetters<BaseSQLStatement> sqlStatements, IIndexListGetters<SQLString> sqlStrings, P parameter) throws E;
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
        final IAddableList<SQLString> sqlStrings = sqlAllocator.allocateList(initialCapacity);

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

    public void freeSQLStatements(IIndexList<BaseSQLStatement> sqlStatements) {

        Objects.requireNonNull(sqlStatements);

        indexListAllocator.freeIndexList(sqlStatements);
    }

    private static <T> IIndexList<T> toIndexList(IAddableList<T> addableList, IndexListAllocator<T> indexListAllocator) {

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(addableList.getNumElements());

        final IndexList.Builder<T> indexListBuilder = IndexList.createBuilder(numElements, indexListAllocator);

        toIndexList(addableList, numElements, indexListBuilder);

        return indexListBuilder.build();
    }

    private static <T> void toIndexList(IAddableList<T> addableList, IndexList.Builder<T> indexListBuilder) {

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(addableList.getNumElements());

        toIndexList(addableList, numElements, indexListBuilder);
    }

    private static <T> void toIndexList(IAddableList<T> addableList, int numElements, IndexList.Builder<T> indexListBuilder) {

        for (int i = 0; i < numElements; ++ i) {

            indexListBuilder.addTail(addableList.get(i));
        }
    }
}
