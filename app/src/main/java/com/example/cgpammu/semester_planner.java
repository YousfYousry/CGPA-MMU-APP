package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import maes.tech.intentanim.CustomIntent;


public class semester_planner extends AppCompatActivity implements setGoalDialog.ExampleDialogListener{

    ArrayList<String> LoadcourseStructure = new ArrayList<>();
    ArrayList<String> selectedItems=new ArrayList<>();
    ArrayList<String> result_Trimester = new ArrayList<>();
    ArrayList<String> result_code = new ArrayList<>();
    ArrayList<String> result_subName = new ArrayList<>();
    ArrayList<String> result_grade = new ArrayList<>();
    ArrayList<String> student_info = new ArrayList<>();
    ArrayList<String> gpa_and_totalHours = new ArrayList<>();
    ArrayList<String> numOfSub = new ArrayList<>();
    ArrayList<subject> dataToPrint = new ArrayList<>();
    String reason="";
    String[] offeredSubjects=new String[500];
    String[][] courseStructure=new String[100][20];
    String[][][] result=new String[20][200][3];
    String[][] choosed_Sub;
    int[] colorarr1;
    boolean allAvailableSub = false,retake =false,enterPressed=false,shownum=false,isShown=false,isShown2=false;
    int num_of_hours_next_sem=0;
    int num_of_selected_sub,flag=0;
    double newGpa,CGPA,goal,r=5;
    double[] hours;
    int[] colour;
    int setplans=10;
    CheckBox checkBox;
    subjectListAdapterCkeck aa;
    setGoalDialog exampleDialog;
    ParseUser user =ParseUser.getCurrentUser();
    semester_planner Semester = this;
    SeekBar seekBar;
    CheckBox checkBoxHours,checkBoxAllPlans;
    Dialog settingsDialog,settingsDialog2;

