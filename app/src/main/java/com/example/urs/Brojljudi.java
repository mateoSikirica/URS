package com.example.urs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Brojljudi extends AppCompatActivity {
    private ImageButton pocetna;
    private ImageButton napredak;
    private ImageButton kartica;
    private ImageButton profil;
    TextView rez, rez2, rez3;
    ImageButton botun, botun2, botun3;
    ImageView group, group2, group3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_brojljudi);

        botun = findViewById(R.id.botun);
        botun2 = findViewById(R.id.botun2);
        botun3 = findViewById(R.id.botun3);

        group = findViewById(R.id.group);
        group2 = findViewById(R.id.group2);
        group3 = findViewById(R.id.group3);

        if(HelperClass.gymconcat1001==false) {
            botun.setVisibility(View.INVISIBLE);
        }
        if(HelperClass.gymconcat1002==false) {
            botun2.setVisibility(View.INVISIBLE);
        }
        if(HelperClass.gymconcat1003==false) {
            botun3.setVisibility(View.INVISIBLE);
        }
        if(HelperClass.gymconcat1001==false && HelperClass.gymconcat1002==false && HelperClass.gymconcat1003==false)
        {
            Toast.makeText(this, "Niste član niti jedne teretane!", Toast.LENGTH_SHORT).show();
        }

        kartica = findViewById(R.id.tvojakartica);
        rez = findViewById(R.id.rez);
        rez2 = findViewById(R.id.rez2);
        rez3 = findViewById(R.id.rez3);

        botun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetBrojLjudiRequest doGetBrojLjudiRequest = new doGetBrojLjudiRequest();
                doGetBrojLjudiRequest.execute(HelperClass.herokuURL + HelperClass.gym + "1001");
                group.setVisibility(View.VISIBLE);
            }
        });

        botun2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetBrojLjudiRequest doGetBrojLjudiRequest = new doGetBrojLjudiRequest();
                doGetBrojLjudiRequest.execute(HelperClass.herokuURL + HelperClass.gym + "1002");
                group2.setVisibility(View.VISIBLE);
            }
        });

        botun3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetBrojLjudiRequest doGetBrojLjudiRequest = new doGetBrojLjudiRequest();
                doGetBrojLjudiRequest.execute(HelperClass.herokuURL + HelperClass.gym + "1003");
                group3.setVisibility(View.VISIBLE);
            }
        });

        kartica=findViewById(R.id.tvojakartica);
        kartica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openKarticaActivity();
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

    public void openNapredakActivity() {
        Intent intent = new Intent (this, Napredak.class);
        startActivity(intent);
    }

    public void openKarticaActivity() {
        Intent intent = new Intent (this, Kartica.class);
        startActivity(intent);
    }

    public void openProfilActivity() {
        Intent intent = new Intent (this, Profil.class);
        startActivity(intent);
    }

    public class doGetBrojLjudiRequest extends AsyncTask<String, String, String>
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
            String currentlyInGym;
            String id;
            Gson gson = new Gson();

            JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
            currentlyInGym = jsonObject.get("currentlyInGym").toString();

            JsonObject jsonObject2 = gson.fromJson(s, JsonObject.class);
            id = jsonObject2.getAsJsonObject("gym").get("id").toString();
            if(id.equals("1001")) {
                rez.setText(currentlyInGym);
            } else if (id.equals("1002")) {
                rez2.setText(currentlyInGym);
            }else if (id.equals("1003")) {
                rez3.setText(currentlyInGym);
            }
        }
    }

}
