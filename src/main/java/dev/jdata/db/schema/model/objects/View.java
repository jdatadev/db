package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.checks.Checks;

public final class View extends ColumnsObject {

    public View(long parsedName, long hashName, int id, IHeapIndexList<Column> columns) {
        super(parsedName, hashName, id, columns);

        Checks.isViewId(id);
    }

    private View(View toCopy, IHeapIndexList<Column> columns) {
        super(toCopy, columns);
    }

    private View(View toCopy, int newSchemaObjectId) {
        super(toCopy, newSchemaObjectId);
    }

    @Override
    public SchemaObject recreateWithNewShemaObjectId(int newSchemaObjectId) {

        return new View(this, newSchemaObjectId);
    }

    @Override
    public DDLObjectType getDDLObjectType() {

        return DDLObjectType.VIEW;
    }

    @Override
    public ColumnsObject makeCopy(IHeapIndexList<Column> columns) {

        return new View(this, columns);
    }

    @Override
    public <P, R> R visit(SchemaObjectVisitor<P, R> visitor, P parameter) {

        return visitor.onView(this, parameter);
    }
}
