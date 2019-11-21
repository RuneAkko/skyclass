package com.android.publiccourse;

public class BarException extends RuntimeException {
	private int statusCode = -1;
	private static final long serialVersionUID = -2623309261327598087L;

	public BarException(String msg) {
		super(msg);
	}

	public BarException(Exception cause) {
		super(cause);
	}

	public BarException(String msg, int statusCode) {
		super(msg);
		this.statusCode = statusCode;

	}

	public BarException(String msg, Exception cause) {
		super(msg, cause);
	}

	public BarException(String msg, Exception cause, int statusCode) {
		super(msg, cause);
		this.statusCode = statusCode;

	}

	public int getStatusCode() {
		return this.statusCode;
	}
}
