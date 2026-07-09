package dev.jdata.db.ddl.helpers.sqltoschema.statements;

interface ISQLToSchemaStringMutators {

    long storeParsedStringRef(long parsedStringRef);

    long storeHashStringRefFromStoredSQLStringRef(long storedStringRef);
}
