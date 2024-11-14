package dev.jdata.db.utils.adt.lists;

import java.util.ArrayList;
import java.util.List;

import dev.jdata.db.utils.checks.Checks;

public final class MVCCTransactions {

    private final List<MVCCTransaction> transactions;

    MVCCTransactions() {

        this.transactions = new ArrayList<>();
    }

    void addTransaction(long transactionId) {

        Checks.isTransactionId(transactionId);

        transactions.add(new MVCCTransaction(transactionId));
    }
}
