package com.example.urs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private Button goToHomepage;
    public static EditText user, pass, email, passAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.register);

        user = findViewById(R.id.imeiprezime);
        pass = findViewById(R.id.lozinka);
        email = findViewById(R.id.emailident);
        passAgain = findViewById(R.id.ponoviLozinku);

        goToHomepage=findViewById(R.id.goToHomepage2);
        goToHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getText().toString().equals(passAgain.getText().toString()) && !pass.getText().toString().equals("") &&
                        !passAgain.getText().toString().equals("") && !user.getText().equals("") && !email.getText().toString().equals("")) {
                    DoSendHTTPRequest doSendHTTPRequest = new DoSendHTTPRequest();
                    doSendHTTPRequest.execute(HelperClass.herokuURL + HelperClass.register);
                } else if (!pass.getText().toString().equals(passAgain.getText().toString()) && !pass.getText().toString().equals("") &&
                        !passAgain.getText().toString().equals("") ){
                    Toast.makeText(Register.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openHomepageActivity() {
        Intent intent = new Intent (this, Homepage.class);
        startActivity(intent);
    }


    public class DoSendHTTPRequest extends AsyncTask<String, String, String>
    {
        String userstr = user.getText().toString();
        String emailstr = email.getText().toString();
        String passstr = pass.getText().toString();

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
             final MediaType JSON
                    = MediaType.get("text/plain; charset=utf-8");

            String jsonString = stringToJsonConverter(userstr, emailstr, passstr);

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(jsonString, JSON);
            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(body)
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
            if(s.contains("failed")) {
                return;
            }
            if(s.contains("exists")) {

            } else if(s.contains("created")) {
                openHomepageActivity();
            }

            String response, response2;
            response = s.substring(12);
            response2 = response.substring(0, response.length()-2);
            Toast.makeText(Register.this, response2, Toast.LENGTH_SHORT).show();
        }
    }

    public String stringToJsonConverter(String user, String email, String password) {
        String jsonString;
        jsonString = "{\"name\":" + "\""+user +"\"" + ", \"email\":" + "\"" + email + "\"" + ", \"password\":" + "\"" + password + "\"" + "}";

        return jsonString;
    }
}
