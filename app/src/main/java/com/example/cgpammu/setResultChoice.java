package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class setResultChoice extends AppCompatActivity {

    public void uploadFromCamsys(View view){
        Intent uploadResultPage = new Intent(this, uploadResultPage.class);
        startActivity(uploadResultPage);
        finish();
    }

    public void manually(View view){
        Intent set_manually = new Intent(this, set_manually.class);
        startActivity(set_manually);
        finish();
    }

    public void skip(View view){
        Intent mainapp = new Intent(this, MainPage.class);
        startActivity(mainapp);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_result_choice);
    }

    @Override
    public void onBackPressed() {
        Intent mainapp = new Intent(setResultChoice.this, MainPage.class);
        startActivity(mainapp);
        finish();
    }
}
