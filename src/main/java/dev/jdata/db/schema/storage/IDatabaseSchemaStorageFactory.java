package dev.jdata.db.schema.storage;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.parse.ISQLString;
import dev.jdata.db.utils.adt.IResettable;

public interface IDatabaseSchemaStorageFactory<E extends Exception> {

    public interface IDatabaseSchemaStorage<E extends Exception> extends IResettable {

        void storeSchemaDiffStatement(BaseSQLDDLOperationStatement sqlDDLStatement, ISQLString sqlString, StringResolver stringResolver) throws E;

        void completeSchemaDiff(IEffectiveDatabaseSchema completeEffectiveDatabaseSchema, IDatabaseSchemaSerializer schemaSerializer, StringResolver stringResolver,
                ISQLOutputter<E> sqlOutputter) throws E;
    }

    IDatabaseSchemaStorage<E> createSchemaDiffStorage(DatabaseSchemaVersion databaseSchemaVersion) throws E;
}
