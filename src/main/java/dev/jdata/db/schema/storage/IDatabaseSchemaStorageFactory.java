package dev.jdata.db.schema.storage;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.parse.SQLParser.SQLString;

public interface IDatabaseSchemaStorageFactory<E extends Exception> {

    public interface IDatabaseSchemaStorage<E extends Exception> {

        void storeSchemaDiffStatement(BaseSQLDDLOperationStatement sqlDDLStatement, SQLString sqlString, StringResolver stringResolver) throws E;

        void completeSchemaDiff(IEffectiveDatabaseSchema completeEffectiveDatabaseSchema, IDatabaseSchemaSerializer<E> schemaSerializer) throws E;
    }

    IDatabaseSchemaStorage<E> storeSchemaDiff(DatabaseSchemaVersion databaseSchemaVersion) throws E;
}
