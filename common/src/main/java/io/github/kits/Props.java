package io.github.kits;

import io.github.kits.enums.Prop;
import io.github.kits.log.Logger;
import io.github.kits.timer.TimedTask;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Profile utility class, load and cache configuration file content
 * Check if the Properties file is modified every 5s, if the modification will be reloaded
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Props {

	private final static String LAST_MODIFY_TIME = "$$LMT$$";

	private final static String TIMER_TASK_NAME = "Properties-Reload";

	private Props() {
	}

	/**
	 * Properties cache
	 */
	private static final ConcurrentHashMap<File, Properties> PROP_CACHE;
	private static final ConcurrentHashMap<String, File>     PROP_FILE_CACHE;

	/**
	 * Get the default configuration file
	 *
	 * @return Default profile name
	 */
	public static String getDefaultConfig() {
		return Prop.DEFAULT_CONFIG_PROPERTIES.getProp();
	}

	/**
	 * Clear the Properties configuration
	 */
	public static void clearAll() {
		PROP_CACHE.clear();
		PROP_FILE_CACHE.clear();
	}

	/**
	 * Clear the contents of a specific profile
	 *
	 * @param file file
	 */
	public static void clear(File file) {
		if (Files.isNotNullOrEmpty(file)) {
			PROP_CACHE.remove(file);
		}
	}

	/**
	 * Settings value
	 *
	 * @param fileName file path
	 * @param key      key
	 * @param value    value
	 */
	public static void set(String fileName, Object key, Object value) {
		getProperties(fileName).ifPresent(properties -> set(properties, key, value));
	}

	/**
	 * Settings value
	 *
	 * @param file  file path
	 * @param key   keyValue
	 * @param value value
	 */
	public static void set(File file, Object key, Object value) {
		getProperties(file).ifPresent(properties -> set(properties, key, value));
	}

	/**
	 * Settings
	 *
	 * @param properties Properties file
	 * @param key        Key value
	 * @param value      value
	 */
	public static void set(Properties properties, Object key, Object value) {
		if (isNotNullOrEmpty(properties)) {
			properties.put(key, value);
		}
	}

	/**
	 * Add a new properties to the cache
	 *
	 * @param key        key
	 * @param properties Properties
	 */
	public static void put(File key, Properties properties) {
		properties.setProperty(LAST_MODIFY_TIME, String.valueOf(key.lastModified()));
		PROP_CACHE.put(key, properties);
	}

	/**
	 * Add a new properties to the cache
	 *
	 * @param key   key
	 * @param value value
	 */
	public static void put(String fileName, String key, String value) {
		File file = PROP_FILE_CACHE.get(fileName);
		file = Objs.nullDefault(file, new File(fileName));
		Properties properties = getProperties(fileName).orElse(new Properties());
		properties.setProperty(LAST_MODIFY_TIME, String.valueOf(file.lastModified()));
		properties.setProperty(key, value);
		PROP_FILE_CACHE.put(fileName, file);
		PROP_CACHE.put(file, properties);
	}

	/**
	 * Get the corresponding value from the path according to the key
	 *
	 * @param fileName file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<Object> getProperty(String fileName, String key) {
		return getProperties(fileName).map(prop -> prop.get(key));
	}

	/**
	 * Get the value corresponding to the key from the file
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Object> getProperty(File file, String key) {
		return getProperties(file).map(prop -> prop.get(key));
	}

	/**
	 * Get the value from the default configuration file according to the key
	 *
	 * @param key key
	 * @return The value obtained from the key
	 */
	public static Optional<Object> getProperty(String key) {
		return getProperties(getDefaultConfig()).map(prop -> prop.get(key));
	}

	/**
	 * Get the value from the specified path file according to the key
	 *
	 * @param fileName file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<String> getString(String fileName, String key) {
		return getProperties(fileName).map(p -> p.getProperty(key));
	}

	/**
	 * Get value, String from the specified file
	 *
	 * @param file file
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<String> getString(File file, String key) {
		return getProperties(file).map(p -> p.getProperty(key));
	}

	/**
	 * Get the String type value from the default configuration file
	 *
	 * @param key key
	 * @return The value obtained from the key
	 */
	public static Optional<String> getString(String key) {
		return getString(getDefaultConfig(), key);
	}

	/**
	 * Get the value of int type from the specified path
	 *
	 * @param fileName file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<Integer> getInt(String fileName, String key) {
		return getString(fileName, key).map(Integer::parseInt);
	}

	/**
	 * Get the value of type int from the specified file
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Integer> getInt(File file, String key) {
		return getString(file, key).map(Integer::parseInt);
	}

	/**
	 * Get the int type value from the default configuration file
	 *
	 * @param key key
	 * @return The value obtained from the key
	 */
	public static Optional<Integer> getInt(String key) {
		return getInt(getDefaultConfig(), key);
	}

	/**
	 * Get value from the specified file path
	 *
	 * @param fileName file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<Short> getShort(String fileName, String key) {
		return getString(fileName, key).map(Short::parseShort);
	}

	/**
	 * Get value from the specified file
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Short> getShort(File file, String key) {
		return getString(file, key).map(Short::parseShort);
	}

	/**
	 * Get value from the default configuration file
	 *
	 * @param key key
	 * @return The value obtained from the key
	 */
	public static Optional<Short> getShort(String key) {
		return getShort(getDefaultConfig(), key);
	}

	/**
	 * Get value from the specified file path
	 *
	 * @param fileName file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<Long> getLong(String fileName, String key) {
		return getString(fileName, key).map(Long::parseLong);
	}

	/**
	 * Get value from the specified file
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Long> getLong(File file, String key) {
		return getString(file, key).map(Long::parseLong);
	}

	/**
	 * Get value from the default configuration file
	 *
	 * @param key key
	 * @return The value obtained from the key
	 */
	public static Optional<Long> getLong(String key) {
		return getLong(getDefaultConfig(), key);
	}

	/**
	 * Get the float type value from the specified file path
	 *
	 * @param fileName file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<Float> getFloat(String fileName, String key) {
		return getString(fileName, key).map(Float::parseFloat);
	}

	/**
	 * Get the float type value from the specified file
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Float> getFloat(File file, String key) {
		return getString(file, key).map(Float::parseFloat);
	}

	/**
	 * Get the float type value from the default configuration file
	 *
	 * @param key key
	 * @return The value obtained from the key
	 */
	public static Optional<Float> getFloat(String key) {
		return getFloat(getDefaultConfig(), key);
	}

	/**
	 * Get the double-precision floating-point type value from the specified file path
	 *
	 * @param fileName file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<Double> getDouble(String fileName, String key) {
		return getString(fileName, key).map(Double::parseDouble);
	}

	/**
	 * Obtain the double-precision floating-point type value from the specified file;
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Double> getDouble(File file, String key) {
		return getString(file, key).map(Double::parseDouble);
	}

	/**
	 * Get the double-precision floating-point type value from the default configuration file
	 *
	 * @param key key
	 * @return The value obtained from the key
	 */
	public static Optional<Double> getDouble(String key) {
		return getDouble(getDefaultConfig(), key);
	}

	/**
	 * Get the boolean value from the specified file path
	 *
	 * @param fileName file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<Boolean> getBoolean(String fileName, String key) {
		return getString(fileName, key).map(Strings::parseBoolean);
	}

	/**
	 * Get the boolean value from the specified configuration file
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Boolean> getBoolean(File file, String key) {
		return getString(file, key).map(Strings::parseBoolean);
	}

	/**
	 * Get the boolean value from the default configuration file
	 *
	 * @param key key
	 * @return The value obtained from the key
	 */
	public static Optional<Boolean> getBoolean(String key) {
		return getBoolean(getDefaultConfig(), key);
	}

	public static Optional<BigDecimal> getDecimal(File file, String key) {
		return getString(file, key).map(BigDecimal::new);
	}

	public static Optional<BigDecimal> getDecimal(String fileName, String key) {
		return getString(fileName, key).map(BigDecimal::new);
	}

	public static Optional<BigDecimal> getDecimal(String key) {
		return getDecimal(getDefaultConfig(), key);
	}

	/**
	 * Determine if it is empty
	 *
	 * @param properties Properties
	 * @return true-empty | false-not empty
	 */
	public static boolean isNullOrEmpty(Properties properties) {
		return properties == null || properties.isEmpty();
	}

	/**
	 * Determine if it is not empty
	 *
	 * @param properties Properties
	 * @return true-not empty | false-empty
	 */
	public static boolean isNotNullOrEmpty(Properties properties) {
		return !isNullOrEmpty(properties);
	}

	public static Optional<Properties> getProperties(File file) {
		try {
			if (Objects.isNull(file) || !file.exists()) {
				return Optional.empty();
			}

			if (!PROP_CACHE.containsKey(file)) {
				Properties properties = new Properties();
				String     content    = null;
				if (!file.getPath().contains("!" + File.separator)) {
					byte[] bytes = Files.loadFile(file);
					if (bytes != null) {
						content = new String(bytes);
					}
				} else {
					String filePath     = file.getPath();
					String resourcePath = filePath.substring(filePath.indexOf("!" + File.separator) + 2);
					byte[] bytes        = Files.loadResourceFile(resourcePath);
					if (bytes != null) {
						content = new String(bytes);
					}
				}
				if (Strings.isBlack(content)) {
					Logger.errorf("Can not find properties file: [{}]", file.getAbsolutePath());
					return Optional.empty();
				}

				properties.load(new StringReader(content));
				properties.setProperty(LAST_MODIFY_TIME, String.valueOf(file.lastModified()));
				PROP_CACHE.put(file, properties);
				Logger.infof("Load properties file: {}", Colors.toBuleBold("[" + file.getAbsolutePath() + "]"));
			}

			return Optional.ofNullable(PROP_CACHE.get(file));
		} catch (IOException err) {
			Logger.errorf("Get properties file failed. File: [{}]", err, file.getAbsolutePath());
			return Optional.empty();
		}
	}

	public static Optional<Properties> getProperties(String fileName) {
		File file = null;
		if (!PROP_FILE_CACHE.containsKey(fileName)) {
			file = Files.getResourceFile(complexEnd(fileName));
			if (Files.isNotNullOrEmpty(file)) {
				PROP_FILE_CACHE.put(fileName, file);
			} else if (Prop.DEFAULT_LOGGER_PROPERTIES.getProp().equals(fileName)) {
				PROP_FILE_CACHE.put(fileName, new File(Strings.isBlack(fileName) ? "" : fileName));
			}
		} else {
			file = PROP_FILE_CACHE.get(fileName);
			if (Objects.isNull(file) || !file.exists() || file.isDirectory()) {
				return Optional.empty();
			}
		}

		if (file != null) {
			return getProperties(file);
		} else {
			Logger.errorf("Get properties file failed. File: [{}]", fileName);
			return Optional.empty();
		}
	}

	/**
	 * Improve profile extension
	 *
	 * @param filePath file path
	 * @return Full file name
	 */
	private static String complexEnd(String filePath) {
		if (Strings.isNotNullOrEmpty(filePath) && !filePath.endsWith(".properties")) {
			filePath += ".properties";
		}
		return filePath;
	}

	static {
		/*
		 * Initialize, periodically check whether the configuration file is modified.
		 */
		PROP_CACHE = new ConcurrentHashMap<>();
		PROP_FILE_CACHE = new ConcurrentHashMap<>();
		Boolean isAsync = Props.getBoolean(Prop.DEFAULT_LOGGER_PROPERTIES.getProp(), "--IS_ASYNC_PRINT--")
							   .orElse(false);
		if (isAsync) {
			TimedTask.addTask(TIMER_TASK_NAME, prop -> {
				if (Maps.isNotNullOrEmpty(PROP_CACHE)) {
					PROP_CACHE.forEach((file, properties) -> {
						if (file.exists() && properties.containsKey(LAST_MODIFY_TIME)) {
							String lastTimeStamp   = String.valueOf(file.lastModified());
							String cachedTimeStamp = properties.getProperty(LAST_MODIFY_TIME);
							if (!lastTimeStamp.equals(cachedTimeStamp)) {
								PROP_CACHE.remove(file);
								getProperties(file);
								Logger.infof("The {} properties has been changed!", Colors.toYellowBold("[ " + file.getAbsolutePath() + " ]"));
							}
						}
					});
				}
			}, 5 * 1000);
		}
	}

}
