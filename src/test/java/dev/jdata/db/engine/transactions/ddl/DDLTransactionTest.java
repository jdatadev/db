package dev.jdata.db.engine.transactions.ddl;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;

import dev.jdata.db.engine.database.DatabaseStringManagement;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.engine.database.StringStorer;
import dev.jdata.db.engine.transactions.ddl.HeapDDLTransaction.HeapDDLTransactionCachedObjects;
import dev.jdata.db.engine.validation.exceptions.SQLValidationException;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.EffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.storage.DatabaseSchemaStorageFactory;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.parse.ISQLString;
import dev.jdata.db.test.TestFileSystemAccess;
import dev.jdata.db.test.unit.BaseDBTest;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.paths.PathIOUtil;

public final class DDLTransactionTest extends BaseDBTest {

    @Test
    @Category(UnitTest.class)
    public void testCreateTable() throws ParserException, SQLValidationException, IOException {

        final String createTableSQL = "create table test_table (test_column integer)";
        final ISQLString createTableSQLString = createSQLString(createTableSQL);

        final ParsedStatement<SQLCreateTableStatement> parsedStatement = checkParseANSIStatementAll(createTableSQL, SQLCreateTableStatement.class);

        final SQLCreateTableStatement sqlCreateTableStatement = parsedStatement.getStatement();
        final StringResolver parserStringResolver = parsedStatement.getStringResolver();

        final StringStorer stringStorer = createStringStorer();
        final DatabaseId databaseId = getTestDatabaseId();

        final IEffectiveDatabaseSchema effectiveDatabaseSchema = EffectiveDatabaseSchema.empty(databaseId);

        final DatabaseStringManagement databaseStringManagement = createDatabaseStringManagement(stringStorer);
        final StringManagement stringManagement = createStringManagement(databaseStringManagement, parserStringResolver);

        final HeapDDLTransaction ddlTransaction = createDDLTransaction(effectiveDatabaseSchema, stringStorer);

        ddlTransaction.addDDLStatement(sqlCreateTableStatement, createTableSQLString, parserStringResolver, stringManagement);

        try (TestFileSystemAccess testFileSystemAccess = TestFileSystemAccess.create()) {

            final IRelativeFileSystemAccess fileSystemAccess = testFileSystemAccess.createRelative();

            final DatabaseSchemaStorageFactory databaseSchemaStorageFactory = new DatabaseSchemaStorageFactory(fileSystemAccess, createStringCache(),
                    createTextToByteOutputPrerequisites());

            final StringBuilder sb = new StringBuilder();

            final DatabaseSchemaVersion nextDatabaseSchemaVersion = effectiveDatabaseSchema.getVersion().next();

            final EffectiveDatabaseSchema newEffectiveDatabaseSchema = ddlTransaction.commit(nextDatabaseSchemaVersion, databaseSchemaStorageFactory,
                    createANSIDatabaseSchemaSerializer(), createSQLOutputterWitIOException(sb), createCompleteSchemaMapsBuilder(), createSchemaObjectIdAllocators());

System.out.println("sql out '" + sb + '\'');

            verifyDDLTransaction(testFileSystemAccess.getRootPath(), sb, parserStringResolver, sqlCreateTableStatement);
        }
    }

    private static void verifyDDLTransaction(Path rootPath, CharSequence outputtedSQL, StringResolver parserStringResolver, BaseSQLDDLOperationStatement ... sqlStatements) {

        try {
            PathIOUtil.printRecursively(rootPath, "path ");
        }
        catch (IOException ex) {

            throw new RuntimeException(ex);
        }
        assertThat(outputtedSQL).isNotBlank();
    }

    private static HeapDDLTransaction createDDLTransaction(IEffectiveDatabaseSchema effectiveDatabaseSchema, StringResolver schemaStringResolver) {

        final HeapDDLTransaction ddlTransaction = new HeapDDLTransaction();

        ddlTransaction.initialize(effectiveDatabaseSchema, schemaStringResolver, new HeapDDLTransactionCachedObjects());

        return ddlTransaction;
    }
}
