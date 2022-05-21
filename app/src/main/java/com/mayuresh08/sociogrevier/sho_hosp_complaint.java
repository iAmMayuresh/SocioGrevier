package com.mayuresh08.sociogrevier;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class sho_hosp_complaint extends AppCompatActivity {

    RecyclerView recview_h;
    myadapter_ho adapter_ho;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sho_hosp_complaint);


        recview_h=(RecyclerView)findViewById(R.id.rec_view_h);
        recview_h.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<model_ho> options=
                new FirebaseRecyclerOptions.Builder<model_ho>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Hosp_posts"),model_ho.class)
                        .build();

        adapter_ho=new myadapter_ho(options);
        recview_h.setAdapter(adapter_ho);

    }
    @Override
    protected void onStart()
    {
        super.onStart();
        adapter_ho.startListening();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        adapter_ho.stopListening();
    }
}
