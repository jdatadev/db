package dev.jdata.db.engine.database;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.jdk.adt.strings.Strings;

public class HashNameStrings {

    public static String getHashNameString(String parsedName) {

        Checks.isDBName(parsedName);

        return Strings.isAllLowerCase(parsedName) ? parsedName : parsedName.toLowerCase();
    }
}
