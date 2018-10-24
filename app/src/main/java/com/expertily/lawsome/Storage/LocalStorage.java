package com.expertily.lawsome.Storage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalStorage {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public TinyDB db;
    public static final String PREF_NAME = "SOS by Expertily";
    public static final String MOBILE = "m";
    public static final String NAME = "n";
    public static final String LOGIN = "l";
    public static final String CASES = "c";

    public static final ArrayList<String> case_titles = new ArrayList<>();

    public LocalStorage(Context context) {
        this._context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        db = new TinyDB(_context);
    }

    public void loginSession(String name, String mobile) {
        editor.putString(NAME, name);
        editor.putString(MOBILE, mobile);
        editor.putBoolean(LOGIN, true);
        editor.commit();
    }

    public void addCase() {
        editor.putBoolean(CASES, true);
        editor.commit();
    }

    public void logout() {
        db.clear();
        editor.clear();
        editor.commit();
    }

    public boolean dashboardIn() {
        return pref.getBoolean(LOGIN, false);
    }
    public boolean hasCases() {
        return pref.getBoolean(CASES, false);
    }

}