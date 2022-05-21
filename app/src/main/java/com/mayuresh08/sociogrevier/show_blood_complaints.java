package com.mayuresh08.sociogrevier;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class show_blood_complaints extends AppCompatActivity {


    RecyclerView recview_b;
    myadapter_b adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_blood_complaints);

        recview_b=(RecyclerView)findViewById(R.id.rec_view_b);
        recview_b.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<model_b> options=
                new FirebaseRecyclerOptions.Builder<model_b>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Blood_posts"),model_b.class)
                        .build();

        adapter=new myadapter_b(options);
        recview_b.setAdapter(adapter);

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
