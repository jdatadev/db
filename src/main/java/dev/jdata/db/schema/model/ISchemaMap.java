package dev.jdata.db.schema.model;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.adt.lists.IBaseIndexList;

public interface ISchemaMap<T extends SchemaObject> extends IObjectIterableElementsView<T> {

    boolean containsSchemaObjectName(long schemaObjectName);

    T getSchemaObjectByName(long schemaObjectName);

    T getSchemaObjectById(int id);

    IBaseIndexList<T> getSchemaObjects();

    boolean isEqualTo(StringResolver thisStringResolver, ISchemaMap<T> other, StringResolver otherStringResolver);
}
