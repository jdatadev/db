package dev.jdata.db.ddl.helpers.sqltoschema.complete;

import java.util.Objects;
import java.util.function.ToIntFunction;

import dev.jdata.db.ddl.helpers.sqltoschema.complete.scratchobjects.DDLSchemaScratchObjects;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.DDLSchemasHelper;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMap;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMapBuilder;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;

public class DDLCompleteSchemasHelper extends DDLSchemasHelper {

    private static final DDLSQLStatements<?, ?> ddlStatementVisitor = new DDLSQLStatements<>();

    public static <
                    INT_SET_BUILDER extends IIntSetBuilder<?, ?>,
                    COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>,
                    COMPLETE_SCHEMA_MAP extends ICompleteSchemaMap, HEAP_COMPLETE_SCHEMA_MAP extends IHeapCompleteSchemaMap>

    COMPLETE_SCHEMA_MAP createSchemasFromSQLStatements(IIndexList<BaseSQLStatement> sqlStatements, StringManagement stringManagement,
            IDDLSchemaSQLStatementsAllocators<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER, COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, ?> ddlSchemaSQLStatementsAllocators,
            DDLSchemaScratchObjects<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER> ddlSchemaScratchObjects, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

        Objects.requireNonNull(sqlStatements);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(ddlSchemaSQLStatementsAllocators);
        Objects.requireNonNull(ddlSchemaScratchObjects);
        Objects.requireNonNull(schemaObjectIdAllocator);

        final long numSQLStatements = sqlStatements.getNumElements();

        final INonDiffSchemaMapBuilder<SchemaObject, COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, ?> completeSchemaMapsBuilder = null;
//                = ddlSchemaSQLStatementsWorkerObjects.allocateCompleteSchemaMapsBuilder();

        final DDLSchemaSQLStatementsParameter<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER> ddlSchemaSQLStatementsParameter
                = ddlSchemaSQLStatementsAllocators.allocateDDLSchemaSQLStatementsParameter();

        final IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> columnIndexListAllocator = ddlSchemaSQLStatementsParameter.getColumnIndexListAllocator();

        ddlSchemaSQLStatementsParameter.initialize(stringManagement, ddlSchemaScratchObjects, schemaObjectIdAllocator, completeSchemaMapsBuilder, columnIndexListAllocator);

        final COMPLETE_SCHEMA_MAP completeSchemaMaps;

        try {
            for (long i = 0L; i < numSQLStatements; ++ i) {

                final BaseSQLStatement sqlStatement = sqlStatements.get(i);

                @SuppressWarnings("unchecked")
                final DDLSQLStatements<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER> visitor = (DDLSQLStatements<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER>)ddlStatementVisitor;

                sqlStatement.visit(visitor, ddlSchemaSQLStatementsParameter);
            }

            completeSchemaMaps = completeSchemaMapsBuilder.buildNotEmpty();
        }
        finally {

// fix
//            ddlSchemaSQLStatementsWorkerObjects.freeCompleteSchemaMapsBuilder(completeSchemaMapsBuilder);

            ddlSchemaSQLStatementsAllocators.freeDDLSchemaSQLStatementsParameter(ddlSchemaSQLStatementsParameter);

            if (Boolean.TRUE) {

                throw new UnsupportedOperationException();
            }
        }

        return completeSchemaMaps;
    }
}
