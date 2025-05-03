package dev.jdata.db.utils.function;

@FunctionalInterface
public interface CharMapper<P> {

    char map(char c, P parameter);
}