    public void setGoal(double goal) {
        this.goal = goal;
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
        displayPlan();
    }
    public void closeShowNum(View view){
        settingsDialog.dismiss();
        settingsDialog2.dismiss();
    }
    public void ShowPlansClicked(View view) {
        if (checkBoxAllPlans.isChecked()) {
            setplans = 100;
        } else {
            setplans = 10;
        }
        displayPlan();
    }
    public void ShowAll(View view) {
        aa.clear();
        if(checkBox.isChecked()){
            allAvailableSub=true;
            displayAllSub();
            displaygpa();
            resetLayout();
        }else{
            allAvailableSub=false;
            display();
            displaygpa();
            resetLayout();
        }
    }
    public void changeGoal2(View view) {
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
    public void EnterPressedForSemesterPlanner(View view){
        if(flag==0) {
            if (num_of_hours_next_sem > 18) {
                TextView more = (TextView) findViewById(R.id.warnnig);
                more.setText("Warning! Total Hours is more than requirement 18 hours");
                more.setTextColor(Color.parseColor("#FF0000"));
                TextView hide = (TextView) findViewById(R.id.textView22);
                TextView hide2 = (TextView) findViewById(R.id.textView44);
                hide.setVisibility(View.INVISIBLE);
                hide2.setVisibility(View.INVISIBLE);

            } else {
                enterPressed = true;
                displayPlan();
                hideLayout();
            }
        }else if(flag==1){
            Intent retakeSubPlanner = new Intent(getApplicationContext(), retakeSubPlanner.class);
            startActivity(retakeSubPlanner);
            CustomIntent.customType(this,"left-to-right");
            finish();
        }
    }
    public void dismiss(){
        float amount = (float) goal;
        ArrayList<String> goalArr = new ArrayList<>();
        goalArr.add(Float.toString((float) amount));
        saveData(goalArr,"goal");
        exampleDialog.dismiss();
        resetLayout();
        displaygpa();
        if(newGpa<goal-0.001){
            retake=true;
        }else{
            retake=false;
        }
        if(enterPressed){
            displayPlan();
            hideLayout();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_planner);

        selectedItems = new ArrayList<String>();
        exampleDialog = new setGoalDialog();
        exampleDialog.setSemester(Semester);
        LoadcourseStructure = loadData("CourseStructure");
        result_Trimester = loadData("resultTrimester");
        result_code = loadData("resultCode");
        result_subName = loadData("resultSubName");
        result_grade = loadData("resultGrade");
        student_info = loadData("studentInfo");
        gpa_and_totalHours = loadData("studentGPAAndTotalHours");
        numOfSub = loadData("numOfSub");
        CGPA = DoubleNumricalOf(gpa_and_totalHours.get(0));
        ArrayList<String> goalArr = new ArrayList<>();
        goalArr = loadData("goal");
        if(goalArr.size()>0) {
            goal = Double.parseDouble(goalArr.get(0));
        }
        checkBox = (CheckBox) findViewById(R.id.checkBox4);
        exampleDialog.setMAXGPA(4);
        exampleDialog.setCGPA(CGPA);
        settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout2
                , null));
        settingsDialog2 = new Dialog(this);
        settingsDialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog2.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
        checkBoxHours = (CheckBox) findViewById(R.id.checkBox2);
        checkBoxAllPlans = (CheckBox) findViewById(R.id.checkBox3);
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
                displayPlan();
            }
        });

        TextView buttonText = (TextView) findViewById(R.id.EnterBText);
        buttonText.setText("ENTER");


        arrayListToArray();

        courseStructure = getcourseStructure();

        displaygpa();

        display();

    }

    @Override
    public void onBackPressed() {
        if (enterPressed) {
            enterPressed=false;
            resetLayout();
            aa.clear();
            if(checkBox.isChecked()){
                displayAllSub();
                displaygpa();
            }else{
                display();
                displaygpa();
            }
        } else {
            Intent mainapp = new Intent(this, MainPage.class);
            startActivity(mainapp);
            CustomIntent.customType(this,"right-to-left");
            finish();
        }
    }

    @Override
    public void applyTexts(String username, String password) {}

    public void hideLayout(){
        if(retake) {

            FrameLayout goalButton = (FrameLayout) findViewById(R.id.frammmmm3);
            goalButton.setVisibility(View.VISIBLE);

            TextView buttonText = (TextView) findViewById(R.id.EnterBText);
            buttonText.setText("Go to retake subject planner");
            ImageView retakeLogo =(ImageView) findViewById(R.id.retakelogo);
            retakeLogo.setBackgroundResource(R.drawable.checklist);

            TextView edit = (TextView) this.findViewById(R.id.editThisAfterEnter);
            edit.setText("Subject taken next semester + suggested plans");

            FrameLayout hidebutton = (FrameLayout) findViewById(R.id.frammmmm);
            hidebutton.setVisibility(View.INVISIBLE);
            CheckBox hideCheckBox = (CheckBox) findViewById(R.id.checkBox4);
            hideCheckBox.setVisibility(View.INVISIBLE);

            flag=1;

            FrameLayout button2 = (FrameLayout) findViewById(R.id.bottomm2);
            button2.setVisibility(View.VISIBLE);
            ImageButton button22 = (ImageButton) findViewById(R.id.EnterB2);
            button22.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent degreePlanner = new Intent(getApplicationContext(), degreePlanner.class);
                    startActivity(degreePlanner);
                    CustomIntent.customType(semester_planner.this, "left-to-right");
                    finish();
                }
            });


            FrameLayout framText = (FrameLayout) findViewById(R.id.framtext);
            framText.setVisibility(View.VISIBLE);
            TextView textView = (TextView) findViewById(R.id.retakeText);
            textView.setText("Warning! your new CGPA still lower than your goal");

        }else{
            TextView edit = (TextView) this.findViewById(R.id.editThisAfterEnter);
            edit.setText("Subject taken next semester + suggested plans");
            FrameLayout hidebutton = (FrameLayout) findViewById(R.id.frammmmm);
            hidebutton.setVisibility(View.INVISIBLE);
            CheckBox hideCheckBox = (CheckBox) findViewById(R.id.checkBox4);
            hideCheckBox.setVisibility(View.INVISIBLE);
            FrameLayout button = (FrameLayout) findViewById(R.id.bottomm);
            button.setVisibility(View.INVISIBLE);
            LinearLayout controlCenter = (LinearLayout) findViewById(R.id.middle);
            controlCenter.setVisibility(View.VISIBLE);

        }
    }
    public void resetLayout(){

        FrameLayout goalButton = (FrameLayout) findViewById(R.id.frammmmm3);
        goalButton.setVisibility(View.INVISIBLE);

        ImageView retakeLogo =(ImageView) findViewById(R.id.retakelogo);
        retakeLogo.setBackgroundResource(R.drawable.semesterenter);
        TextView edit = (TextView) this.findViewById(R.id.editThisAfterEnter);
        if(!allAvailableSub) {
            edit.setText("Offered subjects for next sem, Select subjects to take:");
        }else{
            edit.setText("All subjects in your course structure, Select subjects to take:");
        }
        TextView zero = (TextView) this.findViewById(R.id.totalHoursPlanner);
        zero.setText("0");
        FrameLayout hidebutton =(FrameLayout) findViewById(R.id.frammmmm);
        hidebutton.setVisibility(View.VISIBLE);
        CheckBox hideCheckBox= (CheckBox) findViewById(R.id.checkBox4);
        hideCheckBox.setVisibility(View.VISIBLE);
        TextView buttonText = (TextView) findViewById(R.id.EnterBText);
        buttonText.setText("ENTER");
        FrameLayout buttonframe = (FrameLayout) findViewById(R.id.bottomm);
        buttonframe.setVisibility(View.VISIBLE);
        ImageButton button = (ImageButton) findViewById(R.id.EnterB);
        button.setVisibility(View.VISIBLE);
        flag=0;
        FrameLayout frame = (FrameLayout) findViewById(R.id.bottomm2);
        frame.setVisibility(View.INVISIBLE);
        TextView textView = (TextView) findViewById(R.id.retakeText);
        textView.setText("");
        FrameLayout framText = (FrameLayout) findViewById(R.id.framtext);
        framText.setVisibility(View.INVISIBLE);
        LinearLayout controlCenter = (LinearLayout) findViewById(R.id.middle);
        controlCenter.setVisibility(View.INVISIBLE);
    }
    public ArrayList<String> loadData(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(name, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void displaygpa(){

        TextView hide = (TextView) this.findViewById(R.id.textView22);
        TextView hide2 = (TextView) this.findViewById(R.id.textView44);
        hide.setVisibility(View.VISIBLE);
        hide2.setVisibility(View.VISIBLE);

        TextView currCGPA = (TextView) this.findViewById(R.id.setCGPA);
        currCGPA.setText(Double.toString(DoubleNumricalOf(gpa_and_totalHours.get(0))));
        currCGPA.setTextColor(Color.parseColor("#FF0000"));

        TextView ExpGPA = (TextView) this.findViewById(R.id.setExpectedGpa);
        ExpGPA.setText(expGPA(num_of_hours_next_sem));
        ExpGPA.setTextColor(Color.parseColor("#FF0000"));


        TextView newCGPA = (TextView) this.findViewById(R.id.setNewGpa);
        newCGPA.setText(newGPA());
        newCGPA.setTextColor(Color.parseColor("#FF0000"));


        TextView totalHoursPlanner = (TextView) this.findViewById(R.id.totalHoursPlanner);
        totalHoursPlanner.setText(Integer.toString(num_of_hours_next_sem));
        totalHoursPlanner.setTextColor(Color.parseColor("#000000"));

        TextView removeWarrning = (TextView) this.findViewById(R.id.warnnig);
        removeWarrning.setText("");
    }
    public String expGPA(int num_of_hours){
        double curr_cgpa=DoubleNumricalOf(gpa_and_totalHours.get(0)),
                total_hours=DoubleNumricalOf(gpa_and_totalHours.get(1)) , exgpa;
        if(num_of_hours==0){
            exgpa=0;
        }else {
            exgpa = (goal * (total_hours + num_of_hours) - curr_cgpa * total_hours) / (num_of_hours);
        }
        if(exgpa>4) {
            return "4.00";
        }else if(exgpa>=0){
            return Double.toString(roundNum(exgpa));
        }else {
            return Double.toString(0.00);
        }

    }
    public double expGPA2(int num_of_hours){
        double curr_cgpa=DoubleNumricalOf(gpa_and_totalHours.get(0)),
                total_hours=DoubleNumricalOf(gpa_and_totalHours.get(1)) , exgpa;
        if(num_of_hours==0){
            exgpa=0;
        }else {
            exgpa = (goal * (total_hours + num_of_hours) - curr_cgpa * total_hours) / (num_of_hours);
        }
        if(exgpa>4) {
            return 4.00;
        }else if(exgpa>=0){
            return exgpa;
        }else {
            return 0.00;
        }

    }
    public String newGPA(){
        double curr_cgpa=DoubleNumricalOf(gpa_and_totalHours.get(0)),
                total_hours=DoubleNumricalOf(gpa_and_totalHours.get(1)) ;
        newGpa =roundNum((Double.parseDouble(expGPA(num_of_hours_next_sem))*num_of_hours_next_sem+curr_cgpa*total_hours)/(total_hours+num_of_hours_next_sem));
        return Double.toString(roundNum((Double.parseDouble(expGPA(num_of_hours_next_sem))*num_of_hours_next_sem+curr_cgpa*total_hours)/(total_hours+num_of_hours_next_sem)));
    }
    public double roundNum(double num){
        return (Math.round(num*100.0)/100.0);
    }
    public void display() {
        int num_of_sem_finished = 0,i=0,a=0,c=0,s;
        num_of_hours_next_sem = 0;
        final int[] color = new int[300];
        ArrayList<String> nextSemSubjects_code = new ArrayList<>();
        ArrayList<String> nextSemSubjects_elective_code = new ArrayList<>();
        ArrayList<String> nextSemSubjects_name = new ArrayList<>();
        ArrayList<String> nextSemSubjects_elective_name = new ArrayList<>();
        ArrayList<String> nextSemSubjects_hours = new ArrayList<>();
        ArrayList<String> nextSemSubjects_elective_hours = new ArrayList<>();
        for(i=0;i<result_Trimester.size();i++) {
            if (isFound("Trimester", result_Trimester.get(i))){
                num_of_sem_finished++;
            }
        }

        boolean elective = false, end_of_elective = false;
        for (int y = 0; y < courseStructure.length - 1; y++) {
            for (int x = 2; x < courseStructure[y].length - 1; x++) {
                if (courseStructure[y][0] != null && courseStructure[y][1] != null && courseStructure[y][x] != null) {
                    if (isFound("Elective", courseStructure[y][1]) && (x == (num_of_sem_finished + 2)) && (!courseStructure[y + 1][x].isEmpty())) {
                        elective = true;
                    }
                    if ((x == (num_of_sem_finished + 2)) && (!courseStructure[y][x].isEmpty())) {
                        if (elective) {
                            elective = false;
                            end_of_elective = true;
                            nextSemSubjects_elective_code.add("");
                            nextSemSubjects_elective_name.add("Elective");
                            nextSemSubjects_elective_hours.add(".");
                        }
                        if (available(y)) {
                            if (end_of_elective) {
                                nextSemSubjects_elective_code.add(courseStructure[y][0]);
                                nextSemSubjects_elective_name.add(courseStructure[y][1]);
                                nextSemSubjects_elective_hours.add("*" + courseStructure[y][x] + "*.");
                            } else {
                                nextSemSubjects_code.add(courseStructure[y][0]);
                                nextSemSubjects_name.add(courseStructure[y][1]);
                                nextSemSubjects_hours.add("*" + courseStructure[y][x] + "*.");
                            }
                        } else {
                            if (end_of_elective) {
                                nextSemSubjects_elective_code.add(courseStructure[y][0]);
                                nextSemSubjects_elective_name.add(courseStructure[y][1] + "" + reason);
                                nextSemSubjects_elective_hours.add("*" + courseStructure[y][x] + "*");
                            } else {
                                nextSemSubjects_elective_code.add(courseStructure[y][0]);
                                nextSemSubjects_elective_name.add(courseStructure[y][1] + "" + reason);
                                nextSemSubjects_elective_hours.add("*" + courseStructure[y][x] + "*");
                            }
                        }
                        if (end_of_elective && courseStructure[y + 1][0].isEmpty()) {
                            end_of_elective = false;

                            nextSemSubjects_elective_code.add("");
                            nextSemSubjects_elective_name.add("End of Elective subjects");
                            nextSemSubjects_elective_hours.add(".");
                        }

                    }
                }
            }
        }

        i=0;
        for (s=0; s < nextSemSubjects_name.size(); s++) {
            offeredSubjects[i] = nextSemSubjects_code.get(s);
            i++;
            offeredSubjects[i] = nextSemSubjects_name.get(s);
            i++;
            offeredSubjects[i] = nextSemSubjects_hours.get(s);
            i++;

            if(finishedSubject(nextSemSubjects_code.get(s))) {
                color[c] = 4;
                c++;
            }else if(isFound("*.",nextSemSubjects_hours.get(s))){
                color[c] = 2;
                c++;
            }else if(isFound("*",nextSemSubjects_hours.get(s))){
                color[c] = 3;
                c++;
            }
        }
        for (s=0; s < nextSemSubjects_elective_name.size(); s++) {
            offeredSubjects[i] = nextSemSubjects_elective_code.get(s);
            i++;
            offeredSubjects[i] = nextSemSubjects_elective_name.get(s);
            i++;
            offeredSubjects[i] = nextSemSubjects_elective_hours.get(s);
            i++;
            if(finishedSubject(nextSemSubjects_elective_code.get(s))) {
                color[c] = 4;
                c++;
            }else if(isFound("*.",nextSemSubjects_elective_hours.get(s))){
                color[c] = 2;
                c++;
            }else if(isFound("*",nextSemSubjects_elective_hours.get(s))){
                color[c] = 3;
                c++;
            }
        }



        num_of_selected_sub = nextSemSubjects_name.size() + nextSemSubjects_elective_name.size();
        final String[][] offeredSubFinal = new String[num_of_selected_sub][3];
        i=0;
        for(s=0;s<num_of_selected_sub;s++){
            offeredSubFinal[s][0] = offeredSubjects[i];
            offeredSubFinal[s][1] = offeredSubjects[i+1];
            offeredSubFinal[s][2] = offeredSubjects[i+2];
            i+=3;
        }


        subject[] sub=new subject[num_of_selected_sub];
        for(s=0;s<num_of_selected_sub;s++) {
            sub[s]=new subject(offeredSubFinal[s][0],offeredSubFinal[s][1],HoursFromStarString(offeredSubFinal[s][2]),color[s]);
        }

        final ListView chl=(ListView) findViewById(R.id.checkable_list);
        for(s=0;s<num_of_selected_sub;s++) {
                dataToPrint.add(sub[s]);
        }

        subject subbb = new subject("","","",0);
        dataToPrint.add(subbb);

        aa  = new subjectListAdapterCkeck(this, R.layout.empty_layout, dataToPrint);

        final String[][] choosedSub = new String[num_of_selected_sub][3];
        for(i=0;i<num_of_selected_sub;i++) {
            for (s = 0; s < 3; s++) {
                choosedSub[i][s] = "";
            }
        }

        colour = new int[num_of_selected_sub+1];
        for(i=0;i<num_of_selected_sub;i++){
            colour[i]=color[i];
        }
        colour[i]=0;
        aa.setColor(colour);
        chl.setAdapter(aa);
        final int size =num_of_selected_sub;
        final ArrayList<subject> subb = new ArrayList<>();
        //set OnItemClickListener
        chl.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            int choosedSize=0;
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<size) {
                    if (offeredSubFinal[position][1].compareTo(choosedSub[position][1]) == 0) {
                        if (isFound("*.", offeredSubFinal[position][2])) {
                            choosedSize--;
                            choosedSub[position][0] = "";
                            choosedSub[position][1] = "";
                            choosedSub[position][2] = "";
                            num_of_hours_next_sem -= HoursFromStar(offeredSubFinal[position][2]);
                            displaygpa();
                            colour[position] = 0;
                            view.setBackgroundColor(Color.alpha(0));
                            saveArray(choosedSub,choosedSize);
                        }else{
                            choosedSize--;
                            choosedSub[position][0] = "";
                            choosedSub[position][1] = "";
                            choosedSub[position][2] = "";
                            num_of_hours_next_sem -= HoursFromStar(offeredSubFinal[position][2]);
                            displaygpa();
                            colour[position] = 3;
                            view.setBackgroundColor(Color.RED);
                            saveArray(choosedSub,choosedSize);
                        }
                    } else {
                        if (isFound("MPU", offeredSubFinal[position][1]) || isFound("MPU", offeredSubFinal[position][0])) {
                            Toast.makeText(semester_planner.this, "You can't increase your CGPA with MPU subject, choose another subject.", Toast.LENGTH_LONG).show();
                        } else {
                            if (isFound("*.", offeredSubFinal[position][2]) && colour[position] != 4) {
                                choosedSize++;
                                choosedSub[position][0] = offeredSubFinal[position][0];
                                choosedSub[position][1] = offeredSubFinal[position][1];
                                choosedSub[position][2] = offeredSubFinal[position][2];
                                num_of_hours_next_sem += HoursFromStar(offeredSubFinal[position][2]);
                                displaygpa();
                                colour[position] = 1;
                                view.setBackgroundColor(Color.alpha(1 / 2));
                                view.setBackgroundColor(Color.GRAY);
                                saveArray(choosedSub, choosedSize);
                            } else if (colour[position] == 4) {
                                Toast.makeText(semester_planner.this, "Go to retake subject planner if you want to take this subject again", Toast.LENGTH_LONG).show();
                            } else if (isFound("*", offeredSubFinal[position][2]) && colour[position] != 4) {
                                choosedSize++;
                                Toast.makeText(semester_planner.this, "Prerequisites of this subject are not satisfied", Toast.LENGTH_SHORT).show();
                                choosedSub[position][0] = offeredSubFinal[position][0];
                                choosedSub[position][1] = offeredSubFinal[position][1];
                                choosedSub[position][2] = offeredSubFinal[position][2];
                                num_of_hours_next_sem += HoursFromStar(offeredSubFinal[position][2]);
                                displaygpa();
                                colour[position] = 1;
                                view.setBackgroundColor(Color.alpha(1 / 2));
                                view.setBackgroundColor(Color.GRAY);
                                saveArray(choosedSub, choosedSize);
                            } else {
                            }
                        }
                    }
                }
            }});
    }
    public void displayAllSub(){

        ArrayList<subjectWithTitle> ToPrint = new ArrayList<>();
        subjectWithTitle Print;

        String[][] offeredSubjects=new String[500][3];

        num_of_hours_next_sem = 0;
        boolean  elective , end_of_elective ;
        final int[] colorarr = new int[300];
        ArrayList<String> nextSemSubjects_code = new ArrayList<>();
        ArrayList<String> nextSemSubjects_elective_code = new ArrayList<>();
        ArrayList<String> nextSemSubjects_name = new ArrayList<>();
        ArrayList<String> nextSemSubjects_elective_name = new ArrayList<>();
        ArrayList<String> nextSemSubjects_hours = new ArrayList<>();
        ArrayList<String> nextSemSubjects_elective_hours = new ArrayList<>();

        int year=0,trim=0;
        for(int i=0;i<result_Trimester.size();i++) {
            if(isFound("Trimester",result_Trimester.get(i))){
                trim =  TrimesterOf(result_Trimester.get(i));
                year = YearOf(result_Trimester.get(i));
                break;
            }
        }

        int i,s,c=0,num_of_sem_finished,sizeOfSubjects=0,fortitle=0,colorint;

        for(num_of_sem_finished=0;num_of_sem_finished<12;num_of_sem_finished++) {
            if(fortitle==0){
                colorarr[c]=5;
                Print = new subjectWithTitle("Trimester " + trim + " " + year + "/" + (year + 1), "", "", "");
                ToPrint.add(Print);
            }else {
                colorarr[c]=6;
                Print = new subjectWithTitle("Trimester " + trim + " " + year + "/" + (year + 1), "", "", "");
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
            sizeOfSubjects++;
            offeredSubjects[c][0]="";
            offeredSubjects[c][1]="";
            offeredSubjects[c][2]="";
            c++;
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
                                } else {
                                    nextSemSubjects_code.add(courseStructure[y][0]);
                                    nextSemSubjects_name.add(courseStructure[y][1]);
                                    nextSemSubjects_hours.add("*"+courseStructure[y][x]+"*.");
                                }
                            } else {
                                if (elective || end_of_elective) {
                                    nextSemSubjects_elective_code.add(courseStructure[y][0]+" (Elective)");
                                    nextSemSubjects_elective_name.add(courseStructure[y][1]+""+reason);
                                    nextSemSubjects_elective_hours.add("*"+courseStructure[y][x]+"*");
                                } else {
                                    nextSemSubjects_elective_code.add(courseStructure[y][0]);
                                    nextSemSubjects_elective_name.add(courseStructure[y][1]+""+reason);
                                    nextSemSubjects_elective_hours.add("*"+courseStructure[y][x]+"*");
                                }
                            }
                            if (end_of_elective == true && courseStructure[y + 1][0].isEmpty()) {
                                end_of_elective = false;
                            }

                        }
                    }
            }

            for (s=0; s < nextSemSubjects_name.size(); s++) {

                offeredSubjects[c][0]=nextSemSubjects_code.get(s);
                offeredSubjects[c][1]=nextSemSubjects_name.get(s);
                offeredSubjects[c][2]=nextSemSubjects_hours.get(s);

                if(finishedSubject(nextSemSubjects_code.get(s))) {
                    colorint = 1;
                }else
                if(isFound("*.",nextSemSubjects_hours.get(s))){
                    colorint = 2;
                }else{
                    colorint = 3;
                }
                colorarr[c]=colorint;
                c++;
                Print = new subjectWithTitle("", nextSemSubjects_code.get(s), nextSemSubjects_name.get(s), HoursFromStarString(nextSemSubjects_hours.get(s)));
                ToPrint.add(Print);
                sizeOfSubjects++;
            }

            nextSemSubjects_code.clear();
            nextSemSubjects_name.clear();
            nextSemSubjects_hours.clear();

            for (s=0; s < nextSemSubjects_elective_name.size();  s++) {


                offeredSubjects[c][0]=nextSemSubjects_elective_code.get(s);
                offeredSubjects[c][1]=nextSemSubjects_elective_name.get(s);
                offeredSubjects[c][2]=nextSemSubjects_elective_hours.get(s);

                if(finishedSubject(nextSemSubjects_elective_code.get(s))) {
                    colorint = 1;
                }else
                if(isFound("*.",nextSemSubjects_elective_hours.get(s))){
                    colorint = 2;
                }else{
                    colorint = 3;
                }
                colorarr[c]=colorint;
                c++;
                Print = new subjectWithTitle("", nextSemSubjects_elective_code.get(s), nextSemSubjects_elective_name.get(s), HoursFromStarString(nextSemSubjects_elective_hours.get(s)));
                ToPrint.add(Print);
                sizeOfSubjects++;
            }
            nextSemSubjects_elective_code.clear();
            nextSemSubjects_elective_name.clear();
            nextSemSubjects_elective_hours.clear();

            offeredSubjects[c][0]="";
            offeredSubjects[c][1]="";
            offeredSubjects[c][2]="";
            colorarr[c]=0;
            c++;
            Print = new subjectWithTitle("", "", "", "");
            ToPrint.add(Print);
            sizeOfSubjects++;

        }

        Print = new subjectWithTitle("", "", "", "");
        ToPrint.add(Print);
        sizeOfSubjects++;

        final String[][] offeredSubFinal = new String[sizeOfSubjects][3];

        for(s=0;s<sizeOfSubjects;s++){
            offeredSubFinal[s][0] = offeredSubjects[s][0];
            offeredSubFinal[s][1] = offeredSubjects[s][1];
            offeredSubFinal[s][2] = offeredSubjects[s][2];
        }

        final ListView chl=(ListView) findViewById(R.id.checkable_list);

        int size = sizeOfSubjects;


        semesterlistadabter aa  = new semesterlistadabter(this, R.layout.semesterlayout, ToPrint);

        final String[][] choosedSub = new String[size][3];
        for(i=0;i<size;i++) {
            for (s = 0; s < 3; s++) {
                choosedSub[i][s] = "";
            }
        }
        colorarr1=new int[size];
        for(int h=0;h<colorarr1.length;h++){
            colorarr1[h]=colorarr[h];
        }
        aa.setColor(colorarr1);
        chl.setAdapter(aa);
        final int finalSize = size;
        //set OnItemClickListener
        chl.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            int choosedSize=0;
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<finalSize) {
                    if (offeredSubFinal[position][1].compareTo(choosedSub[position][1]) == 0&&!offeredSubFinal[position][1].isEmpty()) {
                        choosedSub[position][0] = "";
                        choosedSub[position][1] = "";
                        choosedSub[position][2] = "";
                        choosedSize--;
                        num_of_hours_next_sem -= HoursFromStar(offeredSubFinal[position][2]);
                        displaygpa();
                        if(colorarr[position]==0) {
                            view.setBackgroundColor(Color.alpha(0));
                            colorarr1[position]=0;
                        }else if(colorarr[position]==1){
                            view.setBackgroundColor(Color.LTGRAY);
                            colorarr1[position]=1;
                        }else if(colorarr[position]==2){
                            view.setBackgroundColor(Color.alpha(0));
                            colorarr1[position]=2;
                        }else if(colorarr[position]==3){
                            view.setBackgroundColor(Color.RED);
                            colorarr1[position]=3;
                        }
                        saveArray(choosedSub,choosedSize);
                    } else {
                        if(isFound("MPU",offeredSubFinal[position][1])||isFound("MPU",offeredSubFinal[position][0])){
                            Toast.makeText(semester_planner.this, "You can't increase your CGPA with MPU subject, choose another subject.", Toast.LENGTH_LONG).show();
                        }else {

                            if (isFound("*.", offeredSubFinal[position][2]) && colorarr[position] != 1) {
                                choosedSize++;
                                choosedSub[position][0] = offeredSubFinal[position][0];
                                choosedSub[position][1] = offeredSubFinal[position][1];
                                choosedSub[position][2] = offeredSubFinal[position][2];
                                num_of_hours_next_sem += HoursFromStar(offeredSubFinal[position][2]);
                                displaygpa();
                                colorarr1[position] = 4;
                                view.setBackgroundColor(Color.GRAY);
                                saveArray(choosedSub, choosedSize);
                            } else if (colorarr[position] == 1) {
                                Toast.makeText(semester_planner.this, "Go to retake subject planner if you want to take this subject again", Toast.LENGTH_LONG).show();
                            } else if (isFound("*", offeredSubFinal[position][2])) {
                                choosedSize++;
                                Toast.makeText(semester_planner.this, "Prerequisites of this subject are not satisfied", Toast.LENGTH_SHORT).show();
                                choosedSub[position][0] = offeredSubFinal[position][0];
                                choosedSub[position][1] = offeredSubFinal[position][1];
                                choosedSub[position][2] = offeredSubFinal[position][2];
                                num_of_hours_next_sem += HoursFromStar(offeredSubFinal[position][2]);
                                displaygpa();
                                colorarr1[position] = 4;
                                view.setBackgroundColor(Color.GRAY);
                                saveArray(choosedSub, choosedSize);
                            } else {
                            }
                        }
                    }
                }
            }});
    }
    public void saveArray(String[][] choos,int size){
        choosed_Sub=new String[0][3];
        num_of_selected_sub=size;
        choosed_Sub=new String[size][3];
        int l=0;
        for(int i=0;i<choos.length;i++) {
            for (int s = 0; s < 3; s++) {
                if(!choos[i][s].isEmpty()) {
                    choosed_Sub[l][s]=choos[i][s];
                }
            }
            if(!choos[i][1].isEmpty()) {
                l++;
            }
        }
    }
    public void displayPlan(){
        int i;
        retake =false;
        hours=new double[50];
        ArrayList<MarksForDegree> ToPrint = new ArrayList<>();
        MarksForDegree Print;


        Print = new MarksForDegree("", "", "", "Choosen subjects", "", "", "", "", "", "", "", 2);
        ToPrint.add(Print);


        for(i = 0 ; i < choosed_Sub.length ;i++){
            hours[i]=(double) HoursFromStar(choosed_Sub[i][2]);
            Print = new MarksForDegree(choosed_Sub[i][0], choosed_Sub[i][1], HoursFromStarString(choosed_Sub[i][2]), "", "", "", "", "", "", "", "", 3);
            ToPrint.add(Print);
        }
        Print = new MarksForDegree("", "", "", "", "", "", "", "", "", "", "", 4);
        ToPrint.add(Print);

        Print = new MarksForDegree("", "", "", "Suggested plans", "", "", "", "", "", "", "", 2);
        ToPrint.add(Print);


        if(newGpa<goal-0.001){
            retake=true;
        }

        ToPrint.addAll(array(choosed_Sub.length, hours, expGPA2(num_of_hours_next_sem)));

        Print = new MarksForDegree("", "", "", "", "", "", "", "", "", "", "", 4);
        ToPrint.add(Print);
        Print = new MarksForDegree("", "", "", "", "", "", "", "", "", "", "", 4);
        ToPrint.add(Print);

        ListView listView = (ListView) findViewById(R.id.checkable_list);

        SemseterListAdapter arrayAdapter = new SemseterListAdapter(this,R.layout.degree_planner_subjects,ToPrint);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){public void onItemClick(AdapterView<?> parent, View view, int position, long id) {{}}});
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
    public int HoursFromStar(String str){
        String[] temp = str.split("\\*",-1);
        return Integer.parseInt(temp[1]);
    }
    public String HoursFromStarString(String str){
        String[] temp = str.split("\\*",-1);
        if(temp.length>1) {
            return temp[1];
        }else{
            return "";
        }
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
    public boolean CreditHoursIsSatisfied( String creditHours){
        if(numricalOf(gpa_and_totalHours.get(1))>=numricalOf(creditHours)){
            return true;
        }else {
            return false;
        }
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
    public String[][] ArrangeGrades(ArrayList<String> oldGrades){
        String[][] TempGrades=new String[oldGrades.size()][20];

        for(int s=0 ;s<oldGrades.size();s++){
            TempGrades[s] = oldGrades.get(s).split(",",-1);
        }


        return TempGrades;
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
    public boolean finishedSubject(String code){
        for(int y=0;y<result_code.size();y++) {
            if (isFound(clearFromSpacing(result_code.get(y)),code)) {
                return true;
            }
        }
        return false;
    }

}
