package dev.jdata.db.utils.adt.lists;

public interface ILongIndexListAllocator<T extends ILongIndexList, U extends IMutableLongIndexList, V extends ILongIndexListBuilder<T, ?>>

        extends IBaseLongIndexListAllocator<T, U, V> {

    default T of(long value) {

        final T result;

        final V builder = createBuilder(1L);

        try {
            builder.addTail(value);

            result = builder.buildOrEmpty();
        }
        finally {

            freeBuilder(builder);
        }

        return result;
    }

    default T of(long ... values) {

        final T result;

        final V builder = createBuilder(values.length);

        try {
            builder.addTail(values);

            result = builder.buildOrEmpty();
        }
        finally {

            freeBuilder(builder);
        }

        return result;
    }
}
