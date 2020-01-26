package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class availableSub extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    TextView finishedHours,remainingHours,TotalHours;
    ArrayList<String> LoadcourseStructure = new ArrayList<>();
    ArrayList<String> result_Trimester = new ArrayList<>();
    ArrayList<String> result_code = new ArrayList<>();
    ArrayList<String> result_subName = new ArrayList<>();
    ArrayList<String> result_grade = new ArrayList<>();
    ArrayList<String> student_info = new ArrayList<>();
    ArrayList<String> gpa_and_totalHours = new ArrayList<>();
    ArrayList<String> numOfSub = new ArrayList<>();
    ArrayList<subject> dataToPrint = new ArrayList<>();
    String reason="";
    String[][] courseStructure = new String[100][20];
    String[][][] result = new String[20][200][3];
    int num_of_hours_next_sem = 0;
    int totalHours=0,finishedHoursint=0;
    boolean Search=false;
    private AppCompatAutoCompleteTextView autoTextView;
    semesterlistadabterNoCheckForSearch adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_sub);

        LoadcourseStructure = loadData("CourseStructure");
        result_Trimester = loadData("resultTrimester");
        result_code = loadData("resultCode");
        result_subName = loadData("resultSubName");
        result_grade = loadData("resultGrade");
        student_info = loadData("studentInfo");
        gpa_and_totalHours = loadData("studentGPAAndTotalHours");
        numOfSub = loadData("numOfSub");

        finishedHours = findViewById(R.id.setHoursFinished);
        remainingHours = findViewById(R.id.setHoursRemaining);
        TotalHours = findViewById(R.id.setTotalHours);

        arrayListToArray();

        courseStructure = getcourseStructure();

        DisplaySubjects();

        TotalHours.setText(Integer.toString(totalHours));
        finishedHours.setText(Integer.toString(finishedHoursint));
        remainingHours.setText(Integer.toString(totalHours-finishedHoursint));

        autoTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                autoTextView.dismissDropDown();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                autoTextView.dismissDropDown();
                if(!TextUtils.isEmpty(autoTextView.getText())) {
                    Search = true;
                }else{
                    Search = false;
                    dataToPrint.clear();
                    DisplaySubjects();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                autoTextView.dismissDropDown();
                if(!TextUtils.isEmpty(autoTextView.getText())) {
                    Search = true;
                }else{
                    Search = false;
                    dataToPrint.clear();
                    DisplaySubjects();
                }
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        if (Search) {
            dataToPrint.clear();
            DisplaySubjects();
            Search=false;
            autoTextView.setText("");
        } else {
            finish();
            CustomIntent.customType(this, "right-to-left");
        }
    }

    public void DisplaySubjects(){

        ArrayList<subjectWithTitleNoCheck> ToPrint = new ArrayList<>();
        subjectWithTitleNoCheck Print;

        totalHours=0;
        finishedHoursint=0;

        boolean elective, end_of_elective;
        int num_of_sem_finished;

        num_of_hours_next_sem=0;
        ArrayList<String>nextSemSubjects_code = new ArrayList<>();
        ArrayList<String>nextSemSubjects_elective_code = new ArrayList<>();
        ArrayList<String>nextSemSubjects_name = new ArrayList<>();
        ArrayList<String>nextSemSubjects_elective_name = new ArrayList<>();
        ArrayList<String>nextSemSubjects_hours = new ArrayList<>();
        ArrayList<String>nextSemSubjects_elective_hours = new ArrayList<>();


        int year=0,trim=0;
        for(int i=0;i<result_Trimester.size();i++) {
            if(isFound("Trimester",result_Trimester.get(i))){
                trim =  TrimesterOf(result_Trimester.get(i));
                year = YearOf(result_Trimester.get(i));
                break;
            }
        }
        int colorint=0,s,Temphours=0,TempFinishedHours=0,a=0,fortitle=0;

        for(num_of_sem_finished=0;num_of_sem_finished<12;num_of_sem_finished++) {

            if(fortitle==0){
                Print = new subjectWithTitleNoCheck("Trimester " + trim + " " + year + "/" + (year + 1), "", "", "",4);
                ToPrint.add(Print);
            }else {
                Print = new subjectWithTitleNoCheck("Trimester " + trim + " " + year + "/" + (year + 1), "", "", "",5);
                ToPrint.add(Print);
            }
            fortitle++;
            trim++;
            if(fortitle>2){
                fortitle=0;
            }
            if(trim>3){
                year++;
                trim=1;
            }
            Temphours=0;
            TempFinishedHours=0;
            elective = false;
            end_of_elective = false;
            for (int y = 0; y < courseStructure.length - 1; y++) {
                for (int x = 2; x < courseStructure[y].length - 1; x++)
                    if (courseStructure[y][0] != null && courseStructure[y][1] != null && courseStructure[y][x] != null) {
                        if (isFound("Elective", courseStructure[y][1]) && (x == (num_of_sem_finished + 2)) && (!courseStructure[y + 1][x].isEmpty())) {
                            elective = true;
                        }
                        if ((x == (num_of_sem_finished + 2)) && (!courseStructure[y][x].isEmpty())) {
                            if (elective) {
                                elective = false;
                                end_of_elective = true;
                            }
                            if (available(y)) {
                                if (elective || end_of_elective) {
                                    nextSemSubjects_elective_code.add(courseStructure[y][0]+" (Elective)");
                                    nextSemSubjects_elective_name.add(courseStructure[y][1]);
                                    nextSemSubjects_elective_hours.add("*"+courseStructure[y][x]+"*.");
                                    if(!isFound("MPU",courseStructure[y][0])&&!isFound("MPU",courseStructure[y][1])) {
                                        Temphours = Integer.parseInt(courseStructure[y][x]);
                                    }
                                    if(finishedSubject(courseStructure[y][0])&&!isFound("MPU",courseStructure[y][0])&&!isFound("MPU",courseStructure[y][1])){
                                        TempFinishedHours=Integer.parseInt(courseStructure[y][x]);
                                    }
                                } else {
                                    nextSemSubjects_code.add(courseStructure[y][0]);
                                    nextSemSubjects_name.add(courseStructure[y][1]);
                                    nextSemSubjects_hours.add("*"+courseStructure[y][x]+"*.");
                                    if(!isFound("MPU",courseStructure[y][0])&&!isFound("MPU",courseStructure[y][1])) {
                                        totalHours += Integer.parseInt(courseStructure[y][x]);
                                    }
                                    if(finishedSubject(courseStructure[y][0])&&!isFound("MPU",courseStructure[y][0])&&!isFound("MPU",courseStructure[y][1])){
                                        finishedHoursint+=Integer.parseInt(courseStructure[y][x]);
                                    }
                                }
                            } else {
                                if (elective || end_of_elective) {
                                    nextSemSubjects_elective_code.add(courseStructure[y][0]+" (Elective)");
                                    nextSemSubjects_elective_name.add(courseStructure[y][1]+""+reason);
                                    nextSemSubjects_elective_hours.add("*"+courseStructure[y][x]+"*");
                                    if(!isFound("MPU",courseStructure[y][0])&&!isFound("MPU",courseStructure[y][1])) {
                                        Temphours = Integer.parseInt(courseStructure[y][x]);
                                    }
                                } else {
                                    nextSemSubjects_elective_code.add(courseStructure[y][0]);
                                    nextSemSubjects_elective_name.add(courseStructure[y][1]+""+reason);
                                    nextSemSubjects_elective_hours.add("*"+courseStructure[y][x]+"*");
                                    if(!isFound("MPU",courseStructure[y][0])&&!isFound("MPU",courseStructure[y][1])) {
                                        totalHours += Integer.parseInt(courseStructure[y][x]);
                                    }
                                }
                            }
                            if (end_of_elective == true && courseStructure[y + 1][0].isEmpty()) {
                                end_of_elective = false;
                            }

                        }
                    }
            }
            totalHours+=Temphours;
            finishedHoursint+=TempFinishedHours;


            for (s=0; s < nextSemSubjects_name.size(); s++) {
                if(finishedSubject(nextSemSubjects_code.get(s))) {
                    colorint = 1;
                }else
                if(isFound("*.",nextSemSubjects_hours.get(s))){
                    colorint = 2;
                }else{
                    colorint = 3;
                }
                Print = new subjectWithTitleNoCheck("", nextSemSubjects_code.get(s), nextSemSubjects_name.get(s), HoursFromStarString(nextSemSubjects_hours.get(s)),colorint);
                ToPrint.add(Print);
                a++;
            }

            nextSemSubjects_code.clear();
            nextSemSubjects_name.clear();
            nextSemSubjects_hours.clear();

            for (s=0; s < nextSemSubjects_elective_name.size();  s++) {
                if(finishedSubject(nextSemSubjects_elective_code.get(s))) {
                    colorint = 1;
                }else
                if(isFound("*.",nextSemSubjects_elective_hours.get(s))){
                    colorint = 2;
                }else{
                    colorint = 3;
                }
                Print = new subjectWithTitleNoCheck("", nextSemSubjects_elective_code.get(s), nextSemSubjects_elective_name.get(s), HoursFromStarString(nextSemSubjects_elective_hours.get(s)),colorint);
                ToPrint.add(Print);
                a++;
            }

            nextSemSubjects_elective_code.clear();
            nextSemSubjects_elective_name.clear();
            nextSemSubjects_elective_hours.clear();

            Print = new subjectWithTitleNoCheck("", "", "", "",0);
            ToPrint.add(Print);
        }

        Print = new subjectWithTitleNoCheck("", "", "", "",0);
        ToPrint.add(Print);



        ListView chl=(ListView) findViewById(R.id.courseStructure);
        adapter = new semesterlistadabterNoCheckForSearch(this, R.layout.semesterlayout, ToPrint);
        chl.setAdapter(adapter);

        autoTextView = (AppCompatAutoCompleteTextView) findViewById(R.id.searchBar);
        autoTextView.setThreshold(1); //will start working from first character
        autoTextView.setAdapter(adapter);

    }

    public ArrayList<String> loadData(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public boolean available(int y){
        String[] prerequiste ;
        boolean avail=false;
        if(courseStructure[y][14].isEmpty()||isFound("-",courseStructure[y][14])){
            return true;
        }else {
            if (!isFound(",", courseStructure[y][14])) {        //means only one requirement
                if (isFound("credit", courseStructure[y][14])) {
                    if (CreditHoursIsSatisfied(courseStructure[y][14])) {
                        return true;
                    }else {
                        reason = "\n"+courseStructure[y][14]+" Required";
                        return false;
                    }
                } else {
                    for (int i = 0; i < result_code.size(); i++) {
                        if(isFound(clearFromSpacing(result_code.get(i)),courseStructure[y][14])){
                            return true;
                        }
                    }
                    for(int i = 0;i<courseStructure.length;i++){
                        if(courseStructure[i][0]!=null&&!courseStructure[i][0].isEmpty()) {
                            if (isFound(clearFromSpacing(courseStructure[y][14]), courseStructure[i][0])) {
                                reason = "\n"+courseStructure[i][0] +"  "+ courseStructure[i][1] + " Requierd.";
                                return false;
                            }
                        }
                    }
                }
            }else{                      //means many requirements found
                prerequiste= courseStructure[y][14].split(",",-1);
                int newSize=0;
                for(int p=0;p<prerequiste.length;p++){
                    if(prerequiste[p]!=null&&!prerequiste[p].isEmpty()){
                        prerequiste[newSize]=prerequiste[p];
                        newSize++;
                    }
                }
                reason="";
                int counter=0;
                for(int p=0;p<newSize;p++){
                    if (isFound("credit", prerequiste[p])) {
                        if (CreditHoursIsSatisfied(prerequiste[p])) {
                            counter++;
                        }else {
                            reason +=  "\n"+courseStructure[y][14]+" Required";
                        }
                    } else {
                        for (int i = 0; i < result_code.size(); i++) {
                            if(isFound(clearFromSpacing(result_code.get(i)),prerequiste[p])){
                                avail=true;
                                counter++;
                            }
                        }
                        if(!avail) {
                            for (int i = 0; i < courseStructure.length; i++) {
                                if (courseStructure[i][0] != null && !courseStructure[i][0].isEmpty()) {
                                    if (isFound(clearFromSpacing(prerequiste[p]), courseStructure[i][0])) {
                                        reason += "\n"+courseStructure[i][0] +"  "+ courseStructure[i][1] + " Requierd.";
                                    }
                                }
                            }
                        }
                        avail=false;
                    }
                }
                if(newSize<=counter){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
    public boolean finishedSubject(String code){
        for(int y=0;y<result_code.size();y++) {
            if (isFound(clearFromSpacing(result_code.get(y)),code)) {
                return true;
            }
        }
        return false;
    }
    public boolean CreditHoursIsSatisfied( String creditHours){
        if(numricalOf(gpa_and_totalHours.get(1))>=numricalOf(creditHours)){
            return true;
        }else {
            return false;
        }
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
    public int numricalOf(String numrical){
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
    public void arrayListToArray(){
        int i=0;
        for(int z=0;z<result_Trimester.size();z++) {
            for (int y = 0; y <Integer.parseInt(numOfSub.get(z)); y++) {
                if(i<result_code.size()) {
                    result[z][y][0] = result_code.get(i);
                    result[z][y][1] = result_subName.get(i);
                    result[z][y][2] = result_grade.get(i);
                    i++;
                }
            }
        }
    }
    public String[][] getcourseStructure(){

        String[] data =new String[LoadcourseStructure.size()];
        for(int i=0;i<LoadcourseStructure.size();i++){
            data[i]=LoadcourseStructure.get(i);
        }

        String[] column0 = data[0].split("\n", -1);
        String[] column1 = data[1].split("\n", -1);
        String[] column2 = data[2].split("\n", -1);
        String[] column3 = data[3].split("\n", -1);
        String[] column4 = data[4].split("\n", -1);
        String[] column5 = data[5].split("\n", -1);
        String[] column6 = data[6].split("\n", -1);
        String[] column7 = data[7].split("\n", -1);
        String[] column8 = data[8].split("\n", -1);
        String[] column9 = data[9].split("\n", -1);
        String[] column10 = data[10].split("\n", -1);
        String[] column11 = data[11].split("\n", -1);
        String[] column12 = data[12].split("\n", -1);
        String[] column13 = data[13].split("\n", -1);
        String[] column14 = data[14].split("\n", -1);
        String[][] course = new String[100][20];

        for (int i = 0; i < column1.length; i++) {
            course[i][0] = column0[i];
            course[i][1] = column1[i];
            course[i][2] = column2[i];
            course[i][3] = column3[i];
            course[i][4] = column4[i];
            course[i][5] = column5[i];
            course[i][6] = column6[i];
            course[i][7] = column7[i];
            course[i][8] = column8[i];
            course[i][9] = column9[i];
            course[i][10] = column10[i];
            course[i][11] = column11[i];
            course[i][12] = column12[i];
            course[i][13] = column13[i];
            course[i][14] = column14[i];
        }
        course[99][19] = "139";
        return course;
    }
    public boolean isFound(String p,String hph){
        boolean Found = hph.indexOf(p) !=-1? true: false;
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
    public String HoursFromStarString(String str){
        String[] temp = str.split("\\*",-1);
        if(temp.length>1) {
            return temp[1];
        }else{
            return "";
        }
    }


}
