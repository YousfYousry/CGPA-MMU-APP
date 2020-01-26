package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import maes.tech.intentanim.CustomIntent;


public class degreePlanner extends AppCompatActivity implements setGoalDialog.ExampleDialogListener{

    ArrayList<String> LoadcourseStructure = new ArrayList<>();
    ArrayList<String> result_Trimester = new ArrayList<>();
    ArrayList<String> result_code = new ArrayList<>();
    ArrayList<String> result_subName = new ArrayList<>();
    ArrayList<String> result_grade = new ArrayList<>();
    ArrayList<String> student_info = new ArrayList<>();
    ArrayList<String> gpa_and_totalHours = new ArrayList<>();
    ArrayList<String> numOfSub = new ArrayList<>();
    String[][] courseStructure=new String[100][20];
    String[][][] result=new String[20][200][3];
    RadioGroup rg;
    RadioButton rb;
    SeekBar seekBar;
    CheckBox checkBoxHours,checkBoxAllPlans;
    boolean shownum=false,isShown=false,isShown2=false;
    int planChoice;
    double[] planGPA;
    double[][] hours;
    String[][][] offeredSubjects;
    ArrayList<String> subjects = new ArrayList<>();
    double totalHours=0,remainingHours=0,CGPA=0,goal=0,newCGPA,mean,r=5,maxGPA;
    int num_of_hours_next_sem = 0,setplans=3;
    setGoalDialog exampleDialog;
    degreePlanner degree =this;
    ParseUser user =ParseUser.getCurrentUser();
    Dialog settingsDialog,settingsDialog2;


    public void radioD(View view){
        int radiobutton = rg.getCheckedRadioButtonId();
        rb=(RadioButton)findViewById(radiobutton);
        String plan = (String)rb.getText();
        int oldplanChoice=planChoice;
        if(plan.compareTo("Easy plan")==0){
            planChoice=1;
            if(oldplanChoice!=planChoice){
                displayDegreePlanner();
            }
        }else if(plan.compareTo("Medium plan")==0){
            planChoice=2;
            if(oldplanChoice!=planChoice){
                displayDegreePlanner();
            }
        }else if(plan.compareTo("Hard plan")==0){
            planChoice=3;
            if(oldplanChoice!=planChoice){
                displayDegreePlanner();
            }
        }
    }
    public void goalchanger(View view){ openDialog(); }
    public void closeShowNum(View view){
        settingsDialog.dismiss();
        settingsDialog2.dismiss();
    }
    public void ShowHoursClicked(View view) {
        if (checkBoxHours.isChecked()) {
            if(!isShown2) {
                isShown2=true;
                settingsDialog2.show();
            }
            shownum = true;
        } else {
            shownum = false;
        }
        displayDegreePlanner();
    }
    public void ShowPlansClicked(View view) {
        if (checkBoxAllPlans.isChecked()) {
            setplans = 500;
        } else {
            setplans = 3;
        }
        displayDegreePlanner();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_degree_planner);

