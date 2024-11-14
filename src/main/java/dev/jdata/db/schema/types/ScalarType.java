package dev.jdata.db.schema.types;

abstract class ScalarType extends SchemaDataType {

    ScalarType(boolean nullable) {
        super(nullable);
    }
}
