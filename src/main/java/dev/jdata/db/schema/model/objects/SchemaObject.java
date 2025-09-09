package dev.jdata.db.schema.model.objects;

public abstract class SchemaObject extends DBNamedIdentifiableObject {

    public abstract SchemaObject recreateWithNewShemaObjectId(int newSchemaObjectId);
    public abstract DDLObjectType getDDLObjectType();
    public abstract <P, R> R visit(SchemaObjectVisitor<P, R> visitor, P parameter);

    SchemaObject(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);
    }

    SchemaObject(SchemaObject toCopy) {
        super(toCopy);
    }

    SchemaObject(SchemaObject toCopy, int newSchemaObjectId) {
        super(toCopy, newSchemaObjectId);
    }
}
