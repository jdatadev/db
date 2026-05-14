package dev.jdata.db.engine.database.allocators;

import java.util.Objects;

import dev.jdata.db.schema.allocators.databases.schemamanagement.IDatabaseSchemaManagementAllocators;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.sets.IMutableLongLargeSet;

abstract class DatabasesAllocators<T extends IMutableLongLargeArray, U extends IMutableLongLargeSet> implements IDatabasesAllocators<T, U> {

    private final IDatabaseSchemaManagementAllocators databaseSchemaManagementAllocators;

    DatabasesAllocators(IDatabaseSchemaManagementAllocators databaseSchemaManagementAllocators) {

        this.databaseSchemaManagementAllocators = Objects.requireNonNull(databaseSchemaManagementAllocators);
    }

    @Override
    public final IDatabaseSchemaManagementAllocators getDatabaseSchemaManagementAllocators() {
        return databaseSchemaManagementAllocators;
    }
}
