package io.github.kits.json;

import io.github.kits.exception.TypeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public final class JsonArray {

	private List<Object> list = new ArrayList<>();

	/**
	 * Add a value to JsonArray
	 *
	 * @param obj value
	 */
	void add(Object obj) {
		list.add(obj);
	}

	/**
	 * Get Object value from JsonArray
	 *
	 * @param index index
	 * @return Object
	 */
	public Object get(int index) {
		return list.get(index);
	}

	/**
	 * Get JsonArray size
	 *
	 * @return size
	 */
	public int size() {
		return list.size();
	}

	/**
	 * Get JsonObject value from JsonArray
	 *
	 * @param index index
	 * @return JsonObject
	 */
	public JsonObject getJsonObject(int index) {
		Object obj = list.get(index);
		if (!(obj instanceof JsonObject)) {
			throw new TypeException("Type of value is not JsonObject");
		}

		return (JsonObject) obj;
	}

	/**
	 * Get JsonArray value from JsonArray
	 *
	 * @param index index
	 * @return JsonArray
	 */
	public JsonArray getJsonArray(int index) {
		Object obj = list.get(index);
		if (!(obj instanceof JsonArray)) {
			throw new TypeException("Type of value is not JsonArray");
		}

		return (JsonArray) obj;
	}

	/**
	 * JsonArray iterator
	 *
	 * @return Iterator
	 */
	public Iterator iterator() {
		return list.iterator();
	}

	/**
	 * Get JsonArray values
	 *
	 * @return values
	 */
	List getList() {
		return this.list;
	}

}
