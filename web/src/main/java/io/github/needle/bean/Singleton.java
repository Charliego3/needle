package io.github.needle.bean;

import io.github.kits.Assert;
import io.github.kits.log.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例对象容器
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Singleton {

	/**
	 * 对象容器, 用于存储实例化的单例对象
	 */
	private static final Map<String, Object> SINGLETONS = new ConcurrentHashMap<>();

	/**
	 * 私有构造器, 不允许实例化
	 */
	private Singleton() {}

	static {
		// 初始化时将自己添加到容器中
		register(new Singleton());
	}

	/**
	 * 注册对象到容器中
	 *
	 * @param object 单例对象
	 */
	public static void register(Object object) {
		Assert.isNotNull(object, "Register singleton object error, reason: object is null!");
		SINGLETONS.put(object.getClass().getCanonicalName(), object);
	}

	/**
	 * 从容器中获取单例对象实例, 如果容器中不存在, 则实例化一个对象存储到容器中
	 *
	 * @param clazz Class对象
	 * @param <T> 范型约束
	 * @return 单例对象
	 */
	public static <T> T getInstance(Class<T> clazz) {
		Assert.isNotNull(clazz, "Singleton class is null!");
		String className = clazz.getName();
		if (!SINGLETONS.containsKey(className)) {
			synchronized (Singleton.class) {
				if (!SINGLETONS.containsKey(className)) {
					try {
						Class<?> aClass   = Class.forName(className);
						Object   instance = aClass.newInstance();
						register(instance);
					} catch (Exception e) {
						Logger.error("Register singleton object error", e);
					}
				}
			}
		}
		return (T) SINGLETONS.get(className);
	}

}
