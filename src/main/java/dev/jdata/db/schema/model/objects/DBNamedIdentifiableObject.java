package dev.jdata.db.schema.model.objects;

import java.util.Objects;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DBNamedObject;
import dev.jdata.db.utils.checks.Checks;

public abstract class DBNamedIdentifiableObject extends DBNamedObject {

    private final int id;

    protected DBNamedIdentifiableObject(long parsedName, long hashName, int id) {
        super(parsedName, hashName);

        this.id = Checks.isSchemaObjectId(id);
    }

    DBNamedIdentifiableObject(DBNamedIdentifiableObject toCopy) {
        super(toCopy);

        this.id = toCopy.id;
    }

    DBNamedIdentifiableObject(DBNamedIdentifiableObject toCopy, int newId) {
        super(toCopy);

        this.id = Checks.isSchemaObjectId(newId);
    }

    public final int getId() {
        return id;
    }

    public final boolean equals(StringResolver thisStringResolver, DBNamedIdentifiableObject other, StringResolver otherStringResolver, boolean caseSensitive) {

        Objects.requireNonNull(thisStringResolver);
        Objects.requireNonNull(other);
        Objects.requireNonNull(otherStringResolver);

        final boolean result;

        if (this == other) {

            result = true;
        }
        else if (!super.equals(thisStringResolver, other, otherStringResolver, caseSensitive)) {

            result = false;
        }
        else if (getClass() != other.getClass()) {

            result = false;
        }
        else {
            result = equalsInstanceVariables(other);
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (!super.equals(object)) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            final DBNamedIdentifiableObject other = (DBNamedIdentifiableObject)object;

            result = equalsInstanceVariables(other);
        }

        return result;
    }

    private boolean equalsInstanceVariables(DBNamedIdentifiableObject other) {

        return id == other.id;
    }

    @Override
    public void toString(StringResolver stringResolver, StringBuilder sb) {

        Objects.requireNonNull(stringResolver);
        Objects.requireNonNull(sb);

        sb.append("[super=");
        super.toString(stringResolver, sb);

        sb.append(", id=").append(id);

        sb.append(']');
    }

    @Override
    public String toString() {

        return DBNamedIdentifiableObject.class.getSimpleName() + " [super=" + super.toString() + ", id=" + id + ']';
    }
}
