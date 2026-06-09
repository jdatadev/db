package dev.jdata.db.schema.storage.sqloutputter;

import dev.jdata.db.engine.database.strings.IStringWriter;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.scalars.Integers;

abstract class ExceptionAppendableSQLOutputter<P, E extends Exception> extends BaseSQLOutputter<E> implements IResettable {

    private P parameter;
    private IExceptionAppendable<P, E> appendable;

    final void initialize(P parameter, IExceptionAppendable<P, E> appendable) {

        this.parameter = parameter;
        this.appendable = Initializable.checkNotYetInitialized(this.appendable, appendable);

        super.initialize();
    }

    @Override
    public void reset() {

        super.reset();

        this.parameter = null;
        this.appendable = Initializable.checkResettable(appendable);
    }

    @Override
    final void append(char c) throws E {

        appendable.append(c, parameter);
    }

    @Override
    final void append(int integer) throws E {

        Integers.toChars(integer, this, (c, i) -> i.appendable.append(c, i.parameter));
    }

    @Override
    final void append(String string) throws E {

        appendable.append(string, parameter);
    }

    @Override
    final void appendString(long stringRef, IStringWriter stringWriter) throws E {

        stringWriter.append(stringRef, parameter, appendable);
    }

    final P getParameter() {
        return parameter;
    }
}
