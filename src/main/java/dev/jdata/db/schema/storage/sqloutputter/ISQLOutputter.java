package dev.jdata.db.schema.storage.sqloutputter;

import dev.jdata.db.engine.database.strings.IStringWriter;
import dev.jdata.db.sql.parse.SQLToken;
import dev.jdata.db.utils.adt.IResettable;

public interface ISQLOutputter<E extends Exception> extends IResettable {

    ISQLOutputter<E> appendSeparator() throws E;

    ISQLOutputter<E> appendKeyword(SQLToken sqlToken) throws E;

    ISQLOutputter<E> appendName(long stringRef, IStringWriter stringWriter) throws E;

    ISQLOutputter<E> appendStringLiteral(long stringRef, IStringWriter stringWriter) throws E;
    ISQLOutputter<E> appendIntegerLiteral(int integer) throws E;
}
