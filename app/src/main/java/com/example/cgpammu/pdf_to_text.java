package com.example.cgpammu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.parse.ParseUser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class pdf_to_text extends AppCompatActivity {
    public static int PICK_FILE = 1;
    ArrayList<String> courseStructure = new ArrayList<>();
    ArrayList<String> result = new ArrayList<>();
    ArrayList<String> result_Trimester = new ArrayList<>();
    ArrayList<String> result_code = new ArrayList<>();
    ArrayList<String> result_subName = new ArrayList<>();
    ArrayList<String> result_grade = new ArrayList<>();
    ArrayList<String> student_info_to_save = new ArrayList<>();
    ArrayList<String> gpa_and_totalHours = new ArrayList<>();
    ArrayList<String> numOfSub = new ArrayList<>();
    TextView textView;
    ParseUser user =ParseUser.getCurrentUser();
    Dialog settingsDialog;


    public void YES_CLICKED(View view){
        saveData(courseStructure,"CourseStructure");
        saveData(student_info_to_save,"studentInfo");
        saveData(result_Trimester,"resultTrimester");
        saveData(result_code,"resultCode");
        saveData(result_subName,"resultSubName");
        saveData(result_grade,"resultGrade");
        saveData(gpa_and_totalHours,"studentGPAAndTotalHours");
        saveData(numOfSub,"numOfSub");
        Toast.makeText(this, "Your data is saved into your account", Toast.LENGTH_LONG).show();
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.cgpammu", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("goalset",true).apply();
        Intent setGoal = new Intent(this, setGoal.class);
        startActivity(setGoal);
    }
    public void NO_CLICKED(View view){
        settingsDialog.show();
    }
    public void setMan(View view){
        Intent set_manually = new Intent(this, set_manually.class);
        startActivity(set_manually);
        finish();
    }

    public void contactMe(View view){
        Intent contactMe = new Intent(this, contactMe.class);
        startActivity(contactMe);
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
        setContentView(R.layout.activity_pdf_to_text);

            textView = (TextView) findViewById(R.id.textView);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/plain");
            startActivityForResult(intent, PICK_FILE);

            settingsDialog = new Dialog(this);
            settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.no_dialog
                , null));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String fileContent = readTextFile(uri);
                scanner(fileContent);
            } else {
                Intent pdf_to_text = new Intent(this, uploadResultPage.class);
                startActivity(pdf_to_text);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent pdf_to_text = new Intent(this, uploadResultPage.class);
        startActivity(pdf_to_text);
        finish();
    }

    private String readTextFile(Uri uri) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));
            String line = "";

            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
    public void scanner(String data) {
        data+="\n\n\n\n\n\n\n\n\n\n";
        String[] text = data.split("\n",-1);
        String[] subject=new String[200];
        String[] temp={};
        ArrayList<subjectManually> dataToPrint = new ArrayList<>();
        subjectManually sub;


        String[][][] subjects_in_table = new String[50][200][50];    //<--------------
        String[] trim = new String[50];                              //<--------------
        String[] student_info = new String[3];                       //<--------------
        String CGPA="",Total_Hours="";                               //<--------------

        int l=0,x=0,y=0,z=0,z_old;


        l=0;
        //to store values for trim which is the Trimester
        for(int i=0;i<text.length;i++){
            if(isFound(text[i],"Trimester")||isFound(text[i],"Exempted")){
                trim[l]=text[i];
                l++;
            }
        }


        x=0;
        y=0;
        z=0;
        l=0;
        //To store the subjects
        for(int i=0;i<text.length;i++){
            if(isFound(text[i],"Code")){
                y=0;
                for(int s=1;s<7;s++) {
                    if(text[i+s].isEmpty()) {
                        break;
                    }
                    subject[l] = text[i+s];
                    if(subject[l]!=null) {
                        temp = subject[l].split("  ", -1);
                        x = 0;
                        for (int m = 0; m < temp.length; m++) {
                            if (!temp[m].isEmpty()) {
                                subjects_in_table[z][y][x] = temp[m];
                                x++;
                            }
                        }
                        if(x==3) {
                            y++;
                        }
                    }
                    l++;
                }
                numOfSub.add(Integer.toString(y));
                z++;
            }
        }


        //To store the student info
        l=0;
        for(int i=0;i<text.length;i++) {
            if (isFound(text[i], " :   ")) {
                temp = text[i].split(" :   ", -1);
                student_info[l] = temp[temp.length - 1];
                l++;
            }
            if (l >= 3) {
                break;
            }
        }

        //To store the CGPA
        for(int i=0;i<text.length;i++) {
            if (isFound(text[i], "CGPA")) {
                temp = text[i].split("CGPA        ", -1);
                temp[0] = temp[temp.length - 1];
                temp = temp[0].split("                              Academic", -1);
                CGPA=temp[0];
            }
        }

        //To store the Total hours finished
        for(int i=0;i<text.length;i++) {
            if (isFound(text[i], "Total Hours")) {
                temp = text[i].split("Total Hours", -1);
                temp[0] = temp[temp.length - 1];
                temp = temp[0].split("                 Total Point", -1);
                Total_Hours=temp[0];
            }
        }

        //To store all marks in array list
        String grade;
        for (z = 0; z < 50; z++) {
            if (trim[z] != null) {
                    if(isFound(trim[z],"Trimester 1")) {
                        sub = new subjectManually(trim[z], "", "", "", 0);
                        dataToPrint.add(sub);
                    }else{
                        sub = new subjectManually(trim[z], "", "", "", 1);
                        dataToPrint.add(sub);
                    }
                result.add(trim[z]);
                result_Trimester.add(trim[z]);

                for (y = 0; y < 200; y++) {
                    if (subjects_in_table[z][y][0] != null && subjects_in_table[z][y][1] != null && subjects_in_table[z][y][2] != null) {

                        sub=new subjectManually("",subjects_in_table[z][y][0],subjects_in_table[z][y][1] , subjects_in_table[z][y][2] , 2);
                        dataToPrint.add(sub);

                        result_code.add(subjects_in_table[z][y][0]);
                        result_subName.add(subjects_in_table[z][y][1]);
                        result_grade.add(subjects_in_table[z][y][2]);
                    }
                }

                sub = new subjectManually("", "", "", "", 2);
                dataToPrint.add(sub);
            }
        }
        sub=new subjectManually("","","","",2);
        dataToPrint.add(sub);
        sub=new subjectManually("","","","",2);
        dataToPrint.add(sub);
        sub=new subjectManually("","","","",2);
        dataToPrint.add(sub);

        TextView studentId = (TextView) findViewById(R.id.studentid);
        TextView studentName = (TextView) findViewById(R.id.name);
        TextView studentDegree = (TextView) findViewById(R.id.degree);

        TextView CGPA_TEXTVIEW = (TextView) findViewById(R.id.CGPA);
        TextView TotalHours = (TextView) findViewById(R.id.totalhours);


        studentId.setText(student_info[0]);
        student_info_to_save.add(student_info[0]);
        studentName.setText(student_info[1]);
        student_info_to_save.add(student_info[1]);
        studentDegree.setText(student_info[2]);
        student_info_to_save.add(student_info[2]);

        CGPA_TEXTVIEW.setText(CGPA);
        gpa_and_totalHours.add(CGPA);
        TotalHours.setText(Total_Hours);
        gpa_and_totalHours.add(Total_Hours);

        ListView listView = (ListView) findViewById(R.id.listView);
        uploudresultadapter adapter = new uploudresultadapter(this, R.layout.upload_subjects, dataToPrint);
        listView.setAdapter(adapter);

        GetCourseStructure();

    }
    public boolean isFound(String hph,String p){
        boolean Found = hph.indexOf(p) !=-1? true: false;
        return Found;
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
        int yearof=0,trime=0;
        for(int i=0;i<result_Trimester.size();i++) {
            if(isFound(result_Trimester.get(i),"Trimester")){
                trime =  TrimesterOf(result_Trimester.get(i));
                yearof = YearOf(result_Trimester.get(i));
                break;
            }
        }

        String[] data = getData(yearof,trime,student_info_to_save.get(2));

        for(int i=0;i<data.length;i++){
            courseStructure.add(data[i]);
        }
    }
    public String[] getData(int year , int trim,String major) {

       if (isFound(major, "omputer")) {
            ComputerData getDataClass = new ComputerData();
            getDataClass.setTrim(trim);
            getDataClass.setYear(year);
            return getDataClass.getDataMethod();
        } else if (isFound(major, "elecommunications")) {
            TelecommunicationData getDataClass = new TelecommunicationData();
            getDataClass.setTrim(trim);
            getDataClass.setYear(year);
            return getDataClass.getDataMethod();
        } else if (isFound(major, "lectrical")) {
            ElectricalData getDataClass = new ElectricalData();
            getDataClass.setTrim(trim);
            getDataClass.setYear(year);
            return getDataClass.getDataMethod();
        } else if (isFound(major, "anotechnology")) {
            NanoData getDataClass = new NanoData();
            getDataClass.setTrim(trim);
            getDataClass.setYear(year);
            return getDataClass.getDataMethod();
        }else if (isFound(major, "lectronics")) {
           ElectronicsData getDataClass = new ElectronicsData();
           getDataClass.setTrim(trim);
           getDataClass.setYear(year);
           return getDataClass.getDataMethod();
       }
           String[] data = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
        return data;
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

//    public void LoadData(){
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("task list", null);
//        Type type = new TypeToken<ArrayList<String>>() {}.getType();
//        AllData = gson.fromJson(json, type);
//        if (AllData == null) {
//            AllData = new ArrayList<>();
//        }
//    }
//
//    private void display() {
//
//
//        TextView studentId = (TextView) findViewById(R.id.studentid);
//        TextView studentName = (TextView) findViewById(R.id.name);
//        TextView studentDegree = (TextView) findViewById(R.id.degree);
//
//        TextView CGPA_TEXTVIEW = (TextView) findViewById(R.id.CGPA);
//        TextView TotalHours = (TextView) findViewById(R.id.totalhours);
//
//        TotalHours.setText(AllData.get(AllData.size()-1));
//        AllData.remove(AllData.size() - 1);
//        CGPA_TEXTVIEW.setText(AllData.get(AllData.size()-1));
//        AllData.remove(AllData.size() - 1);
//
//
//        studentDegree.setText(AllData.get(AllData.size()-1));
//        AllData.remove(AllData.size() - 1);
//        studentName.setText(AllData.get(AllData.size()-1));
//        AllData.remove(AllData.size() - 1);
//        studentId.setText(AllData.get(AllData.size()-1));
//        AllData.remove(AllData.size() - 1);
//
//        ListView listView = (ListView) findViewById(R.id.listView);
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,AllData);
//        listView.setAdapter(arrayAdapter);
//    }

}
