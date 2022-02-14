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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Napredak extends AppCompatActivity {
    private ImageButton goTohomepage;
    private ImageButton goToBrojljudi;
    private ImageButton kartica;
    private ImageButton profil;
    private TextView brojdolazaka, d, h, m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_napredak);

        doGetBrojDolazakaRequest doSendHTTPLoginRequest = new doGetBrojDolazakaRequest();
        doSendHTTPLoginRequest.execute(HelperClass.herokuURL + HelperClass.brojdolazaka + HelperClass.idconcat);

        brojdolazaka=findViewById(R.id.brojdolazaka);
        d=findViewById(R.id.d);
        h=findViewById(R.id.h);
        m=findViewById(R.id.m);

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
                openKarticaActivity();
            }
        });

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

    public void openKarticaActivity() {
        Intent intent = new Intent (this, Kartica.class);
        startActivity(intent);
    }

    public class doGetBrojDolazakaRequest extends AsyncTask<String, String, String>
    {
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
            }catch (IOException e) {
                e.printStackTrace();
            }
            return "HTTP request failed";
        }

        @Override
        protected void onPostExecute(String s) {
            String brojDolazaka;
            String dani, sati, minute;
            Gson gson = new Gson();

            JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
            brojDolazaka = jsonObject.get("entries").toString();

            JsonObject jsonObject2 = gson.fromJson(s, JsonObject.class);
            dani = jsonObject2.getAsJsonObject("time").get("d").getAsString();
            sati = jsonObject2.getAsJsonObject("time").get("h").getAsString();
            minute = jsonObject2.getAsJsonObject("time").get("m").getAsString();
            brojdolazaka.setText(brojDolazaka);
            d.setText(dani);
            h.setText(sati);
            m.setText(minute);
        }
    }
}
