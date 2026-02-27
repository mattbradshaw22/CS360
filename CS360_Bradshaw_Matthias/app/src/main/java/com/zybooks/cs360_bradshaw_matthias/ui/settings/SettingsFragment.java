package com.zybooks.cs360_bradshaw_matthias.ui.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.zybooks.cs360_bradshaw_matthias.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    // This handles the OS popup result and updates the switch
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                SwitchPreferenceCompat smsSwitch = findPreference("sms_notifications_enabled");
                if (isGranted) {
                    if (smsSwitch != null) smsSwitch.setChecked(true);
                    Toast.makeText(requireContext(), "SMS Notifications Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    if (smsSwitch != null) smsSwitch.setChecked(false);
                    Toast.makeText(requireContext(), "Permission Denied. Notifications disabled.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // 1. Load the preferences from XML
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        // 2. Handle the SMS Switch
        SwitchPreferenceCompat smsSwitch = findPreference("sms_notifications_enabled");
        if (smsSwitch != null) {
            smsSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isEnabled = (boolean) newValue;
                if (isEnabled) {
                    // Check for permission when user tries to enable
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Launch the permission dialog
                        requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
                        return false; // Wait for the permission result before toggling
                    }
                }
                return true; // Allow toggle if turning off or already granted
            });
        }

        // 3. Handle the Test SMS button (if you have one in your XML)
        Preference testSmsPref = findPreference("test_sms");
        if (testSmsPref != null) {
            testSmsPref.setOnPreferenceClickListener(preference -> {
                sendTestSms();
                return true;
            });
        }
    }

    private void sendTestSms() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                String phoneNumber = "5551234567"; // Generic emulator number
                String message = "INVENtracker: This is a test notification.";
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(requireContext(), "Test SMS Sent!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(requireContext(), "SMS Permission not granted.", Toast.LENGTH_SHORT).show();
        }
    }
}