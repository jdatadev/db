package dev.jdata.db.engine.database;

import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLDMLUpdatingStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLSelectStatement;

public interface IDatabaseExecuteOperations {

    public interface DataWriter<E extends Exception> {

        void writeByte(byte b) throws E;
        void writeShort(short s) throws E;
        void writeInt(int i) throws E;
        void writeLong(long l) throws E;

        void writeString(CharSequence charSequence) throws E;
    }

    public interface SelectResultWriter<E extends Exception> extends DataWriter<E>  {

        void startRow() throws E;

        void endRow() throws E;
    }

    void executeDDLSQL(int sessionId, BaseSQLDDLOperationStatement sqlStatement) throws DDLException;

    <E extends Exception> void executeDMLSelectSQL(int sessionId, SQLSelectStatement sqlStatement, SelectResultWriter<E> selectResultWriter) throws DMLException, E;
    long executeDMLUpdateSQL(int sessionId, SQLDMLUpdatingStatement sqlStatement) throws DMLException;
}
