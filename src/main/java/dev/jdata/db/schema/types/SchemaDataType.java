package dev.jdata.db.schema.types;

import dev.jdata.db.utils.debug.ToStringable;

public abstract class SchemaDataType extends ToStringable {

    public abstract <P, R, E extends Exception> R visit(SchemaDataTypeVisitor<P, R, E> visitor, P parameter) throws E;

    @Override
    public boolean equals(Object object) {

        return object != null && getClass().equals(object.getClass());
    }
}
