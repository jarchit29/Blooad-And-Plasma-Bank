package com.e.blood_bank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.blood_bank.Adapter.SearchAdapter;
import com.e.blood_bank.DataModela.Donor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Search_result extends AppCompatActivity {

    List<Donor> donorlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        donorlist = new ArrayList<>();
        String blood_group,city,json;
        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        city = intent.getStringExtra("city");
        blood_group = intent.getStringExtra("blood_group");
        TextView heading = findViewById(R.id.Header);
       String str = "Donors in "+city+" with blood group "+blood_group;
       heading.setText(str);

        Gson gson= new Gson();
        Type type = new TypeToken<List<Donor>>(){}.getType();
        List<Donor> dataModels = gson.fromJson(json,type);

        if(dataModels!=null&&dataModels.isEmpty())
        {
            heading.setText("No results found ");

        }
        else if(dataModels!=null) {
            donorlist.addAll(dataModels);
        }


        RecyclerView recyclerView = findViewById(R.id.RecycleSearch);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        SearchAdapter adapter = new SearchAdapter(donorlist,Search_result.this);
        recyclerView.setAdapter(adapter);
    }
}
