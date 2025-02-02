package dev.jdata.db.engine.transactions;

import java.util.Objects;

import dev.jdata.db.schema.DBType;

public enum RowValueType {

    BOOLEAN(DBType.BOOLEAN),

    BYTE(DBType.INTEGER),
    SHORT(DBType.INTEGER),
    INT(DBType.INTEGER),
    LONG(DBType.INTEGER),

    FLOAT(DBType.FLOATING_POINT),
    DOUBLE(DBType.FLOATING_POINT),

    DECIMAL(DBType.DECIMAL),

    STRING(DBType.STRING),
    CASE_INSENSITIVE_STRING(DBType.STRING);

    private final DBType dbType;

    private RowValueType(DBType dbType) {

        this.dbType = Objects.requireNonNull(dbType);
    }

    public DBType getDBType() {
        return dbType;
    }
}
