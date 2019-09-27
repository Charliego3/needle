package io.github.kits.json.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class JsonTokenList {

	private List<JsonToken> tokens = new ArrayList<>();

	private int pos = 0;

	public void add(JsonToken token) {
		tokens.add(token);
	}

	void clear() {
		tokens.clear();
		pos = 0;
	}

	public int size() {
		return this.tokens.size();
	}

	public void remove(int index) {
		if (index >= this.tokens.size()) {
			throw new IndexOutOfBoundsException();
		}
		this.tokens.remove(index);
	}

	public JsonToken peek() {
		return pos < tokens.size() ? tokens.get(pos) : null;
	}

	public JsonToken peekPrevious() {
		return pos - 1 < 0 ? null : tokens.get(pos - 2);
	}

	public JsonToken next() {
		return tokens.get(pos++);
	}

	public boolean hasMore() {
		return pos < tokens.size();
	}

	@Override
	public String toString() {
		return "TokenList{" +
				"tokens=" + tokens +
				'}';
	}

}
