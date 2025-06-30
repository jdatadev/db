package dev.jdata.db.schema.storage;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.parse.SQLString;
import dev.jdata.db.utils.adt.IResettable;

public interface IDatabaseSchemaStorageFactory<T extends CompleteSchemaMaps<?>, E extends Exception> {

    public interface IDatabaseSchemaStorage<T extends CompleteSchemaMaps<?>, E extends Exception> extends IResettable {

        void storeSchemaDiffStatement(BaseSQLDDLOperationStatement sqlDDLStatement, SQLString sqlString, StringResolver stringResolver) throws E;

        void completeSchemaDiff(IEffectiveDatabaseSchema completeEffectiveDatabaseSchema, IDatabaseSchemaSerializer<T> schemaSerializer, StringResolver stringResolver,
                ISQLOutputter<E> sqlOutputter) throws E;
    }

    IDatabaseSchemaStorage<T, E> storeSchemaDiff(DatabaseSchemaVersion databaseSchemaVersion) throws E;
}
