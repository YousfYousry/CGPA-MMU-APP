package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import maes.tech.intentanim.CustomIntent;

public class resetPassword extends AppCompatActivity {
    EditText edEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        edEmail = findViewById(R.id.resetPassword);
    }

    public void resetpassword(View view) {
        if( TextUtils.isEmpty(edEmail.getText())){
            edEmail.setError( "Email is required!" );
        }else {
            ParseUser.requestPasswordResetInBackground(edEmail.getText().toString(), new RequestPasswordResetCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // An email was successfully sent with reset instructions.
                        Toast.makeText(resetPassword.this, "An email was successfully sent with reset instructions.", Toast.LENGTH_LONG).show();
                    } else {
                        // Something went wrong. Look at the ParseException to see what's up.
                        Toast.makeText(resetPassword.this, "Something went wrong. Look at the ParseException to see what's up.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        Intent signin = new Intent(this, signin.class);
        startActivity(signin);
        CustomIntent.customType(this, "fadein-to-fadeout");
        finish();
    }

}
