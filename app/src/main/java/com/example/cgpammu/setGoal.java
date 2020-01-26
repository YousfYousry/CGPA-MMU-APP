package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class setGoal extends AppCompatActivity {

    ArrayList<String> studentGPAAndTotalHours = new ArrayList<>();

    ParseUser user =ParseUser.getCurrentUser();

    public void Enter(View view) {
        EditText tx = (EditText) findViewById(R.id.goal);
        if (TextUtils.isEmpty(tx.getText())) {
            tx.setError("CGPA is required!");
        } else {
            float amount = Float.valueOf(tx.getText().toString());
            ArrayList<String> goal = new ArrayList<>();
            goal.add(Float.toString((float) amount));
            saveData(goal, "goal");
            Intent mainapp = new Intent(this, MainPage.class);
            startActivity(mainapp);
            finish();
        }
    }

    public void first(View view){
        EditText myText = (EditText) this.findViewById(R.id.goal);
        myText.setText( "3.67" );
    }
    public void second_upper(View view){
        EditText myText = (EditText) this.findViewById(R.id.goal);
        myText.setText( "3.33" );
    }
    public void second_lower(View view){
        EditText myText = (EditText) this.findViewById(R.id.goal);
        myText.setText( "2.67" );
    }
    public void third(View view){
        EditText myText = (EditText) this.findViewById(R.id.goal);
        myText.setText( "2.00" );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);

        studentGPAAndTotalHours = loadData("studentGPAAndTotalHours");


        TextView CGPA = (TextView) this.findViewById(R.id.CGPASetGoal);
        CGPA.setText(Double.toString(numricalOf(studentGPAAndTotalHours.get(0))));

    }
    @Override
    public void onBackPressed() {
        Intent pdf_to_text = new Intent(this, MainPage.class);
        startActivity(pdf_to_text);
        finish();
    }
    public void saveData( ArrayList<String> savedData,String name){
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(savedData);
        editor.putString("task list", json);
        editor.apply();
        user.put(name,savedData);
        user.saveInBackground();
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
    public boolean isnumrical(String str){
        boolean numeric=true;
        try {
            Double num = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        return numeric;
    }
}
