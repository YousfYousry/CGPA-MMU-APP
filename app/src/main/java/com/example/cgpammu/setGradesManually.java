package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class setGradesManually extends AppCompatActivity {
    ListView chl;
    ArrayList<String> LoadcourseStructure = new ArrayList<>();
    ArrayList<String> TrimesterAndYear = new ArrayList<>();
    String[][] courseStructure = new String[100][20];
    int num_of_hours_next_sem = 0;
    ArrayList<String> subjects = new ArrayList<>();
    setGradesManually setGradesManually = this;
    String[][] subInfo = new String[200][4];
    ArrayList<String> subname = new ArrayList<>();
    ArrayList<String> subcode = new ArrayList<>();
    ArrayList<String> submark = new ArrayList<>();
    ArrayList<String> numOfSub = new ArrayList<>();
    ArrayList<String> trimester = new ArrayList<>();
    ArrayList<String> trimestertosave = new ArrayList<>();
    ArrayList<String> result_code = new ArrayList<>();
    ArrayList<String> result_subName = new ArrayList<>();
    ArrayList<String> result_grade = new ArrayList<>();
    int[] selection = new int[200];
    int[] selectionTemp = new int[200];
    String grade;
    boolean selectionis = false;
    String[] numSub = new String[50];
    ParseUser user =ParseUser.getCurrentUser();

    public void ClearFields2(View view){
        selectionis=false;
        DisplaySubjects();
    }
    public void finish(View view) {
        if (saveGrades()) {
            Toast.makeText(this, "Your data is saved into your account", Toast.LENGTH_LONG).show();
            Intent setGoal = new Intent(this, setGoal.class);
            startActivity(setGoal);
            finish();
        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    public void setSelection(int position,int i) {
        subInfo[position][2]=getGrade(i);
    }

    //    public void setSubInfo(String[][] subInfo) {
//        this.subInfo = subInfo;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_grades_manually);

        TrimesterAndYear = loadData("TrimesterSemAndYear");
        LoadcourseStructure = loadData("CourseStructure");
        courseStructure = getcourseStructure();

        result_code = loadData("resultCode");
        result_subName = loadData("resultSubName");
        result_grade = loadData("resultGrade");

        for (int i = 0; i < subInfo.length; i++) {
            subInfo[i][0] = "";
            subInfo[i][1] = "";
            subInfo[i][2] = "";//marks
            subInfo[i][3] = "";
        }

        if(result_subName.size()>0){
            selectionis=true;
        }
        for (int i = 0; i < selectionTemp.length; i++) {
            selectionTemp[i] = 0;
        }
        DisplaySubjects();

    }

    @Override
    public void onBackPressed() {

        Intent set_manually = new Intent(this, set_manually.class);
        startActivity(set_manually);
        finish();
    }
    public boolean saveGrades(){
        numOfSub.clear();
        subcode.clear();
        submark.clear();
        subname.clear();
        trimestertosave.clear();

        int sem=1,counter=0;
        for(int i=0;i<subInfo.length;i++){
            if(!subInfo[i][3].isEmpty()){
                sem++;
                numSub[sem-1]="0";
                counter=0;
            }
            if("-".compareTo(subInfo[i][2])!=0&&!subInfo[i][2].isEmpty()){
                subcode.add(subInfo[i][0]);
                subname.add(subInfo[i][1]);
                submark.add(subInfo[i][2]);
                counter++;
                numSub[sem-1]=Integer.toString(counter);
            }
        }
        for(int i =0;i<numSub.length;i++) {
            if (numSub[i] != null && !numSub[i].isEmpty()) {
                numOfSub.add(numSub[i]);
            }
        }
        int size=0;
        for(int i = numOfSub.size()-1;i>=0;i--){
            if(Integer.parseInt(numOfSub.get(i))!=0){
                size=i;
                break;
            }
        }
        for(int i=0;i<trimester.size();i++){
            if(i<=size){
                trimestertosave.add(trimester.get(i));
            }
        }
            saveData(trimestertosave, "resultTrimester");
            saveData(subcode, "resultCode");
            saveData(subname, "resultSubName");
            saveData(submark, "resultGrade");
            saveData(numOfSub, "numOfSub");
        return true;
    }
    public void DisplaySubjects(){
        boolean elective, end_of_elective;
        int num_of_sem_finished;
        ArrayList<subjectManually> dataToPrint = new ArrayList<>();
        subjectManually sub;


        num_of_hours_next_sem=0;
        ArrayList<String>nextSemSubjects_code = new ArrayList<>();
        ArrayList<String>nextSemSubjects_elective_code = new ArrayList<>();
        ArrayList<String>nextSemSubjects_name = new ArrayList<>();
        ArrayList<String>nextSemSubjects_elective_name = new ArrayList<>();
        ArrayList<String>nextSemSubjects_hours = new ArrayList<>();
        ArrayList<String>nextSemSubjects_elective_hours = new ArrayList<>();


        int year=Integer.parseInt(TrimesterAndYear.get(0)),trim=Integer.parseInt(TrimesterAndYear.get(1));

        int s,sc=0;
        String title="";
        for(num_of_sem_finished=0;num_of_sem_finished<12;num_of_sem_finished++) {
            if(trim>=2&&trim<=3){
                sub=new subjectManually("Trimester "+ trim +" - "+year +"/"+(year+1),"","","",1);
                dataToPrint.add(sub);
                trimester.add("Trimester "+ trim +" - "+year +"/"+(year+1));
                trim++;
                title = "Trimester "+ trim +" - "+year +"/"+(year+1);
                if(trim>3){
                    year++;
                }
            }else {
                trim=1;
                sub=new subjectManually("Trimester "+ trim +" - "+year +"/"+(year+1),"","","",0);
                dataToPrint.add(sub);
                trimester.add("Trimester "+ trim +" - "+year +"/"+(year+1));
                trim++;
                title="Trimester "+ trim +" - "+year +"/"+(year+1);
            }
            subInfo[sc][0]="";
            subInfo[sc][1]="";
            subInfo[sc][2]="";
            subInfo[sc][3]=title;
            sc++;


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
                                if ( end_of_elective) {
                                    nextSemSubjects_elective_code.add(courseStructure[y][0]+" (Elective)");
                                    nextSemSubjects_elective_name.add(courseStructure[y][1]);
                                    nextSemSubjects_elective_hours.add("*"+courseStructure[y][x]+"*.");
                                } else {
                                    nextSemSubjects_code.add(courseStructure[y][0]);
                                    nextSemSubjects_name.add(courseStructure[y][1]);
                                    nextSemSubjects_hours.add("*"+courseStructure[y][x]+"*.");
                                }

                            if (end_of_elective && courseStructure[y + 1][0].isEmpty()) {
                                end_of_elective = false;
                            }

                        }
                    }
            }
            for ( s=0; s < nextSemSubjects_name.size(); s++) {

                sub=new subjectManually("",nextSemSubjects_code.get(s),nextSemSubjects_name.get(s),nextSemSubjects_hours.get(s),2);
                dataToPrint.add(sub);
                if(finishedSubject(nextSemSubjects_code.get(s))){
                    if(!isFound("MPU",nextSemSubjects_name.get(s))&&!isFound("MPU",nextSemSubjects_code.get(s))) {
                        selectionTemp[sc] = getPositionCore(grade);
                    }else{
                        selectionTemp[sc] = getPositionMPU(grade);
                    }
                }else{
                    selectionTemp[sc]=0;
                }
                subInfo[sc][0]=nextSemSubjects_code.get(s);
                subInfo[sc][1]=nextSemSubjects_name.get(s);
                subInfo[sc][2]=getGrade(selectionTemp[sc]);
                subInfo[sc][3]="";
                sc++;
            }
            nextSemSubjects_code.clear();
            nextSemSubjects_name.clear();
            nextSemSubjects_hours.clear();

            for (s=0; s < nextSemSubjects_elective_name.size(); s++) {

                sub=new subjectManually("",nextSemSubjects_elective_code.get(s),nextSemSubjects_elective_name.get(s),nextSemSubjects_elective_hours.get(s),2);
                dataToPrint.add(sub);
                if(finishedSubject(nextSemSubjects_elective_code.get(s))){
                    if(!isFound("MPU",nextSemSubjects_elective_name.get(s))&&!isFound("MPU",nextSemSubjects_elective_code.get(s))) {
                        selectionTemp[sc] = getPositionCore(grade);
                    }else{
                        selectionTemp[sc] = getPositionMPU(grade);
                    }
                }else{
                    selectionTemp[sc]=0;
                }
                subInfo[sc][0]=nextSemSubjects_elective_code.get(s);
                subInfo[sc][1]=nextSemSubjects_elective_name.get(s);
                subInfo[sc][2]=getGrade(selectionTemp[sc]);
                subInfo[sc][3]="";
                sc++;
            }
            nextSemSubjects_elective_code.clear();
            nextSemSubjects_elective_name.clear();
            nextSemSubjects_elective_hours.clear();

            sub=new subjectManually("","","","",2);
            dataToPrint.add(sub);
            subInfo[sc][0]="";
            subInfo[sc][1]="";
            subInfo[sc][2]="";
            subInfo[sc][3]="";
            sc++;

        }

        chl=(ListView) findViewById(R.id.setGradesForMarks);
        setGradesListAdapter adapter = new setGradesListAdapter(this, R.layout.manually_subjects, dataToPrint);

        if(selectionis){
            for (int i = 0; i < selection.length; i++) {
                selection[i] = selectionTemp[i];
            }
        }else{
            for (int i = 0; i < selection.length; i++) {
                selection[i] = 0;
            }
        }
        adapter.setSelection(selection);
