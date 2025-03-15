package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.checks.Checks;

public final class DatabaseSchemas {

    private int versionNumber;

    private int tableIdAllocator;

    private final IndexList<DatabaseSchema> schemas;

    public static DatabaseSchemas of(String databaseName, DatabaseSchema schema) {

        Checks.isDatabaseName(databaseName);
        Objects.requireNonNull(schema);

        return new DatabaseSchemas(databaseName, IndexList.of(schema));
    }

    DatabaseSchemas(String databaseName, IIndexList<DatabaseSchema> schemas) {

        Checks.isDatabaseName(databaseName);
        Checks.isNotEmpty(schemas);
        Checks.areElements(schemas, databaseName, (e, n) -> e.getDatabaseName().equals(n));

        if (!schemas.get(0).getVersion().isInitialVersion()) {

            throw new IllegalArgumentException();
        }

        checkVersionNumber(schemas);

        this.tableIdAllocator = findMaxTableId(schemas);

        this.schemas = new IndexList<>(DatabaseSchema[]::new, schemas);

        this.versionNumber = schemas.getTail().getVersion().getVersionNumber();
    }

    private static void checkVersionNumber(IIndexList<DatabaseSchema> schemas) {

        final long numSchemas = schemas.getNumElements();

        int previousVersionNumber = -1;

        for (int i = 0; i < numSchemas; ++ i) {

            final int versionNumber = schemas.get(i).getVersion().getVersionNumber();

            if (i > 0 && versionNumber != previousVersionNumber + 1) {

                throw new IllegalArgumentException();
            }

            previousVersionNumber = versionNumber;
        }
    }

    private static int findMaxTableId(IIndexList<DatabaseSchema> schemas) {

        int maxTableId = -1;

        final long numSchemas = schemas.getNumElements();

        for (int i = 0; i < numSchemas; ++ i) {

            final DatabaseSchema databaseSchema = schemas.get(i);

            final int schemaMaxTableId = databaseSchema.getMaxTableId();

            if (schemaMaxTableId > maxTableId) {

                maxTableId = schemaMaxTableId;
            }
        }

        return maxTableId;
    }

    public synchronized DatabaseSchema addTable(Table table) {

        Objects.requireNonNull(table);

        final DatabaseSchema newSchema = schemas.getTail().addTable(table, allocateSchemaVersion());

        schemas.add(newSchema);

        return newSchema;
    }

    public synchronized DatabaseSchema getCurrentSchema() {

        return schemas.getTail();
    }

    public synchronized int allocateTableId() {

        return tableIdAllocator ++;
    }

    private DatabaseSchemaVersion allocateSchemaVersion() {

        return DatabaseSchemaVersion.of(++ versionNumber);
    }
}
