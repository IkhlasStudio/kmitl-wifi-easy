
package net.ikhlasstudio.kmitlwifi;

import java.util.HashMap;
import java.util.Map;

import net.ikhlasstudio.kmitwifi.util.LoginResult;
import net.ikhlasstudio.kmitwifi.util.Util;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

public class LoginManager {
    protected static final String LOG_TAG = "KWE-LoginManager";

    private String username;
    private String password;
    private static String testUrl = "http://www.kmitl.ac.th/";
    private String loginUrl = "https://securelogin.arubanetworks.com/cgi-bin/login";
    private String logoutUrl = "https://securelogin.arubanetworks.com/auth/logout.html";
    private Util util;

    public static int DO_LOGIN = 1;
    public static int DO_LOGOUT = 2;

    public LoginManager(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        username = sp.getString("username", "");
        password = sp.getString("password", "");
        util = new Util(context);
    }

    public LoginResult doLogin() {
        if (!util.isWifiConnect()) {
            return util.wifiState;
        }

        Log.i(LOG_TAG, "begin doLogin");

        int responseCode;
        // get current status
        Log.v(LOG_TAG, "get current status");
        responseCode = getHttpStatus();

        if (responseCode == 200) { // OK
            return LoginResult.ALREADY;
        }

        if (responseCode == 302) { // Moved Temporarily
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("password", password);

            HttpRequest request = HttpRequest.post(loginUrl);
            request.trustAllCerts();
            request.trustAllHosts();
            request.form(params);
            request.created();

            // checking login result
            if (checkResult(request.body()) == false) {
                return LoginResult.WRONGINFO;
            }

            // verifying login
            Log.v(LOG_TAG, "verify login");
            responseCode = getHttpStatus();
            if (responseCode != 200) {
                Log.d(LOG_TAG, "http status not 200");
                return LoginResult.FAIL;
            } else {
                return LoginResult.SUCCESS;
            }

        } else { // No 200 or 302
            Log.v(LOG_TAG, "http status not 302 it is " + responseCode);
            if (responseCode == -2) {
                return LoginResult.SOCKET_TIMEOUT;
            }

            return LoginResult.FAIL;
        }
    }

    public LoginResult doLogout() {
        if (!util.isWifiConnect()) {
            return util.wifiState;
        }
        Log.i(LOG_TAG, "begin doLogout");

        if (getHttpStatus(logoutUrl) == 200) {
            return LoginResult.LOGOUT_SUCCESS;
        } else {
            Log.i(LOG_TAG, "logout failed");
            return LoginResult.FAIL;
        }
    }

    public static int getHttpStatus() {
        return getHttpStatus(LoginManager.testUrl);
    }

    public static int getHttpStatus(String httpUrl) {
        int responseCode;
        try {
            HttpRequest request = HttpRequest.get(httpUrl);
            request.trustAllCerts();
            request.trustAllHosts();
            request.getConnection().setInstanceFollowRedirects(false);
            request.getConnection().setUseCaches(false);
            responseCode = request.code();
        } catch (HttpRequestException e) {
            responseCode = -1;
            Log.e(LOG_TAG, e.getCause().getMessage());
        }

        Log.v(LOG_TAG, "getHttpStatus " + httpUrl + " response code is " + responseCode);
        return responseCode;
    }

    public boolean checkResult(String body) {
        if (body.contains("Authentication failed")
                || body.contains("Access denied")) {
            return false;
        } else {
            return true;
        }
    }
}
