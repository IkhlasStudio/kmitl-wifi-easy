
package net.ikhlasstudio.kmitlwifi.util;

import java.util.Map;

import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

public class HttpHelper {
    private static String LOG_TAG = "HttpHelper";

    public static int getHttpStatus(String httpUrl) {
        HttpRequest request = get(httpUrl);

        if (request == null) {
            return -1;
        } else {
            Log.v(LOG_TAG + " getHttpStatus()", Integer.toString(request.code()));
            return request.code();
        }

    }

    public static HttpRequest get(String httpUrl) {
        HttpRequest request = null;
        try {
            request = HttpRequest.get(httpUrl);
            request.trustAllCerts();
            request.trustAllHosts();
            request.getConnection().setInstanceFollowRedirects(false);
            request.getConnection().setUseCaches(false);
        } catch (HttpRequestException e) {
            Log.e(LOG_TAG + " get()", e.getCause().getMessage());
        }

        return request;
    }

    public static HttpRequest post(String httpUrl, Map<String, String> params) {
        HttpRequest request = null;
        try {
            request = HttpRequest.post(httpUrl);
            request.trustAllCerts();
            request.trustAllHosts();
            request.getConnection().setInstanceFollowRedirects(false);
            request.getConnection().setUseCaches(false);
            request.form(params);
            request.created();
        } catch (HttpRequestException e) {
            Log.e(LOG_TAG + " post()", e.getCause().getMessage());
        }

        return request;

    }
}
