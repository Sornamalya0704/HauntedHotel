package com.ctf.flags;

import android.util.Base64;

public final class Base64EncodedFlag {
	public static final String ENCODED = "RkxBR3tnaG9zdF9iYXNlNjR9";

	private Base64EncodedFlag() {
	}

	public static String decode() {
		byte[] decoded = Base64.decode(ENCODED, Base64.DEFAULT);
		return new String(decoded);
	}
}

