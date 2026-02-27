package com.zybooks.cs360_bradshaw_matthias.model;


/* InventoryItem
 *
 * data object that represents one row in the inventory table.
 *
 * Fields:
 *  id: primary key from SQLite
 *  name: item name
 *  quantity: current quantity
 */
public class InventoryItem {
    public long id;
    public String name;
    public int quantity;

    public InventoryItem(long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
}

