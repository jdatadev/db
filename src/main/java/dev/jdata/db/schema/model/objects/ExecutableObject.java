package dev.jdata.db.schema.model.objects;

abstract class ExecutableObject extends SchemaObject {

    ExecutableObject(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);
    }
}
