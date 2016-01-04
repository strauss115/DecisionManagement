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
    // Konstanten

    private static final String PREF_USER_TOKEN = "token";
    private static final String PREF_USER_TOKEN_TIMESTAMP = "timestamp";
    private static final String PREF_USER_EMAIL = "email";
    private static final String PREF_USER_PASS = "password";

    // Possible Return Values

    public static final int RESULT_NOK = 0;
    public static final int RESULT_TOKEN_EXPIRED = 1;
    public static final int RESULT_OK = 2;

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    /**
     * Set user's email address
     * @param ctx
     * @param userEmail user's email to save
     */
    public static void setUserEmail(Context ctx, String userEmail)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_EMAIL, userEmail);
        Log.d("SharedPrefs", "setUserEmail(" + userEmail + ")");
        editor.commit();
    }

    /**
     * Set user's token
     * @param ctx
     * @param userToken user's token to save
     */
    public static void setUserToken(Context ctx, String userToken)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_TOKEN, userToken);
        Log.d("SharedPrefs", "setUserToken(" + userToken + ")");
        editor.commit();
    }

    /**
     * Set user's token timestamp
     * @param ctx
     * @param userTokenTimestamp user's token timestamp to save
     */
    public static void setUserTokenTimestamp(Context ctx, long userTokenTimestamp)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putLong(PREF_USER_TOKEN_TIMESTAMP, userTokenTimestamp);
        Log.d("SharedPrefs", "setUserTokenTimestamp(" + userTokenTimestamp + ")");
        editor.commit();
    }

    /**
     * Set user's password
     * @param ctx
     * @param userPassword user's password to save
     */
    public static void setUserPassword(Context ctx, String userPassword)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_PASS, userPassword);
        Log.d("SharedPrefs", "setUserPassword(" + userPassword + ")");
        editor.commit();
    }

    /**
     * Get user's token timestamp
     * @param ctx
     * @return saved timestamp or 0 if invalid
     */
    public static long getUserTokenTimestamp(Context ctx)
    {
        return getSharedPreferences(ctx).getLong(PREF_USER_TOKEN_TIMESTAMP, 0);
    }

    /**
     * Get user's email
     * @param ctx
     * @return saved email or empty string if no email was saved
     */
    public static String getUserEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "");
    }

    /**
     * Get user's token
     * @param ctx
     * @return saved token or null if no token was saved
     */
    public static String getUserToken(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_TOKEN, null);
    }

    /**
     * Get user's password
     * @param ctx
     * @return saved password or null if no password was saved
     */
    public static String getUserPass(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_PASS, null);
    }

    /**
     * Clears all user information
     * @param ctx
     */
    public static void logout(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }

    /**
     * Check if a user is still valid
     * @param ctx
     * @return RESULT_NOK if user is invalid, RESULT_TOKEN_EXPIRED if token is expired, RESULT_OK if user should be valid
     */
    public static int isValidUser(Context ctx){
        String email   = getUserEmail(ctx);
        String pass    = getUserPass(ctx);
        String token   = getUserToken(ctx);
        long timestamp = getUserTokenTimestamp(ctx);

        if (timestamp <= 0 || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(token)) {
            return RESULT_NOK;
        }

        Timestamp original = new Timestamp(System.currentTimeMillis());
        Timestamp tokenTimestamp = new Timestamp(timestamp);
        Log.d("tokenTimestamp", tokenTimestamp.toString());

        if (original.after(tokenTimestamp)) {
            Log.d("tokenTimestamp", "abgelaufen!!");
            return RESULT_TOKEN_EXPIRED;
        }
        else {
            Log.d("tokenTimestamp", "noch gültig!!");
        }

        return RESULT_OK;
    }
}
