package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.pets.data.petContract;

public class admin_database extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_database);
        final Button adm_insert=(Button)findViewById(R.id.ad_insert);
        adm_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adm=new Intent(admin_database.this,admin_insert.class);
                startActivity(adm);
            }
        });
        Button adm_database=(Button)findViewById(R.id.ad_dat);
        adm_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertADMIN();
                displayA();
            }
        });
    }
    private void insertADMIN() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(petContract.ADMIN.COLUMN_LOGIN,"vibh");
        contentValues.put(petContract.ADMIN.COLUMN_PASSWORD, "p");
        Uri URI = getContentResolver().insert(petContract.ADMIN.CONTENT_URI_2, contentValues);
    }
    private void displayA()
    {

        String[] projection = {
                petContract.ADMIN.COLUMN_ID_2,
                petContract.ADMIN.COLUMN_LOGIN,
                petContract.ADMIN.COLUMN_PASSWORD};

        // Perform a query on the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to access the pet data.
        Cursor cursor = getContentResolver().query(
                petContract.ADMIN.CONTENT_URI_2,   // The content URI of the words table
                projection,             // The columns to return for each row
                null,                   // Selection criteria
                null,                   // Selection criteria
                null);                  // The sort order for the returned rows

        TextView disp = (TextView) findViewById(R.id.adm_contents);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> pets.
            // _id - name - breed - gender - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            disp.setText("The admin table contains " + cursor.getCount() + " admin.\n\n");
            disp.append(petContract.ADMIN.COLUMN_ID_2+ " - " +
                    petContract.ADMIN.COLUMN_LOGIN + " - " +
                    petContract.ADMIN.COLUMN_PASSWORD+" \n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(petContract.ADMIN.COLUMN_ID_2);
            int nameColumnIndex = cursor.getColumnIndex(petContract.ADMIN.COLUMN_LOGIN);
            int breedColumnIndex = cursor.getColumnIndex(petContract.ADMIN.COLUMN_PASSWORD);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBreed = cursor.getString(breedColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                disp.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentBreed ));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    }

