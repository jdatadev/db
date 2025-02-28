package dev.jdata.db.sql.parse;

import org.jutils.ast.objects.BaseASTElement;

abstract class BaseParser {

    protected static long checkIsKeyword(long string) {

        return BaseASTElement.checkIsKeyword(string);
    }
}
