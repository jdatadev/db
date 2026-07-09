package dev.jdata.db.ddl.helpers.sqltoschema.statements;

import java.util.Objects;

import org.jutils.ast.objects.BaseASTElement;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.utils.checks.Checks;

abstract class DDLTableSchemasHelper extends DDLSchemasHelper {

    static <T extends BaseASTElement> Column convertToColumn(SQLTableColumnDefinition sqlTableColumnDefinition, int columnId,
            ISQLToSchemaStringMutators sqlToSchemaStringMutators) {

        Objects.requireNonNull(sqlTableColumnDefinition);
        Checks.isColumnId(columnId);
        Objects.requireNonNull(sqlToSchemaStringMutators);

        final long columnName = sqlToSchemaStringMutators.storeParsedStringRef(sqlTableColumnDefinition.getName());
        final long hashColumnName = sqlToSchemaStringMutators.storeHashStringRefFromStoredSQLStringRef(columnName);

        final boolean hasNotNull =    sqlTableColumnDefinition.getNotKeyword() != BaseASTElement.NO_KEYWORD
                                   && sqlTableColumnDefinition.getNullKeyword() != BaseASTElement.NO_KEYWORD;

        final boolean nullable = !hasNotNull;

        final SchemaDataType schemaDataType = convertDataType(sqlTableColumnDefinition, sqlToSchemaStringMutators);

        return new Column(columnName, hashColumnName, columnId, schemaDataType, nullable);
    }

    private static SchemaDataType convertDataType(SQLTableColumnDefinition sqlTableColumnDefinition, ISQLToSchemaStringMutators sqlToSchemaStringMutators) {

        return sqlTableColumnDefinition.getType();
    }
}
