package com.koursh.inbar.notes;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.biometric.BiometricManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        final SwitchPreference switchPreferenceCompat = findPreference("auth");


        switchPreferenceCompat.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                String err = null;
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.P) {
                    err = "Please update to android P or higher to enable this feature";
                }
                Intent enrollIntent = null;

                BiometricManager biometricManager = BiometricManager.from(getContext());
                switch (biometricManager.canAuthenticate()) {
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        err = "No biometric hardware available";
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        err = "Biometric features are currently unavailable";
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        err = "No fingerprint is currently enrolled";
                        // Prompts the user to create credentials that your app accepts.
                        enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                        break;
                }

                if (err != null) {
                    switchPreferenceCompat.setEnabled(false);
                    switchPreferenceCompat.setChecked(false);
                }


                if (err != null) {
                    Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                    if (enrollIntent != null)
                        startActivity(enrollIntent);
                }
                return false;
            }
        });

    }


}