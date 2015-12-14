package at.jku.se.decisiondocu.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.sql.Timestamp;

/**
 * Created by Benjamin on 17.11.2015.
 */
public class SaveSharedPreference
{
    private static final String PREF_USER_TOKEN = "token";
    private static final String PREF_USER_TOKEN_TIMESTAMP = "timestamp";
    private static final String PREF_USER_EMAIL = "email";
    private static final String PREF_USER_PASS = "password";

    public static final int RESULT_NOK = 0;
    public static final int RESULT_TOKEN_EXPIRED = 1;
    public static final int RESULT_OK = 2;

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserEmail(Context ctx, String userEmail)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_EMAIL, userEmail);
        Log.d("SharedPrefs", "setUserEmail(" + userEmail + ")");
        editor.commit();
    }

    public static void setUserToken(Context ctx, String userToken)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_TOKEN, userToken);
        Log.d("SharedPrefs", "setUserToken(" + userToken + ")");
        editor.commit();
    }

    public static void setUserTokenTimestamp(Context ctx, long userTokenTimestamp)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putLong(PREF_USER_TOKEN_TIMESTAMP, userTokenTimestamp);
        Log.d("SharedPrefs", "setUserTokenTimestamp(" + userTokenTimestamp + ")");
        editor.commit();
    }

    public static void setUserPassword(Context ctx, String userPassword)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_PASS, userPassword);
        Log.d("SharedPrefs", "setUserPassword(" + userPassword + ")");
        editor.commit();
    }

    public static long getUserTokenTimestamp(Context ctx)
    {
        return getSharedPreferences(ctx).getLong(PREF_USER_TOKEN_TIMESTAMP, 0);
    }

    public static String getUserEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "");
    }

    public static String getUserToken(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_TOKEN, null);
    }

    public static String getUserPass(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_PASS, null);
    }

    /**
     *
     * @param ctx
     */
    public static void logout(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }

    /**
     *
     * @param ctx
     * @return
     */
    public static int isValidUser(Context ctx){
        String email = getUserEmail(ctx);
        String pass = getUserPass(ctx);
        String token = getUserToken(ctx);
        long timestamp = getUserTokenTimestamp(ctx);
        if (timestamp <= 0 || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(token)) {
            return RESULT_NOK;
        }
        Timestamp original = new Timestamp(System.currentTimeMillis());
        Timestamp tokenTimestamp = new Timestamp(timestamp);
        Log.d("tokenTimestamp", tokenTimestamp.toString());
        if (original.after(tokenTimestamp)) {
            Log.d("tokenTimestamp", "abgelaufen!!");
            // TODO: 14.12.15 Logik, wenn Token abgelaufen ist überlegen!
            return RESULT_TOKEN_EXPIRED;
        }
        //TODO: Syntax überlegen wann kein Login notwending ist, DB-Abfrage, evt. zeitliche begrenzung
        return RESULT_OK;
    }
}
