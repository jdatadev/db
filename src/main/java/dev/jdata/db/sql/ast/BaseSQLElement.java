package dev.jdata.db.sql.ast;

import org.jutils.ast.objects.BaseASTElement;
import org.jutils.parse.context.Context;

public abstract class BaseSQLElement extends BaseASTElement implements SQLElement {

    protected BaseSQLElement(Context context) {
        super(context);
    }
}
