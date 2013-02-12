
package net.ikhlasstudio.kmitlwifi.activity;

import net.ikhlasstudio.kmitlwifi.R;
import net.ikhlasstudio.kmitlwifi.login.LoginFactory;
import net.ikhlasstudio.kmitlwifi.login.Loginable;
import net.ikhlasstudio.kmitlwifi.util.LoginResult;
import net.ikhlasstudio.kmitlwifi.util.Util;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity implements OnClickListener {
    protected static final String LOG_TAG = "MainActivity";
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        Log.i(LOG_TAG, "MainActivity started");
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean autoOnStart = sp.getBoolean("auto_startapp", false);
        if (autoOnStart == true) {
            Log.i(LOG_TAG, "auto login when start");
            new LoginTask(this).execute(Loginable.DO_LOGIN);
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
    public void onClick(View v) {
        Button button = (Button) v;
        if (button.getText().equals(getResources().getString(R.string.login))) {
            new LoginTask(this).execute(Loginable.DO_LOGIN);
        } else {
            new LoginTask(this).execute(Loginable.DO_LOGOUT);
        }
    }

    private void updateButtonText() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Log.v(LOG_TAG, "updateButtonText");
                final Loginable loginner = LoginFactory.getInstance(MainActivity.this);
                
                if (loginner == null) {
                    return;
                }
                Log.v(LOG_TAG, "update button 's text");
                
                final boolean testResult = loginner.testInternetAccess();

                // runOnUiThread because we need to update UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (testResult) {
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
        private Loginable Loginner;

        public LoginTask(Context context) {
            Log.v(LOG_TAG, "LoginTask");
            this.context = context;
            Loginner = LoginFactory.getInstance(context);
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
            if(Loginner == null){
                return LoginResult.NOCONNECT_WIFI;
            }
            
            if (method[0] == Loginable.DO_LOGIN) {
                return Loginner.login();
            } else {
                return Loginner.logout();
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
