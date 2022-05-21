package com.mayuresh08.sociogrevier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class categories_with_comp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_with_comp);
    }

    public void showallpolicecomp_txt(View view) {
        Intent intent5 = new Intent(categories_with_comp.this, show_police_comp.class);
        startActivity(intent5);
    }

    public void showallhospcomp_txt(View view) {
        Intent intent5 = new Intent(categories_with_comp.this, sho_hosp_complaint.class);
        startActivity(intent5);
    }

    public void showallcovidcomp_txt(View view) {
        Intent intent5 = new Intent(categories_with_comp.this, show_covid_complaints.class);
        startActivity(intent5);
    }

    public void showallBloodcomp_txt(View view) {
        Intent intent5 = new Intent(categories_with_comp.this, show_blood_complaints.class);
        startActivity(intent5);
    }
}
