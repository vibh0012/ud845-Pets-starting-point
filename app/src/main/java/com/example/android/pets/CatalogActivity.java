/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.petContract;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    //private Uri mCurrentPetUri;**********************************************
    private static final int PET_LOADER = 0;
    PetCursorAdapter mCursorAdapter;
    private PetDbHelper mDbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

/**
 // Setup the item click listener
 petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
@Override public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
// Create new intent to go to {@link EditorActivity}
Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

// Form the content URI that represents the specific pet that was clicked on,
// by appending the "id" (passed as input to this method) onto the
// {@link PetEntry#CONTENT_URI}.
// For example, the URI would be "content://com.example.android.pets/pets/2"
// if the pet with ID 2 was clicked on.
Uri currentPetUri = ContentUris.withAppendedId(petContract.PetEntry.CONTENT_URI, id);

// Set the URI on the data field of the intent
intent.setData(currentPetUri);

// Launch the {@link EditorActivity} to display the data for the current pet.
startActivity(intent);
}
});*/
        ListView petListView = (ListView) findViewById(R.id.list1);
        View emptyView=findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);
        mDbhelper = new PetDbHelper(this);
       mCursorAdapter=new PetCursorAdapter(this,null);
       petListView.setAdapter(mCursorAdapter);
       petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent=new Intent(CatalogActivity.this,EditorActivity.class);
               Uri Actual_uri=ContentUris.withAppendedId(petContract.PetEntry.CONTENT_URI,l);
               Log.v("CatalogActivity","          yoyoyooyoyoy        "+Actual_uri.toString());
               intent.setData(Actual_uri);
               //intent.putExtra("Uri",Actual_uri);
               startActivity(intent);
           }
       });
        // Kick off the loader
        getLoaderManager().initLoader(PET_LOADER, null, this);
    }



    /**
     private void displayDatabaseInfo() {
     // Define a projection that specifies which columns from the database
     // you will actually use after this query.
     String[] projection = {
     petContract.PetEntry._ID,
     petContract.PetEntry.COLUMN_PET_NAME,
     petContract.PetEntry.COLUMN_PET_BREED,
     petContract.PetEntry.COLUMN_PET_GENDER,
     petContract.PetEntry.COLUMN_PET_WEIGHT };

     // Perform a query on the provider using the ContentResolver.
     // Use the {@link //PetEntry#CONTENT_URI} to access the pet data.
     Cursor cursor = getContentResolver().query(
     petContract.PetEntry.CONTENT_URI,   // The content URI of the words table
     projection,             // The columns to return for each row
     null,                   // Selection criteria
     null,                   // Selection criteria
     null);                  // The sort order for the returned rows

     TextView displayView = (TextView) findViewById(R.id.text_view_pet);

     try {
     // Create a header in the Text View that looks like this:
     //
     // The pets table contains <number of rows in Cursor> pets.
     // _id - name - breed - gender - weight
     //
     // In the while loop below, iterate through the rows of the cursor and display
     // the information from each column in this order.
     displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
     displayView.append(petContract.PetEntry._ID + " - " +
     petContract.PetEntry.COLUMN_PET_NAME + " - " +
     petContract.PetEntry.COLUMN_PET_BREED + " - " +
     petContract.PetEntry.COLUMN_PET_GENDER + " - " +
     petContract.PetEntry.COLUMN_PET_WEIGHT + "\n");

     // Figure out the index of each column
     int idColumnIndex = cursor.getColumnIndex(petContract.PetEntry._ID);
     int nameColumnIndex = cursor.getColumnIndex(petContract.PetEntry.COLUMN_PET_NAME);
     int breedColumnIndex = cursor.getColumnIndex(petContract.PetEntry.COLUMN_PET_BREED);
     int genderColumnIndex = cursor.getColumnIndex(petContract.PetEntry.COLUMN_PET_GENDER);
     int weightColumnIndex = cursor.getColumnIndex(petContract.PetEntry.COLUMN_PET_WEIGHT);

     // Iterate through all the returned rows in the cursor
     while (cursor.moveToNext()) {
     // Use that index to extract the String or Int value of the word
     // at the current row the cursor is on.
     int currentID = cursor.getInt(idColumnIndex);
     String currentName = cursor.getString(nameColumnIndex);
     String currentBreed = cursor.getString(breedColumnIndex);
     int currentGender = cursor.getInt(genderColumnIndex);
     int currentWeight = cursor.getInt(weightColumnIndex);
     // Display the values from each column of the current row in the cursor in the TextView
     displayView.append(("\n" + currentID + " - " +
     currentName + " - " +
     currentBreed + " - " +
     currentGender + " - " +
     currentWeight));
     }
     } finally {
     // Always close the cursor when you're done reading from it. This releases all its
     // resources and makes it invalid.
     cursor.close();
     }
     }
     */

    /**
     * Cursor cursor=db.query(petContract.PetEntry.TABLE_NAME,projection,null,null,
     * null,null,null);
     * try {
     * // Display the number of rows in the Cursor (which reflects the number of rows in the
     * // pets table in the database).
     * TextView displayView = (TextView) findViewById(R.id.text_view_pet);
     * displayView.setText("Number of rows in pets database table: " +cursor.getCount()+"\n");
     * displayView.append("_id - "+"name"+ "-"+"breed - gender  -  weight"+ "\n");
     * int id_c=cursor.getColumnIndex(petContract.PetEntry._ID);
     * int name_c=cursor.getColumnIndex(petContract.PetEntry.COLUMN_PET_NAME);
     * int breed_c=cursor.getColumnIndex(petContract.PetEntry.COLUMN_PET_BREED);
     * int gender_c=cursor.getColumnIndex(petContract.PetEntry.COLUMN_PET_GENDER);
     * int weight_c=cursor.getColumnIndex(petContract.PetEntry.COLUMN_PET_WEIGHT);
     * while(cursor.moveToNext())
     * {
     * int current_id=cursor.getInt(id_c);
     * String current_name=cursor.getString(name_c);
     * String current_breed=cursor.getString(breed_c);
     * String current_gender=cursor.getString(gender_c);
     * String current_weight=cursor.getString(weight_c);
     * displayView.append("  " + current_id+ "-"+current_name+" -"+current_breed+" - "+current_gender+
     * " -"+ current_weight+ " \n");
     * }
     * } finally {
     * // Always close the cursor when you're done reading from it. This releases all its
     * // resources and makes it invalid.
     * cursor.close();
     * }
     */
    // /** THE ABOVE HIDDEN LINES WERE IN THE DISPALY DATABASE METHOD BLOCK BELOW CURSOR AND ABOVE LISTVIEW*/
    private void insertPet() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(petContract.PetEntry.COLUMN_PET_NAME, "Bruno");
        contentValues.put(petContract.PetEntry.COLUMN_PET_BREED, "pomerian");
        contentValues.put(petContract.PetEntry.COLUMN_PET_GENDER, petContract.PetEntry.GENDER_FEMALE);
        contentValues.put(petContract.PetEntry.COLUMN_PET_WEIGHT, 4);
        //INSERT METHOD USED!!!!!
        Uri URI = getContentResolver().insert(petContract.PetEntry.CONTENT_URI, contentValues);
    }

    private void Createdummy() {
        PetDbHelper mDbHelper = new PetDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(petContract.PetEntry.COLUMN_PET_NAME, "sdfsdf");
        contentValues.put(petContract.PetEntry.COLUMN_PET_BREED, "sdfsdVf");
        contentValues.put(petContract.PetEntry.COLUMN_PET_GENDER, petContract.PetEntry.GENDER_MALE);
        contentValues.put(petContract.PetEntry.COLUMN_PET_WEIGHT, 45);
        long newRowId;
        newRowId = db.insert(petContract.PetEntry.TABLE_NAME, null, contentValues);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // User clicked on a menu option in the app bar overflow menu
            case R.id.action_insert_dummy_data: {
                insertPet();
                //displayDatabaseInfo();
                return true;
            }
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                Integer b = getContentResolver().delete(petContract.PetEntry.CONTENT_URI, null, null);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String pr[]=
                {
                        petContract.PetEntry._ID,
                        petContract.PetEntry.COLUMN_PET_NAME,
                        petContract.PetEntry.COLUMN_PET_BREED
                };
        return new CursorLoader(this,
                petContract.PetEntry.CONTENT_URI,
                pr,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
     mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
     mCursorAdapter.swapCursor(null);
    }

}
