package dev.jdata.db.utils.adt.strings;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;

import dev.jdata.db.utils.checks.Checks;

public class Strings {

    public static String repeat(char c, int length) {

        Checks.isNotNegative(length);

        final char[] chars = new char[length];

        Arrays.fill(chars, c);

        return String.copyValueOf(chars);
    }

    public static <T extends CharSequence> void join(Iterable<T> toJoin, char separator, StringBuilder sb) {

        join(toJoin, separator, sb, (e, b) -> b.append(e));
    }

    public static <T> void join(Iterable<T> toJoin, char separator, StringBuilder sb, BiConsumer<T, StringBuilder> adder) {

        Objects.requireNonNull(toJoin);
        Objects.requireNonNull(sb);
        Objects.requireNonNull(adder);

        boolean first = true;

        for (T value : toJoin) {

            if (first) {

                first = false;
            }
            else {
                sb.append(separator);
            }

            adder.accept(value, sb);
        }
    }

    public static <T extends CharSequence> void join(T[] toJoin, char separator, StringBuilder sb) {

        join(toJoin, separator, sb, (e, b) -> b.append(e));
    }

    public static <T> void join(T[] toJoin, char separator, StringBuilder sb, BiConsumer<T, StringBuilder> adder) {

        Checks.isNotEmpty(toJoin);
        Objects.requireNonNull(sb);
        Objects.requireNonNull(adder);

        boolean first = true;

        for (T value : toJoin) {

            if (first) {

                first = false;
            }
            else {
                sb.append(separator);
            }

            adder.accept(value, sb);
        }
    }
}
