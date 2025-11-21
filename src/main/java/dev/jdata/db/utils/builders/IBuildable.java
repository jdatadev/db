package dev.jdata.db.utils.builders;

public interface IBuildable<T> {

    T buildOrEmpty();
    T buildOrNull();
    T buildNotEmpty();
}
