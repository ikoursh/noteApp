package com.koursh.inbar.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.util.concurrent.Executor;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    private void authenticate(final Function<Boolean, Void> calback) {
        Executor executor = ContextCompat.getMainExecutor(this);
        boolean r;
        BiometricPrompt prompt = new BiometricPrompt(Splash.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
                calback.apply(false);

            }


            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Toast.makeText(getApplicationContext(), "Authentication succeeded",
                        Toast.LENGTH_SHORT)
                        .show();


                calback.apply(true);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
                calback.apply(false);
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setSubtitle("Please Log in using your biometric credential")
                .setNegativeButtonText("CANCEL")
                .build();

        prompt.authenticate(promptInfo);

    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean auth = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("auth", false);
        if (auth && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            authenticate(new Function<Boolean, Void>() {
                @Override
                public Void apply(Boolean input) {
                    if (input) {
                        unlock();
                    }
                    return null;
                }
            });
        } else {
            unlock();
        }
    }

    private void unlock() {
        Intent intent = new Intent(Splash.this, MainActivity.class);
        startActivity(intent);
    }

    public void tryAgain(View view) {
        authenticate(new Function<Boolean, Void>() {
            @Override
            public Void apply(Boolean input) {
                if (input) {
                    unlock();
                }
                return null;
            }
        });
    }
}