package dev.jdata.db.engine.transactions.ddl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

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
import dev.jdata.db.engine.transactions.ddl.DDLTransaction.DDLTransactionSerializationParameter;
import dev.jdata.db.engine.transactions.ddl.HeapDDLTransaction.HeapDDLTransactionCachedObjects;
import dev.jdata.db.engine.validation.exceptions.ColumnAlreadyExistsException;
import dev.jdata.db.engine.validation.exceptions.DropToNoColumnsException;
import dev.jdata.db.engine.validation.exceptions.SQLValidationException;
import dev.jdata.db.engine.validation.exceptions.TableAlreadyExistsException;
import dev.jdata.db.engine.validation.exceptions.TableDoesNotExistException;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IHeapEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMapBuilder;
import dev.jdata.db.schema.storage.DatabaseSchemaStorageFactory;
import dev.jdata.db.schema.types.IntegerType;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLDropTableStatement;
import dev.jdata.db.test.TestFileSystemAccess;
import dev.jdata.db.test.unit.BaseDBTest;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IHeapMutableDoublyLinkedList;
import dev.jdata.db.utils.adt.lists.IHeapMutableDoublyLinkedListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.jdk.adt.strings.StringBuilders;
import dev.jdata.db.utils.paths.PathIOUtil;

public final class DDLTransactionTest extends BaseDBTest {

    private static final boolean DEBUG = Boolean.TRUE;

    private static final BiFunction<DatabaseId, IStringStorer, IEffectiveDatabaseSchema> emptyEffectiveSchema = (d, s) -> IHeapEffectiveDatabaseSchema.empty(d);

    @Test
    @Category(UnitTest.class)
    public void testCreateTableNames() {

// case sensitivity
        throw new UnsupportedOperationException();
    }

    @Test
    @Category(UnitTest.class)
    public void testNamesOfExceptions() {

        throw new UnsupportedOperationException();
    }

    @Test
    @Category(UnitTest.class)
    public void testCreateTable() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_TABLE_NAME;
        final String testColumnName = TEST_COLUMN_NAME;

        final String createTableSQL = makeCreateTableSQL(testTableName, testColumnName);

        final IStringStorer stringStorer = createStringStorer();

