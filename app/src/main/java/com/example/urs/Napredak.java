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
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;

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

        Napredak.DoReadFromDatabase doReadFromDatabase = new Napredak.DoReadFromDatabase();
        doReadFromDatabase.execute("");

        goToBrojljudi=findViewById(R.id.brojljudi);
        goToBrojljudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrojljudiActivity();
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

    public class DoReadFromDatabase extends AsyncTask<String, String, String>
    {
        TextView view;
        String userstr = user.getText().toString();
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
                    query= "select dolazak from user where imeprezime='"+userstr+"';";
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
}
