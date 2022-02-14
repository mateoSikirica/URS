package com.example.urs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Kartica extends AppCompatActivity {
    ImageButton nfccitac1, nfccitac2, nfccitac3;
    ImageButton pocetna, brojljudi, napredak, profil;
    private LinearLayout guliver, joker, champion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.kartica);

        guliver = findViewById(R.id.guliver);
        joker = findViewById(R.id.joker);
        champion = findViewById(R.id.champion);

        if(HelperClass.gymconcat1001==false) {
            guliver.setVisibility(View.INVISIBLE);
        }
        if(HelperClass.gymconcat1002==false) {
            joker.setVisibility(View.INVISIBLE);
        }
        if(HelperClass.gymconcat1003==false) {
            champion.setVisibility(View.INVISIBLE);
        }
        if(HelperClass.gymconcat1001==false && HelperClass.gymconcat1002==false && HelperClass.gymconcat1003==false)
        {
            Toast.makeText(this, "Niste član niti jedne teretane!", Toast.LENGTH_SHORT).show();
        }

        nfccitac1=findViewById(R.id.nfccitac1);
        nfccitac1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kartica.DoSendHTTPLogUserRequest doSendHTTPLogUserRequest = new Kartica.DoSendHTTPLogUserRequest();
                doSendHTTPLogUserRequest.execute(HelperClass.herokuURL + HelperClass.timelog1 + HelperClass.idconcat + HelperClass.timelog2 + "1001");
            }
        });

        nfccitac2=findViewById(R.id.nfccitac2);
        nfccitac2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kartica.DoSendHTTPLogUserRequest doSendHTTPLogUserRequest = new Kartica.DoSendHTTPLogUserRequest();
                doSendHTTPLogUserRequest.execute(HelperClass.herokuURL + HelperClass.timelog1 + HelperClass.idconcat + HelperClass.timelog2 + "1002");
            }
        });

        nfccitac3=findViewById(R.id.nfccitac3);
        nfccitac3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kartica.DoSendHTTPLogUserRequest doSendHTTPLogUserRequest = new Kartica.DoSendHTTPLogUserRequest();
                doSendHTTPLogUserRequest.execute(HelperClass.herokuURL + HelperClass.timelog1 + HelperClass.idconcat + HelperClass.timelog2 + "1003");
            }
        });

        brojljudi=findViewById(R.id.brojljudi);
        brojljudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrojljudiActivity();
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

        profil=findViewById(R.id.profil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfilActivity();
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

    public void openProfilActivity() {
        Intent intent = new Intent (this, Profil.class);
        startActivity(intent);
    }


    public class DoSendHTTPLogUserRequest extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .get()
                    .build();
            try {
                try (Response response = client.newCall(request).execute()) {
                    return response.body().string();
                }
            } catch (IOException e) {
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

            Toast.makeText(Kartica.this, messageConcat, Toast.LENGTH_SHORT).show();
        }
    }
}
