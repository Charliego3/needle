package io.github.kits.timer;

import io.github.kits.Assert;

import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
class TimedKit {

    static final ConcurrentHashMap<String, Timer> TIMER_MAP = new ConcurrentHashMap<>();

    static void checkTaskName(String taskName) {
        Assert.isNotNullOrEmpty(taskName, "TimerTask name is can not be null or empty!");
    }

    private static Timer createTimer(String taskName) {
        checkTaskName(taskName);
        Assert.predicate(() -> TIMER_MAP.containsKey(taskName),
                new IllegalArgumentException("TimerTask name is already exists, Please check your timer, " +
                "if is different others then change taskName try again!, taskName: " + taskName));
        return new Timer(taskName);
    }

    @SuppressWarnings("unchecked")
    private static <T, R> TimerTask getTimerTask(Object task, T t, R r) {
        return new TimerTask() {
            @Override
            public void run() {
                if (task instanceof Consumer) {
                    ((Consumer<T>) task).accept(t);
                } else if (task instanceof BiConsumer) {
                    ((BiConsumer<T, R>) task).accept(t, r);
                }
            }
        };
    }

    static <T, R> void addTask(String taskName, T t, R r, Object task, Long delay, Long period, Date date, Date firstTime) {
        if ((Objects.isNull(delay) && Objects.isNull(period)) ||
                ((Objects.nonNull(delay) && delay == 0) && (Objects.nonNull(period) && period == 0))) {
            if (Objects.isNull(date)) {
                throw new IllegalArgumentException("TimerTask date can not be null!");
            } else {
                throw new IllegalArgumentException("TimerTask delay and period can not be empty or zero at the same time!");
            }
        }
        Timer timer = createTimer(taskName);
        TimerTask timerTask = getTimerTask(task, t, r);
        if (Objects.isNull(delay)) {
            delay = 0L;
        }
        if (Objects.nonNull(date)) {
            timer.schedule(timerTask, date);
        } else if (Objects.nonNull(period)) {
            if (Objects.nonNull(firstTime)) {
                timer.schedule(timerTask, firstTime, period);
            } else {
                timer.schedule(timerTask, delay, period);
            }
        } else {
            timer.schedule(timerTask, delay);
        }
        TIMER_MAP.put(taskName, timer);
    }

}
