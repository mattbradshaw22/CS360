package com.zybooks.cs360_bradshaw_matthias.ui.inventory;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import android.content.SharedPreferences;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zybooks.cs360_bradshaw_matthias.R;
import com.zybooks.cs360_bradshaw_matthias.data.InventoryDataSource;
import com.zybooks.cs360_bradshaw_matthias.model.InventoryItem;
import com.zybooks.cs360_bradshaw_matthias.ui.settings.SmsFragment;

import java.util.ArrayList;
import java.util.List;


/* MainPageFragment displays the main inventory screen
* loads SQLite inventory items and displays in recycler view
* allows user to add items with the floating action button
* allows user to remove radio button selected item with the remove button
* updates the out of stock text view when an item quantity is 0
 */

public class MainPageFragment extends Fragment implements InventoryDialogFragment.OnAddInventoryListener {

    private InventoryAdapter adapter;
    // private InventoryRepository repo;

    private TextView alertText;

    private InventoryDataSource inventoryDataSource;
    private final List<InventoryItem> items = new ArrayList<>();






    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // text view alert for 0 quantity items
        alertText = view.findViewById(R.id.textViewAlertText);
        alertText.setVisibility(View.GONE);




        // initialize sqlite db
        // create the db helper used for CRUD operations
        inventoryDataSource = new InventoryDataSource(requireContext());

        // load SQLite items into the list
        items.clear();
        items.addAll(inventoryDataSource.getAllItems());

        // if the list is empty seed some stater items for testing
        if (items.isEmpty()) {
            seedStarterItems();
            items.clear();
            items.addAll(inventoryDataSource.getAllItems());
        }

        outOfStockCheck(alertText);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Create adapter. When +/- is pressed, adapter calls this callback.
        // update SQLite and then re-check out of stock status.
        adapter = new InventoryAdapter(items, (item, newQty) -> {
            inventoryDataSource.updateQuantity(item.id, newQty);
            outOfStockCheck(alertText);
            checkStockAndNotify(item.name, item.quantity);

        });

        recyclerView.setAdapter(adapter);

        //remove button to remove selected item
        Button btnRemoveItem = view.findViewById(R.id.btnRemoveItem);
        btnRemoveItem.setOnClickListener(v -> {
            long selectedId = adapter.getSelectedId();

            if (selectedId == -1) {
                Toast.makeText(getContext(), "No item selected", Toast.LENGTH_SHORT).show();
                return;
            }
            int rowsDeleted = inventoryDataSource.deleteItem(selectedId);
            if (rowsDeleted == 0) {
                Toast.makeText(getContext(), "Error deleting item", Toast.LENGTH_SHORT).show();
                return;
            }
            if (rowsDeleted > 0) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).id == selectedId) {
                        items.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
                adapter.clearSelection();
                outOfStockCheck(alertText);
                Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error deleting item", Toast.LENGTH_SHORT).show();
            }
        });

        // FAB to add items opens add item dialog
        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            new InventoryDialogFragment().show(getChildFragmentManager(), "InventoryDialog");
        });
    }

    private void outOfStockCheck(TextView alertText) {

        // check for out of stock items, display alert text view if any
        for (InventoryItem item : items) {
            if (item.quantity == 0) {
                alertText.setText(item.name + " is out of stock!");
                alertText.setVisibility(View.VISIBLE);
                return;
            }
        }
        alertText.setVisibility(View.GONE);
    }

    private void checkStockAndNotify(String name, int quantity) {
        if (quantity == 0) {
            // check if the user ENABLED SMS in the Settings Screen
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
            boolean isSmsEnabled = prefs.getBoolean("sms_notifications_enabled", false);

            boolean hasPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED;

            if (!isSmsEnabled || !hasPermission) {
                com.zybooks.cs360_bradshaw_matthias.ui.settings.SmsFragment dialog =
                        new com.zybooks.cs360_bradshaw_matthias.ui.settings.SmsFragment();
                dialog.show(getChildFragmentManager(), "SmsPermissionDialog");
                return;
            }
            sendSmsAlert(name);
            }
        }


    private void sendSmsAlert(String itemName) {
        String message = "Alert: " + itemName + " is out of stock!";
        String phoneNumber = "5551234567";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getContext(), "SMS Alert Sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "SMS Failed", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onAddInventory(String name, int quantity) {
        // validate input and add to database
        String cleanName = (name == null) ? "" : name.trim();
        if (cleanName.isEmpty()) {
            Toast.makeText(getContext(), "Name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        int cleanQuantity = Math.max(0, quantity);
        // insert new item into SQLite Db
        long newId = inventoryDataSource.insertItem(cleanName, cleanQuantity);

        if (newId == -1) {
            Toast.makeText(getContext(), "Error adding item", Toast.LENGTH_SHORT).show();
            return;
        }
        InventoryItem newItem = new InventoryItem(newId, cleanName, cleanQuantity);
        items.add(newItem);
        adapter.notifyItemInserted(items.size() - 1);
        outOfStockCheck(alertText);
    }

    private void seedStarterItems() {
        // seed some starter items for testing
        inventoryDataSource.insertItem("Item A", 10);
        inventoryDataSource.insertItem("Item B", 5);
        inventoryDataSource.insertItem("Item C", 7);
    }



}