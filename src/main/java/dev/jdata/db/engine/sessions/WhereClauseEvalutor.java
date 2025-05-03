package dev.jdata.db.engine.sessions;

import dev.jdata.db.engine.transactions.Transaction;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.sql.ast.clauses.SQLWhereClause;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;

public class WhereClauseEvalutor {

    public static long evaluateWhereClause(SQLWhereClause whereClause, Transaction transaction, Table table, LargeLongArray rowIdsDst) {

        throw new UnsupportedOperationException();
    }
}
