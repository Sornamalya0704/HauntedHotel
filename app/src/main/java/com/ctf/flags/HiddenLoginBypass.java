package com.ctf.flags;

public final class HiddenLoginBypass {
	public static final String BYPASS_FLAG = "FLAG{ghost_login_unlocked}";

	private HiddenLoginBypass() {
	}

	public static String checkCredentials(String username, String password) {
		if ("admin".equals(username) && "1234".equals(password)) {
			return "OK";
		} else if ("ghost".equals(username)) {
			return BYPASS_FLAG;
		}
		return null;
	}
}

