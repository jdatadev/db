package dev.jdata.db.custom.informix.schema.types;

import dev.jdata.db.schema.types.SchemaCustomType;

abstract class InformixCustomType extends SchemaCustomType {

    public abstract <P, R> R visitInformixType(InformixTypeVisitor<P, R> visitor, P parameter);
}
