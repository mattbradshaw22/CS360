package com.zybooks.cs360_bradshaw_matthias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zybooks.cs360_bradshaw_matthias.model.Inventory;
import com.zybooks.cs360_bradshaw_matthias.repo.InventoryRepository;

public class MainPageFragment extends Fragment implements InventoryDialogFragment.OnAddInventoryListener {

    private InventoryAdapter adapter;
    private InventoryRepository repo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repo = InventoryRepository.getInstance(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new InventoryAdapter(repo.getInventoryList(), (item, newQty) -> {
            //TODO change to use DB here
        });

        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            new InventoryDialogFragment().show(getParentFragmentManager(), "InventoryDialog");
        });
    }

    @Override
    public void onAddInventory(String name, int quantity) {
        // Create new Inventory item (matches the model constructor Inventory(String, int))
        Inventory inv = new Inventory(name, quantity);

        // Give it an ID (auto-increment)
        long newId = repo.getInventoryList().size() + 1;
        inv.setmId(newId);

        repo.addInventory(inv);

        // Refresh the list
        adapter.notifyItemInserted(repo.getInventoryList().size() - 1);
    }
}