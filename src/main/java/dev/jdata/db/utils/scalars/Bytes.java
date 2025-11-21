package dev.jdata.db.utils.scalars;

import java.util.Objects;

import dev.jdata.db.utils.jdk.adt.strings.StringBuilders;

public class Bytes {

    public static void toHexUnsigned(byte b, StringBuilder sb) {

        Objects.requireNonNull(sb);

        final int unsigned = Byte.toUnsignedInt(b);

        StringBuilders.hexString(sb, unsigned, false, 2);
    }
}
