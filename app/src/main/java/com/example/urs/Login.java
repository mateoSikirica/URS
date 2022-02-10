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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        email = findViewById(R.id.emailID);
        pass = findViewById(R.id.passID);

        goToHomepage=findViewById(R.id.goToHomepage);
        goToHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoSendHTTPLoginRequest doSendHTTPLoginRequest = new DoSendHTTPLoginRequest();
                doSendHTTPLoginRequest.execute(HelperClass.herokuURL + HelperClass.login);
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

    public class DoSendHTTPLoginRequest extends AsyncTask<String, String, String>
    {
        String emailstr = email.getText().toString();
        String passstr = pass.getText().toString();

        String userstr;

        public String getUserstr() {
            return userstr;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            final MediaType JSON
                    = MediaType.get("text/plain; charset=utf-8");

            String jsonString = stringToJsonConverter(emailstr, passstr);

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
            String user = null;
            if(s.contains("failed")) {
                return;
            }
            if(s.contains("Success")) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
                user = jsonObject.getAsJsonObject("user").get("name").toString();
                HelperClass.userconcat = user.substring(1, user.length() - 1);
                openHomepageActivity();
            } else if(s.contains("Wrong")) {
                String response, response2;
                response = s.substring(12);
                response2 = response.substring(0, response.length()-2);
                Toast.makeText(Login.this, response2, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String stringToJsonConverter(String email, String password) {
        String jsonString;
        jsonString = "{\"email\":" + "\"" + email + "\"" + ", \"password\":" + "\"" + password + "\"" + "}";

        return jsonString;
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
