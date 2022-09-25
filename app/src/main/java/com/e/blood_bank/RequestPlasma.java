package com.e.blood_bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class RequestPlasma extends AppCompatActivity {

    private Button post;
    private EditText reqmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_plasma);

        //Initialize
        post = findViewById(R.id.pPostReq);
        reqmessage = findViewById(R.id.pmessageReq);

        //On click listeners (Below is a sort of copied code from Request_blood)

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid()) {
                    //Code
                    // uploadRequest(reqmessage.getText().toString);
                    String rmessage, number;

                    number = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("number2", "12345");
                    rmessage = reqmessage.getText().toString();
                    Toast.makeText(RequestPlasma.this, "Your contact Number is "+number, Toast.LENGTH_SHORT).show();
                    reqpost(number, rmessage);
                    Intent intent = new Intent(RequestPlasma.this, BloodActivity.class);
                    startActivity(intent);
                    RequestPlasma.this.finish();

                }

            }
        });
    }

    //Using volley below

    private void reqpost(final String number, final String rmessage) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.p_upload_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(RequestPlasma.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RequestPlasma.this, "OOPS :( Something went WRONG", Toast.LENGTH_SHORT).show();
                // error message print
                Log.d("VOLLEY", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("message", rmessage);
                params.put("number", number); // These key k are the vales from php file


                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    private boolean isValid() {
        if (reqmessage.getText().toString().isEmpty()) {
            showMessage("Message field can not be blank");
            return false;
        }

        return true;

    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}