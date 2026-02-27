package com.zybooks.cs360_bradshaw_matthias.ui.settings;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.zybooks.cs360_bradshaw_matthias.R;

public class SmsFragment extends DialogFragment {

    private static final String PREF_SMS_ENABLED = "sms_notifications_enabled";

    private Button actionButton;
    private TextView statusText;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    public SmsFragment() {
        // required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (!isAdded()) return;

                    if (isGranted) {
                        // permission granted -> automatically enable alerts (so user gets what they wanted)
                        setSmsEnabledPref(true);

                        Toast.makeText(requireContext(),
                                "SMS permission granted. Alerts enabled.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // permission denied -> keep alerts off
                        setSmsEnabledPref(false);

                        Toast.makeText(requireContext(),
                                "SMS permission denied. Alerts disabled.",
                                Toast.LENGTH_SHORT).show();
                    }

                    updateUI();
                });
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_sms, container, false);


        actionButton = view.findViewById(R.id.button_request_permission);
        statusText = view.findViewById(R.id.text_permission_status);

        actionButton.setOnClickListener(v -> onActionButtonPressed());

        updateUI();
        return view;
    }

    private void onActionButtonPressed() {
        if (!isAdded()) return;

        boolean hasPermission = hasSmsPermission();
        boolean isEnabled = isSmsEnabledPref();

        if (!hasPermission) {
            // request permission
            requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
            return;
        }

        // permission already granted -> toggle alerts setting
        boolean newValue = !isEnabled;
        setSmsEnabledPref(newValue);

        Toast.makeText(requireContext(),
                newValue ? "SMS alerts enabled." : "SMS alerts disabled.",
                Toast.LENGTH_SHORT).show();

        updateUI();


    }

    private void updateUI() {
        if (!isAdded()) return;

        boolean hasPermission = hasSmsPermission();
        boolean enabled = isSmsEnabledPref();

        // status text
        StringBuilder sb = new StringBuilder();
        sb.append("Permission Status: ").append(hasPermission ? "GRANTED" : "NOT GRANTED");
        sb.append("\nSMS Alerts Setting: ").append(enabled ? "ENABLED" : "DISABLED");

        if (!hasPermission) {
            sb.append("\n\nTo enable SMS alerts, grant SMS permission.");
        } else {
            sb.append("\n\nYou can toggle SMS alerts on/off here.");
        }

        statusText.setText(sb.toString());

        // button label + enabled state
        if (!hasPermission) {
            actionButton.setEnabled(true);
            actionButton.setText("Request SMS Permission");
        } else {
            actionButton.setEnabled(true);
            actionButton.setText(enabled ? "Disable SMS Alerts" : "Enable SMS Alerts");
        }
    }

    private boolean hasSmsPermission() {
        if (!isAdded()) return false;
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isSmsEnabledPref() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        return prefs.getBoolean(PREF_SMS_ENABLED, false);
    }

    private void setSmsEnabledPref(boolean enabled) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        prefs.edit().putBoolean(PREF_SMS_ENABLED, enabled).apply();
    }
}