package dev.jdata.db.schema.model.objects;

abstract class ExecutableObject extends SchemaObject {

    ExecutableObject(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);
    }

    ExecutableObject(ExecutableObject toCopy, int newSchemaObjectId) {
        super(toCopy, newSchemaObjectId);
    }
}
