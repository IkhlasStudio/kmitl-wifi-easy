
package net.ikhlasstudio.kmitwifi.util;

import java.util.Map;

import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

public class HttpHelper {
    private static String LOG_TAG = "HttpHelper";

    public static int getHttpStatus(String httpUrl) {
        HttpRequest request= get(httpUrl);
        
        if(request == null){
            return -1;
        }else{
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
            Log.e(LOG_TAG, e.getCause().getMessage());
        }

        return request;
    }

    public static HttpRequest post(String httpUrl, Map<String, String> params) {
        HttpRequest request = null;
        try {
            request = HttpRequest.post(httpUrl);
            request.trustAllCerts();
            request.trustAllHosts();
            request.form(params);
            request.created();
        } catch (HttpRequestException e) {
            Log.e(LOG_TAG, e.getCause().getMessage());
        }

        return request;

    }
}
