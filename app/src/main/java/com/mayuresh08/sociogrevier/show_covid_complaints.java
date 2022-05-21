package com.mayuresh08.sociogrevier;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class show_covid_complaints extends AppCompatActivity {

    RecyclerView recview_c;
    myadapter_c adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_covid_complaints);

        recview_c=(RecyclerView)findViewById(R.id.rec_view_c);
        recview_c.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<model_c> options=
                new FirebaseRecyclerOptions.Builder<model_c>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Covid_posts"),model_c.class)
                        .build();

        adapter=new myadapter_c(options);
        recview_c.setAdapter(adapter);

    }
    @Override
    protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}
