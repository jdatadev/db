package dev.jdata.db.test.unit;

import java.io.IOException;
import java.nio.ByteBuffer;

import dev.jdata.db.common.storagebits.BaseMaxNumStorageBitsAdapter;
import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.common.storagebits.NumStorageBitsParameters;
import dev.jdata.db.engine.database.DatabaseParameters;
import dev.jdata.db.engine.database.DatabaseParameters.DatabaseStorageType;
import dev.jdata.db.engine.database.DatabaseStringManagement;
import dev.jdata.db.engine.database.Databases;
import dev.jdata.db.engine.database.DatabasesParameters;
import dev.jdata.db.engine.database.IStringStorer;
import dev.jdata.db.engine.database.allocators.HeapDatabasesAllocators;
import dev.jdata.db.engine.database.strings.IStringCache;
import dev.jdata.db.engine.sessions.DBSession.ILargeObjectStorer;
import dev.jdata.db.engine.transactions.Transactions.ITransactionFactory;
import dev.jdata.db.schema.types.SchemaCustomType;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.utils.adt.arrays.IHeapMutableLongLargeArray;
import dev.jdata.db.utils.adt.sets.IHeapMutableLongLargeSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public abstract class BaseDatabasesTest extends BaseDBTest {

    protected static Databases<IHeapMutableLongLargeArray, IHeapMutableLongLargeSet> createDatabases() {

        final DatabasesParameters<IHeapMutableLongLargeArray, IHeapMutableLongLargeSet> databasesParameters = makeDatabasesParameters();

        final IStringCache stringCache = IStringCache.create(0, 0);
        final boolean cacheStatements = false;

        return new Databases<>(AllocationType.HEAP, databasesParameters, stringCache, cacheStatements);
    }

    protected static DatabaseParameters makeDatabaseParameters() {

        final DatabaseParameters databaseParameters = new DatabaseParameters();

        databaseParameters.initialize(DatabaseStorageType.MEMORY, null);

        return databaseParameters;
    }

    private static DatabasesParameters<IHeapMutableLongLargeArray, IHeapMutableLongLargeSet> makeDatabasesParameters() {

        final IStringStorer stringStorer = createStringStorer();

        final DatabaseStringManagement databaseStringManagement = createDatabaseStringManagement(stringStorer);

        final INumStorageBitsGetter numStorageBitsGetter = makeNumStorageBitsGetter();
        final HeapDatabasesAllocators databasesAllocators = new HeapDatabasesAllocators(numStorageBitsGetter);

        final ILargeObjectStorer<IOException> largeObjectStorer = makeLargeObjectStorer();

        final ITransactionFactory transactionFactory = makeTransactionFactory(AllocationType.HEAP);

        final DatabasesParameters<IHeapMutableLongLargeArray, IHeapMutableLongLargeSet> databasesParameters
                = new DatabasesParameters<>(databasesAllocators, databaseStringManagement, largeObjectStorer, transactionFactory);

        return databasesParameters;
    }

    private static INumStorageBitsGetter makeNumStorageBitsGetter() {

        final BaseMaxNumStorageBitsAdapter maxNumStorageBitsAdapter = new BaseMaxNumStorageBitsAdapter() {

            @Override
            public Integer onCustomType(SchemaCustomType schemaDataType, NumStorageBitsParameters parameter) {

                throw new UnsupportedOperationException();
            }
        };

        return new INumStorageBitsGetter() {

            @Override
            public int getMinNumBits(SchemaDataType schemaDataType) {

                throw new UnsupportedOperationException();
            }

            @Override
            public int getMaxNumBits(SchemaDataType schemaDataType) {

                return schemaDataType.visit(maxNumStorageBitsAdapter, null);
            }
        };
    }

    private static ILargeObjectStorer<IOException> makeLargeObjectStorer() {

        return new ILargeObjectStorer<IOException>() {

            @Override
            public long createLargeObject(long length) throws IOException {

                throw new UnsupportedOperationException();
            }

            @Override
            public void addLargeObjectPart(long largeObjectRef, ByteBuffer byteBuffer, int offset, int length) throws IOException {

                throw new UnsupportedOperationException();
            }

            @Override
            public void closeLargeObject(long largeObjectRef) throws IOException {

                throw new UnsupportedOperationException();
            }
        };
    }

    private static ITransactionFactory makeTransactionFactory(AllocationType allocationType) {

        return ITransactionFactory.makeMVCCTransactionFactory(allocationType);
    }
}
