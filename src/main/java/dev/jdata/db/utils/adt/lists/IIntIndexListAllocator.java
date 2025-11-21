package dev.jdata.db.utils.adt.lists;

public interface IIntIndexListAllocator<T extends IIntIndexList, U extends IMutableIntIndexList, V extends IIntIndexListBuilder<T, ?>>

        extends IBaseIntIndexListAllocator<T, U, V> {

    default T of(int value) {

        final T result;

        final V builder = createBuilder(1);

        try {
            builder.addTail(value);

            result = builder.buildOrEmpty();
        }
        finally {

            freeBuilder(builder);
        }

        return result;
    }

    default T of(int ... values) {

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
