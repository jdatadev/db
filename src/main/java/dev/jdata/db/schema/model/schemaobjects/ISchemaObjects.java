package dev.jdata.db.schema.model.schemaobjects;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.contains.IContains;
import dev.jdata.db.utils.adt.elements.IObjectUnorderedOnlyElementsView;
import dev.jdata.db.utils.adt.lists.IIndexList;

public interface ISchemaObjects<T extends SchemaObject> extends IContains, IObjectUnorderedOnlyElementsView<T> {

    boolean containsSchemaObjectName(long schemaObjectName);

    T getSchemaObjectByName(long schemaObjectName);

    T getSchemaObjectById(int id);

    IIndexList<T> getSchemaObjectsList();

    boolean isEqualTo(StringResolver thisStringResolver, ISchemaObjects<T> other, StringResolver otherStringResolver);
}
