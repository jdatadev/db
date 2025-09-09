package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.checks.Checks;

public final class DBFunction extends ExecutableObject {

    DBFunction(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);

        Checks.isDBFunctionId(id);
    }

    private DBFunction(DBFunction toCopy, int newSchemaObjectId) {
        super(toCopy, newSchemaObjectId);
    }

    @Override
    public SchemaObject recreateWithNewShemaObjectId(int newSchemaObjectId) {

        return new DBFunction(this, newSchemaObjectId);
    }

    @Override
    public DDLObjectType getDDLObjectType() {

        return DDLObjectType.FUNCTION;
    }

    @Override
    public <P, R> R visit(SchemaObjectVisitor<P, R> visitor, P parameter) {

        return visitor.onFunction(this, parameter);
    }
}
