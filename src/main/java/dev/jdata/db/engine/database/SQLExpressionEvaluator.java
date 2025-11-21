package dev.jdata.db.engine.database;

import java.nio.ByteBuffer;
import java.sql.JDBCType;
import java.util.Objects;

import org.jutils.ast.objects.expression.CustomExpression;
import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.expression.ExpressionAdapter;
import org.jutils.ast.objects.expression.ExpressionList;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.ast.objects.list.IImmutableIndexList;
import org.jutils.ast.objects.list.IListGetters;
import org.jutils.ast.operator.Arithmetic;
import org.jutils.ast.operator.Operator;

import dev.jdata.db.DBConstants;
import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.schema.DBType;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.sql.ast.expressions.BaseSQLExpression;
import dev.jdata.db.sql.ast.expressions.SQLAggregateFunctionCallExpression;
import dev.jdata.db.sql.ast.expressions.SQLAsteriskExpression;
import dev.jdata.db.sql.ast.expressions.SQLColumnExpression;
import dev.jdata.db.sql.ast.expressions.SQLDecimalLiteral;
import dev.jdata.db.sql.ast.expressions.SQLExpressionVisitor;
import dev.jdata.db.sql.ast.expressions.SQLFunctionCallExpression;
import dev.jdata.db.sql.ast.expressions.SQLIntegerLiteral;
import dev.jdata.db.sql.ast.expressions.SQLLargeIntegerLiteral;
import dev.jdata.db.sql.ast.expressions.SQLParameterExpression;
import dev.jdata.db.sql.ast.expressions.SQLStringLiteral;
import dev.jdata.db.sql.ast.expressions.SQLSubSelectExpression;
import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.numbers.decimals.IHeapMutableDecimal;
import dev.jdata.db.utils.adt.numbers.integers.IHeapMutableLargeInteger;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;

