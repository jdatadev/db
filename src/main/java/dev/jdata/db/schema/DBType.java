package dev.jdata.db.schema;

import java.util.Objects;

@Deprecated
public enum DBType {

    INTEGER(DBCategory.NUMERIC),
    FLOATING_POINT(DBCategory.NUMERIC),
    DECIMAL(DBCategory.NUMERIC),
    STRING(DBCategory.STRING),
    DATE(DBCategory.TIME),
    TIME(DBCategory.TIME),
    TIMESTAMP(DBCategory.TIME),
    LARGE_OBJECT(DBCategory.LARGE_OBJECT);

    public enum DBCategory {

        NUMERIC,
        STRING,
        TIME,
        LARGE_OBJECT;
    }

    private final DBCategory category;

    private DBType(DBCategory category) {

        this.category = Objects.requireNonNull(category);
    }

    public DBCategory getCategory() {
        return category;
    }
}
