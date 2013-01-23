
package net.ikhlasstudio.kmitlwifi;

import net.ikhlasstudio.kmitwifi.util.Logger;
import net.ikhlasstudio.kmitwifi.util.LoginResult;
import net.ikhlasstudio.kmitwifi.util.Util;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity implements OnClickListener {
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        Logger.i("MainActivity started");
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean autoOnStart = sp.getBoolean("auto_startapp", false);
        if (autoOnStart == true) {
            Logger.i("auto login when start");
            new LoginTask(this).execute(LoginManager.DO_LOGIN);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateButtonText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);

        return true;
    }

    public void onMenuClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        if (button.getText().equals(getResources().getString(R.string.login))) {
            new LoginTask(this).execute(LoginManager.DO_LOGIN);
        } else {
            new LoginTask(this).execute(LoginManager.DO_LOGOUT);
        }
    }

    private void updateButtonText() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if ((new Util(MainActivity.this).isWifiConnect()) == false) {
                    return;
                }
                Logger.i("update button 's text");
                final int responseCode = LoginManager.getHttpStatus();

                // runOnUiThread because we need to update UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseCode == 200) {
                            loginButton.setText(R.string.logout);
                        } else {
                            loginButton.setText(R.string.login);
                        }
                    }
                });
            }
        }).start();
    }

    class LoginTask extends AsyncTask<Integer, Integer, LoginResult> {
        private Context context;
        private ProgressDialog progressDialog;

        public LoginTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Processing");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected LoginResult doInBackground(Integer... method) {
            if (method[0] == LoginManager.DO_LOGIN) {
                return new LoginManager(context).doLogin();
            } else {
                return new LoginManager(context).doLogout();
            }
        }

        @Override
        protected void onPostExecute(LoginResult result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Util util = new Util(context);
            util.showToast(util._(result));
            updateButtonText();
        }
    }
}
