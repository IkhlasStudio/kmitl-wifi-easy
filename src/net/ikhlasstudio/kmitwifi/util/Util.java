
package net.ikhlasstudio.kmitwifi.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class Util {
    private Context context;
    public LoginResult wifiState;

    public Util(Context context) {
        this.context = context;
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public String _(LoginResult result) {
        String message;
        switch (result) {
            case NOCONNECT_WIFI:
                message = "No WiFi connection";
                break;
            case NOKMITL_WIFI:
                message = "Not connect to KMITL-WiFi";
                break;
            case WRONGINFO:
                message = "Wrong username and/or password";
                break;
            case ALREADY:
                message = "Already login";
                break;
            case FAIL:
                message = "System failed please try again";
                break;
            case SOCKET_TIMEOUT:
                message = "Connection timeout please try again";
                break;
            case LOGOUT_SUCCESS:
                message = "Logout success";
                break;
            default:
                message = "KMITL WiFi Login success";
                break;
        }
        ;

        return message;
    }

    public boolean isWifiConnect() {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (ssid == null) {
            wifiState = LoginResult.NOCONNECT_WIFI;
            return false;
        } else if (!ssid.startsWith("KMITL-WiFi") || !networkInfo.isConnected()) {
            wifiState = LoginResult.NOKMITL_WIFI;
            return false;
        }

        return true;
    }
}
