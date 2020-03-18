package io.github.kits;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 唯一id序列工具类
 * 需要在config.properties中配置<code>unique_seed_id</code>为int类型的数值
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class UniqueId {

	private static final int SEQ_DEFAULT  = 0;
	private static final int RADIX        = 62;
	private static final int sequenceLeft = 12;
	private static final int signIdLeft   = 10;
	private static final int maxSignId    = 1 << signIdLeft;

	private static volatile AtomicInteger orderedIdSequence = new AtomicInteger(SEQ_DEFAULT);
	private static          Long          lastTime          = 0L;
	private static          int           workId            = 0;

	static {
		int signId = Props.getInt("unique_seed_id")
						  .orElse(new Random().nextInt());
		Assert.predicate(() -> workId > maxSignId || signId < 0,
				new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxSignId)));
		workId = signId;
	}

	/**
	 * 获取下一个 IntId
	 *
	 * @return 返回 userId
	 */
	public static long nextInt() {
		return generateId();
	}

	/**
	 * 获取下一个 StringId
	 *
	 * @return 返回 userId
	 */
	public static String nextString() {
		return Strings.radixConvert(generateId(), RADIX);
	}

	/**
	 * 生成带顺序的 ID 序列
	 *
	 * @return ID字符串
	 */
	public static synchronized long generateId() {
		long currentTime = System.currentTimeMillis();

		if (lastTime < currentTime) {
			orderedIdSequence.set(SEQ_DEFAULT);
		} else if (lastTime > currentTime) {
			throw new RuntimeException("Clock moved backwards.");
		}

		long resultId = (currentTime << (sequenceLeft + signIdLeft)) | (workId << signIdLeft) | orderedIdSequence.getAndAdd(1);

		lastTime = currentTime;
		return resultId;
	}

}
