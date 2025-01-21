package dev.jdata.db.utils.function;

@FunctionalInterface
public interface IntToByteFunction {

    byte apply(int value);
}
