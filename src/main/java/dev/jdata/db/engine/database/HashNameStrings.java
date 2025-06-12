package dev.jdata.db.engine.database;

import dev.jdata.db.utils.adt.strings.Strings;
import dev.jdata.db.utils.checks.Checks;

public class HashNameStrings {

    public static String getHashNameString(String parsedName) {

        Checks.isDBName(parsedName);

        return Strings.isAllLowerCase(parsedName) ? parsedName : parsedName.toLowerCase();
    }
}
