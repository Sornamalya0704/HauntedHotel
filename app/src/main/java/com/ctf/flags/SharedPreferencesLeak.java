package com.ctf.flags;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreferencesLeak {
	public static final String PREFS_NAME = "ctf_prefs";
	public static final String PREF_KEY = "ctf_flag_4";
	public static final String PREF_FLAG_VALUE = "FLAG{shared_prefs_exposed}";

	private SharedPreferencesLeak() {
	}

	public static void writeFlag(Context context) {
		if (context == null) {
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(PREF_KEY, PREF_FLAG_VALUE).apply();
	}

	public static String readFlag(Context context) {
		if (context == null) {
			return null;
		}
		SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return sp.getString(PREF_KEY, null);
	}
}

