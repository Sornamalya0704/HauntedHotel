package com.ctf.flags;

import android.os.Debug;

public final class FridaHookFlag {
	public static final String RUNTIME_FLAG = "FLAG{frida_hook_acquired}";

	private FridaHookFlag() {
	}

	public static String getRuntimeFlag() {
		if (isDebuggerAttached() || isFridaLikelyPresent()) {
			return RUNTIME_FLAG;
		}
		return "NO_FLAG";
	}

	private static boolean isDebuggerAttached() {
		return Debug.isDebuggerConnected() || Debug.waitingForDebugger();
	}

	private static boolean isFridaLikelyPresent() {
		try {
			ClassLoader cl = FridaHookFlag.class.getClassLoader();
			if (cl != null) {
				try {
					cl.loadClass("re.frida.server");
					return true;
				} catch (Throwable ignored) {
				}
				try {
					cl.loadClass("com.frida.Hook");
					return true;
				} catch (Throwable ignored) {
				}
			}
		} catch (Throwable ignored) {
		}
		return false;
	}
}

