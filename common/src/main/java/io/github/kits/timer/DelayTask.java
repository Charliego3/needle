package io.github.kits.timer;

import java.util.function.Consumer;

/**
 * 延迟定时任务, delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class DelayTask {

    private volatile static DelayTask delayTask = null;

    public static DelayTask getDelayTask() {
        if (delayTask == null) {
            synchronized (DelayTask.class) {
                if (delayTask == null) {
                    delayTask = new DelayTask();
                }
            }
        }
        return delayTask;
    }

    /**
     * delay 为long类型：从现在起过delay毫秒执行一次
     *
     * @param taskName
     * @param task
     * @param delay
     * @param <T>
     */
    public <T> void addTask(String taskName, Consumer<T> task, long delay) {
        TimedKit.addTask(taskName, null, null, task, delay, null, null, null);
    }

    /**
     * 从现在起过delay毫秒以后，每隔period毫秒执行一次。
     *
     * @param taskName
     * @param task
     * @param delay
     * @param period
     * @param <T>
     */
    public <T> void addTask(String taskName, Consumer<T> task, long delay, long period) {
        TimedKit.addTask(taskName, null, null, task, delay, period, null, null);
    }

    /**
     * 从现在起过delay毫秒以后，每隔period毫秒执行一次。
     *
     * @param taskName
     * @param t
     * @param task
     * @param delay
     * @param period
     * @param <T>
     */
    public <T> void addTask(String taskName, T t, Consumer<T> task, long delay, long period) {
        TimedKit.addTask(taskName, t, null, task, delay, period, null, null);
    }

}
