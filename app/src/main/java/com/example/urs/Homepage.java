package com.example.urs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Homepage extends AppCompatActivity  {
    private ImageButton brojljudi;
    private ImageButton napredak;
    private ImageButton kartica;
    private ImageButton profil;
    private TextView ime;
    private LinearLayout guliver, joker, champion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.homepage);

        ime = findViewById(R.id.ime);
        ime.setText("Welcome,\n" + HelperClass.userconcat);

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

        kartica = findViewById(R.id.tvojakartica);
        kartica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKarticaActivity();
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

        profil = findViewById(R.id.profil);
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

    public void openKarticaActivity() {
        Intent intent = new Intent (this, Kartica.class);
        startActivity(intent);
    }

    public void openProfilActivity() {
        Intent intent = new Intent (this, Profil.class);
        startActivity(intent);
    }
}
