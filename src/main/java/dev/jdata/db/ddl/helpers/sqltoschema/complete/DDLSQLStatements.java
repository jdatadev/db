package dev.jdata.db.ddl.helpers.sqltoschema.complete;

import dev.jdata.db.ddl.helpers.sqltoschema.complete.scratchobjects.DDLSchemaScratchObjects;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.DDLCreateTableSchemasHelper;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects.ProcessCreateTableScratchObject;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.sql.ast.statements.SQLStatementAdapter;
import dev.jdata.db.sql.ast.statements.function.SQLCreateFunctionStatement;
import dev.jdata.db.sql.ast.statements.index.SQLCreateIndexStatement;
import dev.jdata.db.sql.ast.statements.procedure.SQLCreateProcedureStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.trigger.SQLCreateTriggerStatement;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;

final class DDLSQLStatements<T extends IIntSetBuilder<?, ?>, U extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>>

        extends SQLStatementAdapter<DDLSchemaSQLStatementsParameter<T, U>, Void, RuntimeException> {

    @Override
    public Void onCreateTable(SQLCreateTableStatement createTableStatement, DDLSchemaSQLStatementsParameter<T, U> parameter) {

        final DDLSchemaScratchObjects<T, U> ddlSchemaScratchObjects = parameter.getDDLSchemaScratchObjects();

        final ProcessCreateTableScratchObject processCreateTableScratchObject = ddlSchemaScratchObjects.allocateProcessCreateTableScratchObject();

        try {
            final Table table = DDLCreateTableSchemasHelper.processCreateTable(createTableStatement, parameter.getStringManagement(), parameter.getColumnIndexListAllocator(),
                    processCreateTableScratchObject, parameter, p -> p.allocateSchemaObjectId(DDLObjectType.TABLE));

            parameter.getSchemaMapBuilder().addSchemaObject(table);
        }
        finally {

            ddlSchemaScratchObjects.freeProcessCreateTableScratchObject(processCreateTableScratchObject);
        }

        return null;
    }

    @Override
    public Void onCreateIndex(SQLCreateIndexStatement createIndexStatement, DDLSchemaSQLStatementsParameter<T, U> parameter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Void onCreateTrigger(SQLCreateTriggerStatement createTriggerStatement, DDLSchemaSQLStatementsParameter<T, U> parameter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Void onCreateFunction(SQLCreateFunctionStatement createFunctionStatement, DDLSchemaSQLStatementsParameter<T, U> parameter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Void onCreateProcedure(SQLCreateProcedureStatement createProcedureStatement, DDLSchemaSQLStatementsParameter<T, U> parameter) {

        throw new UnsupportedOperationException();
    }
}
