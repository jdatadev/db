package dev.jdata.db.schema.model.objects;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.DBConstants;
import dev.jdata.db.schema.types.IntegerType;
import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;

public final class TableTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testNumNullableColumns() {

        int columnId = DBConstants.INITIAL_COLUMN_ID;

        final IHeapIndexList<Column> columns = IHeapIndexList.of(
                createColumn(columnId ++, true),
                createColumn(columnId ++, false),
                createColumn(columnId ++, true),
                createColumn(columnId ++, false),
                createColumn(columnId ++, true));

        final Table table = new Table(0L, 0L, DBConstants.INITIAL_SCHEMA_OBJECT_ID, columns);

        assertThat(table.getNumNullableColumns()).isEqualTo(3);
    }

    private static Column createColumn(int id, boolean nullable) {

        return new Column(0L, 0L, id, IntegerType.INSTANCE, nullable);
    }
}
