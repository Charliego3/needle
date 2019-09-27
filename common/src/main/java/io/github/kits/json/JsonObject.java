package io.github.kits.json;

import io.github.kits.Strings;
import io.github.kits.exception.JsonParseException;
import io.github.kits.exception.TypeException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public final class JsonObject {

	private Map<String, Object> ENTRY = new HashMap<>();

	void put(String key, Object value) {
		ENTRY.put(key, value);
	}

	public int size() {
		return this.ENTRY.size();
	}

	/**
	 * Get Object value from ENTRY by key
	 *
	 * @param key key
	 * @return Object value
	 */
	public Object get(String key) {
		return ENTRY.get(key);
	}

	/**
	 * Remove the value from ENTRY by key
	 *
	 * @param key key
	 */
	public void remove(String key) {
		ENTRY.remove(key);
	}

	/**
	 * Get all the key values
	 *
	 * @return All key values
	 */
	List<Map.Entry<String, Object>> getAllKeyValue() {
		return new ArrayList<>(ENTRY.entrySet());
	}

	/**
	 * Get value RNTRY
	 *
	 * @return Map
	 */
	Map<String, Object> getEntry() {
		return this.ENTRY;
	}

	/**
	 * Get BigDecimal type data from ENTRY according to key
	 *
	 * @param key key
	 * @return BigDecimal value
	 */
	public BigDecimal getBigdecimal(String key) {
		BigDecimal result = BigDecimal.ZERO;
		if (!ENTRY.containsKey(key)) {
			return result;
		}
		Object val = ENTRY.get(key);
		if (Objects.nonNull(val)) {
			if (Strings.isNumber(val.toString(), true)) {
				result = new BigDecimal(val.toString());
			}
		}
		return result;
	}

	/**
	 * Determine if the key is included
	 *
	 * @param key key
	 * @return true-include | false-not include
	 */
	public boolean contains(String key) {
		return ENTRY.containsKey(key);
	}

	/**
	 * Get String type data from ENTRY according to key
	 *
	 * @param key key
	 * @return String value
	 */
	public String getString(String key) {
		if (!ENTRY.containsKey(key)) {
			return null;
		}
		Object val = ENTRY.get(key);
		return Objects.isNull(val) ? null : val.toString();
	}

	/**
	 * Get Boolean type data from ENTRY according to key
	 *
	 * @param key key
	 * @return Boolean value
	 */
	public boolean getBoolean(String key) {
		if (!ENTRY.containsKey(key)) {
			return false;
		}
		boolean result = false;
		Object  val    = ENTRY.get(key);
		if (Objects.nonNull(val)) {
			String valS = val.toString()
							 .trim()
							 .toLowerCase();
			if (!(val instanceof Boolean) && !"true".equals(valS) && !"false".equals(valS)) {
				throw new JsonParseException("The value can not cast boolean");
			}
			result = Boolean.valueOf(valS);
		}
		return result;
	}

	/**
	 * Get byte value from ENTRY according to key
	 *
	 * @param key key
	 * @return	byte value
	 */
	public byte getByte(String key) {
		if (!ENTRY.containsKey(key)) {
			return 0;
		}
		Object val = ENTRY.get(key);
		if (Objects.nonNull(val)) {
			if (val instanceof Number) {
				return ((Number) val).byteValue();
			}
		}
		return 0;
	}

	/**
	 * Get Short type data from ENTRY according to key
	 *
	 * @param key key
	 * @return Short value
	 */
	public short getShort(String key) {
		if (!ENTRY.containsKey(key)) {
			return 0;
		}
		Object val    = ENTRY.get(key);
		if (Objects.nonNull(val)) {
			if (val instanceof Number) {
				return ((Number) val).shortValue();
			}
		}
		return 0;
	}

	/**
	 * Get Long type data from ENTRY according to key
	 *
	 * @param key key
	 * @return Long value
	 */
	public long getLong(String key) {
		if (!ENTRY.containsKey(key)) {
			return 0L;
		}
		Object val    = ENTRY.get(key);
		if (Objects.nonNull(val)) {
			if (val instanceof Number) {
				return ((Number) val).longValue();
			}
		}
		return 0L;
	}

	/**
	 * Get Double type data from ENTRY according to key
	 *
	 * @param key key
	 * @return Double value
	 */
	public double getDouble(String key) {
		if (!ENTRY.containsKey(key)) {
			return 0D;
		}
		Object val    = ENTRY.get(key);
		if (Objects.nonNull(val)) {
			if (val instanceof Number) {
				return ((Number) val).doubleValue();
			}
		}
		return 0D;
	}

	/**
	 * Get Float type data from ENTRY according to key
	 *
	 * @param key key
	 * @return Float value
	 */
	public float getFloat(String key) {
		if (!ENTRY.containsKey(key)) {
			return 0F;
		}
		Object val    = ENTRY.get(key);
		if (Objects.nonNull(val)) {
			if (val instanceof Number) {
				return ((Number) val).floatValue();
			}
		}
		return 0F;
	}

	/**
	 * Get Int type data from ENTRY according to key
	 *
	 * @param key key
	 * @return Int value
	 */
	public int getInt(String key) {
		if (!ENTRY.containsKey(key)) {
			return 0;
		}
		Object val    = ENTRY.get(key);
		if (Objects.nonNull(val)) {
			if (val instanceof Number) {
				return ((Number) val).intValue();
			}
		}
		return 0;
	}

	/**
	 * Get JsonObject type data from ENTRY according to key
	 *
	 * @param key key
	 * @return JsonObject value
	 */
	public JsonObject getJsonObject(String key) {
		if (!ENTRY.containsKey(key)) {
			throw new IllegalArgumentException("Invalid key");
		}

		Object obj = ENTRY.get(key);
		if (!(obj instanceof JsonObject)) {
			throw new TypeException("Type of value is not JsonObject");
		}

		return (JsonObject) obj;
	}

	/**
	 * Get JsonArray type data from ENTRY according to key
	 *
	 * @param key key
	 * @return JsonArray value
	 */
	public JsonArray getJsonArray(String key) {
		if (!ENTRY.containsKey(key)) {
			throw new IllegalArgumentException("Invalid key");
		}

		Object obj = ENTRY.get(key);
		if (!(obj instanceof JsonArray)) {
			throw new TypeException("Type of value is not JsonArray");
		}

		return (JsonArray) obj;
	}

}
