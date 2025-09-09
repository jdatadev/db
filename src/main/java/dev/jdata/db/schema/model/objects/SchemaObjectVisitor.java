package dev.jdata.db.schema.model.objects;

public interface SchemaObjectVisitor<P, R> {

    R onTable(Table table, P parameter);
    R onView(View view, P parameter);
    R onIndex(Index index, P parameter);
    R onTrigger(Trigger trigger, P parameter);
    R onFunction(DBFunction function, P parameter);
    R onProcedure(Procedure procedure, P parameter);
}
