package dev.jdata.db.schema.model.diff.databaseschema;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.model.DatabaseSchemaModelRootObject;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.parse.ISQLString;

public final class DatabaseSchemaDiff extends DatabaseSchemaModelRootObject {

    private final BaseSQLDDLOperationStatement sqlDDLStatement;
    private final ISQLString sqlString;

    public DatabaseSchemaDiff(AllocationType allocationType, DatabaseId databaseId, BaseSQLDDLOperationStatement sqlDDLStatement, ISQLString sqlString) {
        super(allocationType, databaseId);

        this.sqlDDLStatement = Objects.requireNonNull(sqlDDLStatement);
        this.sqlString = Objects.requireNonNull(sqlString);
    }

    public BaseSQLDDLOperationStatement getSQLDDLStatement() {
        return sqlDDLStatement;
    }

    public ISQLString getSQLString() {
        return sqlString;
    }
}