        exampleDialog = new setGoalDialog();
        exampleDialog.setDegree(degree);
        LoadcourseStructure = loadData("CourseStructure");
        courseStructure = getcourseStructure();
        result_Trimester = loadData("resultTrimester");
        result_code = loadData("resultCode");
        result_subName = loadData("resultSubName");
        result_grade = loadData("resultGrade");
        student_info = loadData("studentInfo");
        gpa_and_totalHours = loadData("studentGPAAndTotalHours");
        numOfSub = loadData("numOfSub");
        arrayListToArray();
        CGPA = DoubleNumricalOf(gpa_and_totalHours.get(0));
        ArrayList<String> goalArr = new ArrayList<>();
        goalArr = loadData("goal");
        if(goalArr.size()>0) {
            goal = Double.parseDouble(goalArr.get(0));
        }
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r=progress+1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(!isShown) {
                    isShown=true;
                    settingsDialog.show();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                displayDegreePlanner();
            }
        });
        settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout2
                , null));
        settingsDialog2 = new Dialog(this);
        settingsDialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog2.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
        rg = (RadioGroup) findViewById(R.id.bottom);
        rg.check(R.id.radioButton15);
        checkBoxHours = (CheckBox) findViewById(R.id.checkBox2);
        checkBoxAllPlans = (CheckBox) findViewById(R.id.checkBox3);

        planChoice = 3;

        displayDegreePlanner();
        exampleDialog.setMAXGPA(maxGPA);
        exampleDialog.setCGPA(CGPA);
    }
    public void openDialog() { exampleDialog.show(getSupportFragmentManager(), "example dialog"); }
    public void dismiss(){
        float amount = (float) goal;
        ArrayList<String> goalArr = new ArrayList<>();
        goalArr.add(Float.toString((float) amount));
        saveData(goalArr,"goal");
        exampleDialog.dismiss();
        displayDegreePlanner();
    }
    public void setGoal(double goal) {
        this.goal = goal;
    }

    @Override
    public void applyTexts(String username, String password) {}

    @Override
    public void onBackPressed() {
            Intent mainapp = new Intent(this, MainPage.class);
            startActivity(mainapp);
            CustomIntent.customType(this, "right-to-left");
            finish();
    }

    public void displayDegreePlanner(){
        offeredSubjects = new String[12][50][300];
        hours=new double[12][50];
        totalHours=0;
        remainingHours=0;
        num_of_hours_next_sem = 0;
        boolean elective, end_of_elective,sumElective=false;
        int num_of_sem_finished;

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
        int i=0,s=0,c=0,ys=0,xs=0;

        for(num_of_sem_finished=0;num_of_sem_finished<12;num_of_sem_finished++) {

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
                                if (end_of_elective) {
                                    nextSemSubjects_elective_code.add(courseStructure[y][0]+" (Elective)");
                                    nextSemSubjects_elective_name.add(courseStructure[y][1]);
                                    nextSemSubjects_elective_hours.add("*"+courseStructure[y][x]+"*");
                                } else {
                                    nextSemSubjects_code.add(courseStructure[y][0]);
                                    nextSemSubjects_name.add(courseStructure[y][1]);
                                    nextSemSubjects_hours.add("*"+courseStructure[y][x]+"*");
                                }
                            if (end_of_elective  && courseStructure[y + 1][0].isEmpty()) {
                                end_of_elective = false;
                            }

                        }
                    }
            }
            for (s=0; s < nextSemSubjects_name.size(); s++) {
                if(!finishedSubject(nextSemSubjects_code.get(s))) {
                    if (!isFound("MPU", nextSemSubjects_code.get(s))&&!isFound("MPU", nextSemSubjects_name.get(s))) {
                        remainingHours += (double) HoursFromStar(nextSemSubjects_hours.get(s));
                        offeredSubjects[ys][xs][0] = nextSemSubjects_code.get(s);
                        offeredSubjects[ys][xs][1] = nextSemSubjects_name.get(s);
                        offeredSubjects[ys][xs][2] = nextSemSubjects_hours.get(s);
                        hours[ys][xs]=(double) HoursFromStar(nextSemSubjects_hours.get(s));
                        xs++;
                    }
                }
                if (!isFound("MPU", nextSemSubjects_code.get(s))&&!isFound("MPU", nextSemSubjects_name.get(s))) {
                    totalHours += (double) HoursFromStar(nextSemSubjects_hours.get(s));
                }
            }
            nextSemSubjects_code.clear();
            nextSemSubjects_name.clear();
            nextSemSubjects_hours.clear();

            for (s=0; s < nextSemSubjects_elective_name.size(); s++) {
                if (!finishedSubject(nextSemSubjects_elective_code.get(s))) {
                    offeredSubjects[ys][xs][0] = nextSemSubjects_elective_code.get(s);
                    offeredSubjects[ys][xs][1] = nextSemSubjects_elective_name.get(s);
                    offeredSubjects[ys][xs][2] = nextSemSubjects_elective_hours.get(s);
                    xs++;
                    if (!sumElective) {
                        remainingHours +=(double) HoursFromStar(nextSemSubjects_elective_hours.get(s));
                        hours[ys][xs]=(double) HoursFromStar(nextSemSubjects_elective_hours.get(s));
                    }
                }
                if (!sumElective) {
                    totalHours +=(double)  HoursFromStar(nextSemSubjects_elective_hours.get(s));
                    sumElective=true;
                }
            }
            sumElective=false;
            nextSemSubjects_elective_code.clear();
            nextSemSubjects_elective_name.clear();
            nextSemSubjects_elective_hours.clear();
            ys++;
        }

        GetDegreePlanner();
        int f=0;
        double semHours=0,totalGPA,counter=0;
        ArrayList<MarksForDegree> ToPrint = new ArrayList<>();
        MarksForDegree Print;
        i=0;
        int oldYear=year-1,Titlecolor;

        for(ys=0;ys<offeredSubjects.length;ys++) {

            if(oldYear!=year){
                Titlecolor=1;
                oldYear=year;
            }else{
                Titlecolor=2;
            }

            for (xs = 0; xs < hours[ys].length; xs++) {
                if(hours[ys][xs]!=0) {
                    i++;
                    semHours+= hours[ys][xs];
                }
            }

            totalGPA=0;
            counter=0;
            for(;f<semHours;f++){
                totalGPA+=planGPA[f];
                counter++;
            }


            if(i>0){
                Print = new MarksForDegree("", "", "", "Trimester "+ trim +"  " + year +"/"+(year+1), Double.toString(roundNum(totalGPA/counter)), "", "", "", "", "", "", Titlecolor);
                ToPrint.add(Print);
            }


            for (xs = 0; xs < offeredSubjects[ys].length; xs++) {
                if(offeredSubjects[ys][xs][1]!=null) {
                    Print = new MarksForDegree(offeredSubjects[ys][xs][0], offeredSubjects[ys][xs][1], HoursFromStarString(offeredSubjects[ys][xs][2]), "", "", "", "", "", "", "", "", 3);
                    ToPrint.add(Print);
                }
            }
            if(i>0) {
                hours[ys]=DescendingDouble(hours[ys]);
                ToPrint.addAll(array(i, hours[ys], totalGPA / counter));
            }
            if(i>0) {
                Print = new MarksForDegree("", "", "", "", "", "", "", "", "", "", "", 4);
                ToPrint.add(Print);
            }
            i=0;
            trim++;
            if(trim>3){
                trim=1;
                year++;
            }
        }

        //3 for subjects
        //2 for new title
        //1 for old title
        //0 for grades


        ListView listView = (ListView) findViewById(R.id.top2);

        DegreeListAdapter arrayAdapter = new DegreeListAdapter(this,R.layout.degree_planner_subjects,ToPrint);
        listView.setAdapter(arrayAdapter);
        maxGPA=((totalHours-remainingHours)*CGPA+4*remainingHours)/totalHours;
