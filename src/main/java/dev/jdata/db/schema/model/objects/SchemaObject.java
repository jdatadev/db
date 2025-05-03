package dev.jdata.db.schema.model.objects;

public abstract class SchemaObject extends DBNamedIdentifiableObject {

    SchemaObject(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);
    }

    SchemaObject(SchemaObject toCopy) {
        super(toCopy);
    }
}
