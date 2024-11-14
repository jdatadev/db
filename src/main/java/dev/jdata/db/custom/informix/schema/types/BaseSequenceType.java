package dev.jdata.db.custom.informix.schema.types;

abstract class BaseSequenceType extends InformixCustomType {

    BaseSequenceType(boolean nullable) {
        super(nullable);
    }
}