        final IEffectiveDatabaseSchema newEffectiveDatabaseSchema = checkDDLOperation(createTableSQL, SQLCreateTableStatement.class, stringStorer, emptyEffectiveSchema,
                createTableSQL);

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
    public void testCreateTableWithNoColumns() throws ParserException, SQLValidationException, IOException {

        final String createTablesSQL = "create table " + TEST_TABLE_NAME + "()";

        assertThatThrownBy(() -> checkDDLOperation(createTablesSQL, SQLCreateTableStatement.class, emptyEffectiveSchema, "")).isInstanceOf(IOException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testCreateTableAlreadyInEffectiveSchema() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_TABLE_NAME;

        final String createTablesSQL = makeCreateTableSQL(testTableName);

        assertThatThrownBy(() -> checkDDLOperation(createTablesSQL, SQLCreateTableStatement.class, (d, s) -> createTestEffectiveDatabaseSchema(d, testTableName, s), ""))
                .isInstanceOf(TableAlreadyExistsException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testCreateTableAlreadyAddedToSameTransaction() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_TABLE_NAME;

        final String sql = createSQLBuilder()
                .addCreateTableSQL(testTableName)
                .addCreateTableSQL(testTableName)
                .build();

        assertThatThrownBy(() -> checkDDLOperations(sql, IHeapIndexList.of(SQLCreateTableStatement.class, SQLCreateTableStatement.class),
                (d, s) -> createTestEffectiveDatabaseSchema(d, testTableName, s), "")).isInstanceOf(TableAlreadyExistsException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testDropTable() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_TABLE_NAME;

        final String dropTableSQL = makeDropTableSQL(testTableName);

        final IStringStorer stringStorer = createStringStorer();

        final IEffectiveDatabaseSchema newEffectiveDatabaseSchema = checkDDLOperation(dropTableSQL, SQLDropTableStatement.class, stringStorer,
                (d, s) -> createTestEffectiveDatabaseSchema(d, testTableName, s), "");

        final IIndexList<Table> tables = newEffectiveDatabaseSchema.getSchemaObjectsList(DDLObjectType.TABLE);

        assertThat(tables).isNull();
    }

    @Test
    @Category(UnitTest.class)
    public void testDropTableNotInEffectiveSchema() throws ParserException, SQLValidationException, IOException {

        final String dropTableSQL = makeDropTableSQL(TEST_TABLE_NAME);

        assertThatThrownBy(() -> checkDDLOperation(dropTableSQL, SQLDropTableStatement.class, emptyEffectiveSchema, "")).isInstanceOf(TableDoesNotExistException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testDropTableAlreadyAddedToSameTransaction() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;

        final String sql = createSQLBuilder()
                .addCreateTableSQL(testTableName)
                .addDropTableSQL(testTableName)
                .build();

        checkDDLOperations(sql, IHeapIndexList.of(SQLCreateTableStatement.class, SQLDropTableStatement.class), emptyEffectiveSchema, "");
    }

    @Test
    @Category(UnitTest.class)
    public void testDropTableAlreadyDroppedInSameTransaction() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;

        final String sql = createSQLBuilder()
                .addDropTableSQL(testTableName)
                .addDropTableSQL(testTableName)
                .build();

        assertThatThrownBy(() -> checkDDLOperations(sql, IHeapIndexList.of(SQLDropTableStatement.class, SQLDropTableStatement.class),
                (d, s) -> createTestEffectiveDatabaseSchema(d, testTableName, s), "")).isInstanceOf(TableDoesNotExistException.class);
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
                emptyEffectiveSchema, "")).isInstanceOf(TableDoesNotExistException.class);
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

        checkDDLOperations(sql, IHeapIndexList.of(SQLCreateTableStatement.class, SQLDropTableStatement.class, SQLCreateTableStatement.class), emptyEffectiveSchema,
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
                emptyEffectiveSchema, "");
    }

    @Test
    @Category(UnitTest.class)
    public void testAlterTableAddColumn() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;
        final String columnName = makeTestColumnNameWithSuffix(1);
        final SchemaDataType schemaDataType = makeIntegerDataType();

        final String sql = createSQLBuilder()
                .addAlterTableSQL(testTableName, b -> b.addAddColumn(columnName, schemaDataType))
                .build();

        final String expectedStoredSQL = makeCreateTableSQL(testTableName) + ';' + makeAlterTableAddColumnSQL(testTableName, columnName, schemaDataType);
        final String expectedEffectiveSQL = makeCreateTableSQL(testTableName, columnName, schemaDataType);

        checkDDLOperation(sql, SQLAlterTableStatement.class, (d, s) -> createTestEffectiveDatabaseSchema(d, testTableName, s), expectedEffectiveSQL);
    }

    @Test
    @Category(UnitTest.class)
    public void testAlterTableAddColumnToTableCreatedInSameTransaction() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;
        final String columnName = makeTestColumnNameWithSuffix(1);
        final SchemaDataType schemaDataType = makeIntegerDataType();

        final String sql = createSQLBuilder()
                .addCreateTableSQL(testTableName)
                .addAlterTableSQL(testTableName, b -> b.addAddColumn(columnName, schemaDataType))
                .build();

        final String expectedStoredSQL = makeCreateTableSQL(testTableName) + ';' + makeAlterTableAddColumnSQL(testTableName, columnName, schemaDataType);
        final String expectedEffectiveSQL = makeCreateTableSQL(testTableName, columnName, schemaDataType);

