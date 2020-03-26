package io.github.needle.http;

import io.github.kits.Strings;

public class HttpResponse {

	private int status;
	private String statusText;

	public void setStatus(int status) {
		this.status = status;
		String text = HttpStatus.text(status);
		this.statusText = Strings.isBlack(text) ? "Invalid Status" : text;
	}

	public int getStatus() {
		return status;
	}

	public String getStatusText() {
		return statusText;
	}

}
