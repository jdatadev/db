package dev.jdata.db.test.unit;

import java.util.List;

import dev.jdata.db.schema.Column;
import dev.jdata.db.schema.Table;
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
import dev.jdata.db.utils.adt.lists.Lists;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseDBTest extends BaseTest {

    protected static final long TEST_TRANSACTION_ID = 0L;

    private static List<SchemaDataType> createNullableSchemaDataTypes() {

        return createSchemaDataTypes(true);
    }

    private static List<SchemaDataType> createSchemaDataTypes(boolean nullable) {

        return Lists.unmodifiableOf(

                BooleanType.of(nullable),
                SmallIntType.of(nullable),
                IntegerType.of(nullable),
                BigIntType.of(nullable),
                FloatType.of(nullable),
                DoubleType.of(nullable),
                DecimalType.of(nullable, 1, 2),
                CharType.of(nullable, 3),
                VarCharType.of(nullable, 4, 5),
                DateType.of(nullable),
                TimeType.of(nullable),
                TimestampType.of(nullable),
                BlobType.of(nullable),
                TextObjectType.of(nullable));
    }

    protected static Table createTestTable(int tableId) {

        return createTestTable(tableId, "testtable");
    }

    protected static Table createTestTable(int tableId, String tableName) {

        Checks.isTableId(tableId);
        Checks.isTableName(tableName);

        final List<Column> columns = Lists.unmodifiableOf(Lists.map(createNullableSchemaDataTypes(), t -> new Column(t, t.isNullable())));

        return new Table(tableName, tableId, columns);
    }
}
