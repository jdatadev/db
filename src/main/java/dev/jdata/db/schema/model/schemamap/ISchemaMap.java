package dev.jdata.db.schema.model.schemamap;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.contains.IContains;
import dev.jdata.db.utils.adt.elements.IObjectUnorderedOnlyElementsView;
import dev.jdata.db.utils.adt.lists.IIndexList;

public interface ISchemaMap<T extends SchemaObject> extends IContains, IObjectUnorderedOnlyElementsView<T> {

    boolean containsSchemaObjectName(long schemaObjectName);

    T getSchemaObjectByName(long schemaObjectName);

    T getSchemaObjectById(int id);

    IIndexList<T> getSchemaObjects();

    boolean isEqualTo(StringResolver thisStringResolver, ISchemaMap<T> other, StringResolver otherStringResolver);
}
