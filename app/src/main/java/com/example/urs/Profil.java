package com.example.urs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Profil extends AppCompatActivity {
    Button odjava, revealPassword;
    TextView imeiPrezime;
    TextView email;
    TextView password;
    TextView ime2, email2, pass2;
    private ImageButton homepage, brojljudi, napredak, kartica;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;
    Button edit,submit;
    EditText emailedit, imeiprezimeedit, lozinkaedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.profil);

        connectionClass = new ConnectionClass();
        progressDialog = new ProgressDialog(this);

        ime2=findViewById(R.id.ime2);
        email2=findViewById(R.id.email2);
        pass2=findViewById(R.id.pass2);

        imeiPrezime = findViewById(R.id.profilIme);
        imeiPrezime.setText(HelperClass.userconcat);

        email = findViewById(R.id.profilEmail);
        email.setText(HelperClass.emailconcat);

        password = findViewById(R.id.profilPassword);
        password.setText(HelperClass.passconcat);

        homepage = findViewById(R.id.pocetna);
        brojljudi = findViewById(R.id.brojljudi);
        napredak = findViewById(R.id.napredak);

        odjava = findViewById(R.id.odjava);
        revealPassword = findViewById(R.id.revealPassword);

        edit = findViewById(R.id.edit);
        submit = findViewById(R.id.submit);

        imeiprezimeedit = findViewById(R.id.imeiprezimeedit);
        emailedit = findViewById(R.id.emailedit);
        lozinkaedit = findViewById(R.id.lozinkaedit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setVisibility(View.VISIBLE);
                imeiprezimeedit.setVisibility(View.VISIBLE);
                emailedit.setVisibility(View.VISIBLE);
                lozinkaedit.setVisibility(View.VISIBLE);
                ime2.setVisibility(View.VISIBLE);
                email2.setVisibility(View.VISIBLE);
                pass2.setVisibility(View.VISIBLE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profil.DoSendHTTPEditRequest doSendHTTPEditRequest = new Profil.DoSendHTTPEditRequest();
                doSendHTTPEditRequest.execute(HelperClass.herokuURL + HelperClass.edit + HelperClass.idconcat);
            }
        });

        odjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        revealPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!HelperClass.isPasswordVisible) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if(HelperClass.isPasswordVisible) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                HelperClass.isPasswordVisible = !HelperClass.isPasswordVisible;
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
               openKarticaActivity();
            }
        });
    }

    public void openHomepageActivity() {
        HelperClass.isPasswordVisible = false;
        Intent intent = new Intent (this, Homepage.class);
        startActivity(intent);
    }

    public void openBrojLjudiActivity() {
        HelperClass.isPasswordVisible = false;
        Intent intent = new Intent (this, Brojljudi.class);
        startActivity(intent);
    }

    public void openNapredakActivity() {
        HelperClass.isPasswordVisible = false;
        Intent intent = new Intent (this, Napredak.class);
        startActivity(intent);
    }

    public void openLoginActivity() {
        HelperClass.gymconcat1001=false;
        HelperClass.gymconcat1002=false;
        HelperClass.gymconcat1003=false;
        HelperClass.isPasswordVisible = false;
        Intent intent = new Intent (this, Login.class);
        startActivity(intent);
    }

    public void openKarticaActivity() {
        HelperClass.isPasswordVisible = false;
        Intent intent = new Intent (this, Kartica.class);
        startActivity(intent);
    }

    public class DoSendHTTPEditRequest extends AsyncTask<String, String, String>
    {
        String imeiprezime, email, pass;
        @Override
        protected void onPreExecute() {
            try {
                if(!imeiprezimeedit.getText().toString().equals("")) {
                    imeiprezime = imeiprezimeedit.getText().toString();
                } else {
                    imeiprezime = null;
                }
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                if(!emailedit.getText().toString().equals("")) {
                    email = emailedit.getText().toString();
                } else {
                    email = null;
                }
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                if(!lozinkaedit.getText().toString().equals("")) {
                    pass = lozinkaedit.getText().toString();
                } else {
                    pass = null;
                }
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            final MediaType JSON
                    = MediaType.get("text/plain; charset=utf-8");

            String jsonString = stringToJsonConverter(imeiprezime, email, pass);

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(jsonString, JSON);

            Request request = new Request.Builder()
                    .url(strings[0])
                    .patch(body)
                    .build();
            try {
                try (Response response = client.newCall(request).execute()) {
                    return response.body().string();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            return "HTTP request failed";
        }

        @Override
        protected void onPostExecute(String s) {
            String message;
            String messageConcat;
            Gson gson1 = new Gson();

            JsonObject jsonObject1 = gson1.fromJson(s, JsonObject.class);
            message = jsonObject1.get("message").toString();
            messageConcat = message.substring(1, message.length() -1);

            Toast.makeText(Profil.this, messageConcat, Toast.LENGTH_SHORT).show();

            if(s.contains("Successfully changed data")) {
                try {
                    if(!imeiprezimeedit.getText().toString().equals("")) {
                        HelperClass.userconcat = imeiprezimeedit.getText().toString();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    if(!emailedit.getText().toString().equals("")) {
                        HelperClass.emailconcat = emailedit.getText().toString();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    if(!lozinkaedit.getText().toString().equals("")) {
                        HelperClass.passconcat = lozinkaedit.getText().toString();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String stringToJsonConverter(String user, String email, String password) {
        String jsonString=null;

        jsonString = "{\"name\":";
        if(user == null) {
            jsonString += user;
        }else {
            jsonString += "\""+user +"\"";
        }
        jsonString += ", \"email\":";
        if(email == null) {
            jsonString += email;
        }else {
            jsonString += "\""+email +"\"";
        }
        jsonString += ", \"password\":";
        if(password == null) {
            jsonString += password;
        }else {
            jsonString += "\""+password +"\"";
        }
        jsonString += "}";

        return jsonString;
    }
}
