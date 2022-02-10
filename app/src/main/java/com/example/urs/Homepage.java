package com.example.urs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import static com.example.urs.Register.user;

public class Homepage extends AppCompatActivity  {
    private ImageButton brojljudi;
    private ImageButton napredak;
    private ImageButton kartica;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.homepage);

        connectionClass = new ConnectionClass();
        progressDialog = new ProgressDialog(this);

        kartica = findViewById(R.id.tvojakartica);
        kartica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HelperClass.flag) {
                    HelperClass.flag = false;
                } else {
                    HelperClass.flag = true;
                }
                Homepage.DoWriteInDatabase doWriteInDatabase = new Homepage.DoWriteInDatabase();
                doWriteInDatabase.execute("");
            }
        });

        brojljudi=findViewById(R.id.brojljudi);
        brojljudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrojljudiActivity();
            }
        });

        napredak=findViewById(R.id.napredak);
        napredak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNapredakActivity();
            }
        });
    }

    public void openBrojljudiActivity() {
        Intent intent = new Intent (this, Brojljudi.class);
        startActivity(intent);
    }
    public void openNapredakActivity() {
        Intent intent = new Intent (this, Napredak.class);
        startActivity(intent);
    }
    public void openHomepageActivity() {
        Intent intent = new Intent (this, Homepage.class);
        startActivity(intent);
    }

    public class DoWriteInDatabase extends AsyncTask<String, String, String>
    {
        String namestr = user.getText().toString();

        String z = "";
        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            try {
                Connection con = connectionClass.CONN();
                if(con == null) {
                    z="please check your internet connection";
                } else {
                    String query = null;
                    if(HelperClass.flag) {
                        query= "insert into user values('"+namestr+"','"+timestamp+"', NULL)";
                        z = "Welcome!";
                    } else {
                        query= "insert into user values('"+namestr+"',NULL,'"+timestamp+"')";
                        z = "Goodbye!";
                    }
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                    isSuccess= true;
                }
            } catch (SQLException e) {
                isSuccess=false;
                z="exception" + e;
                e.printStackTrace();
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            if(isSuccess) {
                Toast.makeText(getBaseContext(), ""+z, Toast.LENGTH_SHORT).show();
            }
            progressDialog.hide();
        }
    }
}
