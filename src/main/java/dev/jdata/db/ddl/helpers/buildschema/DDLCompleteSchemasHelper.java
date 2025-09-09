package dev.jdata.db.ddl.helpers.buildschema;

import java.util.Objects;
import java.util.function.ToIntFunction;

import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.ddl.helpers.DDLSchemasHelper;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.utils.adt.lists.IndexList;

public class DDLCompleteSchemasHelper extends DDLSchemasHelper {

    private static final DDLSQLStatements<?> ddlStatementVisitor = new DDLSQLStatements<>();

    public static <COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps, HEAP_COMPLETE_SCHEMA_MAPS extends IHeapAllCompleteSchemaMaps>
    COMPLETE_SCHEMA_MAPS createSchemasFromSQLStatements(
            IndexList<BaseSQLStatement> sqlStatements, StringManagement stringManagement,
            IDDLSchemaSQLStatementsWorkerObjects<COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS> ddlSchemaSQLStatementsWorkerObjects,
            DDLSchemaScratchObjects ddlSchemaScratchObjects, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

        Objects.requireNonNull(sqlStatements);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(ddlSchemaSQLStatementsWorkerObjects);
        Objects.requireNonNull(ddlSchemaScratchObjects);
        Objects.requireNonNull(schemaObjectIdAllocator);

        final long numSQLStatements = sqlStatements.getNumElements();

        final ICompleteSchemaMapsBuilder<SchemaObject, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS> completeSchemaMapsBuilder
                = ddlSchemaSQLStatementsWorkerObjects.allocateCompleteSchemaMapBuilders();

        final DDLSchemaSQLStatementsParameter ddlSchemaSQLStatementsParameter = ddlSchemaSQLStatementsWorkerObjects.allocateDDLSchemaSQLStatementsParameter();

        ddlSchemaSQLStatementsParameter.initialize(stringManagement, ddlSchemaScratchObjects, schemaObjectIdAllocator, completeSchemaMapsBuilder);

        final COMPLETE_SCHEMA_MAPS completeSchemaMaps;

        try {
            for (long i = 0L; i < numSQLStatements; ++ i) {

                final BaseSQLStatement sqlStatement = sqlStatements.get(i);

                sqlStatement.visit(ddlStatementVisitor, ddlSchemaSQLStatementsParameter);
            }

            completeSchemaMaps = completeSchemaMapsBuilder.build();
        }
        finally {

            ddlSchemaSQLStatementsWorkerObjects.freeCompleteSchemaMapBuilders(completeSchemaMapsBuilder);

            ddlSchemaSQLStatementsWorkerObjects.freeDDLSchemaSQLStatementsParameter(ddlSchemaSQLStatementsParameter);
        }

        return completeSchemaMaps;
    }
}
