package io.github.kits;

import io.github.kits.enums.Prop;
import io.github.kits.log.Logger;
import io.github.kits.timer.TimedTask;

import java.io.File;
import java.io.StringReader;
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
public final class PropertiesKit {

	private final static String LAST_MODIFY_TIME = "$$LMT";

	private final static String TIMER_TASK_NAME = "PropertiesKit-Reload";

	private PropertiesKit() {}

	/**
	 * Properties cache
	 */
	private static ConcurrentHashMap<File, Properties> PROPERTIES_CACHE_MAP;
	private static ConcurrentHashMap<String, File> PROP_FILE_CACHE;

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
		PROPERTIES_CACHE_MAP.clear();
	}

	/**
	 * Clear the contents of a specific profile
	 *
	 * @param fileName file path
	 */
	public static void clear(String fileName) {
		File propFile = getPropFile(fileName);
		clear(propFile);
	}

	/**
	 * Clear the contents of a specific profile
	 *
	 * @param file file
	 */
	public static void clear(File file) {
		if (Files.isNotNullOrEmpty(file)) {
			PROPERTIES_CACHE_MAP.remove(file);
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
	private static void put(File key, Properties properties) {
		properties.setProperty(LAST_MODIFY_TIME, String.valueOf(key.lastModified()));
		PROPERTIES_CACHE_MAP.put(key, properties);
	}

	/**
	 * Get the corresponding value from the path according to the key
	 *
	 * @param filePath      file path
	 * @param key           key
	 * @return The value obtained from the key
	 */
    public static Optional<Object> getProperty(String filePath, String key) {
        return getObject(filePath, key);
    }

	/**
	 * Get the value corresponding to the key from the file
	 *
	 * @param file          File object
	 * @param key           key
	 * @return The value obtained from the key
	 */
    public static Optional<Object> getProperty(File file, String key) {
        return getObject(file, key);
    }

	/**
	 * Get the value from the default configuration file according to the key
	 *
	 * @param key           key
	 * @return The value obtained from the key
	 */
    public static Optional<Object> getProperty(String key) {
        return getObject(getDefaultConfig(), key);
    }

	/**
	 * Get the value from the specified path file according to the key
	 *
	 * @param fileName file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<String> getString(String fileName, String key) {
		return get(fileName, key).map(String::valueOf);
	}

	/**
	 * Get value, String from the specified file
	 *
	 * @param file file
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<String> getString(File file, String key) {
		return get(file, key).map(String::valueOf);
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
		return get(fileName, key).map(Integer::valueOf);
	}

	/**
	 * Get the value of type int from the specified file
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Integer> getInt(File file, String key) {
		return get(file, key).map(Integer::valueOf);
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
	 * @param filePath file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<Short> getShort(String filePath, String key) {
		return get(filePath, key).map(Short::valueOf);
	}

	/**
	 * Get value from the specified file
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Short> getShort(File file, String key) {
		return get(file, key).map(Short::valueOf);
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
	 * @param filePath file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<Long> getLong(String filePath, String key) {
		return get(filePath, key).map(Long::valueOf);
	}

	/**
	 * Get value from the specified file
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Long> getLong(File file, String key) {
		return get(file, key).map(Long::valueOf);
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
	 * @param filePath file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<Float> getFloat(String filePath, String key) {
		return get(filePath, key).map(Float::valueOf);
	}

	/**
	 * Get the float type value from the specified file
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Float> getFloat(File file, String key) {
		return get(file, key).map(Float::valueOf);
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
	 * @param filePath file path
	 * @param key      key
	 * @return The value obtained from the key
	 */
	public static Optional<Double> getDouble(String filePath, String key) {
		return get(filePath, key).map(Double::valueOf);
	}

	/**
	 * Obtain the double-precision floating-point type value from the specified file;
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Double> getDouble(File file, String key) {
		return get(file, key).map(Double::valueOf);
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
		return get(fileName, key).map(Boolean::valueOf);
	}

	/**
	 * Get the boolean value from the specified configuration file
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	public static Optional<Boolean> getBoolean(File file, String key) {
		return get(file, key).map(Boolean::valueOf);
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

	/**
	 * Get the value corresponding to the key from the specified file or path
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	private static Optional<String> get(Object file, String key) {
		Optional<Properties> properties = getProperties(file);
		return properties.filter(prop -> isNotNullOrEmpty(prop) && Strings.isNotNullOrEmpty(key)).map(prop -> prop.getProperty(key));
	}

	/**
	 * Get the value corresponding to the key from the specified file or path
	 *
	 * @param file File object
	 * @param key  key
	 * @return The value obtained from the key
	 */
	private static Optional<Object> getObject(Object file, String key) {
		Optional<Properties> properties = getProperties(file);
		return properties.filter(prop -> isNotNullOrEmpty(prop) && Strings.isNotNullOrEmpty(key)).map(prop -> prop.get(key));
	}

	/**
	 * Get properties, if there is direct fetch in the cache,
	 * if it does not exist in the cache, load from the file
	 *
	 * @param fileOrPath file path
	 * @return Properties Object
	 */
	public static Optional<Properties> getProperties(Object fileOrPath) {
		File       proFile    = getPropFile(fileOrPath);
		Properties properties = null;
		if (Objects.nonNull(proFile)) {
			properties = PROPERTIES_CACHE_MAP.get(proFile);
		}
		if (isNullOrEmpty(properties) && Objects.nonNull(proFile)) {
			properties = loadProperties(proFile);
		}
		return Optional.ofNullable(properties);
	}

	/**
	 * Load Properties file
	 *
	 * @param proFile file
	 * @return Properties
	 */
	private static Properties loadProperties(File proFile) {
		byte[]     bytes      = Files.loadResourceFile(proFile.getPath().substring(proFile.getPath().lastIndexOf("/") + 1));
		Properties properties = null;
		if (null != bytes) {
			try {
				String content = new String(bytes);
				if (Strings.isNotNullOrEmpty(content)) {
					properties = new Properties();
					properties.load(new StringReader(content));
					put(proFile, properties);
				}
			} catch (Exception ex) {
				Logger.errorf("Load Properties error", ex);
			}
		}
		return properties;
	}

	/**
	 * Get file
	 *
	 * @param file File or path
	 * @return File object
	 */
	private static File getPropFile(Object file) {
		if (Objects.nonNull(file)) {
			if (file instanceof String) {
				if (PROP_FILE_CACHE.containsKey(file.toString()))
					return PROP_FILE_CACHE.get(file.toString());
				else {
					File resourceFile = Files.getResourceFile(complexEnd(file.toString()));
					if (Objects.nonNull(resourceFile) && resourceFile.exists())
						PROP_FILE_CACHE.put(resourceFile.getAbsolutePath(), resourceFile);
					else
						PROP_FILE_CACHE.put(file.toString(), new File(file.toString()));
					return resourceFile;
				}
			} else if (file instanceof File) {
				String absolutePath = ((File) file).getAbsolutePath();
				if (PROP_FILE_CACHE.containsKey(absolutePath)) {
					return PROP_FILE_CACHE.get(absolutePath);
				} else {
					PROP_FILE_CACHE.put(absolutePath, (File) file);
					if (((File) file).exists()) {
						return (File) file;
					} else {
						return null;
					}
				}
			}
		}
		return null;
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
		PROPERTIES_CACHE_MAP = new ConcurrentHashMap<>();
		PROP_FILE_CACHE = new ConcurrentHashMap<>();
		TimedTask.addTask(TIMER_TASK_NAME, prop -> {
			if (Maps.isNotNullOrEmpty(PROPERTIES_CACHE_MAP)) {
				PROPERTIES_CACHE_MAP.forEach((file, properties) -> {
					if (file.exists() && properties.containsKey(LAST_MODIFY_TIME)) {
						String lastTimeStamp   = String.valueOf(file.lastModified());
						String cachedTimeStamp = properties.getProperty(LAST_MODIFY_TIME);
						if (!lastTimeStamp.equals(cachedTimeStamp)) {
							PROPERTIES_CACHE_MAP.remove(file);
							loadProperties(file);
							Logger.infof("The {} properties has been changed!", Colors.toYellowBold("[ " + file.getAbsolutePath() + " ]"));
						}
					}
				});
			}
		}, 5 * 1000);
	}

}
