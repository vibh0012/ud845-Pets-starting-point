package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PetDbHelper extends SQLiteOpenHelper {

    public final static int DATABASE_VERSION=1;
    public static final String LOG_TAG = PetDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "shelter.db";

    public PetDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + petContract.PetEntry.TABLE_NAME + " ("
                + petContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + petContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + petContract.PetEntry.COLUMN_PET_BREED + " TEXT, "
                + petContract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + petContract.PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";
        String SQL_CREATE_ADMINS_TABLE="CREATE TABLE "+petContract.ADMIN.TABLE_NAME_2+" ("
                +petContract.ADMIN.COLUMN_ID_2+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +petContract.ADMIN.COLUMN_LOGIN+" TEXT NOT NULL, "
                +petContract.ADMIN.COLUMN_PASSWORD+" TEXT NOT NULL );";

        // Execute the SQL statement
        //USE THEM MODAF SPACES
        db.execSQL(SQL_CREATE_PETS_TABLE);
        db.execSQL(SQL_CREATE_ADMINS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         /**
        // The database is still at version 1, so there's nothing to do be done here.
         String SQL_DELETE_PETS_TABLE=
                "DROP TABLE IF EXISTS "+ petContract.PetEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_PETS_TABLE);
        onCreate(db);*/
         }
}
