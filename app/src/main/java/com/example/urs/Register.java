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

public class Register extends AppCompatActivity {
    private Button goToHomepage;
    public static EditText user, pass, email;
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

        goToHomepage=findViewById(R.id.goToHomepage2);
        goToHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoSendHTTPRequest doSendHTTPRequest = new DoSendHTTPRequest();
                doSendHTTPRequest.execute(HelperClass.herokuURL + HelperClass.register);
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
            HelperClass.userconcat = userstr;
            HelperClass.emailconcat = emailstr;
            HelperClass.passconcat = passstr;
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
            Gson gson1 = new Gson();
            String message = null;
            String id = null;
            String messageConcat = null;
            JsonObject jsonObject1 = gson1.fromJson(s, JsonObject.class);

            if(s.contains("created")) {
                id = jsonObject1.getAsJsonObject("createdUser").get("id").toString();
                HelperClass.idconcat = id;
                openHomepageActivity();
            }
            message = jsonObject1.get("message").toString();
            messageConcat = message.substring(1, message.length() -1);

            Toast.makeText(Register.this, messageConcat, Toast.LENGTH_SHORT).show();
        }
    }

    public String stringToJsonConverter(String user, String email, String password) {
        String jsonString;
        jsonString = "{\"name\":" + "\""+user +"\"" + ", \"email\":" + "\"" + email + "\"" + ", \"password\":" + "\"" + password + "\"" + "}";

        return jsonString;
    }
}
