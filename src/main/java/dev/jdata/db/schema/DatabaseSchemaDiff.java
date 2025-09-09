package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.schema.model.DatabaseSchemaModelObject;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.parse.ISQLString;

public final class DatabaseSchemaDiff extends DatabaseSchemaModelObject {

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
