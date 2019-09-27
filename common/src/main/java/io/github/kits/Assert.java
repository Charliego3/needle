package io.github.kits;

import io.github.kits.exception.AssertException;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 断言工具类, 不符合条件时抛出异常
 * Assertion tool class, throwing an exception if the condition is not met
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Assert {

	/**
	 * 当且仅当{@code !var1.equals(var2)}时抛出异常
	 * Throws an exception if and only if {@code !var1.equals(var2)}
	 *
	 * @param var1 value1
	 * @param var2 value2
	 * @param <T>  泛型约束
	 *             Generic
	 */
	public static <T> void equal(T var1, T var2, String exMessage) {
		equal(var1, var2, new IllegalArgumentException(exMessage));
	}

	/**
	 * 当且仅当{@code !var1.equals(var2)}时抛出异常
	 * Throws an exception if and only if {@code !var1.equals(var2)}
	 *
	 * @param var1 value1
	 * @param var2 value2
	 * @param <T>  泛型约束
	 *             Generic
	 */
	public static <T> void equal(T var1, T var2, Exception ex) {
		predicate(() -> !Objects.equals(var1, var2), ex);
	}

	/**
	 * 当且仅当{@code var1.equals(var2)}时抛出异常
	 * Throws an exception if and only if {@code var1.equals(var2)}
	 *
	 * @param var1 value1
	 * @param var2 value2
	 * @param <T>  泛型约束
	 *             Generic
	 */
	public static <T> void notEqual(T var1, T var2, String exMessage) {
		notEqual(var1, var2, new IllegalArgumentException(exMessage));
	}

	/**
	 * 当且仅当{@code var1.equals(var2)}时抛出异常
	 * Throws an exception if and only if {@code var1.equals(var2)}
	 *
	 * @param var1 value1
	 * @param var2 value2
	 * @param <T>  泛型约束
	 *             Generic
	 */
	public static <T> void notEqual(T var1, T var2, Exception ex) {
		predicate(() -> Objects.equals(var1, var2), ex);
	}

	/**
	 * 当且仅当{@code var != null}时抛出异常
	 * Throws an exception if and only if {@code var != null}
	 *
	 * @param var       需要判断的值
	 *                  Value to be judged
	 * @param exMessage 不符合条件的错误信息
	 *                  Unqualified error message
	 */
	public static <T> void isNull(T var, String exMessage) {
		isNull(var, new NullPointerException(exMessage));
	}

	/**
	 * 当且仅当{@code var != null}时抛出异常
	 * Throws an exception if and only if {@code var != null}
	 *
	 * @param var 需要判断的值
	 *            Value to be judged
	 * @param ex  不符合条件时抛出的异常
	 *            Exception thrown when the condition is not met
	 */
	public static <T> void isNull(T var, Exception ex) {
		predicate(() -> Objects.nonNull(var), ex);
	}

	/**
	 * 当且仅当{@code var != null || var.equals("")}时抛出异常
	 * Throws an exception if and only if {@code var != null || var.equals("")}
	 *
	 * @param var       需要判断的值
	 *                  Value to be judged
	 * @param exMessage 不符合条件的错误信息
	 *                  Unqualified error message
	 * @param <T>       泛型约束
	 *                  Generic
	 */
	public static <T> void isNullOrEmpty(T var, String exMessage) {
		isNullOrEmpty(var, new NullPointerException(exMessage));
	}

	/**
	 * 当且仅当{@code var != null || var.equals("")}时抛出异常
	 * Throws an exception if and only if {@code var != null || var.equals("")}
	 *
	 * @param var 需要判断的值
	 *            Value to be judged
	 * @param ex  不符合条件时抛出的异常
	 *            Exception thrown when the condition is not met
	 * @param <T> 泛型约束
	 *            Generic constraint
	 */
	public static <T> void isNullOrEmpty(T var, Exception ex) {
		predicate(() -> Envs.notHasNullOrEmpty(var), ex);
	}

	/**
	 * 当且仅当{@code var != null || var.equals("") || var为空字符串}时抛出异常
	 * Throws an exception if and only if {@code var != null || var.equals("")}
	 *
	 * @param var 需要判断的值
	 *            Value to be judged
	 * @param message  不符合条件时抛出的异常
	 *            Exception thrown when the condition is not met
	 * @param <T> 泛型约束
	 *            Generic constraint
	 */
	public static <T> void isNullEmptyBlack(T var, String message) {
		predicate(() -> Envs.isNotNullEmptyBlack(var), new NullPointerException(message));
	}

	/**
	 * 当且仅当{@code var != null || var.equals("")}时抛出异常
	 * Throws an exception if and only if {@code var != null || var.equals("")}
	 *
	 * @param var 需要判断的值
	 *            Value to be judged
	 * @param message  不符合条件时抛出的异常
	 *            Exception thrown when the condition is not met
	 * @param <T> 泛型约束
	 *            Generic constraint
	 */
	public static <T> void isNotNullEmptyBlack(T var, String message) {
		predicate(() -> Envs.isNullEmptyBlack(var), new NullPointerException(message));
	}

	/**
	 * 当且仅当{@code var != null || var.equals("")}时抛出异常
	 * Throws an exception if and only if {@code var != null || var.equals("")}
	 *
	 * @param var 需要判断的值
	 *            Value to be judged
	 * @param exception  不符合条件时抛出的异常
	 *            Exception thrown when the condition is not met
	 * @param <T> 泛型约束
	 *            Generic constraint
	 */
	public static <T> void isNotNullEmptyBlack(T var, Exception exception) {
		predicate(() -> Envs.isNullEmptyBlack(var), exception);
	}

	/**
	 * 当且仅当{@code var == null}时抛出异常
	 * Throws an exception if and only if {@code var == null}
	 *
	 * @param var       需要判断的值
	 *                  Value to be judged
	 * @param exMessage 不符合条件的错误信息
	 *                  Unqualified error message
	 */
	public static <T> void isNotNull(T var, String exMessage) {
		isNotNull(var, new NullPointerException(exMessage));
	}

	/**
	 * 当且仅当{@code var == null}时抛出异常
	 * Throws an exception if and only if {@code var == null}
	 *
	 * <pre>
	 * 		Usage: Assert.isNotNull("this is value", new CustomerException("this is exMessage"));
	 * </pre>
	 *
	 * @param var 需要判断的值
	 *            Value to be judged
	 * @param ex  不符合条件时抛出的异常
	 *            Exception thrown when the condition is not met
	 * @param <T> 泛型约束
	 *            Generic constraint
	 */
	public static <T> void isNotNull(T var, Exception ex) {
		predicate(() -> Objects.isNull(var), ex);
	}

	/**
	 * 当且仅当{@code var == null || !var.equals("")}时抛出异常
	 * Throws an exception if and only if {@code var == null || !var.equals("")}
	 *
	 * @param var       需要判断的值
	 *                  Value to be judged
	 * @param exMessage 不符合条件的错误信息
	 *                  Unqualified error message
	 * @param <T>       泛型约束
	 *                  Generic constraint
	 */
	public static <T> void isNotNullOrEmpty(T var, String exMessage) {
		isNotNullOrEmpty(var, new NullPointerException(exMessage));
	}

	/**
	 * 当且仅当{@code var == null || !var.equals("")}时抛出异常
	 * Throws an exception if and only if {@code var == null || !var.equals("")}
	 *
	 * @param var 需要判断的值
	 *            Value to be judged
	 * @param ex  不符合条件时抛出的异常
	 *            Exception thrown when the condition is not met
	 * @param <T> 泛型约束
	 *            Generic constraint
	 */
	public static <T> void isNotNullOrEmpty(T var, Exception ex) {
		predicate(() -> Envs.hasNullOrEmpty(var), ex);
	}

	/**
	 * 当且仅当{@code predicate == false}时抛出异常
	 *
	 * @param predicate 断言
	 * @param ex        异常
	 * @param <T>       范型约束
	 */
	public static <T> void isTrue(boolean predicate, Exception ex) {
		predicate(() -> !predicate, ex);
	}

	/**
	 * 当且仅当{@code predicate == false}时抛出异常
	 *
	 * @param predicate 断言
	 * @param message   异常信息
	 * @param <T>       范型约束
	 */
	public static <T> void isTrue(boolean predicate, String message) {
		predicate(() -> !predicate, new AssertException(message));
	}

	/**
	 * ==========================================================================================================
	 * ============================================= lessThan0(< 0) =============================================
	 * ==========================================================================================================
	 * <p>
	 * 当且仅当{@code n1 >= 0}时抛出异常
	 * Throws an exception if and only if {@code n1 >= 0}
	 */
	public static void lessThan0(short number, Exception ex) {
		lessThan(number, 0, ex);
	}

	public static void lessThan0(short number, String exMessage) {
		lessThan0(number, new IllegalArgumentException(exMessage));
	}

	public static void lessThan0(int number, Exception ex) {
		lessThan(number, 0, ex);
	}

	public static void lessThan0(int number, String exMessage) {
		lessThan0(number, new IllegalArgumentException(exMessage));
	}

	public static void lessThan0(long number, Exception ex) {
		lessThan(number, 0, ex);
	}

	public static void lessThan0(long number, String exMessage) {
		lessThan0(number, new IllegalArgumentException(exMessage));
	}

	public static void lessThan0(float number, Exception ex) {
		lessThan(number, 0, ex);
	}

	public static void lessThan0(float number, String exMessage) {
		lessThan0(number, new IllegalArgumentException(exMessage));
	}

	public static void lessThan0(double number, Exception ex) {
		lessThan(number, 0, ex);
	}

	public static void lessThan0(double number, String exMessage) {
		lessThan0(number, new IllegalArgumentException(exMessage));
	}

	/**
	 * ==========================================================================================================
	 * ============================================== lessThan (<) ==============================================
	 * ==========================================================================================================
	 * <p>
	 * 当且仅当{@code n1 >= n2}时抛出异常
	 * Throws an exception if and only if {@code n1 >= n2}
	 */
	public static void lessThan(int n1, int n2, Exception ex) {
		predicate(() -> n1 >= n2, ex);
	}

	public static void lessThan(long n1, long n2, Exception ex) {
		predicate(() -> n1 >= n2, ex);
	}

	public static void lessThan(float n1, float n2, Exception ex) {
		predicate(() -> n1 >= n2, ex);
	}

	public static void lessThan(double n1, double n2, Exception ex) {
		predicate(() -> n1 >= n2, ex);
	}

	/**
	 * ==========================================================================================================
	 * =========================================== greaterThan0 (> 0) ===========================================
	 * ==========================================================================================================
	 * <p>
	 * 当且仅当{@code n1 <= 0}时抛出异常
	 * Throws an exception if and only if {@code n1 <= 0}
	 */
	public static void greaterThan0(short number, Exception ex) {
		greaterThan(number, 0, ex);
	}

	public static void greaterThan0(short number, String exMessage) {
		greaterThan0(number, new IllegalArgumentException(exMessage));
	}

	public static void greaterThan0(int number, Exception ex) {
		greaterThan(number, 0, ex);
	}

	public static void greaterThan0(int number, String exMessage) {
		greaterThan0(number, new IllegalArgumentException(exMessage));
	}

	public static void greaterThan0(long number, Exception ex) {
		greaterThan(number, 0, ex);
	}

	public static void greaterThan0(long number, String exMessage) {
		greaterThan0(number, new IllegalArgumentException(exMessage));
	}

	public static void greaterThan0(float number, Exception ex) {
		greaterThan(number, 0, ex);
	}

	public static void greaterThan0(float number, String exMessage) {
		greaterThan0(number, new IllegalArgumentException(exMessage));
	}

	public static void greaterThan0(double number, Exception ex) {
		greaterThan(number, 0, ex);
	}

	public static void greaterThan0(double number, String exMessage) {
		greaterThan0(number, new IllegalArgumentException(exMessage));
	}

	/**
	 * ==========================================================================================================
	 * ============================================= greaterThan(>) =============================================
	 * ==========================================================================================================
	 * <p>
	 * 当且仅当{@code n1 <= n2}时抛出异常
	 * Throws an exception if and only if {@code n1 <= n2}
	 */
	public static void greaterThan(int n1, int n2, Exception ex) {
		predicate(() -> n1 <= n2, ex);
	}

	public static void greaterThan(long n1, long n2, Exception ex) {
		predicate(() -> n1 <= n2, ex);
	}

	public static void greaterThan(float n1, float n2, Exception ex) {
		predicate(() -> n1 <= n2, ex);
	}

	public static void greaterThan(double n1, double n2, Exception ex) {
		predicate(() -> n1 <= n2, ex);
	}

	/**
	 * ==========================================================================================================
	 * ============================================ lessEqual0(<= 0) ============================================
	 * ==========================================================================================================
	 * <p>
	 * 当且仅当{@code n1 > 0}时抛出异常
	 * Throws an exception if and only if {@code n1 > 0}
	 */
	public static void lessEqual0(short number, Exception ex) {
		lessEqual(number, 0, ex);
	}

	public static void lessEqual0(short number, String exMessage) {
		lessEqual0(number, new IllegalArgumentException(exMessage));
	}

	public static void lessEqual0(int number, Exception ex) {
		lessEqual(number, 0, ex);
	}

	public static void lessEqual0(int number, String exMessage) {
		lessEqual0(number, new IllegalArgumentException(exMessage));
	}

	public static void lessEqual0(long number, Exception ex) {
		lessEqual(number, 0, ex);
	}

	public static void lessEqual0(long number, String exMessage) {
		lessEqual0(number, new IllegalArgumentException(exMessage));
	}

	public static void lessEqual0(float number, Exception ex) {
		lessEqual(number, 0, ex);
	}

	public static void lessEqual0(float number, String exMessage) {
		lessEqual0(number, new IllegalArgumentException(exMessage));
	}

	public static void lessEqual0(double number, Exception ex) {
		lessEqual(number, 0, ex);
	}

	public static void lessEqual0(double number, String exMessage) {
		lessEqual0(number, new IllegalArgumentException(exMessage));
	}

	/**
	 * ==========================================================================================================
	 * ============================================= lessEqual (<=) =============================================
	 * ==========================================================================================================
	 * <p>
	 * 当且仅当{@code n1 > n2}时抛出异常
	 * Throws an exception if and only if {@code n1 > n2}
	 */
	public static void lessEqual(int n1, int n2, Exception ex) {
		predicate(() -> n1 > n2, ex);
	}

	public static void lessEqual(long n1, long n2, Exception ex) {
		predicate(() -> n1 > n2, ex);
	}

	public static void lessEqual(float n1, float n2, Exception ex) {
		predicate(() -> n1 > n2, ex);
	}

	public static void lessEqual(double n1, double n2, Exception ex) {
		predicate(() -> n1 > n2, ex);
	}

	/**
	 * ==========================================================================================================
	 * ========================================== greaterEqual0 (>= 0) ==========================================
	 * ==========================================================================================================
	 * <p>
	 * 当且仅当{@code n1 < 0}时抛出异常
	 * Throws an exception if and only if {@code n1 < 0}
	 */
	public static void greaterEqual0(short number, Exception ex) {
		greaterEqual(number, 0, ex);
	}

	public static void greaterEqual0(short number, String exMessage) {
		greaterEqual0(number, new IllegalArgumentException(exMessage));
	}

	public static void greaterEqual0(int number, Exception ex) {
		greaterEqual(number, 0, ex);
	}

	public static void greaterEqual0(int number, String exMessage) {
		greaterEqual0(number, new IllegalArgumentException(exMessage));
	}

	public static void greaterEqual0(long number, Exception ex) {
		greaterEqual(number, 0, ex);
	}

	public static void greaterEqual0(long number, String exMessage) {
		greaterEqual0(number, new IllegalArgumentException(exMessage));
	}

	public static void greaterEqual0(float number, Exception ex) {
		greaterEqual(number, 0, ex);
	}

	public static void greaterEqual0(float number, String exMessage) {
		greaterEqual0(number, new IllegalArgumentException(exMessage));
	}

	public static void greaterEqual0(double number, Exception ex) {
		greaterEqual(number, 0, ex);
	}

	public static void greaterEqual0(double number, String exMessage) {
		greaterEqual0(number, new IllegalArgumentException(exMessage));
	}

	/**
	 * ==========================================================================================================
	 * ============================================ greaterEqual(>=) ============================================
	 * ==========================================================================================================
	 * <p>
	 * 当且仅当{@code n1 < n2}时抛出异常
	 * Throws an exception if and only if {@code n1 < n2}
	 */
	public static void greaterEqual(int n1, int n2, Exception ex) {
		predicate(() -> n1 < n2, ex);
	}

	public static void greaterEqual(long n1, long n2, Exception ex) {
		predicate(() -> n1 < n2, ex);
	}

	public static void greaterEqual(float n1, float n2, Exception ex) {
		predicate(() -> n1 < n2, ex);
	}

	public static void greaterEqual(double n1, double n2, Exception ex) {
		predicate(() -> n1 < n2, ex);
	}

	/**
	 * 当且仅当{@code predicate.get() == true}时抛出异常
	 * Throws an exception if and only if {@code predicate.get() == true}
	 *
	 * @param predicate 判断值提供者
	 *                  Judgment value provider
	 * @param ex        异常
	 *                  Exception
	 */
	public static void predicate(Supplier<Boolean> predicate, Exception ex) {
		if (predicate.get()) {
			throwAsUnchecked(ex);
		}
	}

	private static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E {
		throw (E) exception;
	}

}
