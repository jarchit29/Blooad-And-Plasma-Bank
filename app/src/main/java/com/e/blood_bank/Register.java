package com.e.blood_bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextSelection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.e.blood_bank.Utils.Endpoints;
import com.e.blood_bank.Utils.VolleySingleton;
//import com.e.blood_bank.Utils.VolleySingleton;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private Button register ;
    private EditText rpass,rphone,rcity,rname,rbloodg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize..
        register = findViewById(R.id.Register);
        rpass = findViewById(R.id.reditpass);
        rphone= findViewById(R.id.reditphone);
        rcity = findViewById(R.id.reditcity);
        rname = findViewById(R.id.reditname);
        rbloodg = findViewById(R.id.reditbg);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rpass.getText().toString().isEmpty()||rphone.getText().toString().isEmpty()
                        ||rname.getText().toString().isEmpty()||rbloodg.getText().toString().isEmpty()
                        ||rcity.getText().toString().isEmpty())
                {
                    Toast.makeText(Register.this, "FILL THE DETAILS...", Toast.LENGTH_SHORT).show();

                }
                else if (rphone.getText().toString().length()!=10)
                {
                    Toast.makeText(Register.this, "Give Valid phone number...", Toast.LENGTH_SHORT).show();

                }
                else if (rbloodg.getText().toString().length()>=5)
                {
                    Toast.makeText(Register.this, "Blood Group example :- b+", Toast.LENGTH_SHORT).show();

                }

                else
                {

                    String name,pass,blood,number,city;

                    name = rname.getText().toString();
                    pass = rpass.getText().toString();
                    number = rphone.getText().toString();
                    city= rcity.getText().toString();
                    blood = rbloodg.getText().toString();

                    register(name,pass,number,city,blood);
                    //Handling this stuff below
                    //PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city", city).apply();
                   Intent intent = new Intent(Register.this, Login.class);
                   startActivity(intent);

                   Register.this.finish();
                }


            }
        });
    }
    //Using Volley lib to give and take data

    private void register(final String name , final String pass, final String number, final String city , final String blood )
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city",city).apply();

                /* Below code is not working
                if(response.equals("success"))
                {

                    Toast.makeText(Register.this,"Processing", Toast.LENGTH_SHORT).show();
                    /*
                    Intent intent = new Intent(Register.this,Login.class);
                    startActivity(intent);
                    Register.this.finish();

                }
                else
                {
                  Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();

                }
                */

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, "OOPS :( Something went WRONG", Toast.LENGTH_SHORT).show();
                // error message print
                Log.d("VOLLEY", error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params= new HashMap<>();
                params.put("name",name);
                params.put("password",pass);
                params.put("number",number);
                params.put("city",city);
                params.put("blood_group",blood); // These key k are the vales from php file


                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

}