public final class SQLExpressionEvaluator extends ExpressionAdapter<SQLExpressionEvaluatorParameter, Void, EvaluateException>
        implements SQLExpressionVisitor<SQLExpressionEvaluatorParameter, Void,  EvaluateException>, IClearable {

    private static long HALF_LONG_MIN_VALUE = Long.MIN_VALUE / 2;
    private static long HALF_LONG_MAX_VALUE = Long.MAX_VALUE / 2;

    private final IHeapMutableLargeInteger largeInteger;
    private final IHeapMutableDecimal decimal;

    private DBType dbType;
    private long integer;
    private double floatingPoint;
    private long stringRef;

    private SQLExpressionEvaluatorParameter parameter;

    private IImmutableIndexList<Operator> operators;
    private int numOperators;

    SQLExpressionEvaluator() {

        this.largeInteger = IHeapMutableLargeInteger.create();
        this.decimal = IHeapMutableDecimal.ofPrecision(DBConstants.MAX_DECIMAL_PRECISION);
    }

    public void setValue(ByteBuffer byteBuffer, int byteBufferOffset, JDBCType jdbcType, int referredNumBytes) {

        Objects.requireNonNull(byteBuffer);
        Checks.isIntOffset(byteBufferOffset);
        Objects.requireNonNull(jdbcType);
        Checks.isAboveZero(referredNumBytes);

        switch (jdbcType) {

        case TINYINT:

            this.dbType = DBType.INTEGER;
            this.integer = getLongValue(byteBuffer, byteBufferOffset, Byte.BYTES, Byte.MIN_VALUE, Byte.MAX_VALUE);
            break;

        case SMALLINT:

            this.dbType = DBType.INTEGER;
            this.integer = getLongValue(byteBuffer, byteBufferOffset, Short.BYTES, Short.MIN_VALUE, Short.MAX_VALUE);
            break;

        case INTEGER:

            this.dbType = DBType.INTEGER;
            this.integer = getLongValue(byteBuffer, byteBufferOffset, Integer.BYTES, Integer.MIN_VALUE, Integer.MAX_VALUE);
            break;

        case BIGINT:

            this.dbType = DBType.INTEGER;
            this.integer = getLongValue(byteBuffer, byteBufferOffset, Long.BYTES, Long.MIN_VALUE, Long.MAX_VALUE);
            break;

        case FLOAT:

            this.dbType = DBType.FLOATING_POINT;
            this.floatingPoint = Float.intBitsToFloat((int)getLongValue(byteBuffer, byteBufferOffset, Integer.BYTES, Integer.MIN_VALUE, Integer.MAX_VALUE));
            break;

        case DOUBLE:

            this.dbType = DBType.FLOATING_POINT;
            this.floatingPoint = Double.longBitsToDouble(getLongValue(byteBuffer, byteBufferOffset, Long.BYTES, Long.MIN_VALUE, Long.MAX_VALUE));
            break;

        case CHAR:
        case NCHAR:
        case VARCHAR:
        case LONGVARCHAR:
        case NVARCHAR:
        case LONGNVARCHAR:

            this.dbType = DBType.STRING;

            final long stringRef = getLongValue(byteBuffer, byteBufferOffset, referredNumBytes, Byte.MIN_VALUE, Byte.MAX_VALUE);

            throw new UnsupportedOperationException();

        case BIT:
        case BOOLEAN:

            this.dbType = DBType.BOOLEAN;
            this.integer = getLongValue(byteBuffer, byteBufferOffset, Byte.BYTES, 0, 1);
            break;

        case BLOB:
        case CLOB:
        case NCLOB:

            throw new UnsupportedOperationException();

        default:
            throw new UnsupportedOperationException();
        }
    }

    private static long getLongValue(ByteBuffer byteBuffer, int byteOffset, int numBytes, long minValue, long maxValue) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {

        this.dbType = null;
    }

    @Deprecated // implement
    public final boolean isResolved() {

        return dbType != null;
    }

    public final void evaluate(Expression expression, SQLExpressionEvaluatorParameter parameter) throws EvaluateException {

        expression.visit(this, parameter);
    }

    public final int getNumBits(SchemaDataType schemaDataType, INumStorageBitsGetter numStorageBitsGetter) {

        return numStorageBitsGetter.getMaxNumBits(schemaDataType);
    }

    public final void store(byte[] byteArray, long bitOffset, int numBits) throws OverflowException {

        switch (dbType) {

        case INTEGER:

            final int highestBit = BitsUtil.getIndexOfHighestSetBit(integer);

            if (highestBit >= numBits) {

                throw new OverflowException();
            }

            BitBufferUtil.setLongValue(byteArray, integer, true, bitOffset, numBits);
            break;

        default:
            throw new UnsupportedOperationException();
        }
    }

    private void plus(SQLExpressionEvaluator augend) throws OverflowException {

        switch (dbType) {

        case INTEGER:

            switch (augend.dbType) {

            case INTEGER:

                final long thisInteger = integer;
                final long augendInteger = augend.integer;

                if (    (thisInteger >= 0 && augendInteger >= 0 && thisInteger <= HALF_LONG_MAX_VALUE && augendInteger <= HALF_LONG_MAX_VALUE)
                     || (thisInteger <= 0 && augendInteger >= 0)
                     || (thisInteger >= 0 && augendInteger <= 0)
                     || (thisInteger <= 0 && augendInteger <= 0 && thisInteger >= HALF_LONG_MIN_VALUE && augendInteger >= HALF_LONG_MIN_VALUE)) {

                    this.integer = thisInteger + augendInteger;
                }
                else {
                    largeInteger.setValue(thisInteger);
                    largeInteger.add(augendInteger);

                    setFromIntegerToLargeIntegerIfOverflow();
                }
                break;

            case LARGE_INTEGER:

                largeInteger.setValue(integer);
                largeInteger.add(augend.largeInteger);

                setFromIntegerToLargeIntegerIfOverflow();
                break;

            case FLOATING_POINT:

                decimal.setValue(integer);
                decimal.add(augend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(integer);
                decimal.add(augend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case LARGE_INTEGER:

            switch (augend.dbType) {

            case INTEGER:

                largeInteger.add(augend.integer);

                setFromLargeIntegerToIntegerIfNotOverflow();
                break;

            case LARGE_INTEGER:

                largeInteger.add(augend.largeInteger);

                setFromLargeIntegerToIntegerIfNotOverflow();
                break;

            case FLOATING_POINT:

                decimal.setValue(largeInteger);
                decimal.add(augend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(largeInteger);
                decimal.add(augend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case FLOATING_POINT:

            switch (augend.dbType) {

            case INTEGER:

                decimal.setValue(floatingPoint);
                decimal.add(augend.integer);

                truncateDecimal();
                break;

            case LARGE_INTEGER:

                decimal.setValue(floatingPoint);
                decimal.add(augend.largeInteger);

                truncateDecimal();
                break;

            case FLOATING_POINT:

                decimal.setValue(floatingPoint);
                decimal.add(augend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(floatingPoint);
                decimal.add(augend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case DECIMAL:

            switch (augend.dbType) {

            case INTEGER:

                decimal.add(augend.integer);

                truncateDecimal();
                break;

            case LARGE_INTEGER:

                decimal.add(augend.largeInteger);

                truncateDecimal();
                break;

            case FLOATING_POINT:

                decimal.add(augend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.add(augend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        default:
            throw new UnsupportedOperationException();
        }
    }

    private void minus(SQLExpressionEvaluator subtrahend) throws OverflowException {

        switch (dbType) {

        case INTEGER:

            switch (subtrahend.dbType) {

            case INTEGER:

                final long thisInteger = integer;
                final long augendInteger = subtrahend.integer;

                if (    (thisInteger >= 0 && augendInteger >= 0)
                     || (thisInteger <= 0 && augendInteger <= 0)
                     || (thisInteger >= 0 && augendInteger <= 0 && thisInteger <= HALF_LONG_MAX_VALUE && augendInteger >= HALF_LONG_MIN_VALUE)
                     || (thisInteger <= 0 && augendInteger >= 0 && thisInteger >= HALF_LONG_MIN_VALUE && augendInteger <= HALF_LONG_MAX_VALUE)) {

                    this.integer = thisInteger - augendInteger;
                }
                else {
                    largeInteger.setValue(thisInteger);
                    largeInteger.subtract(augendInteger);

                    setFromIntegerToLargeIntegerIfOverflow();
                }
                break;

            case LARGE_INTEGER:

                largeInteger.setValue(integer);
                largeInteger.subtract(subtrahend.largeInteger);

                setFromIntegerToLargeIntegerIfOverflow();
                break;

            case FLOATING_POINT:

                decimal.setValue(integer);
                decimal.subtract(subtrahend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(integer);
                decimal.subtract(subtrahend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case LARGE_INTEGER:

            switch (subtrahend.dbType) {

            case INTEGER:

                largeInteger.subtract(subtrahend.integer);

                setFromLargeIntegerToIntegerIfNotOverflow();
                break;

            case LARGE_INTEGER:

                largeInteger.subtract(subtrahend.largeInteger);

                setFromLargeIntegerToIntegerIfNotOverflow();
                break;

            case FLOATING_POINT:

                decimal.setValue(largeInteger);
                decimal.subtract(subtrahend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(largeInteger);
                decimal.subtract(subtrahend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case FLOATING_POINT:

            switch (subtrahend.dbType) {

            case INTEGER:

                decimal.setValue(floatingPoint);
                decimal.subtract(subtrahend.integer);

                truncateDecimal();
                break;

            case LARGE_INTEGER:

                decimal.setValue(floatingPoint);
                decimal.subtract(subtrahend.largeInteger);

                truncateDecimal();
                break;

            case FLOATING_POINT:

                decimal.setValue(floatingPoint);
                decimal.subtract(subtrahend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(floatingPoint);
                decimal.subtract(subtrahend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case DECIMAL:

            switch (subtrahend.dbType) {

            case INTEGER:

                decimal.subtract(subtrahend.integer);

                truncateDecimal();
                break;

            case LARGE_INTEGER:

                decimal.subtract(subtrahend.largeInteger);

                truncateDecimal();
                break;

            case FLOATING_POINT:

                decimal.subtract(subtrahend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.subtract(subtrahend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        default:
            throw new UnsupportedOperationException();
        }
    }

    private void multiply(SQLExpressionEvaluator multiplicand) {

        switch (dbType) {

        case INTEGER:

            switch (multiplicand.dbType) {

            case INTEGER:

                final long thisInteger = integer;
                final long augendInteger = multiplicand.integer;

                if (Math.abs(thisInteger) <= 1000 * 1000 && Math.abs(augendInteger) <= 1000 * 1000) {

                    this.integer = thisInteger * augendInteger;
                }
                else {
                    largeInteger.setValue(thisInteger);
                    largeInteger.multiply(augendInteger);

                    setFromIntegerToLargeIntegerIfOverflow();
                }
                break;

            case LARGE_INTEGER:

                largeInteger.setValue(integer);
                largeInteger.multiply(multiplicand.largeInteger);

                setFromIntegerToLargeIntegerIfOverflow();
                break;

            case FLOATING_POINT:

                decimal.setValue(integer);
                decimal.multiply(multiplicand.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(integer);
                decimal.multiply(multiplicand.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case LARGE_INTEGER:

            switch (multiplicand.dbType) {

            case INTEGER:

                largeInteger.multiply(multiplicand.integer);

                setFromLargeIntegerToIntegerIfNotOverflow();
                break;

            case LARGE_INTEGER:

                largeInteger.multiply(multiplicand.largeInteger);

                setFromLargeIntegerToIntegerIfNotOverflow();
                break;

            case FLOATING_POINT:

                decimal.setValue(largeInteger);
                decimal.multiply(multiplicand.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(largeInteger);
                decimal.multiply(multiplicand.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case FLOATING_POINT:

            switch (multiplicand.dbType) {

            case INTEGER:

                decimal.setValue(floatingPoint);
                decimal.multiply(multiplicand.integer);

                truncateDecimal();
                break;

            case LARGE_INTEGER:

                decimal.setValue(floatingPoint);
                decimal.multiply(multiplicand.largeInteger);

                truncateDecimal();
                break;

            case FLOATING_POINT:

                decimal.setValue(floatingPoint);
                decimal.multiply(multiplicand.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(floatingPoint);
                decimal.multiply(multiplicand.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case DECIMAL:

            switch (multiplicand.dbType) {

            case INTEGER:

                decimal.multiply(multiplicand.integer);

                truncateDecimal();
                break;

            case LARGE_INTEGER:

                decimal.multiply(multiplicand.largeInteger);

                truncateDecimal();
                break;

            case FLOATING_POINT:

                decimal.multiply(multiplicand.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.multiply(multiplicand.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        default:
            throw new UnsupportedOperationException();
        }
    }

    private void divide(SQLExpressionEvaluator dividend) {

        switch (dbType) {

        case INTEGER:

            switch (dividend.dbType) {

            case INTEGER:

                final long thisInteger = integer;
                final long augendInteger = dividend.integer;

                if (thisInteger % augendInteger == 0L) {

                    this.integer = thisInteger / augendInteger;
                }
                else {
                    decimal.setValue(thisInteger);
                    decimal.divide(augendInteger);

                    truncateDecimal();
                }
                break;

            case LARGE_INTEGER:

                largeInteger.setValue(integer);

                if (largeInteger.divideIfModulusZero(dividend.largeInteger)) {

                    setFromIntegerToLargeIntegerIfOverflow();
                }
                else {
                    decimal.setValue(integer);
                    decimal.divide(dividend.largeInteger);

                    truncateDecimal();
                }
                break;

            case FLOATING_POINT:

                decimal.setValue(integer);
                decimal.divide(dividend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(integer);
                decimal.divide(dividend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case LARGE_INTEGER:

            switch (dividend.dbType) {

            case INTEGER:

                if (largeInteger.divideIfModulusZero(dividend.integer)) {

                    setFromLargeIntegerToIntegerIfNotOverflow();
                }
                else {
                    decimal.setValue(largeInteger);
                    decimal.divide(dividend.integer);

                    truncateDecimal();
                }
                break;

            case LARGE_INTEGER:

                decimal.setValue(largeInteger);
                decimal.divide(dividend.largeInteger);

                truncateDecimal();
                break;

            case FLOATING_POINT:

                decimal.setValue(largeInteger);
                decimal.divide(dividend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(largeInteger);
                decimal.divide(dividend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case FLOATING_POINT:

            switch (dividend.dbType) {

            case INTEGER:

                decimal.setValue(floatingPoint);
                decimal.divide(dividend.integer);

                truncateDecimal();
                break;

            case LARGE_INTEGER:

                decimal.setValue(floatingPoint);
                decimal.divide(dividend.largeInteger);

                truncateDecimal();
                break;

            case FLOATING_POINT:

                decimal.setValue(floatingPoint);
                decimal.divide(dividend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(floatingPoint);
                decimal.divide(dividend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case DECIMAL:

            switch (dividend.dbType) {

            case INTEGER:

                decimal.divide(dividend.integer);

                truncateDecimal();
                break;

            case LARGE_INTEGER:

                decimal.divide(dividend.largeInteger);

                truncateDecimal();
                break;

            case FLOATING_POINT:

                decimal.divide(dividend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.divide(dividend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        default:
            throw new UnsupportedOperationException();
        }
    }

    private void modulus(SQLExpressionEvaluator dividend) {

        switch (dbType) {

        case INTEGER:

            switch (dividend.dbType) {

            case INTEGER:

                this.integer = this.integer % dividend.integer;
                break;

            case LARGE_INTEGER:

                largeInteger.setValue(integer);
                largeInteger.modulus(dividend.largeInteger);

                setFromIntegerToLargeIntegerIfOverflow();
                break;

            case FLOATING_POINT:

                decimal.setValue(integer);
                decimal.modulus(dividend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(integer);
                decimal.modulus(dividend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case LARGE_INTEGER:

            switch (dividend.dbType) {

            case INTEGER:

                largeInteger.modulus(dividend.integer);

                setFromLargeIntegerToIntegerIfNotOverflow();
                break;

            case LARGE_INTEGER:

                largeInteger.modulus(dividend.largeInteger);

                truncateDecimal();
                break;

            case FLOATING_POINT:

                largeInteger.modulus(dividend.floatingPoint);

                setFromLargeIntegerToIntegerIfNotOverflow();
                break;

            case DECIMAL:

                largeInteger.modulus(dividend.decimal);

                setFromLargeIntegerToIntegerIfNotOverflow();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case FLOATING_POINT:

            switch (dividend.dbType) {

            case INTEGER:

                decimal.setValue(floatingPoint);
                decimal.modulus(dividend.integer);

                truncateDecimal();
                break;

            case LARGE_INTEGER:

                decimal.setValue(floatingPoint);
                decimal.modulus(dividend.largeInteger);

                truncateDecimal();
                break;

            case FLOATING_POINT:

                decimal.setValue(floatingPoint);
                decimal.modulus(dividend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.setValue(floatingPoint);
                decimal.modulus(dividend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        case DECIMAL:

            switch (dividend.dbType) {

            case INTEGER:

                decimal.modulus(dividend.integer);

                truncateDecimal();
                break;

            case LARGE_INTEGER:

                decimal.modulus(dividend.largeInteger);

                truncateDecimal();
                break;

            case FLOATING_POINT:

                decimal.modulus(dividend.floatingPoint);

                truncateDecimal();
                break;

            case DECIMAL:

                decimal.modulus(dividend.decimal);

                truncateDecimal();
                break;

            default:
                throw new UnsupportedOperationException();
            }
            break;

        default:
            throw new UnsupportedOperationException();
        }
    }

    private void setFromIntegerToLargeIntegerIfOverflow() {

        if (largeInteger.isLessThan(Long.MIN_VALUE) || largeInteger.isGreaterThan(Long.MAX_VALUE)) {

            this.dbType = DBType.LARGE_INTEGER;
        }
        else {
            this.integer = largeInteger.longValueExact();
        }
    }

    private void setFromLargeIntegerToIntegerIfNotOverflow() {

        if (largeInteger.isGreaterThanOrEqualTo(Long.MIN_VALUE) || largeInteger.isLessThanOrEqualTo(Long.MAX_VALUE)) {

            this.integer = largeInteger.longValueExact();
            this.dbType = DBType.INTEGER;
        }
    }

    private void truncateDecimal() {

        throw new UnsupportedOperationException();
    }

    @Override
    public Void onExpressionList(ExpressionList expression, SQLExpressionEvaluatorParameter parameter) throws EvaluateException {

        final ASTList<Expression> expressions = expression.getExpressions();

        final int numExpressions = expressions.size();

        switch (numExpressions) {

        case 0:
            throw new IllegalArgumentException();

        case 1:

            evaluate(expressions.getTail(), parameter);
            break;

        default:

            final IListGetters<Operator> operators = expression.getOperators();

            final int numOperators = IOnlyElementsView.intNumElementsRenamed(operators.getNumElements());

            if (numExpressions - 1 != numOperators) {

                throw new IllegalArgumentException();
            }

            this.parameter = parameter;
            this.operators = operators.toImmutableIndexList();
            this.numOperators = numOperators;

            expressions.forEachWithIndexAndParameter(this, (e, i, p) -> {

                final SQLExpressionEvaluatorParameter param = p.parameter;

                if (i == 0) {

                    p.evaluate(e, param);
                }
                else {
                    final org.jutils.ast.objects.list.IIndexListGetters<Operator> ops = p.operators;
                    final int numOps = p.numOperators;

                    final SQLExpressionEvaluator subExpressionEvaluator = param.allocateEvaluator();

                    try {
                        subExpressionEvaluator.evaluate(e, param);

                        if (i < numOps) {

                            final Arithmetic arithmetic = (Arithmetic)ops.get(i);

                            switch (arithmetic) {

                            case PLUS:

                                plus(subExpressionEvaluator);
                                break;

                            case MINUS:

                                minus(subExpressionEvaluator);
                                break;

                            case MULTIPLY:

                                multiply(subExpressionEvaluator);
                                break;

                            case DIVIDE:

                                divide(subExpressionEvaluator);
                                break;

                            case MODULUS:

                                modulus(subExpressionEvaluator);
                                break;

                            default:
                                throw new UnsupportedOperationException();
                            }
                        }
                    }
                    finally {

                        param.freeEvaluator(subExpressionEvaluator);
                    }
                }
            });
            break;
        }

        return null;
    }

    @Override
    public Void onCustomExpression(CustomExpression expression, SQLExpressionEvaluatorParameter parameter) throws EvaluateException {

        final BaseSQLExpression sqlExpression = (BaseSQLExpression)expression;

        sqlExpression.visitSQLExpression(this, parameter);

        return null;
    }

    @Override
    public Void onIntegerLiteral(SQLIntegerLiteral integerLiteral, SQLExpressionEvaluatorParameter parameter) {

        this.dbType = DBType.INTEGER;
        this.integer = integerLiteral.getInteger();

        return null;
    }

    @Override
    public Void onLargeIntegerLiteral(SQLLargeIntegerLiteral largeIntegerLiteral, SQLExpressionEvaluatorParameter parameter) {

        this.dbType = DBType.LARGE_INTEGER;
        largeInteger.setValue(largeIntegerLiteral.getLargeInteger());

        return null;
    }

    @Override
    public Void onDecimalLiteral(SQLDecimalLiteral decimalLiteral, SQLExpressionEvaluatorParameter parameter) {

        this.dbType = DBType.DECIMAL;
        decimal.setValue(decimalLiteral.getDecimal());

        return null;
    }

    @Override
    public Void onStringLiteral(SQLStringLiteral stringLiteral, SQLExpressionEvaluatorParameter parameter) {

        this.dbType = DBType.STRING;
        this.stringRef = stringLiteral.getString();

        return null;
    }

    @Override
    public Void onColumn(SQLColumnExpression columnExpression, SQLExpressionEvaluatorParameter parameter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Void onParameter(SQLParameterExpression parameterExpression, SQLExpressionEvaluatorParameter parameter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Void onFunctionCall(SQLFunctionCallExpression functionCallExpression, SQLExpressionEvaluatorParameter parameter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Void onAggregateFunctionCall(SQLAggregateFunctionCallExpression aggregateFunctionCallExpression, SQLExpressionEvaluatorParameter parameter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Void onAsterisk(SQLAsteriskExpression asteriskExpression, SQLExpressionEvaluatorParameter parameter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Void onSubSelect(SQLSubSelectExpression subSelectExpression, SQLExpressionEvaluatorParameter parameter) {

        throw new UnsupportedOperationException();
    }
}
