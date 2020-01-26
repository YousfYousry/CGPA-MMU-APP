package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {
    ParseUser user =ParseUser.getCurrentUser();
    ArrayList<String> studentInfo=new ArrayList<>();
//    VideoView videoView;

    public void signin(View view){
        Intent signin = new Intent(this, signin.class);
        startActivity(signin);
        CustomIntent.customType(this, "fadein-to-fadeout");
        finish();
    }
    public void signup(View view){
        Intent signup = new Intent(this, signup.class);
        startActivity(signup);
        CustomIntent.customType(this, "fadein-to-fadeout");
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        videoView = findViewById(R.id.videoView4);
//        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.instr;
//        Uri uri = Uri.parse(videoPath);
//        videoView.setVideoURI(uri);
//        videoView.start();

        if(user != null){
            if(user.getList("resultCode")!=null) {
                loadUploudedData();
                Intent mainapp = new Intent(MainActivity.this, MainPage.class);
                startActivity(mainapp);
                finish();
            }else{
                Intent setResultChoice = new Intent(MainActivity.this, setResultChoice.class);
                startActivity(setResultChoice);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed(){
        finish();
        System.exit(0);
    }

    public void loadUploudedData(){
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
    public void saveData(ArrayList<String> savedData, String name){
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(savedData);
        editor.putString("task list", json);
        editor.apply();
    }
    public ArrayList<String> loadData(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public ArrayList<String> objectToString(List<Object> objects){
        ArrayList<String> strings = new ArrayList<>(objects.size());
        for (Object object : objects) {
            strings.add(object != null ? object.toString() : null);
        }
        return strings;
    }

}
