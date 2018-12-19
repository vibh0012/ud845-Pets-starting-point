package com.example.android.pets;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.pets.data.petContract;

public class starting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        final Toast t=Toast.makeText(getApplicationContext(),"INVALID login or password",Toast.LENGTH_SHORT);

            Button log = (Button) findViewById(R.id.log_button);
            log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText login_edit = (EditText) findViewById(R.id.login);

                    EditText password_edit = (EditText) findViewById(R.id.pasword);
                     String login_string = login_edit.getText().toString().trim();
                     String password_string = password_edit.getText().toString().trim();
                    String pro[]={petContract.ADMIN.COLUMN_LOGIN,
                            petContract.ADMIN.COLUMN_PASSWORD};
                    Cursor curse=getContentResolver().query(
                            petContract.ADMIN.CONTENT_URI_2,
                            pro,
                            null,null,null
                    );
                    Boolean a=false;
                    try
                    {
                        int login_columnid=curse.getColumnIndex(petContract.ADMIN.COLUMN_LOGIN);
                        int password_columnid=curse.getColumnIndex(petContract.ADMIN.COLUMN_PASSWORD);
                        while(curse.moveToNext())
                        {
                            String cursor_row_login=curse.getString(login_columnid);
                            String cursor_row_password=curse.getString(password_columnid);
                            if(cursor_row_login.equals(login_string)&&cursor_row_password.equals(password_string))
                            {
                                a=true;
                                break;
                            }
                        }
                    }
                    finally {
                        curse.close();
                    }
                     Log.v("starting","login:"+login_string+"password:"+password_string);
                    if(a){
                    Intent i=new Intent(starting.this,CatalogActivity.class);
                    startActivity(i);}
                    else
                    {Log.v("starting","login:"+login_string+"password:"+password_string);
                        t.show();
                }}
            });
            Button aboutus=(Button)findViewById(R.id.options_aboutus);
            aboutus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent j=new Intent(starting.this,About_us.class);
                    startActivity(j);
                }
            });
            Button query=(Button)findViewById(R.id.options_email);
            final String subject="QUERY";
            final String[] addresses={"dogspot_msg@gmail.com"};
            query.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });
        Button call=(Button)findViewById(R.id.options_call);
        final String phoneNumber="8376023348";
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        Button catalogue=(Button)findViewById(R.id.options_catalogue);
        catalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k=new Intent(starting.this,User_catalogue_activity.class);
                startActivity(k);
            }
        });
    }
}
