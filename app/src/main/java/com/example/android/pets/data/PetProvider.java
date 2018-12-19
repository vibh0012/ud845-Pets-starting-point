package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * {@link ContentProvider} for Pets app.
 */
public class PetProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = PetProvider.class.getSimpleName();
    public static final int PETS=100;
    public static final int PETS_ID=101;
    public static final int ADM=102;
    public static final int ADM_ID=103;
    private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        sUriMatcher.addURI(petContract.CONTENT_AUTHORITY,petContract.PATH_PETS,PETS);
        sUriMatcher.addURI(petContract.CONTENT_AUTHORITY,petContract.PATH_PETS+"/#",PETS_ID);
        sUriMatcher.addURI(petContract.CONTENT_AUTHORITY,petContract.PATH_ADMIN,ADM);
        sUriMatcher.addURI(petContract.CONTENT_AUTHORITY,petContract.PATH_ADMIN+"/#",ADM_ID);
    }
    /**
     * Initialize the provider and the database helper object.
     */
   private PetDbHelper mDbhelper;
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        mDbhelper=new PetDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database=mDbhelper.getReadableDatabase();
        Cursor cursor;
        int match=sUriMatcher.match(uri);
        switch(match)
        {
            case PETS:
            {
                cursor=database.query(petContract.PetEntry.TABLE_NAME,projection,null,
                        null,null,null,null);
                break;
            }
            case PETS_ID:
            {
               selection= petContract.PetEntry._ID+"=?";
               selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
               cursor=database.query(petContract.PetEntry.TABLE_NAME,projection,selection,
                       selectionArgs,null,null,sortOrder);
                break;
            }
            case ADM:
            {
                cursor=database.query(petContract.ADMIN.TABLE_NAME_2,projection,null,
                        null,null,null,null);
                break;
            }
            case ADM_ID:
            {
                selection= petContract.ADMIN.COLUMN_ID_2+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=database.query(petContract.ADMIN.TABLE_NAME_2,projection,selection,
                        selectionArgs,null,null,sortOrder);
                break;
            }
            default: throw new IllegalArgumentException("uri not valid!"+ uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {


        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS: {
                Log.v(LOG_TAG, "ACCEPTED CASE PETS\n");
                return insertPet(uri,contentValues);

            }
            case ADM: {

                Log.v(LOG_TAG, "ACCEPTED INTO INSERT PETPROVIDER\n");
                return insertAdmin(uri,contentValues);


            }
            default:

                throw new IllegalArgumentException("uri not valid!" + uri);

    }

    }
    private Uri insertAdmin(Uri uri,ContentValues values)
    {
        String log=values.getAsString(petContract.ADMIN.COLUMN_LOGIN);
        if(log.trim().isEmpty())
        {
            throw new IllegalArgumentException("login_id cannot be null");
        }
        String pas=values.getAsString(petContract.ADMIN.COLUMN_PASSWORD);

        if(pas.trim().isEmpty())
        {
            throw new IllegalArgumentException("password cannot be null");
        }
        SQLiteDatabase database = mDbhelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(petContract.ADMIN.TABLE_NAME_2, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }
    private Uri insertPet(Uri uri, ContentValues values) {
        // Check that the name is not null
        String names = values.getAsString(petContract.PetEntry.COLUMN_PET_NAME);
        Log.v(LOG_TAG,"name is:"+names);
        if (names == null||names.trim().isEmpty()) {
            Log.v(LOG_TAG,"REACHES INSIDE NAME NULL");
            throw new IllegalArgumentException("Pet requires a name");
        }
        Log.v(LOG_TAG,"REACHEs below NAME NULL");
        // Check that the gender is valid
        Integer gender = values.getAsInteger(petContract.PetEntry.COLUMN_PET_GENDER);
        if (gender == null || !petContract.PetEntry.IsGender(gender)) {
            throw new IllegalArgumentException("Pet requires valid gender");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer weight = values.getAsInteger(petContract.PetEntry.COLUMN_PET_WEIGHT);
     //chANGE HERE!!
        if (weight == null||weight>200) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }

        // No need to check the breed, any value is valid (including null).

        // Get writeable database
        SQLiteDatabase database = mDbhelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(petContract.PetEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        Log.v(LOG_TAG,"REACHES END OF INSERT PETS");
        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }
        /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PETS_ID: {   // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = petContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            }
                case ADM:
                return updateAdmin(uri, contentValues, selection, selectionArgs);
            case ADM_ID: {// For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = petContract.ADMIN.COLUMN_ID_2 + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateAdmin(uri, contentValues, selection, selectionArgs);
            }
                default:
                throw new IllegalArgumentException("Update is not supported for " + uri);}

    }
    private int updateAdmin(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        if (values.containsKey(petContract.ADMIN.COLUMN_LOGIN)) {
            String log = values.getAsString(petContract.ADMIN.COLUMN_LOGIN);
            if (log.trim().isEmpty()) {
                throw new IllegalArgumentException("login_id cannot be null");
            }
        }
        if (values.containsKey(petContract.ADMIN.COLUMN_PASSWORD)) {
            String pas = values.getAsString(petContract.ADMIN.COLUMN_PASSWORD);

            if (pas.trim().isEmpty()) {
                throw new IllegalArgumentException("password cannot be null");
            }
        }
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbhelper.getWritableDatabase();
        int rowsupdated=database.update(petContract.ADMIN.TABLE_NAME_2,values,selection,selectionArgs);
        if(rowsupdated!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return rowsupdated;
    }
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(petContract.PetEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(petContract.PetEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(petContract.PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(petContract.PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !petContract.PetEntry.IsGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(petContract.PetEntry.COLUMN_PET_WEIGHT)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(petContract.PetEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbhelper.getWritableDatabase();
        int rowsupdated=database.update(petContract.PetEntry.TABLE_NAME,values,selection,selectionArgs);
        if(rowsupdated!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return rowsupdated;
    }
    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbhelper.getWritableDatabase();
        int rowsdeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // Delete all rows that match the selection and selection args
                rowsdeleted = database.delete(petContract.PetEntry.TABLE_NAME, selection, selectionArgs);
                 break;

            case PETS_ID:
                // Delete a single row given by the ID in the URI
                selection = petContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsdeleted = database.delete(petContract.PetEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ADM:
                // Delete all rows that match the selection and selection args
                rowsdeleted = database.delete(petContract.ADMIN.TABLE_NAME_2, selection, selectionArgs);
                break;

            case ADM_ID:
                // Delete a single row given by the ID in the URI
                selection = petContract.ADMIN.COLUMN_ID_2 + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsdeleted = database.delete(petContract.ADMIN.TABLE_NAME_2, selection, selectionArgs);
                break;
                default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsdeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);

        }
        return rowsdeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
        public String getType(Uri uri) {
            int match = sUriMatcher.match(uri);
            switch (match) {
                case PETS:
                    return petContract.PetEntry.CONTENT_LIST_TYPE;
                case PETS_ID:
                    return petContract.PetEntry.CONTENT_ITEM_TYPE;
                case ADM:
                    return petContract.ADMIN.CONTENT_LIST_TYPE_2;
                case ADM_ID:
                    return petContract.ADMIN.CONTENT_ITEM_TYPE_2;
                    default:
                    throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
            }
        }
}