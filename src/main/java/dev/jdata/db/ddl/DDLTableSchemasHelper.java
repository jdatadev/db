package dev.jdata.db.ddl;

import java.util.Objects;

import org.jutils.ast.objects.BaseASTElement;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.utils.checks.Checks;

class DDLTableSchemasHelper extends DDLSchemasHelper {
/*
    private static <T extends BaseASTElement> void convertColumnsMapped(ASTList<T> sqlTableColumnDefinitions, ProcessTableColumnsScratchObject scratchObject) {

        Objects.requireNonNull(sqlTableColumnDefinitions);
        Objects.requireNonNull(scratchObject);

        sqlTableColumnDefinitions.forEachWithIndexAndParameter(scratchObject, (c, i, s) -> {

            final Column column = convertToColumn(c, s.allocateColumnId(), s.getStringManagement());

            s.addColumn(column);
        });
    }

    private static <T extends BaseASTElement> Column convertToColumn(T columnDefinition, int columnId, StringManagement stringManagement,
            Function<T, SQLTableColumnDefinition> columnDefinitionMapper) {

        final SQLTableColumnDefinition sqlTableColumnDefinition = columnDefinitionMapper.apply(columnDefinition);

        return convertToColumn(sqlTableColumnDefinition, columnId, stringManagement);
    }
*/
    static <T extends BaseASTElement> Column convertToColumn(SQLTableColumnDefinition sqlTableColumnDefinition, int columnId, StringManagement stringManagement) {

        Objects.requireNonNull(sqlTableColumnDefinition);
        Checks.isColumnId(columnId);
        Objects.requireNonNull(stringManagement);

        final long columnName = stringManagement.resolveParsedStringRef(sqlTableColumnDefinition.getName());
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
