package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class signin extends AppCompatActivity {

    EditText edEmail, edPassword;
    ArrayList<String> studentInfo=new ArrayList<>();

    public void createaccount(View view){
        Intent signup = new Intent(this, signup.class);
        startActivity(signup);
        CustomIntent.customType(this, "fadein-to-fadeout");
        finish();
    }

    public void forgotPassword(View view){
        Intent resetPassword = new Intent(this, resetPassword.class);
        startActivity(resetPassword);
        CustomIntent.customType(this, "fadein-to-fadeout");
        finish();
    }

    public void next(View view){

        if( TextUtils.isEmpty(edEmail.getText())){
            edEmail.setError( "Email is required!" );
        }else if( TextUtils.isEmpty(edPassword.getText())){
            edPassword.setError( "Password is required!" );
        }else{
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading ...");
            progress.show();
            ParseUser.logInInBackground(edEmail.getText().toString(), edPassword.getText().toString() , new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    progress.dismiss();
                    if (parseUser != null) {
                        clearOldData();
                        if(parseUser.getList("studentInfo")==null){
                            Intent setResultChoice = new Intent(signin.this, setResultChoice.class);
                            startActivity(setResultChoice);
                            finish();
                        }
                        loadUploudedData(parseUser);
                        Toast.makeText(signin.this, "Welcome back!", Toast.LENGTH_LONG).show();
                        Intent mainapp = new Intent(signin.this, MainPage.class);
                        startActivity(mainapp);
                        CustomIntent.customType(signin.this, "left-to-right");
                        finish();
                    } else {
                        ParseUser.logOut();
                        Toast.makeText(signin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edEmail = findViewById(R.id.emailLogin);
        edPassword = findViewById(R.id.passwordLogin);

    }

    @Override
    public void onBackPressed(){
        Intent launchGame = new Intent(this, MainActivity.class);
        startActivity(launchGame);
        CustomIntent.customType(this, "fadein-to-fadeout");
        finish();
    }

    public ArrayList<String> loadData(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void loadUploudedData(ParseUser user){
        if(user.getList("goal")!=null) {
            saveData(objectToString(user.getList("goal")), "goal");
        }
        saveData(objectToString(user.getList("CourseStructure")),"CourseStructure");
        saveData(objectToString(user.getList("studentInfo")),"studentInfo");
        saveData(objectToString(user.getList("resultTrimester")),"resultTrimester");
        saveData(objectToString(user.getList("resultCode")),"resultCode");
        saveData(objectToString(user.getList("resultSubName")),"resultSubName");
        saveData(objectToString(user.getList("resultGrade")),"resultGrade");
        saveData(objectToString(user.getList("studentGPAAndTotalHours")),"studentGPAAndTotalHours");
        saveData(objectToString(user.getList("numOfSub")),"numOfSub");
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
    public ArrayList<String> objectToString(List<Object> objects){
        ArrayList<String> strings = new ArrayList<>(objects.size());
        for (Object object : objects) {
            strings.add(object != null ? object.toString() : null);
        }
        return strings;
    }
}
