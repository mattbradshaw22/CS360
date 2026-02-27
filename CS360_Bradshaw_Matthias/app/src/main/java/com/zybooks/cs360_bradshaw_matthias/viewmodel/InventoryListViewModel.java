package com.zybooks.cs360_bradshaw_matthias.viewmodel;
import android.app.Application;
import com.zybooks.cs360_bradshaw_matthias.model.Inventory;
import com.zybooks.cs360_bradshaw_matthias.repo.InventoryRepository;
import java.util.List;

public class InventoryListViewModel {
    private  InventoryRepository inventoryRepo;

    public InventoryListViewModel(Application application) {
        inventoryRepo = InventoryRepository.getInstance(application.getApplicationContext());
    }

    public List<Inventory> getInventoryList() {
        return inventoryRepo.getmInventoryList();
    }

    public void addInventory(Inventory inventory) {
        inventoryRepo.addInventory(inventory);
    }
}
