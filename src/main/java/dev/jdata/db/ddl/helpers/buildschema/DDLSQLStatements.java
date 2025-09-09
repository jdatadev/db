package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.ddl.helpers.DDLCreateTableSchemasHelper;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.sql.ast.statements.SQLStatementAdapter;
import dev.jdata.db.sql.ast.statements.function.SQLCreateFunctionStatement;
import dev.jdata.db.sql.ast.statements.index.SQLCreateIndexStatement;
import dev.jdata.db.sql.ast.statements.procedure.SQLCreateProcedureStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.trigger.SQLCreateTriggerStatement;

final class DDLSQLStatements<T extends ICompleteSchemaMapsBuilder<SchemaObject, ?, ?>>

        extends SQLStatementAdapter<DDLSchemaSQLStatementsParameter, Void, RuntimeException> {

    @Override
    public Void onCreateTable(SQLCreateTableStatement createTableStatement, DDLSchemaSQLStatementsParameter parameter) {

        final Table table = DDLCreateTableSchemasHelper.processCreateTable(createTableStatement, parameter.getStringManagement(), parameter.getDDLSchemaScratchObjects(),
                parameter, p -> p.allocateSchemaObjectId(DDLObjectType.TABLE));

        parameter.getCompleteSchemaMapsBuilder().addSchemaObject(table);

        return null;
    }

    @Override
    public Void onCreateIndex(SQLCreateIndexStatement createIndexStatement, DDLSchemaSQLStatementsParameter parameter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Void onCreateTrigger(SQLCreateTriggerStatement createTriggerStatement, DDLSchemaSQLStatementsParameter parameter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Void onCreateFunction(SQLCreateFunctionStatement createFunctionStatement, DDLSchemaSQLStatementsParameter parameter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Void onCreateProcedure(SQLCreateProcedureStatement createProcedureStatement, DDLSchemaSQLStatementsParameter parameter) {

        throw new UnsupportedOperationException();
    }
}
