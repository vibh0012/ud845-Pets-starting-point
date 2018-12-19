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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.petContract;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** EditText field to enter the pet's name */
    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;
    private static final int Loader=1;
    PetCursorAdapter madap;
    private Uri currentPetUri;
    private boolean mPetHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        currentPetUri = intent.getData();
        if (currentPetUri == null)
        {       setTitle("add a pet");
        invalidateOptionsMenu();
    }
        else {
            setTitle(getString(R.string.editor_activity_title_edit_pet));
            getSupportLoaderManager().initLoader(0, null, this);
        }
        Button admin_access=(Button)findViewById(R.id.admin_database);
       admin_access.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent abc=new Intent(EditorActivity.this,admin_database.class);
               startActivity(abc);
           }
       });

        // Find all relevant views that we will need to read user input from

        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mNameEditText.setOnTouchListener(mTouchListener);
        mBreedEditText.setOnTouchListener(mTouchListener);
        mWeightEditText.setOnTouchListener(mTouchListener);
        setupSpinner();
        mGenderSpinner.setOnTouchListener(mTouchListener);

    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (currentPetUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = petContract.PetEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = petContract.PetEntry.GENDER_FEMALE; // Female
                    } else {
                        mGender = petContract.PetEntry.GENDER_UNKNOWN; // Unknown
                    }


                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0;
                // Unknown
            }
        });
    }

    /**void pet_save()
    {
       String name= mNameEditText.getText().toString().trim();
        String breed= mBreedEditText.getText().toString().trim();
        String weight= mWeightEditText.getText().toString().trim();
        int weight_int=Integer.parseInt(weight);
        PetDbHelper mDbHelper = new PetDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(petContract.PetEntry.COLUMN_PET_NAME,name);
        contentValues.put(petContract.PetEntry.COLUMN_PET_BREED,breed);
        contentValues.put(petContract.PetEntry.COLUMN_PET_GENDER, mGender);//this is a method of
        //editoractivity class and can access private data items
        contentValues.put(petContract.PetEntry.COLUMN_PET_WEIGHT,weight_int);
        long newRowId;
        newRowId=db.insert(petContract.PetEntry.TABLE_NAME,null,contentValues);
        if(newRowId==-1)
        {
            Toast.makeText(this,"not inserted correctly",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"inserted correctly",Toast.LENGTH_LONG).show();
        }
    }
    */
    private void insertPet() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String breedString = mBreedEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim();
        int weight;
        if(weightString.isEmpty())
             weight=0;
        else
         weight = Integer.parseInt(weightString);

        if(TextUtils.isEmpty(nameString)||TextUtils.isEmpty(breedString)||mGender==2)
            return;

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(petContract.PetEntry.COLUMN_PET_NAME, nameString);
        values.put(petContract.PetEntry.COLUMN_PET_BREED, breedString);
        values.put(petContract.PetEntry.COLUMN_PET_GENDER, mGender);
        values.put(petContract.PetEntry.COLUMN_PET_WEIGHT, weight);

        if(currentPetUri==null) {
            // Insert a new pet into the provider, returning the content URI for the new pet.
            Uri newUri = getContentResolver().insert(petContract.PetEntry.CONTENT_URI, values);
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            int rows_updated=getContentResolver().update(currentPetUri,values,null,null);
            if (rows_updated == 0) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        // Show a toast message depending on whether or not the insertion was successful

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:

                insertPet();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                Integer a=getContentResolver().delete(currentPetUri,null,null);
                Toast.makeText(this,getString(R.string.delete_specific),Toast.LENGTH_SHORT).show();
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case R.id.action_query:
                ContentValues contentValues = new ContentValues();
                contentValues.put(petContract.PetEntry.COLUMN_PET_NAME, "TOTO");
                contentValues.put(petContract.PetEntry.COLUMN_PET_BREED, "TERRIER");
                contentValues.put(petContract.PetEntry.COLUMN_PET_GENDER, petContract.PetEntry.GENDER_FEMALE);
                contentValues.put(petContract.PetEntry.COLUMN_PET_WEIGHT, 56);
               String selection = petContract.PetEntry.COLUMN_PET_BREED + "=?";
                String[] Arg = {"pomerian"};
                int rows_updated=getContentResolver().update(petContract.PetEntry.CONTENT_URI,
                        contentValues,selection,Arg);
                /**TextView display = (TextView) findViewById(R.id.query);
                display.setText("rows according to requested query are: \n");
               // displayView.append+("_id - "+ "  BREED  "+"\n");
                int id_c=cursor.getColumnIndex(petContract.PetEntry._ID);
                int name_c=cursor.getColumnIndex(petContract.PetEntry.COLUMN_PET_NAME);
                while(cursor.moveToNext())
                {
                    int current_id=cursor.getInt(id_c);
                    String c_breed=cursor.getString(name_c);
                    display.append("  " + current_id+"  "+c_breed+ " \n");
                }
                cursor.close();
                finish();*/
                return true;
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mPetHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String entity_proj[]=
                {
                        petContract.PetEntry._ID,
                        petContract.PetEntry.COLUMN_PET_NAME,
                        petContract.PetEntry.COLUMN_PET_BREED,
                        petContract.PetEntry.COLUMN_PET_WEIGHT,
                        petContract.PetEntry.COLUMN_PET_GENDER
                };
        return new CursorLoader(
                    this,
                    currentPetUri,
                    entity_proj,
                    null,
                    null,
                    null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data==null||data.getCount()<1)
            return;
        if(data.moveToFirst()) {
            String name_edit = data.getString(data.getColumnIndexOrThrow(petContract.PetEntry.COLUMN_PET_NAME));
            String breed_edit = data.getString(data.getColumnIndexOrThrow(petContract.PetEntry.COLUMN_PET_BREED));
            int gender_edit = data.getColumnIndexOrThrow(petContract.PetEntry.COLUMN_PET_GENDER);
            String weight_edit = data.getString(data.getColumnIndexOrThrow(petContract.PetEntry.COLUMN_PET_WEIGHT));
            mNameEditText.setText(name_edit);
            mWeightEditText.setText(weight_edit);
            mBreedEditText.setText(breed_edit);
            if (gender_edit==0)
            mGenderSpinner.setSelection(petContract.PetEntry.GENDER_MALE);
            else if(gender_edit==1)
                mGenderSpinner.setSelection(petContract.PetEntry.GENDER_FEMALE);
            else
                mGenderSpinner.setSelection(petContract.PetEntry.GENDER_UNKNOWN);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mWeightEditText.setText("");
        mBreedEditText.setText("");
        mGenderSpinner.setSelection(petContract.PetEntry.GENDER_UNKNOWN);
    }
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            return false;
        }
    };
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }
}