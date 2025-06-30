package dev.jdata.db.sql.ast.statements.table;

import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

public final class SQLAddForeignKeyConstraintOperation extends SQLAlterTableAddConstraintMultiColumnOperation {

    private final long foreignKeyword;
    private final long keyKeyword;

    private final long referencesKeyword;
    private final long referencesTableName;
    private final ASTSingle<SQLColumnNames> referencesColumnNames;

    private final ASTSingle<SQLOnDeleteCascade> onDeleteCascade;

    public SQLAddForeignKeyConstraintOperation(Context context, long addKeyword, long constraintKeyword, long foreignKeyword, long keyKeyword, SQLColumnNames columnNames,
            long referencesKeyword, long referencesTableName, SQLColumnNames referencesColumnNames, SQLOnDeleteCascade onDeleteCascade, long name) {
        super(context, addKeyword, constraintKeyword, columnNames, name);

        this.foreignKeyword = checkIsKeyword(foreignKeyword);
        this.keyKeyword = checkIsKeyword(keyKeyword);

        this.referencesKeyword = checkIsKeyword(referencesKeyword);
        this.referencesTableName = checkIsString(referencesTableName);
        this.referencesColumnNames = makeSingle(referencesColumnNames);
        this.onDeleteCascade = safeMakeSingle(onDeleteCascade);
    }

    public long getForeignKeyword() {
        return foreignKeyword;
    }

    public long getKeyKeyword() {
        return keyKeyword;
    }

    public long getReferencesKeyword() {
        return referencesKeyword;
    }

    public long getReferencesTableName() {
        return referencesTableName;
    }

    public SQLColumnNames getReferencesColumnNames() {
        return referencesColumnNames.get();
    }

    public SQLOnDeleteCascade getOnDeleteCascade() {

        return safeGet(onDeleteCascade);
    }

    @Override
    public <T, R, E extends Exception> R visit(SQLAlterTableOperationVisitor<T, R, E> visitor, T parameter) throws E{

        return visitor.onAddForeignKeyConstraint(this, parameter);
    }
}
