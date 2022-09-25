package com.e.blood_bank;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.blood_bank.Utils.Endpoints;
import com.e.blood_bank.Utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {

    private Button login;
    private EditText lpass, lphone;
    private TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.Login);
        lpass = findViewById(R.id.editpass);
        lphone = findViewById(R.id.editphn);
        forgot = findViewById(R.id.forgot);


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this, "On the way...", Toast.LENGTH_SHORT).show();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

              lpass.setError(null);
              lphone.setError(null);

              String pass = lpass.getText().toString();
              String number = lphone.getText().toString();

              if(isValid(number,pass))
              {
                  loginto(number,pass);

              }
            }
        });

    }

    //Using Volley lib to give and take data

   private void loginto(final String number,final String pass)
   {
       StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.login_url, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {

               if (!response.trim().equals("Invalid Credentials")) {
                   Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(Login.this, BloodActivity.class));
                   PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("number", number).apply();
                   PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("number2", number).apply();
                   PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city", response).apply();
                   Login.this.finish();
               }
               else {
                   Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(Login.this, "OOps something went wrong :(", Toast.LENGTH_SHORT).show();
               Log.d("Volley",Objects.requireNonNull(error.getMessage()));
           }
   })
       {
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> params = new HashMap<>();
               params.put("number",number);
               params.put("password",pass);
               return params;
           }
       };
       VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
   }

    private boolean isValid(String number,String pass)
    {
        if(number.isEmpty())
        {
            Toast.makeText(this, "Fields can not be blank...", Toast.LENGTH_SHORT).show();
            lphone.setError("Empty number");
            return  false;
        }
        else if(pass.isEmpty())
        {
            Toast.makeText(this, "Fields can not be blank...", Toast.LENGTH_SHORT).show();
            lpass.setError("Empty password");
            return  false;
        }
      return  true;

    }
}
