
package dev.jdata.db.utils.function;

@FunctionalInterface
@Deprecated // IntBinaryOperator?
public interface BiIntToIntFunction {

    int apply(int parameter1, int parameter2);
}