//        Toast.makeText(getApplicationContext(),"hours finished: "+(semHours),Toast.LENGTH_LONG).show();

    }
    public ArrayList<MarksForDegree>  array(int num_of_sub,double[] hours,double expGPA){
        String[] grads = {"A","A-","B+","B","B-","C+","C",""};
        ArrayList<String> grades = new ArrayList<>();
        int plans=0;
        int hours1,hours2,hours3,hours4,hours5,hours6;
            hours1=(int)hours[0];
            hours2=(int)hours[1];
            hours3=(int)hours[2];
            hours4=(int)hours[3];
            hours5=(int)hours[4];
            hours6=(int)hours[5];

        if(num_of_sub<6){
            hours6=0;
        }
        if(num_of_sub<5){
            hours5=0;
        }
        if(num_of_sub<4){
            hours4=0;
        }
        if(num_of_sub<3){
            hours3=0;
        }
        if(num_of_sub<2){
            hours2=0;
        }

        double grmin , grmax;
        int s;
        int l1,l2,l3,l4,l5,l6;
            for (l1 = 0; l1 < 7 && plans < setplans; l1++) {
                if (num_of_sub < 6) {
                    l1 = 7;
                }
                for (l2 = 0; l2 < 7 && plans < setplans; l2++) {
                    if (num_of_sub < 5) {
                        l2 = 7;
                    }
                    for (l3 = 0; l3 < 7 && plans < setplans; l3++) {
                        if (num_of_sub < 4) {
                            l3 = 7;
                        }
                        for (l4 = 0; l4 < 7 && plans < setplans; l4++) {
                            if (num_of_sub < 3) {
                                l4 = 7;
                            }
                            for (l5 = 0; l5 < 7 && plans < setplans; l5++) {
                                if (num_of_sub < 2) {
                                    l5 = 7;
                                }
                                for (l6 = 0; l6 < 7 && plans < setplans; l6++) {

                                    grmin = (hours6 * grademin(grads[l1]) + hours5 * grademin(grads[l2]) + hours4 * grademin(grads[l3]) + hours3 * grademin(grads[l4]) + hours2 * grademin(grads[l5]) + hours1 * grademin(grads[l6])) / (hours1 + hours2 + hours3 + hours4 + hours5 + hours6);
                                    grmax = (hours6 * grademax(grads[l1]) + hours5 * grademax(grads[l2]) + hours4 * grademax(grads[l3]) + hours3 * grademax(grads[l4]) + hours2 * grademax(grads[l5]) + hours1 * grademax(grads[l6])) / (hours1 + hours2 + hours3 + hours4 + hours5 + hours6);

                                    if (possible(grmin, grmax, expGPA) && plans < setplans) {
                                        plans++;
                                        String[] showGrads = {hours6 + grads[l1], hours5 + grads[l2], hours4 + grads[l3], hours3 + grads[l4], hours2 + grads[l5], hours1 + grads[l6]};
                                        showGrads = Descending2(showGrads);
                                        grades.add(showGrads[0] + "," + showGrads[1] + "," + showGrads[2] + "," + showGrads[3] + "," + showGrads[4] + "," + showGrads[5] + "," + "," + "," + "," + "," + ",");
                                    }
                                }
                            }
                        }
                    }
                }
            }



        ArrayList<MarksForDegree> ToPrint = new ArrayList<>();
        MarksForDegree Print;

        Set<String> set = new HashSet<>(grades);
        grades.clear();
        grades.addAll(set);



        String[][] TempGrades1 = ArrangeGrades(grades);
        for(s=0 ;s<TempGrades1.length;s++){
            Print = new MarksForDegree("","","","","",noNum(TempGrades1[s][0]),noNum(TempGrades1[s][1]),noNum(TempGrades1[s][2]),noNum(TempGrades1[s][3]),noNum(TempGrades1[s][4]),noNum(TempGrades1[s][5]),0);
            ToPrint.add(Print);
        }

        return ToPrint;
    }
    public ArrayList<String> loadData(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
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
    public boolean isFound(String p,String hph){
        boolean Found = hph.indexOf(p) !=-1? true: false;
        return Found;
    }
    public double roundNum(double num){
        return (Math.round(num*100.0)/100.0);
    }
    public boolean finishedSubject(String code){
        for(int y=0;y<result_code.size();y++) {
            if (isFound(clearFromSpacing(result_code.get(y)),code)) {
                return true;
            }
        }
        return false;
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
    public double DoubleNumricalOf(String numrical){
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
    public String noNum(String grad) {
        if (!shownum) {
            String newGrade="";
            if (isFound("A", grad)) {
                newGrade = "A";
            } else if (isFound("B", grad)) {
                newGrade = "B";
            } else if (isFound("C", grad)) {
                newGrade = "C";
            }

            if (isFound("+", grad)) {
                newGrade += "+";
            } else if (isFound("-", grad)) {
                newGrade += "-";
            }
            return newGrade;
        }
        if(grad.compareTo("0")==0){
            return "";
        }
        return grad;
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
    public int HoursFromStar(String str){
        String[] temp = str.split("\\*",-1);
        String[] temp2 = temp[1].split(" ",-1);
        return Integer.parseInt(temp2[0]);
    }
    public String HoursFromStarString(String str){
        String[] temp = str.split("\\*",-1);
        if(temp.length>1) {
            return temp[1];
        }else{
            return "";
        }
    }
    public double grademin(String grade) {
        double marks=0, gpa;

        if(grade.isEmpty()){
            return 0;
        }

        if(isFound("A",grade)){
            marks=80;
        }else if(isFound("B",grade)){
            marks = 65;
        }else if(isFound("C",grade)){
            marks = 50;
        }

        if(isFound("+",grade)){
            marks+=5;
        }else if(isFound("-",grade)){
            marks-=5;
        }

        if(marks>=80&&marks<=100){
            return 4;
        }else if(marks<=80&&marks>=50) {
            gpa = (((marks - 50) / 30) * 2) + 2;
            return gpa;
        }else if(marks<=50&&marks>=0){
            return 0;
        }
        return  999999;

    }
    public double grademax(String grade) {
        double marks=0, gpa;

        if(grade.isEmpty()){
            return 0;
        }

        if(isFound("A-",grade)){
            marks=75+r;
        }else if(isFound("A",grade)){
            marks=80;
        }else if(isFound("B+",grade)){
            marks = 70+r;
        }else if(isFound("B-",grade)){
            marks = 60+r;
        }else if(isFound("B",grade)){
            marks = 65+r;
        }else if(isFound("C+",grade)){
            marks = 55+r;
        }else if(isFound("C",grade)){
            marks = 50+r;
        }

        if(marks>=80&&marks<=100){
            return 4;
        }else if(marks<=80&&marks>=50) {
            gpa = (((marks - 50) / 30) * 2) + 2;
            return gpa;
        }else if(marks<=50&&marks>=0){
            return 0;
        }
        return  999999;

    }
    public boolean possible(double grmin,double grmmax,double cgpa){
        if(cgpa==4){
            if(grmin==cgpa){
                return true;
            }else {
                return false;
            }
        }
        if(grmin<=cgpa&&cgpa<grmmax){
            return true;
        }else {
            return false;
        }
    }
    public String[] Descending2 (String[] order){
        String temp;
        for (int i = 0; i < order.length; i++) {
            for (int j = i + 1; j < order.length; j++) {
                if (grade(order[i]) < grade(order[j])) {
                    temp = order[i];
                    order[i] = order[j];
                    order[j] = temp;
                }
            }
        }
        return order;
    }
    public double grade(String grade) {
        double marks=0, gpa;

        if(grade.isEmpty()){
            return 0;
        }

        if(isFound("A",grade)){
            marks=80;
        }else if(isFound("B",grade)){
            marks = 65;
        }else if(isFound("C",grade)){
            marks = 50;
        }

        if(isFound("+",grade)){
            marks+=5;
        }else if(isFound("-",grade)){
            marks-=5;
        }
//            if (isFound("1", grade)) {
//                marks += 1;
//            } else if (isFound("2", grade)) {
//                marks += 2;
//            } else if (isFound("3", grade)) {
//                marks += 3;
//            } else if (isFound("4", grade)) {
//                marks += 4;
//            }
        if(marks>=80&&marks<=100){
            return 4;
        }else if(marks<=80&&marks>=50) {
            gpa = (((marks - 50) / 30) * 2) + 2;
            return gpa;
        }else if(marks<=50&&marks>=0){
            return 0;
        }
        return  999999;

    }
    public String[][] ArrangeGrades(ArrayList<String> oldGrades){
        String[][] TempGrades=new String[oldGrades.size()][20];

        for(int s=0 ;s<oldGrades.size();s++){
            TempGrades[s] = oldGrades.get(s).split(",",-1);
        }


        return TempGrades;
    }
    public double[] DescendingDouble (double[] order){
        double temp;
        for (int i = 0; i < order.length; i++)
        {
            for (int j = i + 1; j < order.length; j++)
            {
                if (order[i] < order[j]) {
                    temp = order[i];
                    order[i] = order[j];
                    order[j] = temp;
                }
            }
        }
        return order;
    }
    public String noNumNoCon(String grad) {
        String newGrade="";
        if (isFound("A", grad)) {
            newGrade = "A";
        } else if (isFound("B", grad)) {
            newGrade = "B";
        } else if (isFound("C", grad)) {
            newGrade = "C";
        }

        if (isFound("+", grad)) {
            newGrade += "+";
        } else if (isFound("-", grad)) {
            newGrade += "-";
        }
        return newGrade;
    }
    public void GetDegreePlanner() {
        if (planChoice == 1) {
            //assigning values
            double hoursFinished = (totalHours - remainingHours);
            newCGPA = (goal * totalHours - CGPA * hoursFinished) / remainingHours;
            planGPA = new double[(int) remainingHours];
            double temp;
            int tot = 0;
            int counter = 0;
            for (int i = 1; i < remainingHours + 1; i++) {
                tot += i;
            }
            double expValue = (goal * totalHours - CGPA * totalHours) / tot;
            double total = 0;

            //making the curve
            for (int i = 1; i < remainingHours + 1; i++) {
                planGPA[i - 1] = CGPA + i * expValue;
            }

            //reversing values less than 4
            for (int i = (int) remainingHours; i > 0; i--) {
                if ((planGPA[i - 1] < 4) && i > counter) {
                    temp = planGPA[i - 1];
                    planGPA[i - 1] = planGPA[counter];
                    planGPA[counter] = temp;
                    counter++;
                }
            }

            //chopping values bigger than 4 and store it in refine
            double refine = 0;
            for (int i = (int) remainingHours; i > 0; i--) {
                if (planGPA[i - 1] > 4) {
                    refine += (planGPA[i - 1] - 4);
                    planGPA[i - 1] = 4;
                }
            }

            //restore the values chopped to values less than 4
            double piece;
            for (int i = (int) remainingHours; i > 0; i--) {
                if (refine > 0 && planGPA[i - 1] < 4) {
                    piece = (4 - planGPA[i - 1]);
                    planGPA[i - 1] = 4;
                    refine -= piece;
                }
            }
            //reverse back the values
            counter = 0;
            for (int i = (int) remainingHours; i > 0; i--) {
                if ((planGPA[i - 1] < 4) && i > counter) {
                    temp = planGPA[i - 1];
                    planGPA[i - 1] = planGPA[counter];
                    planGPA[counter] = temp;
                    counter++;
                }
            }


            for (int i = 0; i < remainingHours; i++) {
                total += planGPA[i];
            }
            mean = total / remainingHours;
        } else if (planChoice == 2) {

            //assigning values
            double hoursFinished = (totalHours - remainingHours);
            newCGPA = (goal * totalHours - CGPA * hoursFinished) / remainingHours;
            if(newCGPA>4){
                newCGPA=4;
            }
            planGPA = new double[(int) remainingHours];
            for (int i = 1; i < remainingHours + 1; i++) {
                planGPA[i - 1] = newCGPA;
            }
            mean = newCGPA;

        } else if (planChoice == 3) {

            //assigning values
            double hoursFinished = (totalHours - remainingHours);
            newCGPA = (goal * totalHours - CGPA * hoursFinished) / remainingHours;
            planGPA = new double[(int) remainingHours];
            double[] Reverse = new double[(int) remainingHours];
            double temp;
            int tot = 0;
            int counter = 0;
            for (int i = 1; i < remainingHours + 1; i++) {
                tot += i;
            }
            double expValue = (goal * totalHours - CGPA * totalHours) / tot;
            double total = 0;

            //making the curve
            for (int i = 1; i < remainingHours + 1; i++) {
                planGPA[i - 1] = CGPA + i * expValue;
            }

            //reversing values less than 4
            for (int i = (int) remainingHours; i > 0; i--) {
                if ((planGPA[i - 1] < 4) && i > counter) {
                    temp = planGPA[i - 1];
                    planGPA[i - 1] = planGPA[counter];
                    planGPA[counter] = temp;
                    counter++;
                }
            }

            //chopping values bigger than 4 and store it in refine
            double refine = 0;
            for (int i = (int) remainingHours; i > 0; i--) {
                if (planGPA[i - 1] > 4) {
                    refine += (planGPA[i - 1] - 4);
                    planGPA[i - 1] = 4;
                }
            }

            //restore the values chopped to values less than 4
            double piece;
            for (int i = (int) remainingHours; i > 0; i--) {
                if (refine > 0 && planGPA[i - 1] < 4) {
                    piece = (4 - planGPA[i - 1]);
                    planGPA[i - 1] = 4;
                    refine -= piece;
                }
            }
            //reverse back the values
            counter = 0;
            for (int i = (int) remainingHours; i > 0; i--) {
                if ((planGPA[i - 1] < 4) && i > counter) {
                    temp = planGPA[i - 1];
                    planGPA[i - 1] = planGPA[counter];
                    planGPA[counter] = temp;
                    counter++;
                }
            }
            counter = 0;
            for (int i = (int) remainingHours; i > 0; i--) {
                    Reverse[counter]=planGPA[i-1];
                    counter++;
            }
            for (int i = 0; i < (int) remainingHours; i++) {
                planGPA[i]=Reverse[i];
            }

            for (int i = 0; i < remainingHours; i++) {
                total += planGPA[i];
            }
            mean = total / remainingHours;

        }

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


}
