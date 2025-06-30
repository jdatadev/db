package dev.jdata.db.sql.ast.statements.table;

public interface SQLAlterTableOperationVisitor<T, R, E extends Exception> {

    R onAddColumn(SQLAddColumnsOperation addColumnOperation, T parameter) throws E;
    R onModifyColumns(SQLModifyColumnsOperation addColumnOperation, T parameter) throws E;
    R onDropColumn(SQLDropColumnsOperation dropColumnOperation, T parameter) throws E;

    R onAddPrimaryKeyConstraint(SQLAddPrimaryKeyConstraintOperation addPrimaryKeyConstraintOperation, T parameter) throws E;
    R onAddForeignKeyConstraint(SQLAddForeignKeyConstraintOperation addForeignKeyConstraintOperation, T parameter) throws E;
    R onAddUniqueConstraint(SQLAddUniqueConstraintOperation addUniqueConstraintOperation, T parameter) throws E;
    R onAddNullConstraint(SQLAddNullConstraintOperation addNullConstraintOperation, T parameter) throws E;
    R onAddNotNullConstraint(SQLAddNotNullConstraintOperation addNotNullConstraintOperation, T parameter) throws E;

    R onDropConstraint(SQLDropConstraintOperation dropConstraintOperation, T parameter) throws E;
}
