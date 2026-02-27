package com.zybooks.cs360_bradshaw_matthias.data;

import com.zybooks.cs360_bradshaw_matthias.model.InventoryItem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/* InventoryDataSource
 *
 * data access class for the inventory table.
 *
 * This is where we do the database CRUD work:
 *   insertItem()
 *   getAllItems()
 *   updateQuantity()
 *   deleteItem()
 *
 * UI code calls these methods instead of writing directly to SQLite.
 */
public class InventoryDataSource {

    private final AppDbHelper dbHelper;

    public InventoryDataSource(Context context) {
        dbHelper = new AppDbHelper(context.getApplicationContext());
    }

    public long insertItem(String name, int quantity) {
        // insert new item into SQLite Db
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppDbHelper.INV_COLUMN_NAME, name);
        values.put(AppDbHelper.INV_COLUMN_QUANTITY, quantity);
        return db.insert(AppDbHelper.TABLE_INVENTORY, null, values);
    }
public List<InventoryItem> getAllItems() {
        // load SQLite items into the list
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<InventoryItem> items = new ArrayList<>();

        try (Cursor cursor = db.query(
                AppDbHelper.TABLE_INVENTORY,
                null,
                null,
                null,
                null,
                null,
                null
        )) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(AppDbHelper.INV_COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(AppDbHelper.INV_COLUMN_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(AppDbHelper.INV_COLUMN_QUANTITY));
                items.add(new InventoryItem(id, name, quantity));
            }
        }

        return items;
    }

    public int updateQuantity(long id, int newQty) {
        // update SQLite quantity
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppDbHelper.INV_COLUMN_QUANTITY, newQty);

        return db.update(
                AppDbHelper.TABLE_INVENTORY,
                values,
                AppDbHelper.INV_COLUMN_ID + "=?",
                new String[]{ String.valueOf(id) }
        );
    }

    public int deleteItem(long id) {
        // delete SQLite item
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
                AppDbHelper.TABLE_INVENTORY,
                AppDbHelper.INV_COLUMN_ID + "=?",
                new String[]{ String.valueOf(id) }
        );

}
}
