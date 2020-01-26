package com.example.cgpammu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;
import java.lang.reflect.Type;
import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class MainPage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<String> StudentInfo = new ArrayList<>();
    ArrayList<String> studentGPAAndTotalHours = new ArrayList<>();
    ArrayList<String> resultCode = new ArrayList<>();
    ArrayList<String> goalArr = new ArrayList<>();
    private long backPressedTime;
    private Toast backToast;
    private DrawerLayout drawer;
    boolean features=false;
    private static ArrayList<String> array = new ArrayList<>();
    double goal;
    int i=0;

    public void semPlanner(View view) {
        if (features) {

            Intent semester_planner = new Intent(this, semester_planner.class);
            startActivity(semester_planner);
            CustomIntent.customType(this, "left-to-right");
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "set goal first", Toast.LENGTH_LONG).show();
        }
    }
    public void retakeSubject(View view) {
        if (features) {
            Intent retakeSubPlanner = new Intent(this, retakeSubPlanner.class);
            startActivity(retakeSubPlanner);
            CustomIntent.customType(this, "left-to-right");
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "set goal first", Toast.LENGTH_LONG).show();
        }
    }
    public void both(View view){
        Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show();
    }
    public void AllFoe(View view){
        Toast.makeText(this, "Coming soon", Toast.LENGTH_LONG).show();
    }
    public void DegreePlanner(View view) {
        if (features) {
            Intent degreePlanner = new Intent(this, degreePlanner.class);
            startActivity(degreePlanner);
            CustomIntent.customType(this, "left-to-right");
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "set goal first", Toast.LENGTH_LONG).show();
        }
    }
    public void availablesub(View view){
        if(features) {
            Intent availableSub = new Intent(this, availableSub.class);
            startActivity(availableSub);
            CustomIntent.customType(this, "left-to-right");
        }else{
            Toast.makeText(getApplicationContext(),"set goal first",Toast.LENGTH_LONG).show();
        }
    }
    public void uploadDiff(View view){
        Intent uploadResultPage = new Intent(this, uploadResultPage.class);
        startActivity(uploadResultPage);
        finish();
    }
    public void gpacalculator(View view){
        Intent cgpa_to_grads_and_op = new Intent(this, grades_and_gpa_calc.class);
        startActivity(cgpa_to_grads_and_op);
        CustomIntent.customType(this, "left-to-right");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainapp);

        StudentInfo = loadData("studentInfo");
        studentGPAAndTotalHours = loadData("studentGPAAndTotalHours");
        resultCode = loadData("resultCode");
        goalArr = loadData("goal");
        if(goalArr.size()>0) {
            goal = Double.parseDouble(goalArr.get(0));
        }
        if(goal!=0){
            features=true;
        }
        resultCode.add("77");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new fragmentPlanner()).commit();
            navigationView.setCheckedItem(R.id.nav_Planner);
        }


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.usernameFordrawer);
        TextView navID = (TextView) headerView.findViewById(R.id.IDFordrawer);

        if(i<StudentInfo.size()) {
            navID.setText(StudentInfo.get(0));
            i++;
        }

        if(i<StudentInfo.size()) {
            navUsername.setText(StudentInfo.get(1));
            i++;
        }
//        TextView ID = (TextView) nav_header.findViewById(R.id.IDFordrawer);
//        ID.setText(StudentInfo.get(0));
//        TextView name = (TextView) nav_header.findViewById(R.id.usernameFordrawer);
//        name.setText(StudentInfo.get(1));
//        TextView CGPA = (TextView) this.findViewById(R.id.cgpa);
//        CGPA.setText(Double.toString(numricalOf(studentGPAAndTotalHours.get(0))));
//        TextView TOTALHOURS = (TextView) this.findViewById(R.id.totalHours);
//        TOTALHOURS.setText(Integer.toString(IntegerNumricalOf(studentGPAAndTotalHours.get(1))));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_Planner:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragmentPlanner()).commit();
                break;
            case R.id.nav_Calculator:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragmentcalculator()).commit();
                break;
            case R.id.nav_course:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragmentcourse()).commit();
                break;
            case R.id.nav_goal:
                if(resultCode.get(0).compareTo("77")!=0) {
                    Intent setGoal = new Intent(MainPage.this, setGoal.class);
                    startActivity(setGoal);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Upload result first",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_Upload:
                    Intent setResultChoice = new Intent(MainPage.this, setResultChoice.class);
                    startActivity(setResultChoice);
                    finish();
                break;
            case R.id.nav_contact:
                Intent contactMe = new Intent(MainPage.this, contactMe.class);
                startActivity(contactMe);
                finish();
                break;
            case R.id.nav_out:
                ProgressDialog progress = new ProgressDialog(this);
                progress.setMessage("Loading ...");
                progress.show();
                ParseUser.logOut();
                clearOldData();
                Intent MainActivity = new Intent(MainPage.this, MainActivity.class);
                startActivity(MainActivity);
                CustomIntent.customType(this, "right-to-left");
                finish();
                progress.dismiss();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                super.onBackPressed();
                return;
            } else {
                backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }

            backPressedTime = System.currentTimeMillis();
        }
    }

    public ArrayList<String> loadData(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public double numricalOf(String numrical){
        String[] temp = numrical.split(" ",-1);
        for(int i=0;i<temp.length;i++) {
            if (isnumrical(temp[i])) {
                return Double.parseDouble(temp[i]);
            }
        }
        return 0;
    }
    public int IntegerNumricalOf(String numrical){
        String[] temp = numrical.split(" ",-1);
        for(int i=0;i<temp.length;i++) {
            if (isnumrical(temp[i])) {
                return Integer.parseInt(temp[i]);
            }
        }
        return 0;
    }
    public boolean isnumrical(String str){
        boolean numeric=true;
        try {
            Double num = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        return numeric;
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
