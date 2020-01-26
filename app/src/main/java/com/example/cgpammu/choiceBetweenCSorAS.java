package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class choiceBetweenCSorAS extends AppCompatActivity {

    public void cs(View view){
        Intent semester_planner = new Intent(this, semester_planner.class);
        startActivity(semester_planner);
        finish();
    }

    public void av(View view){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_between_csor_as);
    }
    @Override
    public void onBackPressed() {
        Intent mainapp = new Intent(this, MainPage.class);
        startActivity(mainapp);
        finish();
    }
}
