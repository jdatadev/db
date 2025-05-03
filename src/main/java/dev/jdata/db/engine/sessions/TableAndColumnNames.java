package dev.jdata.db.engine.sessions;

import java.util.Objects;

import org.jutils.io.strings.StringRef;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.sql.ast.statements.dml.SQLObjectName;
import dev.jdata.db.utils.checks.Checks;

public final class TableAndColumnNames extends ColumnsObjectAndColumnNames<Table> {

    public int getTableId(long tableName) {

        StringRef.checkIsString(tableName);

        return getColumnsObjectId(tableName);
    }

    public int getTableId(SQLObjectName objectName) {

        Objects.requireNonNull(objectName);

        return getColumnsObjectId(objectName);
    }

    public Table getTable(int tableId) {

        Checks.isTableId(tableId);

        return getColumnsObject(tableId);
    }

    @Override
    DDLObjectType getDDLObjectType() {

        return DDLObjectType.TABLE;
    }
}
