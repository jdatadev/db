package dev.jdata.db.schema.storage.sqloutputter;

import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;

public abstract class TextSQLOutputter<P, E extends Exception> extends ExceptionAppendableSQLOutputter<TextSQLOutputter<P, E>, E> implements IResettable {

    @FunctionalInterface
    public interface ICharOutputter<P, E extends Exception> {

        void output(char c, P parameter) throws E;
    }

    private P parameter;
    private ICharOutputter<P, E> charOutputter;

    protected final void initialize(P parameter, ICharOutputter<P, E> charOutputter) {

        this.parameter = parameter;
        this.charOutputter = Initializable.checkNotYetInitialized(this.charOutputter, charOutputter);

        final IExceptionAppendable<TextSQLOutputter<P, E>, E> appendable = (c, i) -> {

            i.charOutputter.output(c, i.parameter);
        };

        initialize(this, appendable);
    }

    @Override
    public void reset() {

        super.reset();

        this.parameter = null;
        this.charOutputter = Initializable.checkResettable(charOutputter);
    }
}
