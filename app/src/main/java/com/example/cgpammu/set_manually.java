package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class set_manually extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String[] titles = new String[]{"Choose your major", "Electronics",
            "Computer", "Telecommunications", "Electrical", "Nanotechnology"};

    public static final Integer[] images = {R.drawable.major, R.drawable.electronics,
            R.drawable.computer, R.drawable.telecommunication, R.drawable.electrical, R.drawable.nano};
    ArrayList<String> saved_student_info = new ArrayList<>();
    ArrayList<String> saved_gpa_and_totalHours = new ArrayList<>();
    ArrayList<String> saved_result_Trimester = new ArrayList<>();

    Spinner spinner,spinner2,spinner3;
    List<spinnercourseStructure> rowItems;
    TextInputEditText name, id, CGPA, TotalHours;
    int checkCounter = 0;
    String Intake="Intake", Year="Year",Major="Choose your major";
    int sem=0;
    ArrayList<String> courseStructure = new ArrayList<>();
    ArrayList<String> result_Trimester = new ArrayList<>();
    ArrayList<String> student_info_to_save = new ArrayList<>();
    ArrayList<String> gpa_and_totalHours = new ArrayList<>();
    ParseUser user =ParseUser.getCurrentUser();
    LinearLayout spin2,spin3;

    public void ClearAll(View view){
        name.setText("");
        id.setText("");
        CGPA.setText("");
        TotalHours.setText("");
        spinner.setSelection(0);
        spinner2.setSelection(0);
        spinner3.setSelection(0);
        Toast.makeText(this, "All fields cleared", Toast.LENGTH_SHORT).show();
    }
    public void enterFormanually(View view) {
        checkCounter = 0;
        if (TextUtils.isEmpty(name.getText())) {
            name.setError("Name is required!");
        } else {
            checkCounter++;
        }
        if (TextUtils.isEmpty(id.getText())) {
            id.setError("Id is required!");
        } else {
            checkCounter++;
        }

        if (TextUtils.isEmpty(CGPA.getText())) {
            CGPA.setError("CGPA is required!");
        } else if(!gpaAvailable(CGPA.getText().toString())) {
            CGPA.setError("CGPA is wrong");
        }else{
            checkCounter++;
        }

        if (TextUtils.isEmpty(TotalHours.getText())) {
            TotalHours.setError("Hours finished is required!");
        } else if(!hoursAvailable(TotalHours.getText().toString())) {
            TotalHours.setError("Hours are too much");
        }else{
            checkCounter++;
        }

        if (isFound("Choose",Major)) {
            Toast.makeText(getApplicationContext(),"Choose your major",Toast.LENGTH_SHORT).show();
            spinner.setBackgroundColor(Color.RED);
        }else{
            checkCounter++;
        }

        if (isFound("Year",Year)) {
            Toast.makeText(getApplicationContext(),"Year is required!",Toast.LENGTH_SHORT).show();
            spin2.setBackgroundColor(Color.RED);
        }else{
            checkCounter++;
        }
        if ("Intake".compareTo(Intake)==0) {
            Toast.makeText(getApplicationContext(),"Intake is required!",Toast.LENGTH_SHORT).show();
            spin3.setBackgroundColor(Color.RED);
        }else{
            checkCounter++;
        }



        if(checkCounter>=7) {
            if (0 == Double.parseDouble(CGPA.getText().toString())|| 0 == Integer.parseInt( TotalHours.getText().toString())) {
                if (0 == Double.parseDouble(CGPA.getText().toString())&& 0 == Integer.parseInt( TotalHours.getText().toString())) {

                    GetCourseStructure();
                    saveInfoInArrays();
                    saveInfo();

                    Intent mainapp = new Intent(this, MainPage.class);
                    startActivity(mainapp);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"CGPA and hours are wrong",Toast.LENGTH_SHORT).show();
                    checkCounter-=7;
                }
            }else{

                GetCourseStructure();
                saveInfoInArrays();
                saveInfo();
                Intent setGradesManually = new Intent(this, setGradesManually.class);
                startActivity(setGradesManually);
                finish();

            }
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_manually);

        name = (TextInputEditText) findViewById(R.id.name);
        id = (TextInputEditText) findViewById(R.id.id);
        CGPA = (TextInputEditText) findViewById(R.id.CGPA);
        TotalHours = (TextInputEditText) findViewById(R.id.TotalHours);
        spin2 = (LinearLayout) findViewById(R.id.spin2);
        spin3 = (LinearLayout) findViewById(R.id.spin3);

        rowItems = new ArrayList<spinnercourseStructure>();
        for (int i = 0; i < titles.length; i++) {
            spinnercourseStructure item = new spinnercourseStructure(titles[i], images[i]);
            rowItems.add(item);
        }

        spinner = findViewById(R.id.spinner);
        spinnerAdapter adapter = new spinnerAdapter(set_manually.this,
                R.layout.coursestructure, R.id.title, rowItems);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        spinner3 = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.Intake, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);

        saved_student_info = loadData("studentInfo");
        saved_gpa_and_totalHours = loadData("studentGPAAndTotalHours");
        saved_result_Trimester = loadData("resultTrimester");
        if(saved_student_info.size()>0) {
            if (saved_student_info.get(1) != null && !saved_student_info.get(1).isEmpty()) {
                name.setText(saved_student_info.get(1));
            }
            if (saved_student_info.get(0) != null && !saved_student_info.get(0).isEmpty()) {
                id.setText(clearFromSpacing(saved_student_info.get(0)));
            }
            if (saved_gpa_and_totalHours.get(0) != null && !saved_gpa_and_totalHours.get(0).isEmpty()) {
                CGPA.setText(clearFromSpacing(saved_gpa_and_totalHours.get(0)));
            }
            if (saved_gpa_and_totalHours.get(1) != null && !saved_gpa_and_totalHours.get(1).isEmpty()) {
                TotalHours.setText(clearFromSpacing(saved_gpa_and_totalHours.get(1)));
            }
            if (saved_student_info.get(2) != null && !saved_student_info.get(2).isEmpty()) {
                int major = 0;
                if (isFound("computer", saved_student_info.get(2).toLowerCase())) {
                    major = 2;
                } else if (isFound("telecommunications", saved_student_info.get(2).toLowerCase())) {
                    major = 3;
                } else if (isFound("electrical", saved_student_info.get(2).toLowerCase())) {
                    major = 4;
                } else if (isFound("nanotechnology", saved_student_info.get(2).toLowerCase())) {
                    major = 5;
                } else if (isFound("electronics", saved_student_info.get(2).toLowerCase())) {
                    major = 1;
                }
                spinner.setSelection(major);
            }
            if(saved_result_Trimester.size()>0) {
                if (saved_result_Trimester.get(0) != null && !saved_result_Trimester.get(0).isEmpty()) {
                    int year = 0, trim = 0;
                    int yearCounter = 0;
                    for (int i = 0; i < saved_result_Trimester.size(); i++) {
                        if (isFound("trimester", saved_result_Trimester.get(i).toLowerCase())) {
                            trim = TrimesterOf(saved_result_Trimester.get(i));
                            year = YearOf(saved_result_Trimester.get(i));
                            break;
                        }
                    }
                    for (int start = 2021; start > year; start--) {
                        yearCounter++;
                    }
                    spinner2.setSelection(yearCounter);
                    spinner3.setSelection(trim);

                }
            }else{
                result_Trimester = loadData("TrimesterSemAndYear");
                if(result_Trimester.size()>0) {
                    if (result_Trimester.get(0) != null && !result_Trimester.get(0).isEmpty()) {
                        int year = Integer.parseInt(result_Trimester.get(0)), trim = Integer.parseInt(result_Trimester.get(1));
                        int yearCounter = 0;
                        for (int start = 2021; start > year; start--) {
                            yearCounter++;
                        }
                        spinner2.setSelection(yearCounter);
                        spinner3.setSelection(trim);

                    }
                }
            }
        }

    }
    public ArrayList<String> loadData(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String text = parent.getItemAtPosition(position).toString();
        if (isFound("20", text)||isFound("Year", text)) {
            Year = text;
            spin2.setBackgroundColor(Color.alpha(0));
        } else if (isFound("Intake", text)) {
            Intake = text;
            spin3.setBackgroundColor(Color.alpha(0));
        }else{
            Major = text;
            spinner.setBackgroundColor(Color.alpha(0));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Intent setResultChoice = new Intent(this, setResultChoice.class);
        startActivity(setResultChoice);
        finish();
    }

    public boolean gpaAvailable(String gpa){
        double gpad=Double.parseDouble(gpa);
        if(0<=gpad&&gpad<=4){
            return true;
        }else{
            return false;
        }
    }
    public boolean hoursAvailable(String hours){
        double hoursd=Double.parseDouble(hours);
        if(0<=hoursd&&hoursd<=150){
            return true;
        }else{
            return false;
        }
    }
    public void saveInfoInArrays(){
        student_info_to_save.add(id.getText().toString());
        student_info_to_save.add(name.getText().toString());
        student_info_to_save.add("Bachelor of Engineering (Honours) Electronics majoring in "+Major);

        gpa_and_totalHours.add(CGPA.getText().toString());
        gpa_and_totalHours.add(TotalHours.getText().toString());

        result_Trimester.add(Year);
        result_Trimester.add(Integer.toString(sem));

    }
    public void saveInfo(){
        saveData(courseStructure,"CourseStructure");
        saveData(student_info_to_save,"studentInfo");
        saveData(result_Trimester,"TrimesterSemAndYear");
        saveData(gpa_and_totalHours,"studentGPAAndTotalHours");
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
    public void GetCourseStructure()  {
        if(isFound("June",Intake)){
            sem=1;
        }else if(isFound("October",Intake)){
            sem=2;
        }else if(isFound("March",Intake)){
            sem=3;
        }

        String[] data = getData(Integer.parseInt(Year),sem,Major);

        for(int i=0;i<data.length;i++){
            courseStructure.add(data[i]);
        }
    }
    public String[] getData(int year , int trim,String major) {

        if (isFound("omputer",major)) {
            ComputerData getDataClass = new ComputerData();
            getDataClass.setTrim(trim);
            getDataClass.setYear(year);
            return getDataClass.getDataMethod();
        } else if (isFound("elecommunications",major)) {
            TelecommunicationData getDataClass = new TelecommunicationData();
            getDataClass.setTrim(trim);
            getDataClass.setYear(year);
            return getDataClass.getDataMethod();
        } else if (isFound("lectrical",major)) {
            ElectricalData getDataClass = new ElectricalData();
            getDataClass.setTrim(trim);
            getDataClass.setYear(year);
            return getDataClass.getDataMethod();
        } else if (isFound("anotechnology",major)) {
            NanoData getDataClass = new NanoData();
            getDataClass.setTrim(trim);
            getDataClass.setYear(year);
            return getDataClass.getDataMethod();
        }else if (isFound("lectronics",major)) {
            ElectronicsData getDataClass = new ElectronicsData();
            getDataClass.setTrim(trim);
            getDataClass.setYear(year);
            return getDataClass.getDataMethod();
        }
        String[] data = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
        return data;
    }
    public boolean isFound(String p, String hph) {
        boolean Found = hph.indexOf(p) != -1 ? true : false;
        return Found;
    }
    public int TrimesterOf(String numrical){
        String[] temp = numrical.split(" ",-1);
        for(int i=0;i<temp.length;i++) {
            if (isnumrical(temp[i])) {
                return Integer.parseInt(temp[i]);
            }
        }
        return 0;
    }
    public int YearOf(String numrical){
        String[] temp = numrical.split("- ",-1);
        for(int i=0;i<temp.length;i++) {
            String[] temp2 = temp[i].split("/",-1);
            for(int j=0;j<temp2.length;j++) {
                if (isnumrical(temp2[j])) {
                    return Integer.parseInt(temp2[j]);
                }
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
    public String clearFromSpacing(String numrical){
        numrical =numrical;
        String[] temp = numrical.split(" ",-1);
        for(int i=0;i<temp.length;i++) {
            if (!temp[i].isEmpty()) {
                return temp[i];
            }
        }
        return "";
    }
}
