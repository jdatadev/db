package dev.jdata.db.schema.types;

abstract class BaseSequenceType extends BaseIntegerType {

    BaseSequenceType(boolean nullable) {
        super(nullable);
    }
}
