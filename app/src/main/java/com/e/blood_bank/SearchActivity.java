package com.e.blood_bank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SearchActivity extends AppCompatActivity {

   private  EditText et_city,et_bloodgroup;
   private Button find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //Intialize
        et_city = findViewById(R.id.city);
        et_bloodgroup = findViewById(R.id.bg);
        find = findViewById(R.id.Searchnow);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city=et_city.getText().toString();
                String blood_group=et_bloodgroup.getText().toString();

                if(city.isEmpty()||blood_group.isEmpty())
                {
                    Toast.makeText(SearchActivity.this, "Details can not be empty ", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    get_search_results(blood_group,city);
                }
            }
        });

    }

    private void get_search_results(final String blood_group,final String city)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.search_donors, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //json response
                Intent intent = new Intent(SearchActivity.this,Search_result.class);
                intent.putExtra("city",city);
                intent.putExtra("blood_group",blood_group);
                intent.putExtra("json",response);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "OOps something went wrong :(", Toast.LENGTH_SHORT).show();
                Log.d("Volley", Objects.requireNonNull(error.getMessage()));
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("city",city);
                params.put("blood_group",blood_group);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}

