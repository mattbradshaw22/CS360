package com.zybooks.cs360_bradshaw_matthias.ui.inventory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.cs360_bradshaw_matthias.R;
import com.zybooks.cs360_bradshaw_matthias.model.InventoryItem;

import java.util.List;

 /* InventoryAdapter
 * RecyclerView adapter that draws one inventory row per item.
 *
 * Each row includes:
 *  - A radio button (used to select an item for deletion)
 *  - Item name
 *  - Quantity
 *  - Minus/Plus buttons to update quantity
 */
public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryVH> {

    public interface OnQuantityChangedListener {
        void onQuantityChanged(InventoryItem item, int newQty);
    }

    private final List<InventoryItem> items;
    private final OnQuantityChangedListener listener;


    // track selected items
    private long selectedId = -1;

    //gets selected item
    public long getSelectedId() {
        return selectedId;
    }

    // clears selected item
    public void clearSelection() {
        selectedId = -1;
        notifyDataSetChanged();
    }

    public InventoryAdapter(List<InventoryItem> items, OnQuantityChangedListener listener) {
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
        InventoryItem item = items.get(position);

        // fills recycler view rows with Db info
        holder.textName.setText(item.name);
        holder.textQty.setText(String.valueOf(item.quantity));

        // set checked state
        holder.radioSelect.setChecked(item.id == selectedId);

        // onClick listener for radio button
        holder.radioSelect.setOnClickListener(v -> {
            if (selectedId == item.id) {
                // Uncheck if same item tapped again
                selectedId = -1;
            } else {
                // Select new item
                selectedId = item.id;
            }
            notifyDataSetChanged(); // refresh all radio buttons
        });

        // onClick listeners for - buttons
        holder.buttonMinus.setOnClickListener(v -> {

            // allows user to decrease quantity by 1 stopping at 0
            int newQty = Math.max(0, item.quantity - 1);
            item.quantity = newQty;
            notifyItemChanged(holder.getAdapterPosition());

            // if listener is not null call onQuantityChanged to update DB
            if (listener != null) listener.onQuantityChanged(item, newQty);
        });

        // onClick listeners for + buttons
        holder.buttonPlus.setOnClickListener(v -> {

            int newQty = item.quantity + 1;
            item.quantity = newQty;
            notifyItemChanged(holder.getAdapterPosition());

            // if listener is not null call onQuantityChanged to update DB
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