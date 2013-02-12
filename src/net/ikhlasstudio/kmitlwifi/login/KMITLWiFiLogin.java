package net.ikhlasstudio.kmitlwifi.login;

import android.content.Context;

public class KMITLWiFiLogin extends LoginBase {

    public KMITLWiFiLogin(Context context) {
        super(context);
    }

    @Override
    public String getLoginUrl() {
        return "https://securelogin.arubanetworks.com/cgi-bin/login";
    }

    @Override
    public String getLogoutUrl() {
        return "https://securelogin.arubanetworks.com/auth/logout.html";
    }

    @Override
    protected boolean checkLoginResult(String pageSource) {
        if (pageSource.contains("Authentication failed")
                || pageSource.contains("Access denied")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getWiFiName() {
        return "KMITL-WiFi";
    }

}
