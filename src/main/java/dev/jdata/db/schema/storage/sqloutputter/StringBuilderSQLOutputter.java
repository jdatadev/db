package dev.jdata.db.schema.storage.sqloutputter;

import dev.jdata.db.utils.Initializable;

public final class StringBuilderSQLOutputter extends TextSQLOutputter<StringBuilder, RuntimeException> {

    private StringBuilder sb;

    public void initialize(StringBuilder sb) {

        this.sb = Initializable.checkNotYetInitialized(this.sb, sb);

        super.initialize(sb, (c, b) -> b.append(c));
    }

    @Override
    public void reset() {

        this.sb = Initializable.checkResettable(sb);

        super.reset();
    }
}
