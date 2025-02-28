package dev.jdata.db.sql.ast;

public interface SQLFreeable {

    void free(SQLAllocator allocator);
}
