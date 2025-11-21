package dev.jdata.db.engine.transactions.ddl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;

import dev.jdata.db.DBConstants;
import dev.jdata.db.DBNamedObject;
import dev.jdata.db.engine.database.DatabaseStringManagement;
import dev.jdata.db.engine.database.IStringStorer;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.engine.transactions.ddl.HeapDDLTransaction.HeapDDLTransactionCachedObjects;
import dev.jdata.db.engine.validation.exceptions.SQLValidationException;
import dev.jdata.db.engine.validation.exceptions.TableAlreadyExistsException;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IHeapEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.storage.DatabaseSchemaStorageFactory;
import dev.jdata.db.schema.types.IntegerType;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLDropTableStatement;
import dev.jdata.db.sql.parse.ISQLString;
import dev.jdata.db.test.TestFileSystemAccess;
import dev.jdata.db.test.unit.BaseDBTest;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.paths.PathIOUtil;

public final class DDLTransactionTest extends BaseDBTest {

    private static final boolean DEBUG = Boolean.TRUE;

    @Test
    @Category(UnitTest.class)
    public void testCreateTableNames() {

        throw new UnsupportedOperationException();
    }

    @Test
    @Category(UnitTest.class)
    public void testCreateTable() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_TABLE_NAME;
        final String testColumnName = TEST_COLUMN_NAME;

        final String createTableSQL = makeCreateTableSQL(testTableName, testColumnName);

        final IStringStorer stringStorer = createStringStorer();

        final IEffectiveDatabaseSchema newEffectiveDatabaseSchema = checkDDLOperation(createTableSQL, SQLCreateTableStatement.class, stringStorer,
                IHeapEffectiveDatabaseSchema::empty, createTableSQL);

        final IIndexList<Table> tables = newEffectiveDatabaseSchema.getSchemaObjects(DDLObjectType.TABLE);

        assertThat(tables).isNotNull();
        assertThat(tables).hasNumElements(1L);

        final Table table = tables.get(0L);

        assertThat(table).isNotNull();
        assertThat(table.getId()).isEqualTo(DBConstants.INITIAL_SCHEMA_OBJECT_ID);
        checkDBNamedObject(table, stringStorer, testTableName);

        assertThat(table.getNumColumns()).isEqualTo(1);

        final Column column = table.getColumn(0);

        assertThat(column).isNotNull();
        assertThat(column.getId()).isEqualTo(DBConstants.INITIAL_COLUMN_ID);
        checkDBNamedObject(column, stringStorer, testColumnName);

