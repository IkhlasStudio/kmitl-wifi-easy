
package net.ikhlasstudio.kmitlwifi.activity;

import net.ikhlasstudio.kmitlwifi.R;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

public class PreferencesActivity extends SherlockPreferenceActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
