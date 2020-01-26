package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class contactMe extends AppCompatActivity {


    public void gmail(View view){
        Uri uri = Uri.parse("https://mail.google.com/mail/"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);

    }

    @Override
    public void onBackPressed() {
        Intent MainActivity = new Intent(contactMe.this, MainPage.class);
        startActivity(MainActivity);
        finish();
    }
}
