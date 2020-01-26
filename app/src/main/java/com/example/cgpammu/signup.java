package com.example.cgpammu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class signup extends AppCompatActivity {

    EditText edName, edPassword, edConfirmPassword, edEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edName = findViewById(R.id.username);
        edEmail = findViewById(R.id.email);
        edPassword = findViewById(R.id.password);
        edConfirmPassword = findViewById(R.id.secondpassword);
    }

    public void signin(View view) {
        Intent signin = new Intent(this, signin.class);
        startActivity(signin);
        CustomIntent.customType(this, "fadein-to-fadeout");
        finish();
    }

    public void signup(View view) {

        if( TextUtils.isEmpty(edName.getText())){
            edName.setError( "Name is required!" );
        }else if( TextUtils.isEmpty(edEmail.getText())){
            edEmail.setError( "Email is required!" );
        }else if( TextUtils.isEmpty(edPassword.getText())){
            edPassword.setError( "Password is required!" );
        }else if( TextUtils.isEmpty(edConfirmPassword.getText())){
            edConfirmPassword.setError( "Confirm password is required!" );
        }else if(!edPassword.getText().toString().equals(edConfirmPassword.getText().toString())){
            Toast.makeText(signup.this, "Password are not equal!", Toast.LENGTH_LONG).show();
        }else{

            final ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading ...");
            progress.show();
            ParseUser user = new ParseUser();
            user.setUsername(edEmail.getText().toString().trim());
            user.setEmail(edEmail.getText().toString().trim());
            user.setPassword(edPassword.getText().toString());
            user.put("name", edName.getText().toString().trim());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    progress.dismiss();
                    if (e == null) {
                        clearOldData();
                        Toast.makeText(signup.this, "Welcome!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(signup.this, setResultChoice.class);
                        startActivity(intent);
                        CustomIntent.customType(signup.this, "left-to-right");
                        finish();
                    } else {
                        ParseUser.logOut();
                        Toast.makeText(signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }



//        Intent uploadResultPage = new Intent(this, uploadResultPage.class);
//        startActivity(uploadResultPage);
//        finish();




    @Override
    public void onBackPressed(){
        Intent MainActivity = new Intent(this, MainActivity.class);
        startActivity(MainActivity);
        CustomIntent.customType(this, "fadein-to-fadeout");
        finish();
    }
    public void clearOldData(){
        ArrayList<String> empty=new ArrayList<>();
        saveData(empty,"goal");
        saveData(empty,"CourseStructure");
        saveData(empty,"studentInfo");
        saveData(empty,"resultTrimester");
        saveData(empty,"resultCode");
        saveData(empty,"resultSubName");
        saveData(empty,"resultGrade");
        saveData(empty,"studentGPAAndTotalHours");
        saveData(empty,"numOfSub");
    }
    public void saveData(ArrayList<String> savedData, String name){
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(savedData);
        editor.putString("task list", json);
        editor.apply();
    }

}
