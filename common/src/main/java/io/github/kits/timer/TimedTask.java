package io.github.kits.timer;

import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.function.Consumer;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class TimedTask {

    public static DelayTask delay() {
        return DelayTask.getDelayTask();
    }

    /**
     * 从firstTime时刻开始，每隔period毫秒执行一次。
     *
     * @param taskName
     * @param task
     * @param firstTime
     * @param period
     * @param <T>
     */
    public static <T> void addFirstTimeTask(String taskName, Consumer<T> task, Date firstTime, long period) {
        if (Objects.isNull(firstTime)) {
            firstTime = new Date();
        }
        TimedKit.addTask(taskName, null, null, task, null, period, null, firstTime);
    }

    /**
     * 从firstTime时刻开始，每隔period毫秒执行一次。
     *
     * @param taskName
     * @param task
     * @param firstTime
     * @param period
     * @param <T>
     */
    public static <T> void addFirstTimeTask(String taskName, T t, Consumer<T> task, Date firstTime, long period) {
        if (Objects.isNull(firstTime)) {
            firstTime = new Date();
        }
        TimedKit.addTask(taskName, t, null, task, null, period, null, firstTime);
    }

    /**
     * 在指定时间执行一次
     *
     * @param taskName
     * @param task
     * @param date
     * @param <T>
     */
    public static <T> void addTask(String taskName, Consumer<T> task, Date date) {
        TimedKit.addTask(taskName, null, null, task, null, null, date, null);
    }

    /**
     * 每隔period毫秒执行一次
     *
     * @param taskName
     * @param task
     * @param period
     * @param <T>
     */
    public static <T> void addTask(String taskName, Consumer<T> task, long period) {
        TimedKit.addTask(taskName, null, null, task, null, period, null, null);
    }

    /**
     * 每隔period毫秒执行一次
     *
     * @param taskName
     * @param t
     * @param task
     * @param period
     * @param <T>
     */
    public static <T> void addTask(String taskName, T t, Consumer<T> task, long period) {
        TimedKit.addTask(taskName, t, null, task, null, period, null, null);
    }

    /**
     * 取消定时任务
     *
     * @param taskName
     */
    public static void cancel(String taskName) {
        TimedKit.checkTaskName(taskName);
        if (TimedKit.TIMER_MAP.containsKey(taskName)) {
            Timer timer = TimedKit.TIMER_MAP.get(taskName);
            if (Objects.isNull(timer)) {
                throw new NullPointerException("Timer is not exists!");
            }
            timer.cancel();
        }
    }

}