//        adapter.starter();
        adapter.setManually(setGradesManually);
        chl.setAdapter(adapter);
    }
    public int getPositionCore(String grade){
        if(isFound("A+",grade)){
            return 1;
        }else
        if(isFound("A-",grade)){
            return 3;
        }else
        if(isFound("A",grade)){
            return 2;
        }else
        if(isFound("B+",grade)){
            return 4;
        }else
        if(isFound("B-",grade)){
            return 6;
        }else
        if(isFound("B",grade)){
            return 5;
        }else
        if(isFound("C+",grade)){
            return 7;
        }else
        if(isFound("C",grade)){
            return 0;
        }else{return 0;}
    }
    public int getPositionMPU(String grade){
        if(isFound("PS",grade)){
            return 1;
        }else
        if(isFound("FL",grade)){
            return 2;
        }else{
            return 0;
        }
    }
    public String getGrade(int pos){
        if(pos==0){
            return "-";
        }else
        if(pos==1){
            return "A+";
        }else
        if(pos==2){
            return "A";
        }else
        if(pos==3){
            return "A-";
        }else
        if(pos==4){
            return "B+";
        }else
        if(pos==5){
            return "B";
        }else
        if(pos==6){
            return "B-";
        }else
        if(pos==7){
            return "C+";
        }else
        if(pos==8){
            return "C";
        }else {
            return "-";
        }
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
    public boolean isFound(String p,String hph){
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
    public boolean finishedSubject(String code){
        grade="";
        for(int y=0;y<result_code.size();y++) {
            if (isFound(clearFromSpacing(result_code.get(y)),code)) {
                grade=result_grade.get(y);
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
}
