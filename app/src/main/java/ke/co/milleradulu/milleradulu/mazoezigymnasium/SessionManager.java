package ke.co.milleradulu.milleradulu.mazoezigymnasium;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import ke.co.milleradulu.milleradulu.mazoezigymnasium.members.LoginActivity;

public class SessionManager {

  SharedPreferences sharedPrefs;
  SharedPreferences.Editor editor;
  Context _context;
  int PRIVATE_MODE = 0;
  private static final String PREF_NAME = "MazoeziPref";
  private static final String IS_LOGGED_IN = "IsLoggedIn";
  public static final  String KEY_MEMBER_ID = "0";
  public static final String KEY_NAME = "Mazoezi";
  public static final String KEY_EMAIL = "mazoezi@mazoezi.com";

  @SuppressLint("CommitPrefEdits")
  public SessionManager(Context context) {
    this._context = context;
    sharedPrefs = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    editor = sharedPrefs.edit();
  }

  public void createLoginSession(String memberId, String name, String email) {
    editor.putString(KEY_MEMBER_ID, memberId);
    editor.putString(KEY_NAME, name);
    editor.putString(KEY_EMAIL, email);
    editor.putBoolean(IS_LOGGED_IN, true);

    editor.commit();
  }

  public HashMap<String, String> getMemberDetails() {
    HashMap<String, String> member = new HashMap<>();
    member.put(KEY_MEMBER_ID, sharedPrefs.getString(KEY_MEMBER_ID, "0"));
    member.put(KEY_NAME, sharedPrefs.getString(KEY_NAME, null));
    member.put(KEY_EMAIL, sharedPrefs.getString(KEY_EMAIL, null));
    return member;
  }

  public void checkLogin() {
    if(!this.isLoggedIn()) {
      this.clearApp();
    }
  }

  public void logOutMember() {
    editor.clear();
    editor.commit();
    this.clearApp();
  }

  public boolean isLoggedIn() {
    return sharedPrefs.getBoolean(IS_LOGGED_IN, false);
  }

  private void clearApp() {
    Intent intent = new Intent(_context, LoginActivity.class);

    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    _context.startActivity(intent);
  }
}
