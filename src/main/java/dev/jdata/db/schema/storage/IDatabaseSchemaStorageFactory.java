package dev.jdata.db.schema.storage;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.engine.database.strings.IStringWriter;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.strings.ISQLString;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.elements.IIntAnyOrderAddable;

public interface IDatabaseSchemaStorageFactory<E extends Exception> {

    public interface IDatabaseSchemaStorage<E extends Exception> extends IResettable {

        void storeSchemaDiffStatement(BaseSQLDDLOperationStatement sqlDDLStatement, StringResolver sqlDDLStatementStringResolver, ISQLString sqlString) throws E;

        void completeSchemaDiff(IEffectiveDatabaseSchema completeEffectiveDatabaseSchema, IDatabaseSchemaSerializer schemaSerializer, IStringWriter stringWriter,
                ISQLOutputter<E> sqlOutputter) throws E;
    }

    public interface IDatabaseSchemaRetrieval<E extends Exception> extends IResettable {

    }

    IDatabaseSchemaStorage<E> createSchemaDiffStorage(DatabaseSchemaVersion databaseSchemaVersion) throws E;

    void listVersions(IIntAnyOrderAddable dst);

    IDatabaseSchemaRetrieval<E> createSchemaDiffRetrieval(DatabaseSchemaVersion databaseSchemaVersion) throws E;
}
