package com.leepandar.starter.core.util;

import com.leepandar.starter.core.constant.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 异常工具类
 */
public class ExceptionUtils {
    private static final Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

    private ExceptionUtils() {
    }

    /**
     * 打印线程异常信息
     *
     * @param runnable  线程执行内容
     * @param throwable 异常
     */
    public static void printException(Runnable runnable, Throwable throwable) {
        if (throwable == null && runnable instanceof Future<?> future) {
            try {
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException e) {
                throwable = e;
            } catch (ExecutionException e) {
                throwable = e.getCause();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (throwable != null) {
            log.error(throwable.getMessage(), throwable);
        }
    }

    /**
     * 如果有异常，返回 null
     *
     * @param exSupplier 可能会出现异常的方法执行
     * @param <T>        /
     * @return /
     */
    public static <T> T exToNull(ExSupplier<T> exSupplier) {
        return exToDefault(exSupplier, null);
    }

    /**
     * 如果有异常，执行异常处理
     *
     * @param supplier   可能会出现异常的方法执行
     * @param exConsumer 异常处理
     * @param <T>        /
     * @return /
     */
    public static <T> T exToNull(ExSupplier<T> supplier, Consumer<Exception> exConsumer) {
        return exToDefault(supplier, null, exConsumer);
    }

    /**
     * 如果有异常，返回空字符串
     *
     * @param exSupplier 可能会出现异常的方法执行
     * @return /
     */
    public static String exToBlank(ExSupplier<String> exSupplier) {
        return exToDefault(exSupplier, StringConstants.EMPTY);
    }

    /**
     * 如果有异常，返回默认值
     *
     * @param exSupplier   可能会出现异常的方法执行
     * @param defaultValue 默认值
     * @param <T>          /
     * @return /
     */
    public static <T> T exToDefault(ExSupplier<T> exSupplier, T defaultValue) {
        return exToDefault(exSupplier, defaultValue, null);
    }

    /**
     * 如果有异常，抛出自定义异常
     *
     * @param exSupplier      可能会出现异常的方法执行
     * @param exceptionMapper 异常转换函数
     * @param <T>             返回值类型
     * @param <E>             自定义异常类型
     * @return 执行结果
     * @throws E 自定义异常
     */
    public static <T, E extends Exception> T exToThrow(ExSupplier<T> exSupplier,
                                                       Function<Exception, E> exceptionMapper) throws E {
        try {
            return exSupplier.get();
        } catch (Exception e) {
            throw exceptionMapper.apply(e);
        }
    }

    /**
     * 如果有异常，执行异常处理，返回默认值
     *
     * @param exSupplier   可能会出现异常的方法执行
     * @param defaultValue 默认值
     * @param exConsumer   异常处理
     * @param <T>          /
     * @return /
     */
    public static <T> T exToDefault(ExSupplier<T> exSupplier, T defaultValue, Consumer<Exception> exConsumer) {
        try {
            return exSupplier.get();
        } catch (Exception e) {
            if (exConsumer != null) {
                exConsumer.accept(e);
            }
            return defaultValue;
        }
    }

    /**
     * 异常提供者
     *
     * @param <T> /
     */
    public interface ExSupplier<T> {
        /**
         * 获取返回值
         *
         * @return /
         * @throws Exception /
         */
        T get() throws Exception;

    }
}
