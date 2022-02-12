package com.example.urs;

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

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import static com.example.urs.Register.user;

public class Profil extends AppCompatActivity {
    Button odjava;
    TextView imeiPrezime;
    TextView email;
    TextView password;
    private ImageButton homepage, brojljudi, napredak, kartica;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.profil);

        connectionClass = new ConnectionClass();
        progressDialog = new ProgressDialog(this);

        String namestr = null;
        String emailstr = null;
        String passstr = null;

        //get username
        try {//get username from registration
            if (!Register.user.getText().toString().equals("")) {
                namestr = Register.user.getText().toString();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {//get username from login
            if (!HelperClass.userconcat.equals("")) {
                namestr = HelperClass.userconcat;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //get email
        try {
            if (!Register.email.getText().toString().equals("")) {
                emailstr = Register.email.getText().toString();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            if (!HelperClass.emailconcat.equals("")) {
                emailstr = HelperClass.emailconcat;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        //get password
        try {
            if (!Register.pass.getText().toString().equals("")) {
                passstr = Register.pass.getText().toString();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            if (!HelperClass.passconcat.equals("")) {
                passstr = HelperClass.passconcat;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        imeiPrezime = findViewById(R.id.profilIme);
        imeiPrezime.setText(namestr);

        email = findViewById(R.id.profilEmail);
        email.setText(emailstr);

        password = findViewById(R.id.profilPassword);
        password.setText(passstr);

        homepage = findViewById(R.id.pocetna);
        brojljudi = findViewById(R.id.brojljudi);
        napredak = findViewById(R.id.napredak);

        odjava = findViewById(R.id.odjava);
        odjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomepageActivity();
            }
        });
        brojljudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrojLjudiActivity();
            }
        });
        napredak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNapredakActivity();
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
                Profil.DoWriteInDatabase doWriteInDatabase = new Profil.DoWriteInDatabase();
                doWriteInDatabase.execute("");
            }
        });
    }

    public void openHomepageActivity() {
        Intent intent = new Intent (this, Homepage.class);
        startActivity(intent);
    }

    public void openBrojLjudiActivity() {
        Intent intent = new Intent (this, Brojljudi.class);
        startActivity(intent);
    }

    public void openNapredakActivity() {
        Intent intent = new Intent (this, Napredak.class);
        startActivity(intent);
    }

    public void openLoginActivity() {
        Intent intent = new Intent (this, Login.class);
        startActivity(intent);
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
