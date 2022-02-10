package com.example.urs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import static com.example.urs.Register.user;

public class Brojljudi extends AppCompatActivity {
    private ImageButton pocetna;
    private ImageButton napredak;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;
    private ImageButton kartica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_brojljudi);

        connectionClass = new ConnectionClass();
        progressDialog = new ProgressDialog(this);

        kartica = findViewById(R.id.tvojakartica);

        Button botun = findViewById(R.id.botun);
        botun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Brojljudi.DoReadFromDatabase doReadFromDatabase = new Brojljudi.DoReadFromDatabase();
                doReadFromDatabase.execute("");
            }
        });

        kartica=findViewById(R.id.tvojakartica);
        kartica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HelperClass.flag) {
                    HelperClass.flag = false;
                } else {
                    HelperClass.flag = true;
                }
                Brojljudi.DoWriteInDatabase doWriteInDatabase = new Brojljudi.DoWriteInDatabase();
                doWriteInDatabase.execute("");
            }
        });

        pocetna=findViewById(R.id.pocetna);
        pocetna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomepageActivity();
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
    public void openHomepageActivity() {
        Intent intent = new Intent (this, Homepage.class);
        startActivity(intent);
    }

    public void openNapredakActivity() {
        Intent intent = new Intent (this, Napredak.class);
        startActivity(intent);
    }

    public class DoReadFromDatabase extends AsyncTask<String, String, String>
    {
        TextView view;
        String z = "";
        boolean isSuccess=false;
        int counter = 0;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading..");
            progressDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = connectionClass.CONN();
                if(con == null) {
                    z="please check your internet connection";
                } else {
                    String query = null;
                    query= "select odlazak from user";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next()) {
                        if(rs.getTimestamp(1) == null)
                        {
                            counter++;
                        }
                        else {
                            counter--;
                        }
                    }
                    z = "Welcome";
                    isSuccess= true;
                }
            } catch (SQLException e) {
                isSuccess=false;
                z="exception" + e;
                e.printStackTrace();
            }
            return Integer.toString(counter);
        }

        @Override
        protected void onPostExecute(String s) {
            if(isSuccess) {
                view = findViewById(R.id.rez);
                view.setText(s);
            }
            progressDialog.hide();
        }
    }

    public class DoWriteInDatabase extends AsyncTask<String, String, String>
    {
        String namestr;

        String z = "";
        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {
            try {
                if (!user.getText().toString().equals("")) {
                    namestr = user.getText().toString();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                if (!HelperClass.userconcat.equals("")) {
                    namestr = HelperClass.userconcat;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
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
