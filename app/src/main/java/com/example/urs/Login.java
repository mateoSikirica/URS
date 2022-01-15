package com.example.urs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private Button goToRegister;
    private Button goToHomepage;
    private EditText email;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.login);
        goToRegister=findViewById(R.id.goToRegister);
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        email = (EditText) findViewById(R.id.emailID);
        pass = (EditText) findViewById(R.id.passID);

        goToHomepage=findViewById(R.id.goToHomepage);
        goToHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("admin") && pass.getText().toString().equals("admin")) {
                    openHomepageActivity();
                } else {
                    Toast.makeText(Login.this, "Krivo korisničko ime ili lozinka", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void openRegisterActivity() {
        Intent intent = new Intent (this, Register.class);
        startActivity(intent);
    }

    public void openHomepageActivity() {
        Intent intent = new Intent (this, Homepage.class);
        startActivity(intent);
    }

    /*protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hides the status and navigation bar until the user clicks
        // on the screeen.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }*/
}
