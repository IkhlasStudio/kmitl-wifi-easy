
package net.ikhlasstudio.kmitlwifi;

import net.ikhlasstudio.kmitwifi.util.LoginResult;
import net.ikhlasstudio.kmitwifi.util.Util;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class ConnectivityActionReceiver extends BroadcastReceiver {
    protected static final String LOG_TAG = "KWE-ConnectivityActionReceiver";
    private Util util;

    @Override
    public void onReceive(Context context, Intent arg1) {
        final Context mcontext = context;

        Log.v(LOG_TAG, "WiFi event");

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (sp.getBoolean("auto_wificonn", false) == false) {
            return;
        }

        util = new Util(context);
        if (!util.isWifiConnect()) {
            return;
        }
        Log.i(LOG_TAG, "KMITL-WiFi connected");

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {

                }
                Log.v(LOG_TAG, "logining to KMITL-WiFi");
                LoginResult rest = new LoginManager(mcontext).doLogin();
                if (rest == LoginResult.SUCCESS || rest == LoginResult.ALREADY) {
                    Intent intent = new Intent(mcontext, MainActivity.class);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(mcontext)
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle("KMITL WiFi")
                                    .setContentText("KMITL WiFi Login success");

                    PendingIntent pending = TaskStackBuilder.create(mcontext)
                            .addParentStack(MainActivity.class)
                            .addNextIntent(intent)
                            .getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

                    mBuilder.setContentIntent(pending);
                    mBuilder.setAutoCancel(true);
                    NotificationManager mNotificationManager =
                            (NotificationManager) mcontext
                                    .getSystemService(Context.NOTIFICATION_SERVICE);
                    // mId allows you to update the notification later on.
                    mNotificationManager.notify(1234, mBuilder.build());
                    Log.i(LOG_TAG, "notified");
                }
            }
        }).start();
    }

}
