package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.schema.model.DatabaseSchemaModelObject;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.parse.SQLString;

public final class DatabaseSchemaDiff extends DatabaseSchemaModelObject {

    private final BaseSQLDDLOperationStatement sqlDDLStatement;
    private final SQLString sqlString;

    public DatabaseSchemaDiff(AllocationType allocationType, DatabaseId databaseId, BaseSQLDDLOperationStatement sqlDDLStatement, SQLString sqlString) {
        super(allocationType, databaseId);

        this.sqlDDLStatement = Objects.requireNonNull(sqlDDLStatement);
        this.sqlString = Objects.requireNonNull(sqlString);
    }

    public BaseSQLDDLOperationStatement getSQLDDLStatement() {
        return sqlDDLStatement;
    }

    public SQLString getSQLString() {
        return sqlString;
    }
}
