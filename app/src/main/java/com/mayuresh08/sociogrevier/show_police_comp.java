package com.mayuresh08.sociogrevier;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class show_police_comp extends AppCompatActivity {

    RecyclerView recview_p;
    myadapter_p adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_police_comp);

        recview_p=(RecyclerView)findViewById(R.id.rec_view_p);
        recview_p.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<model_p> options=
                new FirebaseRecyclerOptions.Builder<model_p>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Police_posts"),model_p.class)
                .build();

        adapter=new myadapter_p(options);
        recview_p.setAdapter(adapter);

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
