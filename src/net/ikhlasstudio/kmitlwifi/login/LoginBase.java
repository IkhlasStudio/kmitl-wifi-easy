
package net.ikhlasstudio.kmitlwifi.login;

import java.util.HashMap;
import java.util.Map;

import net.ikhlasstudio.kmitlwifi.util.HttpHelper;
import net.ikhlasstudio.kmitlwifi.util.LoginResult;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

public abstract class LoginBase implements Loginable {

    protected static String TEST_URL = "http://www.google.co.th/";
    protected static String LOG_TAG = "LOG_TAG";

    protected Context context;
    protected Map<String, String> params = new HashMap<String, String>();;

    public LoginBase(Context context) {
        this.context = context;
    }

    public abstract String getLoginUrl();

    public abstract String getLogoutUrl();

    @Override
    public LoginResult login() {
        if (HttpHelper.getHttpStatus(TEST_URL) == 200) {
            return LoginResult.ALREADY;
        }

        readCredential();

        Log.v(LOG_TAG, "login");
        HttpRequest request = HttpHelper.post(getLoginUrl(), params);

        if (request == null) {
            return LoginResult.FAIL;
        }

        if (checkLoginResult(request.body())) {
            return LoginResult.SUCCESS;
        } else {
            return LoginResult.WRONGINFO;
        }
    }

    @Override
    public LoginResult logout() {
        if (HttpHelper.getHttpStatus(getLogoutUrl()) == 200) {
            return LoginResult.LOGOUT_SUCCESS;
        } else {
            return LoginResult.FAIL;
        }
    }

    protected void readCredential() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        params.put("username", sp.getString("username", ""));
        params.put("password", sp.getString("password", ""));
    }

    protected abstract boolean checkLoginResult(String pageSource);
    
    public abstract String getWiFiName();

    @Override
    public boolean testInternetAccess() {
        boolean testResult = HttpHelper.getHttpStatus(TEST_URL) == 200;
        if(!testResult){
            Log.v(LOG_TAG, "no internet connection");
        }
        
        return testResult;
    }
}
