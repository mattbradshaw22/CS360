package com.zybooks.cs360_bradshaw_matthias.model;
import androidx.annotation.NonNull;

public class Inventory {

    private long mId;
    private String mName;
    private int mQuantity;
    public Inventory(String name, int quantity) {
        mName = name;
        mQuantity = quantity;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }
}
