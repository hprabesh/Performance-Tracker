package templates_and_keys;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String SHARED_PREF_NAME = "login_session";
    String SESSION_KEY_USERNAME = "user_email";
    String SESSION_KEY_PWD = "user_password";

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String userEmail, String userpassword) {
        editor.putString(SESSION_KEY_USERNAME, userEmail);
        editor.putString(SESSION_KEY_PWD, userpassword);
        editor.commit();
    }

    public String[] getSession() {
        String userEmail = sharedPreferences.getString(SESSION_KEY_USERNAME, null);
        String userPassword = sharedPreferences.getString(SESSION_KEY_PWD, null);
        String[] returnPacket = {userEmail, userPassword};
        return returnPacket;
    }

    public void clearSession() {
        this.saveSession(null, null);
    }

}
