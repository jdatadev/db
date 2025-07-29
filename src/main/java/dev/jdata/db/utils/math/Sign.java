package dev.jdata.db.utils.math;

import dev.jdata.db.utils.checks.Checks;

public enum Sign {

    PLUS() {

        @Override
        public int apply(int unsignedInt) {

            return Checks.isNotNegative(unsignedInt);
        }

        @Override
        public long apply(long unsignedLong) {

            return Checks.isNotNegative(unsignedLong);
        }
    },
    MINUS() {

        @Override
        public int apply(int unsignedInt) {

            return - Checks.isNotNegative(unsignedInt);
        }

        @Override
        public long apply(long unsignedLong) {

            return - Checks.isNotNegative(unsignedLong);
        }
    };

    public abstract int apply(int unsignedInt);
    public abstract long apply(long unsignedLong);
}
