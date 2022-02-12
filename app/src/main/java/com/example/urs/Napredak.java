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
import android.widget.EditText;
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

public class Napredak extends AppCompatActivity {
    private ImageButton goTohomepage;
    private ImageButton goToBrojljudi;
    private ImageButton kartica;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;
    private ImageButton profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_napredak);

        connectionClass = new ConnectionClass();
        progressDialog = new ProgressDialog(this);

        goTohomepage=findViewById(R.id.pocetna);
        goTohomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomepageActivity();
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
                Napredak.DoWriteInDatabase doWriteInDatabase = new Napredak.DoWriteInDatabase();
                doWriteInDatabase.execute("");
            }
        });

        Napredak.DoReadFromDatabase doReadFromDatabase = new Napredak.DoReadFromDatabase();
        doReadFromDatabase.execute("");

        goToBrojljudi=findViewById(R.id.brojljudi);
        goToBrojljudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrojljudiActivity();
            }
        });
        profil = findViewById(R.id.profil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfilActivity();
            }
        });
    }

    public void openHomepageActivity() {
        Intent intent = new Intent (this, Homepage.class);
        startActivity(intent);
    }

    public void openBrojljudiActivity() {
        Intent intent = new Intent (this, Brojljudi.class);
        startActivity(intent);
    }

    public void openProfilActivity() {
        Intent intent = new Intent (this, Profil.class);
        startActivity(intent);
    }

    public class DoReadFromDatabase extends AsyncTask<String, String, String>
    {
        TextView view;
        String namestr;
        String z = "";
        boolean isSuccess=false;
        int counter = 0;

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

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = connectionClass.CONN();
                if(con == null) {
                    z="please check your internet connection";
                } else {
                    String query = null;
                    query= "select dolazak from user where imeprezime='"+namestr+"';";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next()) {
                        if(rs.getTimestamp(1) != null)
                       counter++;
                    }
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
                view = findViewById(R.id.brojdolazaka);
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
