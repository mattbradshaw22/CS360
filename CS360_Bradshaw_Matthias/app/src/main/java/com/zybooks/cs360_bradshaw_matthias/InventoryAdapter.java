package com.zybooks.cs360_bradshaw_matthias;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.cs360_bradshaw_matthias.R;
import com.zybooks.cs360_bradshaw_matthias.model.Inventory;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryVH> {

    public interface OnQuantityChangedListener {
        void onQuantityChanged(Inventory item, int newQty);
    }

    private final List<Inventory> items;
    private final OnQuantityChangedListener listener;

    private long selectedId = -1;

    public InventoryAdapter(List<Inventory> items, OnQuantityChangedListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InventoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_inventory, parent, false);
        return new InventoryVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryVH holder, int position) {
        Inventory item = items.get(position);

        holder.textName.setText(item.getmName());
        holder.textQty.setText(String.valueOf(item.getmQuantity()));

        // set checked state
        holder.radioSelect.setChecked(item.getmId() == selectedId);

        holder.radioSelect.setOnClickListener(v -> {
            if (selectedId == item.getmId()) {
                // Uncheck if same item tapped again
                selectedId = -1;
            } else {
                // Select new item
                selectedId = item.getmId();
            }
            notifyDataSetChanged(); // refresh all radios
        });

        holder.buttonMinus.setOnClickListener(v -> {
            int current = item.getmQuantity();
            int newQty = Math.max(0, current - 1);
            item.setmQuantity(newQty);
            notifyItemChanged(holder.getAdapterPosition());

            if (listener != null) listener.onQuantityChanged(item, newQty);
        });

        holder.buttonPlus.setOnClickListener(v -> {
            int current = item.getmQuantity();
            int newQty = current + 1;
            item.setmQuantity(newQty);
            notifyItemChanged(holder.getAdapterPosition());

            if (listener != null) listener.onQuantityChanged(item, newQty);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class InventoryVH extends RecyclerView.ViewHolder {
        RadioButton radioSelect;
        TextView textName;
        TextView textQty;
        Button buttonMinus;
        Button buttonPlus;

        InventoryVH(@NonNull View itemView) {
            super(itemView);
            radioSelect = itemView.findViewById(R.id.radioSelect);
            textName = itemView.findViewById(R.id.textName);
            textQty = itemView.findViewById(R.id.textQty);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);
        }
    }
}