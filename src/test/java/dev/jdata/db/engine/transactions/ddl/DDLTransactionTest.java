package dev.jdata.db.engine.transactions.ddl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;
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
import dev.jdata.db.engine.database.strings.IStringWriter;
import dev.jdata.db.engine.transactions.ddl.HeapDDLTransaction.HeapDDLTransactionCachedObjects;
import dev.jdata.db.engine.validation.exceptions.SQLValidationException;
import dev.jdata.db.engine.validation.exceptions.TableAlreadyExistsException;
import dev.jdata.db.engine.validation.exceptions.TableDoesNotExistException;
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
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLDropTableStatement;
import dev.jdata.db.test.TestFileSystemAccess;
import dev.jdata.db.test.unit.BaseDBTest;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.jdk.adt.strings.StringBuilders;
import dev.jdata.db.utils.paths.PathIOUtil;

public final class DDLTransactionTest extends BaseDBTest {

    private static final boolean DEBUG = Boolean.TRUE;

    @Test
    @Category(UnitTest.class)
    public void testCreateTableNames() {

// case sensitivity
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

        final IIndexList<Table> tables = newEffectiveDatabaseSchema.getSchemaObjectsList(DDLObjectType.TABLE);

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
    public void testCreateTableAlreadyInEffectiveSchema() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_TABLE_NAME;

        final String createTablesSQL = makeCreateTableSQL(testTableName);

        final IStringStorer stringStorer = createStringStorer();

        assertThatThrownBy(() -> checkDDLOperation(createTablesSQL, SQLCreateTableStatement.class, stringStorer,
                d -> createTestEffectiveDatabaseSchema(d, testTableName, stringStorer), "")).isInstanceOf(TableAlreadyExistsException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testCreateTableAlreadyAddedToSameTransaction() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_TABLE_NAME;

        final String sql = createSQLBuilder()
                .addCreateTableSQL(testTableName)
                .addCreateTableSQL(testTableName)
                .build();

        final IStringStorer stringStorer = createStringStorer();

        assertThatThrownBy(() -> checkDDLOperations(sql, IHeapIndexList.of(SQLCreateTableStatement.class, SQLCreateTableStatement.class), stringStorer,
                d -> createTestEffectiveDatabaseSchema(d, testTableName, stringStorer), "")).isInstanceOf(TableAlreadyExistsException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testDropTable() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_TABLE_NAME;

        final String dropTableSQL = makeDropTableSQL(testTableName);

        final IStringStorer stringStorer = createStringStorer();

        final IEffectiveDatabaseSchema newEffectiveDatabaseSchema = checkDDLOperation(dropTableSQL, SQLDropTableStatement.class, stringStorer,
                d -> createTestEffectiveDatabaseSchema(d, testTableName, stringStorer), "");

        final IIndexList<Table> tables = newEffectiveDatabaseSchema.getSchemaObjectsList(DDLObjectType.TABLE);

        assertThat(tables).isNull();
    }

    @Test
    @Category(UnitTest.class)
    public void testDropTableNotInEffectiveSchema() throws ParserException, SQLValidationException, IOException {

        final String dropTableSQL = makeDropTableSQL(TEST_TABLE_NAME);

        assertThatThrownBy(() -> checkDDLOperation(dropTableSQL, SQLDropTableStatement.class, IHeapEffectiveDatabaseSchema::empty, ""))
                .isInstanceOf(TableDoesNotExistException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testDropTableAlreadyAddedToSameTransaction() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;

        final String sql = createSQLBuilder()
                .addCreateTableSQL(testTableName)
                .addDropTableSQL(testTableName)
                .build();

        checkDDLOperations(sql, IHeapIndexList.of(SQLCreateTableStatement.class, SQLDropTableStatement.class), IHeapEffectiveDatabaseSchema::empty, "");
    }

    @Test
    @Category(UnitTest.class)
    public void testDropTableAlreadyDroppedInSameTransaction() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;

        final String sql = createSQLBuilder()
                .addDropTableSQL(testTableName)
                .addDropTableSQL(testTableName)
                .build();

        final IStringStorer stringStorer = createStringStorer();

        assertThatThrownBy(() -> checkDDLOperations(sql, IHeapIndexList.of(SQLDropTableStatement.class, SQLDropTableStatement.class), stringStorer,
                d -> createTestEffectiveDatabaseSchema(d, testTableName, stringStorer), "")).isInstanceOf(TableDoesNotExistException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testDropTableCreatedAndAlreadyDroppedInSameTransaction() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;

        final String sql = createSQLBuilder()
                .addCreateTableSQL(testTableName)
                .addDropTableSQL(testTableName)
                .addDropTableSQL(testTableName)
                .build();

        assertThatThrownBy(() -> checkDDLOperations(sql, IHeapIndexList.of(SQLCreateTableStatement.class, SQLDropTableStatement.class, SQLDropTableStatement.class),
                IHeapEffectiveDatabaseSchema::empty, "")).isInstanceOf(TableDoesNotExistException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testCreateTableAlreadyCreatedAndDroppedInSameTransaction() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;

        final String sql = createSQLBuilder()
                .addCreateTableSQL(testTableName)
                .addDropTableSQL(testTableName)
                .addCreateTableSQL(testTableName)
                .build();

        checkDDLOperations(sql, IHeapIndexList.of(SQLCreateTableStatement.class, SQLDropTableStatement.class, SQLCreateTableStatement.class), IHeapEffectiveDatabaseSchema::empty,
                makeCreateTableSQL(testTableName));
    }

    @Test
    @Category(UnitTest.class)
    public void testCreateAndDropTableTwiceInSameTransaction() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;

        final String sql = createSQLBuilder()
                .addCreateTableSQL(testTableName)
                .addDropTableSQL(testTableName)
                .addCreateTableSQL(testTableName)
                .addDropTableSQL(testTableName)
                .build();

        checkDDLOperations(sql, IHeapIndexList.of(SQLCreateTableStatement.class, SQLDropTableStatement.class, SQLCreateTableStatement.class, SQLDropTableStatement.class),
                IHeapEffectiveDatabaseSchema::empty, "");
    }

    @Test
    @Category(UnitTest.class)
    public void testAlterTableAddColumn() throws SQLValidationException, ParserException, IOException {

        final String testTableName = TEST_DATABASE_NAME;
        final String columnName = makeTestColumnNameWithSuffix(1);
        final String integerDataType = makeIntegerDataType();

        final String sql = createSQLBuilder()
                .addAlterTableSQL(testTableName, b -> b.addAddColumn(columnName, integerDataType))
                .build();

        final String expectedSQL = makeCreateTableSQL(testTableName) + ";alter table " + testTableName + " add " + columnName + ' ' + integerDataType;

        final IStringStorer stringStorer = createStringStorer();

        checkDDLOperation(sql, SQLAlterTableStatement.class, stringStorer, d -> createTestEffectiveDatabaseSchema(d, testTableName, stringStorer), expectedSQL);

        throw new UnsupportedOperationException();
    }

    @Test
    @Category(UnitTest.class)
    public void testAlterTableDropColumn() {

        throw new UnsupportedOperationException();
    }

    private static <T extends BaseSQLDDLOperationStatement> IEffectiveDatabaseSchema checkDDLOperations(String sql,
            IIndexList<Class<? extends BaseSQLDDLOperationStatement>> sqlStatementClasses, Function<DatabaseId, IEffectiveDatabaseSchema> createEffectiveDatabaseSchema,
            String expectedSQL) throws ParserException, SQLValidationException, IOException {

        return checkDDLOperations(sql, sqlStatementClasses, createStringStorer(), createEffectiveDatabaseSchema, expectedSQL);
    }

    private static <T extends BaseSQLDDLOperationStatement> IEffectiveDatabaseSchema checkDDLOperations(String sql,
            IIndexList<Class<? extends BaseSQLDDLOperationStatement>> sqlStatementClasses, IStringStorer stringStorer,
            Function<DatabaseId, IEffectiveDatabaseSchema> createEffectiveDatabaseSchema, String expectedSQL) throws ParserException, SQLValidationException, IOException {

        return checkDDL(sql, sqlStatementClasses, stringStorer, createEffectiveDatabaseSchema, expectedSQL);
    }

    private static IEffectiveDatabaseSchema checkDDLOperation(String sql, Class<? extends BaseSQLDDLOperationStatement> sqlStatementClass,
            Function<DatabaseId, IEffectiveDatabaseSchema> createEffectiveDatabaseSchema, String expectedSQL) throws ParserException, SQLValidationException, IOException {

        return checkDDLOperation(sql, sqlStatementClass, createStringStorer(), createEffectiveDatabaseSchema, expectedSQL);
    }

    private static IEffectiveDatabaseSchema checkDDLOperation(String sql, Class<? extends BaseSQLDDLOperationStatement> sqlStatementClass, IStringStorer stringStorer,
            Function<DatabaseId, IEffectiveDatabaseSchema> createEffectiveDatabaseSchema, String expectedSQL) throws ParserException, SQLValidationException, IOException {

        return checkDDL(sql, IHeapIndexList.of(sqlStatementClass), stringStorer, createEffectiveDatabaseSchema, expectedSQL);
    }

    private static IEffectiveDatabaseSchema checkDDL(String sql, IIndexList<Class<? extends BaseSQLDDLOperationStatement>> sqlStatementClasses, IStringStorer stringStorer,
            Function<DatabaseId, IEffectiveDatabaseSchema> createEffectiveDatabaseSchema, String expectedSQL) throws ParserException, SQLValidationException, IOException {

        final IEffectiveDatabaseSchema result;

        final ParsedStatements parsedStatements = checkParseANSIStatements(sql, sqlStatementClasses);

        final IIndexList<ParsedStatement> parsedStatementList = parsedStatements.getStatements();

        final StringResolver sqlStatementsStringResolver = parsedStatements.getStringResolver();

        final DatabaseId databaseId = getTestDatabaseId();

        final IEffectiveDatabaseSchema effectiveDatabaseSchema = createEffectiveDatabaseSchema.apply(databaseId);

        final DatabaseStringManagement databaseStringManagement = createDatabaseStringManagement(stringStorer);
        final StringManagement stringManagement = createStringManagement(databaseStringManagement, sqlStatementsStringResolver);

        final HeapDDLTransaction ddlTransaction = createDDLTransaction(effectiveDatabaseSchema, stringStorer);

        final long numSQLStatements = parsedStatementList.getNumElements();

        for (int i = 0; i < numSQLStatements; ++ i) {

            final ParsedStatement parsedStatement = parsedStatementList.get(i);
            final BaseSQLDDLOperationStatement sqlStatement = (BaseSQLDDLOperationStatement)parsedStatement.getStatement();

            ddlTransaction.addDDLStatement(sqlStatement, sqlStatementsStringResolver, parsedStatement.getSQLString(), stringManagement);
        }

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

            final IIndexList<BaseSQLDDLOperationStatement> sqlStatements = parsedStatementList.mapOrEmptyInt(
                    c -> IHeapIndexListBuilder.create(c, BaseSQLDDLOperationStatement[]::new), e -> (BaseSQLDDLOperationStatement)e.getStatement(), IHeapIndexList::empty);

            verifyDDLTransaction(testFileSystemAccess.getRootPath(), sb, sqlStatementsStringResolver, sqlStatements, expectedSQL);
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

        verifyDDLTransaction(rootPath, outputtedSQL, parserStringResolver, IHeapIndexList.of(sqlStatement), expectedSQL);
    }

    private static void verifyDDLTransaction(Path rootPath, CharSequence outputtedSQL, StringResolver parserStringResolver,
            IIndexList<BaseSQLDDLOperationStatement> sqlStatements, String expectedSQL) {

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

    private static HeapDDLTransaction createDDLTransaction(IEffectiveDatabaseSchema effectiveDatabaseSchema, IStringWriter schemaStringWriter) {

        final HeapDDLTransaction ddlTransaction = new HeapDDLTransaction();

        ddlTransaction.initialize(effectiveDatabaseSchema, schemaStringWriter, new HeapDDLTransactionCachedObjects());

        return ddlTransaction;
    }

    private static String makeCreateTableSQL(String tableName) {

        return makeCreateTableSQL(tableName, TEST_COLUMN_NAME);
    }

    private static String makeCreateTableSQL(String tableName, String columnName) {

        return "create table " + tableName + " (" + columnName +" integer)";
    }

    private static String makeDropTableSQL(String tableName) {

        return "drop table " + tableName;
    }

    private static SQLBuilder createSQLBuilder() {

        return new SQLBuilder();
    }

    private static final class SQLBuilder extends BaseSQLBuilder<SQLBuilder> {

        SQLBuilder addCreateTableSQL(String tableName) {

            appendSQL(makeCreateTableSQL(tableName));

            return this;
        }

        SQLBuilder addCreateTableSQL(String tableName, String columnName) {

            appendSQL(makeCreateTableSQL(tableName, columnName));

            return this;
        }

        SQLBuilder addDropTableSQL(String tableName) {

            appendSQL(makeDropTableSQL(tableName));

            return this;
        }

        SQLBuilder addAlterTableSQL(String tableName, Consumer<AlterTableSQLBuilder> b) {

            final AlterTableSQLBuilder builder = new AlterTableSQLBuilder(this);

            appendSQLStatementSeparatorIfNecessary().append("alter table ").append(tableName).append(' ');

            b.accept(builder);

            return this;
        }

        String build() {

            return getStringBuilder().toString();
        }
    }

    private static final class AlterTableSQLBuilder extends BaseSQLBuilder<AlterTableSQLBuilder> {

        AlterTableSQLBuilder(BaseSQLBuilder<?> sqlBuilder) {
            super(sqlBuilder);
        }

        AlterTableSQLBuilder addAddColumn(String columnName, String dataType) {

            append("add ").append(columnName).append(' ').append(dataType);

            return this;
        }
    }

    private static abstract class BaseSQLBuilder<T extends BaseSQLBuilder<T>> {

        private final StringBuilder sb;

        BaseSQLBuilder() {

            this.sb = new StringBuilder();
        }

        BaseSQLBuilder(BaseSQLBuilder<?> sqlBuilder) {

            Objects.requireNonNull(sqlBuilder);

            this.sb = sqlBuilder.sb;
        }

        final void appendSQL(String sql) {

            Checks.isNotEmptyNorBlank(sql);

            appendSQLStatementSeparatorIfNecessary();

            sb.append(sql);
        }

        final T appendSQLStatementSeparatorIfNecessary() {

            if (!StringBuilders.isEmpty(sb)) {

                sb.append(';');
            }

            return getThis();
        }

        final T append(String string) {

            Checks.isNotEmptyNorBlank(string);

            sb.append(string);

            return getThis();
        }

        final T append(char c) {

            sb.append(c);

            return getThis();
        }

        final StringBuilder getStringBuilder() {

            return sb;
        }

        @SuppressWarnings("unchecked")
        private T getThis() {

            return (T)this;
        }
    }
}
