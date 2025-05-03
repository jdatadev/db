package dev.jdata.db.test.unit;

import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.types.BigIntType;
import dev.jdata.db.schema.types.BlobType;
import dev.jdata.db.schema.types.BooleanType;
import dev.jdata.db.schema.types.CharType;
import dev.jdata.db.schema.types.DateType;
import dev.jdata.db.schema.types.DecimalType;
import dev.jdata.db.schema.types.DoubleType;
import dev.jdata.db.schema.types.FloatType;
import dev.jdata.db.schema.types.IntegerType;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.schema.types.SmallIntType;
import dev.jdata.db.schema.types.TextObjectType;
import dev.jdata.db.schema.types.TimeType;
import dev.jdata.db.schema.types.TimestampType;
import dev.jdata.db.schema.types.VarCharType;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseDBTest extends BaseTest {

    protected static final long TEST_TRANSACTION_ID = 0L;

    private static IIndexList<SchemaDataType> schemaDataTypes = IndexList.of(

            BooleanType.INSTANCE,
            SmallIntType.INSTANCE,
            IntegerType.INSTANCE,
            BigIntType.INSTANCE,
            FloatType.INSTANCE,
            DoubleType.INSTANCE,
            DecimalType.of(1, 2),
            CharType.of(3),
            VarCharType.of(4, 5),
            DateType.INSTANCE,
            TimeType.INSTANCE,
            TimestampType.INSTANCE,
            BlobType.INSTANCE,
            TextObjectType.INSTANCE);

    protected static Table createTestTable(int tableId) {

        return createTestTable(tableId, "testtable");
    }

    protected static Table createTestTable(int tableId, String tableName) {

        Checks.isTableId(tableId);
        Checks.isTableName(tableName);

        final TableBuilder tableBuilder = TableBuilder.create(tableName, tableId);

        schemaDataTypes.forEach(tableBuilder, (t, b) -> b.addColumn(t.getClass().getSimpleName().toLowerCase() + "_column", t));

        return tableBuilder.build();
    }
}
