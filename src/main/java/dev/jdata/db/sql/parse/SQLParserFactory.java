package dev.jdata.db.sql.parse;

import dev.jdata.db.sql.parse.ddl.index.SQLCreateIndexParser;
import dev.jdata.db.sql.parse.ddl.index.SQLDropIndexParser;
import dev.jdata.db.sql.parse.ddl.table.SQLAlterTableParser;
import dev.jdata.db.sql.parse.ddl.table.SQLColumnDefinitionParser;
import dev.jdata.db.sql.parse.ddl.table.SQLCreateTableParser;
import dev.jdata.db.sql.parse.ddl.table.SQLDropTableParser;
import dev.jdata.db.sql.parse.dml.delete.SQLDeleteParser;
import dev.jdata.db.sql.parse.dml.insert.SQLInsertParser;
import dev.jdata.db.sql.parse.dml.select.SQLSelectParser;
import dev.jdata.db.sql.parse.dml.update.SQLUpdateParser;
import dev.jdata.db.sql.parse.expression.SQLConditionParser;
import dev.jdata.db.sql.parse.expression.SQLExpressionParser;
import dev.jdata.db.sql.parse.expression.SQLSubSelectParser;
import dev.jdata.db.sql.parse.trigger.SQLCreateTriggerParser;
import dev.jdata.db.sql.parse.trigger.SQLDropTriggerParser;
import dev.jdata.db.sql.parse.where.SQLWhereClauseParser;

public abstract class SQLParserFactory {

    private static final SQLToken sqlStatementTokens[] = new SQLToken[] {

            SQLToken.CREATE,
            SQLToken.SELECT,
            SQLToken.INSERT,
            SQLToken.UPDATE,
            SQLToken.DELETE
    };

    private static final SQLToken sqlCreateTokens[] = new SQLToken[] {

            SQLToken.TABLE
    };

    private static final SQLToken[] SCHEMA_DATATYPE_TOKENS = new SQLToken[] {

            SQLToken.SMALLINT,
            SQLToken.INTEGER,
            SQLToken.BIGINT,
            SQLToken.FLOAT,
            SQLToken.DOUBLE,
            SQLToken.DECIMAL,
            SQLToken.CHAR,
            SQLToken.VARCHAR,
            SQLToken.DATE
    };

    private static final SQLToken[] ADD_CONSTRAINT_TOKENS = new SQLToken[] {

            SQLToken.UNIUQE,
            SQLToken.NOT,
            SQLToken.NULL,
//            SQLToken.REFERENCES,
            SQLToken.DISTINCT,
            SQLToken.PRIMARY,
            SQLToken.FOREIGN,
            SQLToken.CHECK
    };

    public abstract SQLParser createParser(SQLParserFactory parserFactory);

    public SQLExpressionParser createExpressionParser() {

        return new SQLExpressionParser();
    }

    public SQLConditionParser createConditionParser(SQLExpressionParser expressionParser) {

        return new SQLConditionParser(expressionParser);
    }

    public SQLColumnDefinitionParser createColumnDefinitionParser(SQLExpressionParser expressionParser) {

        return new SQLColumnDefinitionParser(expressionParser);
    }

    public SQLCreateTableParser createCreateTableParser(SQLColumnDefinitionParser columnDefinitionParser) {

        return new SQLCreateTableParser(columnDefinitionParser, SCHEMA_DATATYPE_TOKENS);
    }

    public SQLAlterTableParser createAlterTableParser(SQLColumnDefinitionParser columnDefinitionParser) {

        return new SQLAlterTableParser(columnDefinitionParser, SCHEMA_DATATYPE_TOKENS, ADD_CONSTRAINT_TOKENS);
    }

    public SQLDropTableParser createDropTableParser() {

        return new SQLDropTableParser();
    }

    public SQLCreateIndexParser createCreateIndexParser() {

        return new SQLCreateIndexParser();
    }

    public SQLDropIndexParser createDropIndexParser() {

        return new SQLDropIndexParser();
    }

    public SQLCreateTriggerParser createCreateTriggerParser(SQLConditionParser conditionParser, SQLInsertParser insertParser, SQLUpdateParser updateParser,
            SQLDeleteParser deleteParser) {

        return new SQLCreateTriggerParser(conditionParser, insertParser, updateParser, deleteParser);
    }

    public SQLDropTriggerParser createDropTriggerParser() {

        return new SQLDropTriggerParser();
    }

    public SQLWhereClauseParser createWhereClauseParser(SQLConditionParser conditionParser) {

        return new SQLWhereClauseParser(conditionParser);
    }

    public SQLSubSelectParser createSubSelectParser(SQLExpressionParser expressionParser, SQLConditionParser conditionParser, SQLWhereClauseParser whereClauseParser) {

        return new SQLSubSelectParser(expressionParser, conditionParser, whereClauseParser);
    }

    public SQLSelectParser createSelectParser(SQLExpressionParser expressionParser, SQLConditionParser conditionParser, SQLWhereClauseParser whereClauseParser) {

        return new SQLSelectParser(expressionParser, conditionParser, whereClauseParser);
    }

    public SQLInsertParser createInsertParser(SQLExpressionParser expressionParser) {

        return new SQLInsertParser(expressionParser);
    }

    public SQLUpdateParser createUpdateParser(SQLExpressionParser expressionParser, SQLWhereClauseParser whereClauseParser) {

        return new SQLUpdateParser(expressionParser, whereClauseParser);
    }

    public SQLDeleteParser createDeleteParser(SQLWhereClauseParser whereClauseParser) {

        return new SQLDeleteParser(whereClauseParser);
    }
}
