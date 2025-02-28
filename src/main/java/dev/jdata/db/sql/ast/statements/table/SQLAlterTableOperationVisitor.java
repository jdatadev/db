package dev.jdata.db.sql.ast.statements.table;

public interface SQLAlterTableOperationVisitor<T, R> {

    R onAddColumn(SQLAddColumnsOperation addColumnOperation, T parameter);
    R onModifyColumns(SQLModifyColumnsOperation addColumnOperation, T parameter);
    R onDropColumn(SQLDropColumnOperation dropColumnOperation, T parameter);

    R onAddPrimaryKeyConstraint(SQLAddPrimaryKeyConstraintOperation addPrimaryKeyConstraintOperation, T parameter);
    R onAddForeignKeyConstraint(SQLAddForeignKeyConstraintOperation addForeignKeyConstraintOperation, T parameter);
    R onAddUniqueConstraint(SQLAddUniqueConstraintOperation addUniqueConstraintOperation, T parameter);
    R onAddNullConstraint(SQLAddNullConstraintOperation addNullConstraintOperation, T parameter);
    R onAddNotNullConstraint(SQLAddNotNullConstraintOperation addNotNullConstraintOperation, T parameter);

    R onDropConstraint(SQLDropConstraintOperation dropConstraintOperation, T parameter);
}
