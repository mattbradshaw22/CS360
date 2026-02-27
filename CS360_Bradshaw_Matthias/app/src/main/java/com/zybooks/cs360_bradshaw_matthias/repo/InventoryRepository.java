package com.zybooks.cs360_bradshaw_matthias.repo;

import android.content.Context;
import com.zybooks.cs360_bradshaw_matthias.model.Inventory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryRepository {
    private static InventoryRepository mInventoryRepository;

    private final List<Inventory> mInventoryList;
    private final HashMap<Long, Inventory> mInventoryMap;
    public static InventoryRepository getInstance(Context context) {
        if (mInventoryRepository == null) {
            mInventoryRepository = new InventoryRepository(context);
        }
        return mInventoryRepository;
    }
    private InventoryRepository(Context context) {
        mInventoryList = new ArrayList<>();
        mInventoryMap = new HashMap<>();

        addStarterData ();
        }

        private void addStarterData() {
            Inventory inventory = new Inventory("Item A", 10);
            inventory.setmId(1);
            addInventory(inventory);

            inventory = new Inventory("Item B", 5);
            inventory.setmId(2);
            addInventory(inventory);

            inventory = new Inventory("Item C", 7);
            inventory.setmId(3);
            addInventory(inventory);

            inventory = new Inventory("Item D", 2);
            inventory.setmId(4);
            addInventory(inventory);

            inventory = new Inventory("Item E", 8);
            inventory.setmId(5);
            addInventory(inventory);

            inventory = new Inventory("Item F", 3);
            inventory.setmId(6);
            addInventory(inventory);

            inventory = new Inventory("Item G", 6);
            inventory.setmId(7);
            addInventory(inventory);

            inventory = new Inventory("Item H", 4);
            inventory.setmId(8);
            addInventory(inventory);

            inventory = new Inventory("Item I", 9);
            inventory.setmId(9);
            addInventory(inventory);

            inventory = new Inventory("Item J", 1);
            inventory.setmId(10);
            addInventory(inventory);

            inventory = new Inventory("Item K", 11);
            inventory.setmId(11);
            addInventory(inventory);

            inventory = new Inventory("Item L", 12);
            inventory.setmId(12);
            addInventory(inventory);

            inventory = new Inventory("Item M", 13);
            inventory.setmId(13);
            addInventory(inventory);

            inventory = new Inventory("Item N", 14);
            inventory.setmId(14);
            addInventory(inventory);


        }

        public void addInventory(Inventory inventory) {
            mInventoryList.add(inventory);
            mInventoryMap.put(inventory.getmId(), inventory);
        }

    public List<Inventory> getmInventoryList() {
        return mInventoryList;
    }

    public Inventory getInventoryByID(long id) {
        return mInventoryMap.get(id);
    }

    public List<Inventory> getInventoryList() {
        return mInventoryList;
    }
}
