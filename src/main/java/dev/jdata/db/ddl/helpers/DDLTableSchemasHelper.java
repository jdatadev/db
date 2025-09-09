package dev.jdata.db.ddl.helpers;

import java.util.Objects;

import org.jutils.ast.objects.BaseASTElement;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.utils.checks.Checks;

class DDLTableSchemasHelper extends DDLSchemasHelper {

    static <T extends BaseASTElement> Column convertToColumn(SQLTableColumnDefinition sqlTableColumnDefinition, int columnId, StringManagement stringManagement) {

        Objects.requireNonNull(sqlTableColumnDefinition);
        Checks.isColumnId(columnId);
        Objects.requireNonNull(stringManagement);

        final long columnName = stringManagement.storeParsedStringRef(sqlTableColumnDefinition.getName());
        final long hashColumnName = stringManagement.getHashStringRef(columnName);

        final boolean hasNotNull =    sqlTableColumnDefinition.getNotKeyword() != BaseASTElement.NO_KEYWORD
                                   && sqlTableColumnDefinition.getNullKeyword() != BaseASTElement.NO_KEYWORD;

        final boolean nullable = !hasNotNull;

        final SchemaDataType schemaDataType = convertDataType(sqlTableColumnDefinition, stringManagement);

        return new Column(columnName, hashColumnName, columnId, schemaDataType, nullable);
    }

    private static SchemaDataType convertDataType(SQLTableColumnDefinition sqlTableColumnDefinition, StringManagement stringManagement) {

        return sqlTableColumnDefinition.getType();
    }
}
