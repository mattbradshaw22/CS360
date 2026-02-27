package com.zybooks.cs360_bradshaw_matthias.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zybooks.cs360_bradshaw_matthias.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        // checks database for username and password and if match returns successful login user
        // or a login error if unsuccessful

       SQLiteDatabase db = dbHelper.getReadableDatabase();

       String selection = AppDbHelper.COLUMN_USERNAME + " = ? AND " + AppDbHelper.COLUMN_PASSWORD + " = ?";

       String[] selectionArgs = { username, password };

       Cursor cursor = db.query(AppDbHelper.TABLE_USERS,
               null,
               selection,
               selectionArgs,
               null,
               null,
               null);
       if (cursor.moveToFirst()) {
           long id = cursor.getLong(cursor.getColumnIndexOrThrow(AppDbHelper.COLUMN_ID));

           LoggedInUser user = new LoggedInUser(String.valueOf(id), username);

           cursor.close();
           return new Result.Success<>(user);
       }

       cursor.close();
       return new Result.Error(new IOException("Error logging in"));
    }

    private final AppDbHelper dbHelper;

    public LoginDataSource(Context context) {
        dbHelper = new AppDbHelper(context);
    }

    public AppDbHelper getDbHelper() {
        return dbHelper;
    }

    public Result<Boolean> createUser(String username, String password) {
        // creates a new user in the user table of the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppDbHelper.COLUMN_USERNAME, username);
        values.put(AppDbHelper.COLUMN_PASSWORD, password);

        long result = db.insert(AppDbHelper.TABLE_USERS, null, values);

        if (result == -1) {
            return new Result.Error(new IOException("Error creating user"));
        } else {
            return new Result.Success<>(true);
        }
    }



    public void logout() {
        // TODO: revoke authentication
    }
}