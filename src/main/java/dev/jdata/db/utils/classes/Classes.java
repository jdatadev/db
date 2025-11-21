package dev.jdata.db.utils.classes;

import java.util.Objects;

public class Classes {

    @SuppressWarnings("unchecked")
    public static <T> Class<T> genericClass(Class<?> javaClass) {

        Objects.requireNonNull(javaClass);

        return (Class<T>)javaClass;
    }
}