        assertThat(column.getSchemaType()).isSameAs(IntegerType.INSTANCE);
        assertThat(column.getCheckCondition()).isNull();
    }

    @Test
    @Category(UnitTest.class)
    public void testCreateTableWhenAlreadyInEffectiveSchema() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_TABLE_NAME;

        final String createTableSQL = makeCreateTableSQL(testTableName);

        final IStringStorer stringStorer = createStringStorer();

        assertThatThrownBy(() -> checkDDLOperation(createTableSQL, SQLCreateTableStatement.class, stringStorer,
                d -> createTestEffectiveDatabaseSchema(d, testTableName, stringStorer), "")).isInstanceOf(TableAlreadyExistsException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testCreateTableWhenAlreadyAddedToSameTransaction() throws ParserException, SQLValidationException, IOException {

        throw new UnsupportedOperationException();
    }

    @Test
    @Category(UnitTest.class)
    public void testDropTable() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_TABLE_NAME;

        final String dropTableSQL = "drop table " + testTableName;

        final IStringStorer stringStorer = createStringStorer();

        final IEffectiveDatabaseSchema newEffectiveDatabaseSchema = checkDDLOperation(dropTableSQL, SQLDropTableStatement.class, stringStorer,
                d -> createTestEffectiveDatabaseSchema(d, testTableName, stringStorer), "");

        final IIndexList<Table> tables = newEffectiveDatabaseSchema.getSchemaObjects(DDLObjectType.TABLE);

        assertThat(tables).isNull();
    }

    private static <T extends BaseSQLDDLOperationStatement> IEffectiveDatabaseSchema checkDDLOperation(String sql, Class<T> sqlStatementClass, IStringStorer stringStorer,
            Function<DatabaseId, IEffectiveDatabaseSchema> createEffectiveDatabaseSchema, String expectedSQL) throws ParserException, SQLValidationException, IOException {

        final IEffectiveDatabaseSchema result;

        final ISQLString createTableSQLString = createSQLString(sql);

        final ParsedStatement<T> parsedStatement = checkParseANSIStatementAll(sql, sqlStatementClass);

        final T sqlStatement = parsedStatement.getStatement();
        final StringResolver parserStringResolver = parsedStatement.getStringResolver();

        final DatabaseId databaseId = getTestDatabaseId();

        final IEffectiveDatabaseSchema effectiveDatabaseSchema = createEffectiveDatabaseSchema.apply(databaseId);

        final DatabaseStringManagement databaseStringManagement = createDatabaseStringManagement(stringStorer);
        final StringManagement stringManagement = createStringManagement(databaseStringManagement, parserStringResolver);

        final HeapDDLTransaction ddlTransaction = createDDLTransaction(effectiveDatabaseSchema, stringStorer);

        ddlTransaction.addDDLStatement(sqlStatement, createTableSQLString, parserStringResolver, stringManagement);

        try (TestFileSystemAccess testFileSystemAccess = TestFileSystemAccess.create()) {

            final IRelativeFileSystemAccess fileSystemAccess = testFileSystemAccess.createRelative();

            final DatabaseSchemaStorageFactory databaseSchemaStorageFactory = new DatabaseSchemaStorageFactory(fileSystemAccess, createStringCache(),
                    createTextToByteOutputPrerequisites());

            final StringBuilder sb = new StringBuilder();

            final DatabaseSchemaVersion nextDatabaseSchemaVersion = effectiveDatabaseSchema.getVersion().next();

            result = ddlTransaction.commit(nextDatabaseSchemaVersion, databaseSchemaStorageFactory, createANSIDatabaseSchemaSerializer(),  createSQLOutputterWitIOException(sb),
                    createCompleteSchemaMapsBuilder(), createSchemaObjectIdAllocators());

            assertThat(result).isNotNull();

            if (DEBUG) {

                System.out.println("sql out '" + sb + '\'');
            }

            verifyDDLTransaction(testFileSystemAccess.getRootPath(), sb, parserStringResolver, sqlStatement, expectedSQL);
        }

        return result;
    }

    private static void checkDBNamedObject(DBNamedObject dbNamedObject, IStringStorer stringStorer, String expectedName) {

        checkDBNamedObject(dbNamedObject, stringStorer, expectedName, expectedName);
    }

    private static void checkDBNamedObject(DBNamedObject dbNamedObject, IStringStorer stringStorer, String expectedParsedName, String expectedStoredName) {

        assertThat(stringStorer.asString(dbNamedObject.getParsedName())).isEqualTo(expectedParsedName);
        assertThat(stringStorer.asString(dbNamedObject.getFileSystemName())).isEqualTo(expectedStoredName);
        assertThat(stringStorer.asString(dbNamedObject.getHashName())).isEqualTo(expectedStoredName);
        assertThat(stringStorer.asString(dbNamedObject.getStoredName())).isEqualTo(expectedStoredName);
    }

    private static void verifyDDLTransaction(Path rootPath, CharSequence outputtedSQL, StringResolver parserStringResolver, BaseSQLDDLOperationStatement sqlStatement,
            String expectedSQL) {

        verifyDDLTransaction(rootPath, outputtedSQL, parserStringResolver, new BaseSQLDDLOperationStatement[] { sqlStatement }, expectedSQL);
    }

    private static void verifyDDLTransaction(Path rootPath, CharSequence outputtedSQL, StringResolver parserStringResolver, BaseSQLDDLOperationStatement[] sqlStatements,
            String expectedSQL) {

        if (DEBUG) {

            try {
                System.out.println("schema paths:");

                PathIOUtil.printRecursively(rootPath, "path ");
            }
            catch (IOException ex) {

                throw new RuntimeException(ex);
            }
        }

        assertThatCharSeq(outputtedSQL).isEqualToCharSequence(expectedSQL);
    }

    private static HeapDDLTransaction createDDLTransaction(IEffectiveDatabaseSchema effectiveDatabaseSchema, StringResolver schemaStringResolver) {

        final HeapDDLTransaction ddlTransaction = new HeapDDLTransaction();

        ddlTransaction.initialize(effectiveDatabaseSchema, schemaStringResolver, new HeapDDLTransactionCachedObjects());

        return ddlTransaction;
    }

    private static String makeCreateTableSQL(String tableName) {

        return makeCreateTableSQL(tableName, TEST_COLUMN_NAME);
    }

    private static String makeCreateTableSQL(String tableName, String columnName) {

        return "create table " + tableName + " (" + columnName +" integer)";
    }
}
