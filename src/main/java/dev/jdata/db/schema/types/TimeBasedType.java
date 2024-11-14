package dev.jdata.db.schema.types;

abstract class TimeBasedType extends ScalarType {

    TimeBasedType(boolean nullable) {
        super(nullable);
    }
}
