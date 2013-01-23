
package net.ikhlasstudio.kmitlwifi;

import net.ikhlasstudio.kmitwifi.util.Logger;
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

public class ConnectivityActionReceiver extends BroadcastReceiver {
    private Util util;

    @Override
    public void onReceive(Context context, Intent arg1) {
        final Context mcontext = context;

        Logger.i("WiFi event");

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (sp.getBoolean("auto_wificonn", false) == false) {
            return;
        }

        util = new Util(context);
        if (!util.isWifiConnect()) {
            return;
        }
        Logger.i("KMITL-WiFi connected");

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {

                }
                Logger.i("logining to KMITL-WIFI");
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
                    Logger.i("notified");
                }
            }
        }).start();
    }

}
