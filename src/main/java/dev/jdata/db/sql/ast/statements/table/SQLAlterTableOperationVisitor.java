package dev.jdata.db.sql.ast.statements.table;

public interface SQLAlterTableOperationVisitor<P, R, E extends Exception> {

    R onAddColumn(SQLAddColumnsOperation addColumnOperation, P parameter) throws E;
    R onModifyColumns(SQLModifyColumnsOperation addColumnOperation, P parameter) throws E;
    R onDropColumn(SQLDropColumnsOperation dropColumnOperation, P parameter) throws E;

    R onAddPrimaryKeyConstraint(SQLAddPrimaryKeyConstraintOperation addPrimaryKeyConstraintOperation, P parameter) throws E;
    R onAddForeignKeyConstraint(SQLAddForeignKeyConstraintOperation addForeignKeyConstraintOperation, P parameter) throws E;
    R onAddUniqueConstraint(SQLAddUniqueConstraintOperation addUniqueConstraintOperation, P parameter) throws E;
    R onAddNullConstraint(SQLAddNullConstraintOperation addNullConstraintOperation, P parameter) throws E;
    R onAddNotNullConstraint(SQLAddNotNullConstraintOperation addNotNullConstraintOperation, P parameter) throws E;

    R onDropConstraint(SQLDropConstraintOperation dropConstraintOperation, P parameter) throws E;
}
