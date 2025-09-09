package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.checks.Checks;

public final class Trigger extends SchemaObject {

    Trigger(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);

        Checks.isTriggerId(id);
    }

    private Trigger(Trigger toCopy, int newSchemaObjectId) {
        super(toCopy, newSchemaObjectId);
    }

    @Override
    public SchemaObject recreateWithNewShemaObjectId(int newSchemaObjectId) {

        return new Trigger(this, newSchemaObjectId);
    }

    @Override
    public DDLObjectType getDDLObjectType() {

        return DDLObjectType.TRIGGER;
    }

    @Override
    public <P, R> R visit(SchemaObjectVisitor<P, R> visitor, P parameter) {

        return visitor.onTrigger(this, parameter);
    }
}
