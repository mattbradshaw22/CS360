package com.zybooks.cs360_bradshaw_matthias;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class InventoryDialogFragment extends DialogFragment {

    public interface OnAddInventoryListener {
        void onAddInventory(String name, int quantity);
    }

    private OnAddInventoryListener mListener;

    @NonNull

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Name input
        final EditText nameEditText = new EditText(requireActivity());
        nameEditText.setHint(R.string.item_name);
        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        nameEditText.setMaxLines(1);

        // Quantity input
        final EditText qtyEditText = new EditText(requireActivity());
        qtyEditText.setHint(R.string.quantity);
        qtyEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        qtyEditText.setMaxLines(1);

        // Layout for both fields
        LinearLayout layout = new LinearLayout(requireActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        int padding = (int) (16 * requireActivity().getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding, padding, padding);

        layout.addView(nameEditText);
        layout.addView(qtyEditText);

        return new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.inventory)
                .setView(layout)
                .setPositiveButton(R.string.create, (dialog, whichButton) -> {
                    String name = nameEditText.getText().toString().trim();

                    int qty = 0;
                    String qtyText = qtyEditText.getText().toString().trim();
                    if (!qtyText.isEmpty()) {
                        try {
                            qty = Integer.parseInt(qtyText);
                        } catch (NumberFormatException ignored) {
                            qty = 0;
                        }
                    }

                    if (mListener != null) {
                        mListener.onAddInventory(name, qty);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAddInventoryListener) {
            mListener = (OnAddInventoryListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement InventoryDialogFragment.OnAddInventoryListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}