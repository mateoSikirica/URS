package com.example.urs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Homepage extends AppCompatActivity {
    private ImageButton brojljudi;
    private ImageButton napredak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.homepage);


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
}
