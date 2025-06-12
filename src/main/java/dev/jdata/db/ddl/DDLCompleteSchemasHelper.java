package dev.jdata.db.ddl;

import java.util.Objects;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilderAllocators;
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
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public class DDLCompleteSchemasHelper extends DDLSchemasHelper {

    public static final class DDLCompleteSchemaCachedObjects extends DDLSchemaCachedObjects {

        private final NodeObjectCache<DDLCompleteSchemaParameter<?>> ddlEffectiveSchemaParameterCache;

        public DDLCompleteSchemaCachedObjects(SchemaMapBuilderAllocators schemaMapBuilderAllocators) {
            super(schemaMapBuilderAllocators);

            this.ddlEffectiveSchemaParameterCache = new NodeObjectCache<>(DDLCompleteSchemaParameter::new);
        }

        DDLCompleteSchemaParameter<?> allocateDDLCompleteSchemaParameter() {

            return ddlEffectiveSchemaParameterCache.allocate();
        }

        void freeDDLEffectiveSchemaParameter(DDLCompleteSchemaParameter<?> ddlEffectiveSchemaParameter) {

            ddlEffectiveSchemaParameterCache.free(ddlEffectiveSchemaParameter);
        }
    }

    private static final SQLStatementAdapter<DDLCompleteSchemaParameter<?>, Void, RuntimeException> ddlStatementVisitor
            = new SQLStatementAdapter<DDLCompleteSchemaParameter<?>, Void, RuntimeException>() {

        @Override
        public Void onCreateTable(SQLCreateTableStatement createTableStatement, DDLCompleteSchemaParameter<?> parameter) {

            DDLCreateTableSchemasHelper.processCreateTable(createTableStatement, parameter.stringManagement, parameter.schemaMapBuilders, parameter.ddlSchemaCachedObjects,
                    parameter.schemaObjectIdAllocator, p -> p.allocateSchemaObjectId(DDLObjectType.TABLE));

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

    private static final class DDLCompleteSchemaParameter<P> extends ObjectCacheNode {

        private StringManagement stringManagement;
        private SchemaMapBuilders schemaMapBuilders;
        private DDLSchemaCachedObjects ddlSchemaCachedObjects;
        private SchemaObjectIdAllocator<P> schemaObjectIdAllocator;

        void initialize(StringManagement stringManagement, SchemaMapBuilders schemaMapBuilders, DDLSchemaCachedObjects ddlSchemaCachedObjects,
                SchemaObjectIdAllocator<P> schemaObjectIdAllocator) {

            this.stringManagement = Objects.requireNonNull(stringManagement);
            this.schemaMapBuilders = Objects.requireNonNull(schemaMapBuilders);
            this.ddlSchemaCachedObjects = Objects.requireNonNull(ddlSchemaCachedObjects);
            this.schemaObjectIdAllocator = Objects.requireNonNull(schemaObjectIdAllocator);
        }
    }

    public static <P> CompleteSchemaMaps processSQLStatements(IIndexList<BaseSQLStatement> sqlStatements, StringManagement stringManagement,
            DDLCompleteSchemaCachedObjects ddlEffetiveSchemaCachedObjects, SchemaObjectIdAllocator<P> schemaObjectIdAllocator) {

        Objects.requireNonNull(sqlStatements);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(ddlEffetiveSchemaCachedObjects);
        Objects.requireNonNull(schemaObjectIdAllocator);

        final long numSQLStatements = sqlStatements.getNumElements();

        final SchemaMapBuilders schemaMapBuilders = ddlEffetiveSchemaCachedObjects.allocateSchemaMapBuilders();

        @SuppressWarnings("unchecked")
        final DDLCompleteSchemaParameter<P> ddlCompleteSchemaParameter = (DDLCompleteSchemaParameter<P>)ddlEffetiveSchemaCachedObjects.allocateDDLCompleteSchemaParameter();

        ddlCompleteSchemaParameter.initialize(stringManagement, schemaMapBuilders, ddlEffetiveSchemaCachedObjects, schemaObjectIdAllocator);

        final CompleteSchemaMaps completeSchemaMaps;

        try {
            for (long i = 0; i < numSQLStatements; ++ i) {

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
