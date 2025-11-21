package dev.jdata.db.engine.sessions;

import java.util.Objects;

import org.jutils.io.strings.StringRef;

import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.sql.ast.statements.dml.SQLObjectName;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToIntWithRemoveStaticMap;
import dev.jdata.db.utils.adt.maps.ILongToIntMapAllocator;
import dev.jdata.db.utils.adt.maps.ILongToIntMapView;
import dev.jdata.db.utils.adt.maps.IMutableLongToIntWithRemoveStaticMap;
import dev.jdata.db.utils.checks.Checks;

abstract class ColumnsObjectAndColumnNames<T extends ColumnsObject> {

    private final IHeapMutableLongToIntWithRemoveStaticMap columnsObjectIdByName;

    private IEffectiveDatabaseSchema databaseSchema;
    private IMutableLongToIntWithRemoveStaticMap[] columnNameToIndexMapsByColumnsObjectId;
    private int maxColumnsObjectId;

    abstract DDLObjectType getDDLObjectType();

    ColumnsObjectAndColumnNames() {

        this.columnsObjectIdByName = IHeapMutableLongToIntWithRemoveStaticMap.create(0);
    }

    public final void initialize(IEffectiveDatabaseSchema databaseSchema, ILongToIntMapAllocator allocator) {

        Objects.requireNonNull(databaseSchema);
        Objects.requireNonNull(allocator);

        this.databaseSchema = databaseSchema;

        final DDLObjectType ddlObjectType = getDDLObjectType();

        final int newMaxColumnsObjectId = databaseSchema.computeMaxId(ddlObjectType, -1);

        final int arrayLength = columnNameToIndexMapsByColumnsObjectId.length;

        final int oldMaxColumnsObjectId = maxColumnsObjectId;

        for (int i = 0; i <= oldMaxColumnsObjectId; ++ i) {

            final IMutableLongToIntWithRemoveStaticMap longToIntMap = columnNameToIndexMapsByColumnsObjectId[i];

            if (longToIntMap != null) {

                allocator.freeLongToIntMap(longToIntMap);

                columnNameToIndexMapsByColumnsObjectId[i] = null;
            }
        }

        if (newMaxColumnsObjectId >= arrayLength) {

            this.columnNameToIndexMapsByColumnsObjectId = new IMutableLongToIntWithRemoveStaticMap[newMaxColumnsObjectId + 1];
        }

        columnsObjectIdByName.clear();

        for (int columnsObjectId = 0; columnsObjectId <= newMaxColumnsObjectId; ++ columnsObjectId) {

            final T columnsObject = databaseSchema.getSchemaObject(ddlObjectType, columnsObjectId);

            final long columnsObjectNameStringRef = columnsObject.getHashName();

            columnsObjectIdByName.put(columnsObjectNameStringRef, columnsObjectId);

            final int numColumns = columnsObject.getNumColumns();

            final int capacityExponent = CapacityExponents.computeIntCapacityExponent(numColumns);

            final IMutableLongToIntWithRemoveStaticMap longToIntMap = allocator.allocateLongToIntMap(capacityExponent);

            for (int columnIndex = 0; columnIndex < numColumns; ++ columnIndex) {

                final Column column = columnsObject.getColumn(columnIndex);

                final long columnNameStringRef = column.getHashName();

                longToIntMap.put(columnNameStringRef, columnIndex);
            }

            columnNameToIndexMapsByColumnsObjectId[columnsObjectId] = longToIntMap;
        }

        this.maxColumnsObjectId = newMaxColumnsObjectId;
    }

    final int getColumnsObjectId(long columnsObjectName) {

        StringRef.checkIsString(columnsObjectName);

        return columnsObjectIdByName.get(columnsObjectName);
    }

    final int getColumnsObjectId(SQLObjectName objectName) {

        Objects.requireNonNull(objectName);

        return getColumnsObjectId(objectName.getName());
    }

    final T getColumnsObject(int columnsObjectId) {

        Checks.isSchemaObjectId(columnsObjectId);

        return databaseSchema.getSchemaObject(getDDLObjectType(), columnsObjectId);
    }

    public final ILongToIntMapView getColumnIndices(int columnsObjectId) {

        checkColumnsObjectId(columnsObjectId);

        return columnNameToIndexMapsByColumnsObjectId[columnsObjectId];
    }

    public final int getColumnIndex(int columnsObjectId, long columnName) {

        checkColumnsObjectId(columnsObjectId);
        StringRef.checkIsString(columnName);

        return columnNameToIndexMapsByColumnsObjectId[columnsObjectId].get(columnName);
    }

    public final SchemaDataType getSchemaDataType(int columnsObjectId, int columnIndex) {

        checkColumnsObjectId(columnsObjectId);
        Checks.isColumnIndex(columnIndex);

        final ColumnsObject columnsObject = databaseSchema.getSchemaObject(getDDLObjectType(), columnsObjectId);

        return columnsObject.getColumn(columnIndex).getSchemaType();
    }

    private void checkColumnsObjectId(int columnsObjectId) {

        Checks.checkIndex(columnsObjectId, maxColumnsObjectId + 1);
    }
}
