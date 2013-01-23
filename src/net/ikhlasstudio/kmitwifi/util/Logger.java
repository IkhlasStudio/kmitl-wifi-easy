
package net.ikhlasstudio.kmitwifi.util;

import android.util.Log;

public class Logger {

    public static void i(String message) {
        Log.i("KWE", message);
    }

    public static void i(int message) {
        i(String.valueOf(message));
    }

}