        checkDDLOperation(sql, SQLAlterTableStatement.class, (d, s) -> createTestEffectiveDatabaseSchema(d, testTableName, s), expectedEffectiveSQL);
    }

    @Test
    @Category(UnitTest.class)
    public void testAlterTableAddColumnWithSameNameInCurrentSchema() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;
        final String columnName = TEST_COLUMN_NAME;

        final String sql = createSQLBuilder()
                .addAlterTableSQL(testTableName, b -> b.addAddColumn(columnName, makeIntegerDataType()))
                .build();

        assertThatThrownBy(() -> checkDDLOperation(sql, SQLAlterTableStatement.class, (d, s) -> createTestEffectiveDatabaseSchema(d, testTableName, columnName, s), ""))
                .isInstanceOf(ColumnAlreadyExistsException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testAlterTableAddColumnWithSameNameAddedInSameTransaction() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;
        final String columnName = TEST_COLUMN_NAME;
        final SchemaDataType schemaDataType = makeIntegerDataType();

        final String sql = createSQLBuilder()
                .addAlterTableSQL(testTableName, b -> b.addAddColumn(columnName, schemaDataType))
                .addAlterTableSQL(testTableName, b -> b.addAddColumn(columnName, schemaDataType))
                .build();

        assertThatThrownBy(() -> checkDDLOperation(sql, SQLAlterTableStatement.class, (d, s) -> createTestEffectiveDatabaseSchema(d, testTableName, s), ""))
                .isInstanceOf(ColumnAlreadyExistsException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testAlterTableAddDropAndAddColumnWithSameNameAddedInSameTransaction() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;
        final String columnName = TEST_COLUMN_NAME;
        final SchemaDataType schemaDataType = makeIntegerDataType();

        final String sql = createSQLBuilder()
                .addAlterTableSQL(testTableName, b -> b.addAddColumn(columnName, schemaDataType))
                .addAlterTableSQL(testTableName, b -> b.addDropColumn(columnName))
                .addAlterTableSQL(testTableName, b -> b.addAddColumn(columnName, schemaDataType))
                .build();

        final String expectedStoredSQL = makeCreateTableSQL(testTableName, columnName) + ';' + makeAlterTableAddColumnSQL(testTableName, columnName, schemaDataType) + ';'
                + makeAlterTableDropColumnSQL(testTableName, columnName) + ';' + makeAlterTableAddColumnSQL(testTableName, columnName, schemaDataType);
        final String expectedEffectiveSQL = makeCreateTableSQL(testTableName, columnName);

        checkDDLOperation(sql, SQLAlterTableStatement.class, (d, s) -> createTestEffectiveDatabaseSchema(d, testTableName, s), expectedEffectiveSQL);
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckSerializedDiffSQLsForAllTests() {

        throw new UnsupportedOperationException();
    }

    @Test
    @Category(UnitTest.class)
    public void testAlterTableDropColumn() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;
        final String columnName1 = makeTestColumnNameWithSuffix(1);
        final String columnName2 = makeTestColumnNameWithSuffix(2);
        final SchemaDataType schemaDataType = IntegerType.INSTANCE;

        final String sql = createSQLBuilder()
                .addAlterTableSQL(testTableName, b -> b.addDropColumn(columnName1))
                .build();

        final String expectedStoredSQL = createSQLBuilder().addCreateTableSQL(testTableName, columnName2, schemaDataType).build() + ';'
                + makeAlterTableDropColumnSQL(testTableName, columnName1);
        final String expectedSQL = makeCreateTableSQL(testTableName, columnName2, schemaDataType);

        checkDDLOperation(sql, SQLAlterTableStatement.class,
                (d, s) -> createSchemaBuilder(d, s)
                    .addTable(testTableName, b -> b.addColumn(columnName1, schemaDataType).addColumn(columnName2, schemaDataType))
                    .buildEffectiveSchema(),
              expectedSQL);
    }

    @Test
    @Category(UnitTest.class)
    public void testAlterTableDropLastColumn() throws ParserException, SQLValidationException, IOException {

        final String testTableName = TEST_DATABASE_NAME;
        final String columnName = TEST_COLUMN_NAME;

        final String sql = createSQLBuilder()
                .addAlterTableSQL(testTableName, b -> b.addDropColumn(columnName))
                .build();

        assertThatThrownBy(() -> checkDDLOperation(sql, SQLAlterTableStatement.class, (d, s) -> createTestEffectiveDatabaseSchema(d, testTableName, s), ""))
                .isInstanceOf(DropToNoColumnsException.class);
    }

    private static <T extends BaseSQLDDLOperationStatement> IEffectiveDatabaseSchema checkDDLOperations(String sql,
            IIndexList<Class<? extends BaseSQLDDLOperationStatement>> sqlStatementClasses,
            BiFunction<DatabaseId, IStringStorer, IEffectiveDatabaseSchema> createEffectiveDatabaseSchema, String expectedSQL)
                    throws ParserException, SQLValidationException, IOException {

        return checkDDLOperations(sql, sqlStatementClasses, createStringStorer(), createEffectiveDatabaseSchema, expectedSQL);
    }

    private static <T extends BaseSQLDDLOperationStatement> IEffectiveDatabaseSchema checkDDLOperations(String sql,
            IIndexList<Class<? extends BaseSQLDDLOperationStatement>> sqlStatementClasses, IStringStorer stringStorer,
            BiFunction<DatabaseId, IStringStorer, IEffectiveDatabaseSchema> createEffectiveDatabaseSchema, String expectedSQL)
                    throws ParserException, SQLValidationException, IOException {

        return checkDDL(sql, sqlStatementClasses, stringStorer, createEffectiveDatabaseSchema, expectedSQL);
    }

    private static IEffectiveDatabaseSchema checkDDLOperation(String sql, Class<? extends BaseSQLDDLOperationStatement> sqlStatementClass,
            BiFunction<DatabaseId, IStringStorer, IEffectiveDatabaseSchema> createEffectiveDatabaseSchema, String expectedSQL)
                    throws ParserException, SQLValidationException, IOException {

        return checkDDLOperation(sql, sqlStatementClass, createStringStorer(), createEffectiveDatabaseSchema, expectedSQL);
    }

    private static IEffectiveDatabaseSchema checkDDLOperation(String sql, Class<? extends BaseSQLDDLOperationStatement> sqlStatementClass, IStringStorer stringStorer,
            BiFunction<DatabaseId, IStringStorer, IEffectiveDatabaseSchema> createEffectiveDatabaseSchema, String expectedSQL)
                    throws ParserException, SQLValidationException, IOException {

        return checkDDL(sql, IHeapIndexList.of(sqlStatementClass), stringStorer, createEffectiveDatabaseSchema, expectedSQL);
    }

    private static IEffectiveDatabaseSchema checkDDL(String sql, IIndexList<Class<? extends BaseSQLDDLOperationStatement>> sqlStatementClasses, IStringStorer stringStorer,
            BiFunction<DatabaseId, IStringStorer, IEffectiveDatabaseSchema> createEffectiveDatabaseSchema, String expectedSQL)
                    throws ParserException, SQLValidationException, IOException {

        final IEffectiveDatabaseSchema result;

        final ParsedStatements parsedStatements = checkParseANSIStatements(sql, sqlStatementClasses);

        final IIndexList<ParsedStatement> parsedStatementList = parsedStatements.getStatements();

        final StringResolver sqlStatementsStringResolver = parsedStatements.getStringResolver();

        final DatabaseId databaseId = getTestDatabaseId();

        final IEffectiveDatabaseSchema effectiveDatabaseSchema = createEffectiveDatabaseSchema.apply(databaseId, stringStorer);

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

            final DDLTransactionSerializationParameter<IOException> ddlTransactionSerializationParameter = new DDLTransactionSerializationParameter<>(AllocationType.HEAP);

            ddlTransactionSerializationParameter.initialize(databaseSchemaStorageFactory, createANSIDatabaseSchemaSerializer(),  createSQLOutputterWitIOException(sb));

            final DDLComputeEffectiveDatabaseSchemaParameter<IHeapMutableDoublyLinkedList<SchemaObject>, IHeapCompleteSchemaMapBuilder> ddlComputeEffectiveDatabaseSchemaParameter
                    = new DDLComputeEffectiveDatabaseSchemaParameter<>(AllocationType.HEAP);

            final IHeapIndexListAllocator<Column> columnIndexListAllocator = IHeapIndexListAllocator.create(Column[]::new);

            ddlComputeEffectiveDatabaseSchemaParameter.initialize(IHeapMutableDoublyLinkedListAllocator.create(), createCompleteSchemaMapsBuilderAllocator(),
                    createSchemaObjectIdAllocators(), (o, d) -> d.applyToColumnsObject(o, columnIndexListAllocator));

            result = ddlTransaction.commit(nextDatabaseSchemaVersion, ddlTransactionSerializationParameter, ddlComputeEffectiveDatabaseSchemaParameter);

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

        assertThat(stringStorer.asString(dbNamedObject.getStoredSQLName())).isEqualTo(expectedParsedName);
        assertThat(stringStorer.asString(dbNamedObject.getHashKeyName())).isEqualTo(expectedStoredName);
        assertThat(stringStorer.asString(dbNamedObject.getStoredName())).isEqualTo(expectedStoredName);
        assertThat(stringStorer.asString(dbNamedObject.getFileSystemName())).isEqualTo(expectedStoredName);
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

        return makeCreateTableSQL(tableName, columnName, IntegerType.INSTANCE);
    }

    private static String makeCreateTableSQL(String tableName, String columnName, SchemaDataType schemaDataType) {

        final String schemaDataTypeString = makeDataTypeString(schemaDataType);

        return "create table " + tableName + " (" + columnName + ' ' + schemaDataTypeString +')';
    }

    private static String makeDropTableSQL(String tableName) {

        return "drop table " + tableName;
    }

    private static String makeAlterTableAddColumnSQL(String tableName, String columnName, SchemaDataType schemaDataType) {

        return "alter table " + tableName + " add " + columnName + ' ' + makeDataTypeString(schemaDataType);
    }

    private static String makeAlterTableDropColumnSQL(String tableName, String columnName) {

        return "alter table " + tableName + " add " + columnName;
    }

    private static SQLBuilder createSQLBuilder() {

        return new SQLBuilder();
    }

    private static final class SQLBuilder extends BaseSQLBuilder<SQLBuilder> {

        SQLBuilder addCreateTableSQL(String tableName) {

            appendSQL(makeCreateTableSQL(tableName));

            return this;
        }

        SQLBuilder addCreateTableSQL(String tableName, String columnName, SchemaDataType schemaDataType) {

            appendSQL(makeCreateTableSQL(tableName, columnName, schemaDataType));

            return this;
        }

        SQLBuilder addCreateTableSQL(String tableName, Consumer<ColumnSQLBuilder> b) {

            final ColumnSQLBuilder builder = new ColumnSQLBuilder(this);

            b.accept(builder);

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

    private static final class ColumnSQLBuilder extends BaseSQLBuilder<ColumnSQLBuilder> {

        ColumnSQLBuilder(BaseSQLBuilder<?> sqlBuilder) {
            super(sqlBuilder);
        }

        ColumnSQLBuilder addColumn(String columnName, SchemaDataType schemaDataTypes) {

            append(' ').append(columnName).append(' ').append(makeDataTypeString(schemaDataTypes));

            return this;
        }
    }

    private static final class AlterTableSQLBuilder extends BaseSQLBuilder<AlterTableSQLBuilder> {

        AlterTableSQLBuilder(BaseSQLBuilder<?> sqlBuilder) {
            super(sqlBuilder);
        }

        AlterTableSQLBuilder addAddColumn(String columnName, SchemaDataType schemaDataTypes) {

            append("add ").append(columnName).append(' ').append(makeDataTypeString(schemaDataTypes));

            return this;
        }

        AlterTableSQLBuilder addDropColumn(String columnName) {

            append("drop ").append(columnName);

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
