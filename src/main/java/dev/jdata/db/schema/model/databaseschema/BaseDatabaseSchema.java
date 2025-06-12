package dev.jdata.db.schema.model.databaseschema;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.DatabaseSchemaModelObject;
import dev.jdata.db.schema.model.IDatabaseSchema;
import dev.jdata.db.schema.model.ISchemaMap;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.BaseSchemaMaps;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseDatabaseSchema<T extends BaseSchemaMaps<?>> extends DatabaseSchemaModelObject implements IDatabaseSchema {

    private final T schemaMaps;
    private final DatabaseSchemaVersion version;

    protected BaseDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, T schemaMaps) {
        super(allocationType, databaseId);

        this.schemaMaps = Objects.requireNonNull(schemaMaps);
        this.version = Objects.requireNonNull(version);
    }

    @Override
    public final DatabaseSchemaVersion getVersion() {
        return version;
    }

    @Override
    public final boolean containsSchemaObjectName(DDLObjectType ddlObjectType, long hashObjectName) {

        Objects.requireNonNull(ddlObjectType);
        StringRef.checkIsString(hashObjectName);

        return schemaMaps.containsSchemaObjectName(ddlObjectType, hashObjectName);
    }

    @Override
    public final <R extends SchemaObject> R getSchemaObject(DDLObjectType ddlObjectType, int schemaObjectId) {

        Objects.requireNonNull(ddlObjectType);
        Checks.isSchemaObjectId(schemaObjectId);

        return schemaMaps.getSchemaObject(ddlObjectType, schemaObjectId);
    }

    @Override
    public final <R extends SchemaObject> ISchemaMap<R> getSchemaMap(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return schemaMaps.getSchemaMap(ddlObjectType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <R extends SchemaObject> IIndexList<R> getSchemaObjects(DDLObjectType ddlObjectType) {

        return (IIndexList<R>)getSchemaMap(ddlObjectType).getSchemaObjects();
    }

    public final int computeMaxId(DDLObjectType objectType, int defaultValue) {

        Objects.requireNonNull(objectType);

        return schemaMaps.computeMaxId(objectType, defaultValue);
    }

    final T getSchemaMaps() {

        return schemaMaps;
    }

    @Override
    public final boolean isEqualTo(StringResolver thisStringResolver, IDatabaseSchema other, StringResolver otherStringResolver) {

        final boolean result;

        final Object object = other;

        if (this == other) {

            result = true;
        }
        else if (!super.equals(object)) {

            result = false;
        }
        else if (getClass() != other.getClass()) {

            result = false;
        }
        else {
            final BaseDatabaseSchema<?> otherDatabaseSchema = (BaseDatabaseSchema<?>)other;

            @SuppressWarnings("unchecked")
            final BaseSchemaMaps<ISchemaMap<?>> maps = (BaseSchemaMaps<ISchemaMap<?>>)schemaMaps;

            @SuppressWarnings("unchecked")
            final BaseSchemaMaps<ISchemaMap<?>> otherMaps = (BaseSchemaMaps<ISchemaMap<?>>)otherDatabaseSchema.schemaMaps;

            result = version.equals(otherDatabaseSchema.version) && maps.isEqualTo(thisStringResolver, otherMaps, otherStringResolver);
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
            final BaseDatabaseSchema<?> other = (BaseDatabaseSchema<?>)object;

            result = version.equals(other.version) && schemaMaps.equals(other.schemaMaps);
        }

        return result;
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [version=" + version + ", schemaMaps=" + schemaMaps + ", super=" + super.toString() + "]";
    }
}
