package dev.jdata.db.schema.model.databaseschema;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.BaseSchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseDatabaseSchema<T extends ISchemaMap> extends DatabaseSchemaModelRootObject implements IDatabaseSchema {

    private final T schemaMap;
    private final DatabaseSchemaVersion version;

    protected BaseDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, T schemaMap) {
        super(allocationType, databaseId);

        this.schemaMap = Objects.requireNonNull(schemaMap);
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

        return schemaMap.containsSchemaObjectName(ddlObjectType, hashObjectName);
    }

    @Override
    public final <R extends SchemaObject> R getSchemaObject(DDLObjectType ddlObjectType, int schemaObjectId) {

        Objects.requireNonNull(ddlObjectType);
        Checks.isSchemaObjectId(schemaObjectId);

        return schemaMap.getSchemaObject(ddlObjectType, schemaObjectId);
    }

    @Override
    public final <R extends SchemaObject> ISchemaObjects<R> getSchemaObjects(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return schemaMap.getSchemaObjects(ddlObjectType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <R extends SchemaObject> IIndexList<R> getSchemaObjectsList(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        final ISchemaObjects<R> schemaObjects = (ISchemaObjects<R>)getSchemaObjects(ddlObjectType);

        return schemaObjects != null ? schemaObjects.getSchemaObjectsList() : null;
    }

    @Override
    public final int computeMaxId(DDLObjectType ddlObjectType, int defaultValue) {

        Objects.requireNonNull(ddlObjectType);

        return schemaMap.computeMaxId(ddlObjectType, defaultValue);
    }

    final T getSchemaMap() {

        return schemaMap;
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
            final BaseSchemaMap<ISchemaObjects<?>> map = (BaseSchemaMap<ISchemaObjects<?>>)schemaMap;

            @SuppressWarnings("unchecked")
            final BaseSchemaMap<ISchemaObjects<?>> otherMap = (BaseSchemaMap<ISchemaObjects<?>>)otherDatabaseSchema.schemaMap;

            result = version.equals(otherDatabaseSchema.version) && map.isEqualTo(thisStringResolver, otherMap, otherStringResolver);
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

            result = version.equals(other.version) && schemaMap.equals(other.schemaMap);
        }

        return result;
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [version=" + version + ", schemaMaps=" + schemaMap + ", super=" + super.toString() + "]";
    }
}
