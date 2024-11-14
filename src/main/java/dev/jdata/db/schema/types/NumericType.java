package dev.jdata.db.schema.types;

abstract class NumericType extends ScalarType {

    NumericType(boolean nullable) {
        super(nullable);
    }
}
