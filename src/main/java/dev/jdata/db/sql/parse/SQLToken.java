package dev.jdata.db.sql.parse;

import org.jutils.parse.CharType;
import org.jutils.parse.CharTypeWS;
import org.jutils.parse.IToken;
import org.jutils.parse.TokenType;

public enum SQLToken implements IToken {

    NONE(TokenType.NONE),
    EOF(TokenType.EOF),

    WS(CharTypeWS.INSTANCE),
//    C_COMMENT("/*", "*/"),
    SQL_COMMENT("--", TokenType.FROM_STRING_TO_EOL),

    NAME(NameCharType.INSTANCE),
    INTEGER_NUMBER(IntegerCharType.INSTANCE),

    STRING_LITERAL('\'', '\''),

    PLUS('+'),
    MINUS('-'),
    MULTIPLY('*'),
    DIVIDE('/'),
    MODULUS('%'),

    ASTERISK('*'),
    SLASH('/'),

    SEMI_COLON(';'),

    SMALLINT("smallint"),
    INTEGER("integer"),
    BIGINT("bigint"),
    CHAR("char"),
    VARCHAR("varchar"),
    DATE("date"),
    FLOAT("float"),
    DOUBLE("double"),
    DECIMAL("decimal"),

    CREATE("create"),
    TABLE("table"),
    DEFAULT("default"),

    ALTER("alter"),
    ADD("add"),
    CONSTRAINT("constraint"),
    MODIFY("modify"),

    INDEX("index"),

    TRIGGER("trigger"),
    OF("of"),
    REFERENCING("referencing"),
    OLD("new"),
    NEW("new"),

    WHEN("when"),

    FOR("for"),
    EACH("each"),
    ROW("row"),

    AFTER("after"),

    UNIUQE("unique"),
    DISTINCT("distinct"),
    PRIMARY("primary"),
    KEY("key"),
    FOREIGN("foreign"),

    BEFORE("before"),

    REFERENCES("references"),
    CHECK("check"),
    CASCADE("cascade"),

    DROP("drop"),

    SELECT("select"),
    FROM("from"),
    INNER("inner"),
    LEFT("left"),
    RIGHT("right"),
    FULL("full"),
    OUTER("outer"),
    JOIN("join"),
    ON("on"),
    AND("and"),
    OR("or"),
    NOT("not"),
    WHERE("where"),
    GROUP("group"),
    BY("by"),
    HAVING("having"),
    ORDER("order"),
    ASCENDING("ascending"),
    DESCENDING("descending"),
    UNION("union"),

    MIN("min"),
    MAX("max"),
    COUNT("count"),
    AVG("avg"),
    SUM("sum"),

    INSERT("insert"),
    INTO("into"),
    VALUES("values"),

    UPDATE("update"),
    SET("set"),
    DELETE("delete"),

    AS("as"),

    NULL("null"),

    LT('<'),
    LTE("<="),
    EQ('='),
    NE("<>"),
    GTE(">="),
    GT('>'),

    COMMA(','),
    PERIOD('.'),
    QUESTION_MARK('?'),

    LPAREN('('),
    RPAREN(')');

    private final TokenType tokenType;
    private final String literal;
    private final String toLiteral;
    private final char fromCharacter;
    private final char toCharacter;
    private final CharType charType;

    private SQLToken(TokenType tokenType) {
        this(tokenType, null, null, (char)0, (char)0, null);
    }

    private SQLToken(String literal) {
        this(TokenType.CI_LITERAL, literal, null, (char)0, (char)0, null);
    }

    private SQLToken(String fromLiteral, String toLiteral) {
        this(TokenType.FROM_STRING_TO_STRING, fromLiteral, toLiteral, (char)0, (char)0, null);
    }

    private SQLToken(char character) {
        this(TokenType.CHARACTER, null, null, character, (char)0, null);
    }

    private SQLToken(char fromCharacter, char toCharacter) {
        this(TokenType.FROM_CHAR_TO_CHAR, null, null, fromCharacter, toCharacter, null);
    }

    private SQLToken(CharType charType) {
        this(TokenType.CHARTYPE, null, null, (char)0, (char)0, charType);
    }

    private SQLToken(String fromLiteral, TokenType tokenType) {

        this.tokenType = tokenType;
        this.literal = fromLiteral;
        this.toLiteral = null;
        this.fromCharacter = 0;
        this.toCharacter = 0;
        this.charType = null;
    }

    private SQLToken(
            TokenType tokenType,
            String literal, String toLiteral,
            char fromCharacter, char toCharacter,
            CharType charType) {

        this.tokenType = tokenType;
        this.literal = literal;
        this.toLiteral = toLiteral;
        this.fromCharacter = fromCharacter;
        this.toCharacter = toCharacter;
        this.charType = charType;
    }

    @Override
    public TokenType getTokenType() {
        return tokenType;
    }

    @Override
    public char getCharacter() {

        return fromCharacter;
    }

    @Override
    public char getFromCharacter() {
        return fromCharacter;
    }

    @Override
    public char getToCharacter() {
        return toCharacter;
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    @Override
    public String getFromLiteral() {
        return literal;
    }

    @Override
    public String getToLiteral() {
        return toLiteral;
    }

    @Override
    public CharType getCharType() {
        return charType;
    }

    @Override
    public CustomMatcher getCustom() {

        throw new UnsupportedOperationException();
    }
}
