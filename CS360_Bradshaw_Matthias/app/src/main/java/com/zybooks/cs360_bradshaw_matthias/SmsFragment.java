package com.zybooks.cs360_bradshaw_matthias;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import androidx.fragment.app.Fragment;

public class SmsFragment extends Fragment {

    private TextView permissionStatusText;
    private Button sendSmsButton;
    private Button requestPermissionButton;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    updateUI();
                    Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    updateUI();
                    Toast.makeText(requireContext(), "Permission Denied. Notifications disabled.", Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms, container, false);

        permissionStatusText = view.findViewById(R.id.text_permission_status);
        sendSmsButton = view.findViewById(R.id.button_send_sms);
        requestPermissionButton = view.findViewById(R.id.button_request_permission);

        requestPermissionButton.setOnClickListener(v -> requestSmsPermission());

        sendSmsButton.setOnClickListener(v -> sendNotificationSms());

        updateUI();

        return view;
    }

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
            updateUI();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
        }
    }

    private void updateUI() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            permissionStatusText.setText("Status: SMS Notifications Enabled");
            sendSmsButton.setVisibility(View.VISIBLE);
            requestPermissionButton.setText("Permission Granted");
            requestPermissionButton.setEnabled(false);
        } else {
            permissionStatusText.setText("Status: SMS Notifications Disabled");
            sendSmsButton.setVisibility(View.GONE);
            requestPermissionButton.setText("Enable SMS Notifications");
            requestPermissionButton.setEnabled(true);
        }
    }

    private void sendNotificationSms() {
        try {
            // Using standard SmsManager
            SmsManager smsManager = SmsManager.getDefault();

            String phoneNumber = "5551234567"; 
            String message = "INVENtracker Alert: Low inventory detected for 'Item ABC'.";
            
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(requireContext(), "Test Notification Sent!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
