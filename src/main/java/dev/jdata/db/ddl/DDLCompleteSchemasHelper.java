package dev.jdata.db.ddl;

import java.util.Objects;

import dev.jdata.db.ddl.allocators.DDLCompleteSchemaCachedObjects;
import dev.jdata.db.ddl.scratchobjects.DDLCompleteSchemaParameter;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.SchemaMapBuilders;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.ast.statements.SQLStatementAdapter;
import dev.jdata.db.sql.ast.statements.function.SQLCreateFunctionStatement;
import dev.jdata.db.sql.ast.statements.index.SQLCreateIndexStatement;
import dev.jdata.db.sql.ast.statements.procedure.SQLCreateProcedureStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.trigger.SQLCreateTriggerStatement;
import dev.jdata.db.utils.adt.lists.IndexList;

public class DDLCompleteSchemasHelper extends DDLSchemasHelper {

    private static final SQLStatementAdapter<DDLCompleteSchemaParameter<?>, Void, RuntimeException> ddlStatementVisitor
            = new SQLStatementAdapter<DDLCompleteSchemaParameter<?>, Void, RuntimeException>() {

        @Override
        public Void onCreateTable(SQLCreateTableStatement createTableStatement, DDLCompleteSchemaParameter<?> parameter) {

            DDLCreateTableSchemasHelper.processCreateTable(createTableStatement, parameter.getStringManagement(), parameter.getSchemaMapBuilders(),
                    parameter.getDDLSchemaCachedObjects(), parameter.getSchemaObjectIdAllocator(), p -> p.allocateSchemaObjectId(DDLObjectType.TABLE));

            return null;
        }

        @Override
        public Void onCreateIndex(SQLCreateIndexStatement createIndexStatement, DDLCompleteSchemaParameter<?> parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Void onCreateTrigger(SQLCreateTriggerStatement createTriggerStatement, DDLCompleteSchemaParameter<?> parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Void onCreateFunction(SQLCreateFunctionStatement createFunctionStatement, DDLCompleteSchemaParameter<?> parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Void onCreateProcedure(SQLCreateProcedureStatement createProcedureStatement, DDLCompleteSchemaParameter<?> parameter) {

            throw new UnsupportedOperationException();
        }
    };

    public static <P, COMPLETE_SCHEMA_MAPS extends CompleteSchemaMaps<?>, SCHEMA_MAP_BUILDER extends SchemaMapBuilders<?, ?, ?, ?, ?, ?, COMPLETE_SCHEMA_MAPS>>
    COMPLETE_SCHEMA_MAPS processSQLStatements(IndexList<BaseSQLStatement> sqlStatements,
            StringManagement stringManagement,
            DDLCompleteSchemaCachedObjects<SCHEMA_MAP_BUILDER> ddlEffetiveSchemaCachedObjects,
            SchemaObjectIdAllocator<P> schemaObjectIdAllocator) {

        Objects.requireNonNull(sqlStatements);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(ddlEffetiveSchemaCachedObjects);
        Objects.requireNonNull(schemaObjectIdAllocator);

        final long numSQLStatements = sqlStatements.getNumElements();

        final SCHEMA_MAP_BUILDER schemaMapBuilders = ddlEffetiveSchemaCachedObjects.allocateSchemaMapBuilders();

        @SuppressWarnings("unchecked")
        final DDLCompleteSchemaParameter<P> ddlCompleteSchemaParameter = (DDLCompleteSchemaParameter<P>)ddlEffetiveSchemaCachedObjects.allocateDDLCompleteSchemaParameter();

        ddlCompleteSchemaParameter.initialize(stringManagement, schemaMapBuilders, ddlEffetiveSchemaCachedObjects, schemaObjectIdAllocator);

        final COMPLETE_SCHEMA_MAPS completeSchemaMaps;

        try {
            for (long i = 0L; i < numSQLStatements; ++ i) {

                final BaseSQLStatement sqlStatement = sqlStatements.get(i);

                sqlStatement.visit(ddlStatementVisitor, ddlCompleteSchemaParameter);
            }

            completeSchemaMaps = schemaMapBuilders.build();
        }
        finally {

            ddlEffetiveSchemaCachedObjects.freeSchemaMapBuilders(schemaMapBuilders);

            ddlEffetiveSchemaCachedObjects.freeDDLEffectiveSchemaParameter(ddlCompleteSchemaParameter);
        }

        return completeSchemaMaps;
    }
}
