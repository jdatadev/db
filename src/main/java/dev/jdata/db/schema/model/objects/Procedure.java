package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.checks.Checks;

public final class Procedure extends ExecutableObject {

    Procedure(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);

        Checks.isProcedureId(id);
    }

    private Procedure(Procedure toCopy, int newSchemaObjectId) {
        super(toCopy, newSchemaObjectId);
    }

    @Override
    public SchemaObject recreateWithNewShemaObjectId(int newSchemaObjectId) {

        return new Procedure(this, newSchemaObjectId);
    }

    @Override
    public DDLObjectType getDDLObjectType() {

        return DDLObjectType.PROCEDURE;
    }

    @Override
    public <P, R> R visit(SchemaObjectVisitor<P, R> visitor, P parameter) {

        return visitor.onProcedure(this, parameter);
    }
}
