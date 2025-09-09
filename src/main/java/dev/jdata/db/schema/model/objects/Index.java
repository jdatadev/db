package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.checks.Checks;

public final class Index extends SchemaObject {

    Index(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);

        Checks.isIndexId(id);
    }

    private Index(Index toCopy, int newSchemaObjectId) {
        super(toCopy, newSchemaObjectId);
    }

    @Override
    public SchemaObject recreateWithNewShemaObjectId(int newSchemaObjectId) {

        return new Index(this, newSchemaObjectId);
    }

    @Override
    public DDLObjectType getDDLObjectType() {

        return DDLObjectType.INDEX;
    }

    @Override
    public <P, R> R visit(SchemaObjectVisitor<P, R> visitor, P parameter) {

        return visitor.onIndex(this, parameter);
    }
}
