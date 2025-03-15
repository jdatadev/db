package dev.jdata.db.utils.enums;

import java.util.Objects;
import java.util.function.Function;

public interface StringEnum {

    String getString();

    public static <E extends Enum<E> & StringEnum> E findEnum(Class<E> enumValuesClass, CharSequence charSequence) {

        return findEnum(enumValuesClass.getEnumConstants(), charSequence);
    }

    public static <E extends Enum<E> & StringEnum> E findEnum(E[] enumValues, CharSequence charSequence) {

        return findEnum(enumValues, charSequence, StringEnum::getString);
    }

    public static <E extends Enum<E>> E findEnum(E[] enumValues, CharSequence charSequence, Function<E, CharSequence> charSequenceGetter) {

        Objects.requireNonNull(enumValues);
        Objects.requireNonNull(charSequence);
        Objects.requireNonNull(charSequenceGetter);

        final int charSequenceLength = charSequence.length();

        E found = null;

        for (E enumValue : enumValues) {

            final CharSequence enumValueCharSequence = charSequenceGetter.apply(enumValue);

            final int enumValueStringLength = enumValueCharSequence.length();

            if (enumValueStringLength == charSequenceLength) {

                boolean equals = true;

                for (int i = 0; i < enumValueStringLength; ++ i) {

                    if (enumValueCharSequence.charAt(i) != charSequence.charAt(i)) {

                        equals = false;
                        break;
                    }
                }

                if (equals) {

                    found = enumValue;
                    break;
                }
            }
        }

        return found;
    }
}
