
package net.ikhlasstudio.kmitlwifi.receiver;

import net.ikhlasstudio.kmitlwifi.R;
import net.ikhlasstudio.kmitlwifi.activity.MainActivity;
import net.ikhlasstudio.kmitlwifi.login.LoginFactory;
import net.ikhlasstudio.kmitlwifi.login.Loginable;
import net.ikhlasstudio.kmitlwifi.util.LoginResult;
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

    protected static final String LOG_TAG = "ConnectivityActionReceiver";

    @Override
    public void onReceive(Context context, Intent arg1) {
        final Context mcontext = context;

        Log.v(LOG_TAG, "WiFi event");

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (sp.getBoolean("auto_wificonn", false) == false) {
            return;
        }

        final Loginable Loginner = LoginFactory.getInstance(mcontext);
        if(Loginner == null){
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {

                }
                Log.v(LOG_TAG, "logining to KMITL-WiFi");
                LoginResult result = Loginner.login();
                if (result == LoginResult.SUCCESS || result == LoginResult.ALREADY) {
                    Intent intent = new Intent(mcontext, MainActivity.class);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(mcontext)
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle("KMITL WiFi")
                                    .setContentText("KMITL WiFi Login success")
                                    .setTicker("KMITL WiFi Login success");

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
