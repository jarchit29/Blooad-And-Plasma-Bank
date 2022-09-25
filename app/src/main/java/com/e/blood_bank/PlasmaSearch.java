package com.e.blood_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PlasmaSearch extends AppCompatActivity {

    private EditText et_city,et_bloodgroup;
    private Button find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plasma_search);

        et_city = findViewById(R.id.pcity);
        et_bloodgroup = findViewById(R.id.pbg);
        find = findViewById(R.id.PSearchnow);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city=et_city.getText().toString();
                String blood_group=et_bloodgroup.getText().toString();

                if(city.isEmpty()||blood_group.isEmpty())
                {
                    Toast.makeText(PlasmaSearch.this, "Details can not be empty ", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    //get_search_results(blood_group,city);
                }
            }
        });


    }
}
