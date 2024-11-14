package dev.jdata.db.storage.backend.transactionlog.backend.file;

import dev.jdata.db.utils.checks.Checks;

enum TransactionOperationCode {

    START_TRANSACTION(0x0),
    COMMIT_TRANSACTION(0x1),

    INSERT(0x2),
    UPDATE(0x3),
    DELETE(0x4);

    private final int code;

    public static TransactionOperationCode ofCode(int code) {

        Checks.isNotNegative(code);

        TransactionOperationCode found = null;

        for (TransactionOperationCode operationCode : TransactionOperationCode.values()) {

            if (code == operationCode.code) {

                found = operationCode;
                break;
            }
        }

        return found;
    }

    private TransactionOperationCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}