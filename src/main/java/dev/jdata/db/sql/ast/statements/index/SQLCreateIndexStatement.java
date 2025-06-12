package dev.jdata.db.sql.ast.statements.index;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;
import dev.jdata.db.sql.ast.statements.dml.SQLObjectName;

public final class SQLCreateIndexStatement extends SQLIndexStatement {

    private final ASTSingle<SQLIndexTypeOptions> indexTypeOptions;
    private final long onKeyword;
    private final ASTSingle<SQLObjectName> objectName;
    private final ASTList<SQLIndexColumn> columns;

    public SQLCreateIndexStatement(Context context, long createKeyword, SQLIndexTypeOptions indexTypeOptions, long indexKeyword, long indexName, long onKeyword,
            SQLObjectName objectName, IIndexListGetters<SQLIndexColumn> columns) {
        super(context, createKeyword, indexKeyword, indexName);

        this.indexTypeOptions = safeMakeSingle(indexTypeOptions);
        this.onKeyword = checkIsKeyword(onKeyword);
        this.objectName = makeSingle(objectName);
        this.columns = makeList(columns);
    }

    public SQLIndexTypeOptions getIndexTypeOptions() {
        return indexTypeOptions.get();
    }

    public long getOnKeyword() {
        return onKeyword;
    }

    public SQLObjectName getObjectName() {
        return objectName.get();
    }

    public ASTList<SQLIndexColumn> getColumns() {
        return columns;
    }

    @Override
    public <P, R, E extends Exception> R visit(SQLStatementVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onCreateIndex(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(columns, recurseMode, iterator);
    }
}
