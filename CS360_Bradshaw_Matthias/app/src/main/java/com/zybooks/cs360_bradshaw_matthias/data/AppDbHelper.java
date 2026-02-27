package com.zybooks.cs360_bradshaw_matthias.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/* AppDbHelper
 *
 * SQLiteOpenHelper responsible for creating the database and tables.
 *
 * one database file (user_database.db) with multiple tables
 *   users (for login)
 *   inventory (for inventory tracking)
 */
public class AppDbHelper extends SQLiteOpenHelper {

    // user database constants
    public static final String DATABASE_NAME = "user_database.db";
    public static final int DATABASE_VERSION = 3;

    // USER Table and fields
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // INVENTORY Table and fields
    public static final String TABLE_INVENTORY = "inventory";
    public static final String INV_COLUMN_ID = "_id";
    public static final String INV_COLUMN_NAME = "name";
    public static final String INV_COLUMN_QUANTITY = "quantity";


    // create user table
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL UNIQUE," +
                    COLUMN_PASSWORD + " TEXT NOT NULL)";

    // create inventory table
    private static final String CREATE_TABLE_INVENTORY =
        "CREATE TABLE " + TABLE_INVENTORY + " (" +
                INV_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                INV_COLUMN_NAME + " TEXT NOT NULL, " +
                INV_COLUMN_QUANTITY + " INTEGER NOT NULL)";


    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables the first time the DB is created.
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_INVENTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);

        onCreate(db);
    }
}
