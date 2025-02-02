package dev.jdata.db.custom.informix.schema.types;

import dev.jdata.db.schema.types.SchemaCustomType;

abstract class InformixCustomType extends SchemaCustomType {

    public abstract <R, T> R visitInformixType(InformixTypeVisitor<T, R> visitor, T parameter);
}
