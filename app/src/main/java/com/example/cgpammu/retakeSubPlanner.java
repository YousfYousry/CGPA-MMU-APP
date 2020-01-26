package com.example.cgpammu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class retakeSubPlanner extends AppCompatActivity{
    RadioGroup rg;
    String[][][] result = new String[20][200][3];
    ArrayList<String> LoadcourseStructure = new ArrayList<>();
    ArrayList<String> result_Trimester = new ArrayList<>();
    ArrayList<String> result_code = new ArrayList<>();
    ArrayList<String> result_subName = new ArrayList<>();
    ArrayList<String> result_grade = new ArrayList<>();
    ArrayList<String> student_info = new ArrayList<>();
    ArrayList<String> gpa_and_totalHours = new ArrayList<>();
    ArrayList<String> numOfSub = new ArrayList<>();
    String[][] courseStructure=new String[100][20];
    String[][] choosed_Sub;
    int choice = 1;
    retakeSubPlanner retakeSubPlanner =this;
    double[][][] hoursAndGpaLast=new double[3][200][3];
    InterstitialAd mInterstitialAd;

    int[] colour;
    boolean ascending=true,displayed=false;

    public void radioButton1(View view){
        choice =1;
        DisplayResultAscendingMode();
    }
    public void radioButton2(View view){
        choice =2;
        DisplayResultDescendingMode();
    }
    public void radioButton3(View view){
        choice =3;
        DisplayResultDfaultMode();
    }
    public void EnterPressed(View view) {
        if (choosed_Sub == null) {
            Toast.makeText(this, "You didn't choose any subjects to retake!", Toast.LENGTH_LONG).show();
        } else {
            if (choosed_Sub.length == 0) {
                Toast.makeText(this, "Choose subjects to retake.", Toast.LENGTH_LONG).show();
            } else {
                displayChoosenSub();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retake_sub_planner);

        MobileAds.initialize(this,"ca-app-pub-2523793828851593~2615527037");

        numOfSub = loadData("numOfSub");
        LoadcourseStructure = loadData("CourseStructure");
        result_Trimester = loadData("resultTrimester");
        result_code = loadData("resultCode");
        result_subName = loadData("resultSubName");
        result_grade = loadData("resultGrade");
        student_info = loadData("studentInfo");
        gpa_and_totalHours = loadData("studentGPAAndTotalHours");

        rg = (RadioGroup) findViewById(R.id.sortBy);

        courseStructure = getcourseStructure();

        arrayListToArray();

        DisplayResultAscendingMode();
    }

    public void showInterstitial(){
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(displayed){
            hoursAndGpaLast=new double[3][200][3];
            displayed=false;
            choosed_Sub=new String[0][0];
            Space space =(Space) findViewById(R.id.spaceWeight);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    0f
            );
            space.setLayoutParams(param);
            TextView CurrGPA =(TextView) this.findViewById(R.id.textView1400);
            CurrGPA.setText("current CGPA");
            TextView chooseGrad =(TextView) this.findViewById(R.id.textView26);
            chooseGrad.setText("Choose subjects to retake :");
            TextView NewCGPA =(TextView) this.findViewById(R.id.textView2000);
            NewCGPA.setText("New CGPA (Default new grade is A )");
            FrameLayout visible = (FrameLayout) this.findViewById(R.id.bottomm);
            visible.setVisibility(View.VISIBLE);
            RadioGroup HideRadio =(RadioGroup) this.findViewById(R.id.sortBy);
            HideRadio.setVisibility(View.VISIBLE);
            TextView hide1 =(TextView) this.findViewById(R.id.textView30);
            hide1.setText("");
            TextView hide2 =(TextView) this.findViewById(R.id.textView36);
            hide2.setText("");
            if(choice == 1){
                DisplayResultAscendingMode();
            }else if(choice == 2) {
                DisplayResultDescendingMode();
            }else if(choice == 3){
                DisplayResultDfaultMode();
            }
        }else {
            showInterstitial();
            Intent mainapp = new Intent(this, MainPage.class);
            startActivity(mainapp);
            CustomIntent.customType(this, "right-to-left");
            finish();
        }
    }

    public ArrayList<String> loadData(String name){
        SharedPreferences sharedPreferences = getSharedPreferences(name,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void changeGradDisplay(String[][] subInfo,int position){

        TextView Minimum = (TextView) findViewById(R.id.textView1400);
        TextView MinEdit = (TextView) findViewById(R.id.setCGPAA);

        TextView Average = (TextView) findViewById(R.id.textView2000);
        TextView AveEdit = (TextView) findViewById(R.id.setNewGpaa);

        TextView Maximum = (TextView) findViewById(R.id.textView30);
        TextView MaxEdit = (TextView) findViewById(R.id.textView36);

//        for(int y = 0; y<subInfo.length;y++){
//            for(int x = 0; x<2;x++){
//                System.out.println(subInfo[y][x]+"     "+position);
//            }
//        }


        Minimum.setText("Minimum CGPA");
        hoursAndGpaLast[0][position][0] = gradeFormin(subInfo[position][1],subInfo[position][0]);
        hoursAndGpaLast[0][position][1] = HoursFrom(subInfo[position][0]);
        hoursAndGpaLast[0][position][2] = gradeForMax(choosed_Sub[position][2],choosed_Sub[position][0]); //old
        MinEdit.setText(NewGpa2(hoursAndGpaLast[0]));
        MinEdit.setTextColor(Color.parseColor("#FF0000"));

        Average.setText("Average CGPA");
        hoursAndGpaLast[1][position][0] = grade(subInfo[position][1],subInfo[position][0]);
        hoursAndGpaLast[1][position][1] = HoursFrom(subInfo[position][0]);
        hoursAndGpaLast[1][position][2] = grade(choosed_Sub[position][2],choosed_Sub[position][0]);
        AveEdit.setText(NewGpa2(hoursAndGpaLast[1]));
        AveEdit.setTextColor(Color.parseColor("#FF0000"));

        Maximum.setText("Maximum CGPA");
        hoursAndGpaLast[2][position][0] = gradeForMax(subInfo[position][1],subInfo[position][0]);
        hoursAndGpaLast[2][position][1] = HoursFrom(subInfo[position][0]);
        hoursAndGpaLast[2][position][2] = gradeFormin(choosed_Sub[position][2],choosed_Sub[position][0]);
        MaxEdit.setText(NewGpa2(hoursAndGpaLast[2]));
        MaxEdit.setTextColor(Color.parseColor("#FF0000"));
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
    public void DisplayResultDfaultMode(){

        ArrayList<String> dataToPrint = new ArrayList<>();

        ArrayList<subjectWithTitle> ToPrint = new ArrayList<>();
        subjectWithTitle sub;

        int[] Colour=new int[500];
        int c=0;

        displaygpaForStarter();

        //storing data to print
        for(int z=0;z<result_Trimester.size();z++) {
            if(z!=0){
                sub = new subjectWithTitle("","","","");
                ToPrint.add(sub);
                Colour[c]=0;
                c++;
                dataToPrint.add("");
                dataToPrint.add("");
                dataToPrint.add("");
            }

            sub = new subjectWithTitle(result_Trimester.get(z),"","","");
            ToPrint.add(sub);
            Colour[c]=5;
            c++;
            dataToPrint.add("");
            dataToPrint.add(result_Trimester.get(z));
            dataToPrint.add("");

            for (int y = 0; y < result[z].length; y++) {
                if (result[z][y][0] != null && !result[z][y][0].isEmpty() && result[z][y][1] != null && !result[z][y][1].isEmpty() && result[z][y][2] != null && !result[z][y][2].isEmpty()) {
                    sub = new subjectWithTitle("",result[z][y][0],result[z][y][1],result[z][y][2]);
                    ToPrint.add(sub);
                    Colour[c]=0;
                    c++;
                    dataToPrint.add(result[z][y][0]);
                    dataToPrint.add(result[z][y][1]);
                    dataToPrint.add(result[z][y][2]);
                }
            }
        }

        int size =ToPrint.size();
        final String[][] defaulte = new String[size][3];
        final String[][] choosedSub = new String[size][3];
        for(int i=0;i<size;i++) {
            for (int s = 0; s < 3; s++) {
                choosedSub[i][s] = "";
            }
        }

        final double[][] hoursAndGpa = new double[size][2];
        for(int i=0;i<hoursAndGpa.length;i++) {
            for (int s = 0; s < hoursAndGpa[i].length; s++) {
                hoursAndGpa[i][s] = 0;
            }
        }

        int i=0;
        for(int s=0;s<size;s++) {
            defaulte[s][0]=dataToPrint.get(i);
            defaulte[s][1]=dataToPrint.get(i+1);
            defaulte[s][2]=dataToPrint.get(i+2);
            i+=3;
        }

        final ListView chl=(ListView) findViewById(R.id.retakeList);

        semesterlistadabter aa = new semesterlistadabter(this, R.layout.semesterlayout, ToPrint);

        colour = new int[size];
        for(i=0;i<size;i++){
            colour[i]=Colour[i];
        }
        aa.setColor(colour);
        chl.setAdapter(aa);

        chl.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(defaulte[position][1].compareTo(choosedSub[position][1])==0){
                    choosedSub[position][0] = "";
                    choosedSub[position][1] = "";
                    choosedSub[position][2] = "";
                    hoursAndGpa[position][0] = 0;
                    hoursAndGpa[position][1] = 0;
                    displaygpa(hoursAndGpa);
                    colour[position]=0;
                    view.setBackgroundColor(Color.alpha(0));
                    saveArray(choosedSub);
                }else{
                    if(!defaulte[position][2].isEmpty()&&!isFound("MPU",defaulte[position][0])&&!isFound("MPU",defaulte[position][1])) {
                        choosedSub[position][0] = defaulte[position][0];
                        choosedSub[position][1] = defaulte[position][1];
                        choosedSub[position][2] = defaulte[position][2];
                        hoursAndGpa[position][0] = grade(defaulte[position][2],defaulte[position][0]);
                        hoursAndGpa[position][1] = HoursFrom(defaulte[position][0]);
                        displaygpa(hoursAndGpa);
                        colour[position] = 4;
                        view.setBackgroundColor(Color.GRAY);
                        saveArray(choosedSub);
                    }else if(isFound("MPU",defaulte[position][0])||isFound("MPU",defaulte[position][1])){
                        Toast.makeText(retakeSubPlanner.this, "MPU subject won't affect your CGPA", Toast.LENGTH_LONG).show();
                    }
                }
            }});

    }
    public void DisplayResultAscendingMode(){
        ArrayList<subject> ToPrint = new ArrayList<>();
        subject sub;

        displaygpaForStarter();


        ascending=true;
        String[][] Ascending = new String[200][3];
        double[][] gpa = new double[200][2];
        int i;
        for(i = 0 ;i<200;i++){
            gpa[i][1]=i;
        }
        i=0;
        for(int z=0;z<result.length;z++) {
            for (int y = 0; y < result[z].length; y++) {
                if (result[z][y][0] != null && !result[z][y][0].isEmpty() && result[z][y][1] != null && !result[z][y][1].isEmpty() && result[z][y][2] != null && !result[z][y][2].isEmpty()) {
                    Ascending[i][0]=result[z][y][0];
                    Ascending[i][1]=result[z][y][1];
                    Ascending[i][2]=result[z][y][2];
                    gpa[i][0]=grade(result[z][y][2],result[z][y][0]);
                    i++;
                }
            }
        }
        int size =i;
        final String[][] Ascended = new String[size][3];//
        gpa=Ascending(gpa,size);

        final String[][] choosedSub = new String[size][3];//
        for(i=0;i<size;i++) {
            for (int s = 0; s < 3; s++) {
                choosedSub[i][s] = "";
            }
        }
        final double[][] hoursAndGpa = new double[size][2];
        for(i=0;i<hoursAndGpa.length;i++) {
            for (int s = 0; s < hoursAndGpa[i].length; s++) {
                hoursAndGpa[i][s] = 0;
            }
        }
        //

        for (i = 0; i < Ascending.length; i++) {
                if (Ascending[(int)gpa[i][1]][0] != null && !Ascending[(int)gpa[i][1]][0].isEmpty() && Ascending[(int)gpa[i][1]][1] != null && !Ascending[(int)gpa[i][1]][1].isEmpty() && Ascending[(int)gpa[i][1]][2] != null && !Ascending[(int)gpa[i][1]][2].isEmpty()) {
                    Ascended[i][0] = Ascending[(int)gpa[i][1]][0];
                    Ascended[i][1] = Ascending[(int)gpa[i][1]][1];
                    Ascended[i][2] = Ascending[(int)gpa[i][1]][2];
                }
            }
        for(i=0;i<size;i++){
            sub=new subject(Ascended[i][0],Ascended[i][1],Ascended[i][2],0);
            ToPrint.add(sub);
        }

        final ListView chl=(ListView) findViewById(R.id.retakeList);

        subjectListAdapterCkeck aa = new subjectListAdapterCkeck(this, R.layout.empty_layout, ToPrint);

        colour = new int[size];
        for(i=0;i<size;i++){
            colour[i]=0;
        }
        aa.setColor(colour);
        chl.setAdapter(aa);
        //
        chl.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(Ascended[position][1].compareTo(choosedSub[position][1])==0){
                    choosedSub[position][0] = "";
                    choosedSub[position][1] = "";
                    choosedSub[position][2] = "";
                    hoursAndGpa[position][0] = 0;
                    hoursAndGpa[position][1] = 0;
                    displaygpa(hoursAndGpa);
                    colour[position]=0;
                    view.setBackgroundColor(Color.alpha(0));
                    saveArray(choosedSub);
                }else{
                    if(!Ascended[position][2].isEmpty()&&!isFound("MPU",Ascended[position][0])&&!isFound("MPU",Ascended[position][1])) {
                        choosedSub[position][0] = Ascended[position][0];
                        choosedSub[position][1] = Ascended[position][1];
                        choosedSub[position][2] = Ascended[position][2];
                        hoursAndGpa[position][0] = grade(Ascended[position][2],Ascended[position][0]);
                        hoursAndGpa[position][1] = HoursFrom(Ascended[position][0]);
                        displaygpa(hoursAndGpa);
                        colour[position] = 1;
                        view.setBackgroundColor(Color.GRAY);
                        saveArray(choosedSub);
                    }else if(isFound("MPU",Ascended[position][0])||isFound("MPU",Ascended[position][1])){
                        Toast.makeText(retakeSubPlanner.this, "MPU subject won't affect your CGPA", Toast.LENGTH_LONG).show();
                    }
                }
            }});
        //
    }
    public void DisplayResultDescendingMode(){
        ArrayList<subject> ToPrint = new ArrayList<>();
        subject sub;

        displaygpaForStarter();

        ascending=false;
        String[][] Descending = new String[200][3];
        double[][] gpa = new double[200][2];
        int i;
        for(i = 0 ;i<200;i++){
            gpa[i][1]=i;
        }
        i=0;
        for(int z=0;z<result.length;z++) {
            for (int y = 0; y < result[z].length; y++) {
                if (result[z][y][0] != null && !result[z][y][0].isEmpty() && result[z][y][1] != null && !result[z][y][1].isEmpty() && result[z][y][2] != null && !result[z][y][2].isEmpty()) {
                    Descending[i][0]=result[z][y][0];
                    Descending[i][1]=result[z][y][1];
                    Descending[i][2]=result[z][y][2];
                    gpa[i][0]=grade(result[z][y][2],result[z][y][0]);
                    i++;
                }
            }
        }
        int size =i;
        final String[][] Descended = new String[size][3]; //
        gpa=Descending(gpa,size);

        final String[][] choosedSub = new String[size][3];//
        for(i=0;i<size;i++) {
            for (int s = 0; s < 3; s++) {
                choosedSub[i][s] = "";
            }
        }
        final double[][] hoursAndGpa = new double[size][2];
        for(i=0;i<hoursAndGpa.length;i++) {
            for (int s = 0; s < hoursAndGpa[i].length; s++) {
                hoursAndGpa[i][s] = 0;
            }
        }
        //

        for (i = 0; i < Descending.length; i++) {
            if (Descending[(int)gpa[i][1]][0] != null && !Descending[(int)gpa[i][1]][0].isEmpty() && Descending[(int)gpa[i][1]][1] != null && !Descending[(int)gpa[i][1]][1].isEmpty() && Descending[(int)gpa[i][1]][2] != null && !Descending[(int)gpa[i][1]][2].isEmpty()) {
                Descended[i][0] = Descending[(int)gpa[i][1]][0];
                Descended[i][1] = Descending[(int)gpa[i][1]][1];
                Descended[i][2] = Descending[(int)gpa[i][1]][2];
            }
        }
        for(i=0;i<size;i++){
            sub=new subject(Descended[i][0],Descended[i][1],Descended[i][2],0);
            ToPrint.add(sub);
        }

        final ListView chl=(ListView) findViewById(R.id.retakeList);


        subjectListAdapterCkeck aa = new subjectListAdapterCkeck(this, R.layout.empty_layout, ToPrint);

        colour = new int[size];
        for(i=0;i<size;i++){
            colour[i]=0;
        }
        aa.setColor(colour);
        chl.setAdapter(aa);

        //
        chl.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(Descended[position][1].compareTo(choosedSub[position][1])==0){
                    choosedSub[position][0] = "";
                    choosedSub[position][1] = "";
                    choosedSub[position][2] = "";
                    hoursAndGpa[position][0] = 0;
                    hoursAndGpa[position][1] = 0;
                    displaygpa(hoursAndGpa);
                    colour[position]=0;
                    view.setBackgroundColor(Color.alpha(0));
                    saveArray(choosedSub);
                }else{
                    if(!Descended[position][2].isEmpty()&&!isFound("MPU",Descended[position][0])&&!isFound("MPU",Descended[position][1])) {
                        choosedSub[position][0] = Descended[position][0];
                        choosedSub[position][1] = Descended[position][1];
                        choosedSub[position][2] = Descended[position][2];
                        hoursAndGpa[position][0] = grade(Descended[position][2],Descended[position][0]);
                        hoursAndGpa[position][1] = HoursFrom(Descended[position][0]);
                        displaygpa(hoursAndGpa);
                        colour[position] = 1;
                        view.setBackgroundColor(Color.GRAY);
                        saveArray(choosedSub);
                    }else if(isFound("MPU",Descended[position][0])||isFound("MPU",Descended[position][1])){
                        Toast.makeText(retakeSubPlanner.this, "MPU subject won't affect your CGPA", Toast.LENGTH_LONG).show();
                    }
                }
            }});
        //

    }
    public void displayChoosenSub(){
        ArrayList<subject> ToPrint = new ArrayList<>();
        displayed=true;

        TextView chooseGrad =(TextView) this.findViewById(R.id.textView26);
        chooseGrad.setText("Choose grades for each subject:");

        FrameLayout visible = (FrameLayout) this.findViewById(R.id.bottomm);
        visible.setVisibility(View.INVISIBLE);

        RadioGroup HideRadio =(RadioGroup) this.findViewById(R.id.sortBy);
        HideRadio.setVisibility(View.INVISIBLE);

        Space space =(Space) findViewById(R.id.spaceWeight);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        space.setLayoutParams(param);

        subject sub;
        int i,size=choosed_Sub.length;

        for(i=0;i<size;i++){
            sub=new subject(choosed_Sub[i][0],choosed_Sub[i][1],choosed_Sub[i][2],0);
            ToPrint.add(sub);
        }


        final ListView chl=(ListView) findViewById(R.id.retakeList);


        subjectListAdapterforRetake aa = new subjectListAdapterforRetake(this, R.layout.empty_layout2, ToPrint);
        aa.setNum_of_sub(size);

        colour = new int[size];
        for(i=0;i<size;i++){
            colour[i]=0;
        }
        aa.setColor(colour);
        aa.starter();
        aa.setRetakeSubPlanner(retakeSubPlanner);

        chl.setAdapter(aa);
        chl.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}});

    }
    public void displaygpa(double [][] hoursNgpa){
        TextView currCGPA = (TextView) this.findViewById(R.id.setCGPAA);
        currCGPA.setText(Double.toString(DoubleNumricalOf(gpa_and_totalHours.get(0))));
        currCGPA.setTextColor(Color.parseColor("#FF0000"));

        TextView setNewGpaa = (TextView) this.findViewById(R.id.setNewGpaa);
        setNewGpaa.setText(NewGpa(hoursNgpa));
        setNewGpaa.setTextColor(Color.parseColor("#FF0000"));
    }
    public void saveArray(String[][] choos){
        int l=0;
        choosed_Sub = new String[l][3];
        for(int i=0;i<choos.length;i++) {
            for (int s = 0; s < 3; s++) {
                if(!choos[i][s].isEmpty()) {
                    l++;
                }
            }
        }
        choosed_Sub = new String[l/3][3];
        l=0;
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
    public String NewGpa(double [][] hoursNgpa){
        double currCGPA =DoubleNumricalOf(gpa_and_totalHours.get(0));
        double totalHours =DoubleNumricalOf(gpa_and_totalHours.get(1));
        double currPoints = currCGPA*totalHours;
        for(int y = 0;y<hoursNgpa.length;y++){
            if(hoursNgpa[y][1]!=0){
                currPoints-=hoursNgpa[y][0]*hoursNgpa[y][1];
                currPoints+=4*hoursNgpa[y][1];
            }
        }
        if(currPoints/totalHours>4){
            return Double.toString(4);
        }
        return Double.toString(roundNum(currPoints/totalHours));
    }
    public String NewGpa2(double [][] hoursNgpa){
        double currCGPA =DoubleNumricalOf(gpa_and_totalHours.get(0));
        double totalHours =DoubleNumricalOf(gpa_and_totalHours.get(1));
        double currPoints = currCGPA*totalHours;

//        for(int y = 0; y<hoursNgpa.length;y++) {
//            if (hoursNgpa[y][1] != 0) {
//                System.out.println("num hours:  " + hoursNgpa[y][1] + "  old GPA:  " + hoursNgpa[y][2] + "  New Gpa:  " + hoursNgpa[y][0]);
//            }
//        }
        for(int y = 0;y<hoursNgpa.length;y++){
            if(hoursNgpa[y][1]!=0){
                currPoints-=hoursNgpa[y][1]*hoursNgpa[y][2];
            }
        }

        for(int y = 0;y<hoursNgpa.length;y++){
            if(hoursNgpa[y][1]!=0){
                currPoints+=hoursNgpa[y][0]*hoursNgpa[y][1];
            }
        }
        if(currPoints/totalHours>4){
            return Double.toString(4);
        }
        return Double.toString(roundNum(currPoints/totalHours));
    }
    public double roundNum(double num){
        return (Math.round(num*100.0)/100.0);
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
    public double grade(String grade,String code) {
        if(isFound("MPU",code)){
            if(ascending) {
                return 9999;
            }else{
                return -1;
            }
        }
        double marks=0, gpa;

        if(isFound("A+",grade)){
            return 4.000001;
        }else if(isFound("A",grade)){
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

            marks+=2.5;


        if(marks<=100&&marks>=80) {
            return 4;
        }else if(marks<80&&marks>=50) {
            gpa = (((marks - 50) / 30) * 2) + 2;
            return gpa;
        }else if(marks<=50&&marks>=0){
            return 0;
        }
        return  999999;

    }
    public double gradeFormin(String grade,String code) {
        if(isFound("MPU",code)){
            if(ascending) {
                return 9999;
            }else{
                return -1;
            }
        }
        double marks=0, gpa;

        if(isFound("A+",grade)){
            return 4.000001;
        }else if(isFound("A",grade)){
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

        if(marks<=100&&marks>=80) {
            return 4;
        }else if(marks<80&&marks>=50) {
            gpa = (((marks - 50) / 30) * 2) + 2;
            return gpa;
        }else if(marks<=50&&marks>=0){
            return 0;
        }
        return  999999;

    }
    public double gradeForMax(String grade,String code) {
        if(isFound("MPU",code)){
            if(ascending) {
                return 9999;
            }else{
                return -1;
            }
        }
        double marks=0, gpa;

        if(isFound("A+",grade)){
            return 4.000001;
        }else if(isFound("A",grade)){
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

        marks+=4.5;


        if(marks<=100&&marks>=80) {
            return 4;
        }else if(marks<80&&marks>=50) {
            gpa = (((marks - 50) / 30) * 2) + 2;
            return gpa;
        }else if(marks<=50&&marks>=0){
            return 0;
        }
        return  999999;

    }
    public boolean isFound(String p,String hph){
        boolean Found = hph.indexOf(p) !=-1? true: false;
        return Found;
    }
    public int HoursFrom(String subCode){
        if(!isFound("MPU",subCode)){
            for(int y=0;y<courseStructure.length;y++){
                if(isFound(subCode,courseStructure[y][0])){
                    for(int x=2;x<14;x++){
                        if(isnumrical(courseStructure[y][x])){
                            return Integer.parseInt(courseStructure[y][x]);
                        }
                    }
                }
            }
        }
        return 0;
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
    public double[][] Ascending (double[][] order,int n){
        double temp;
        for (int i = 0; i < n; i++)
        {
            for (int j = i + 1; j < n; j++)
            {
                if (order[i][0] > order[j][0])
                {
                    temp = order[i][0];
                    order[i][0] = order[j][0];
                    order[j][0] = temp;
                    temp = order[i][1];
                    order[i][1] = order[j][1];
                    order[j][1] = temp;
                }
            }
        }
        return order;
    }
    public double[][] Descending (double[][] order,int n){
        double temp;
        for (int i = 0; i < n; i++)
        {
            for (int j = i + 1; j < n; j++)
            {
                if (order[i][0] < order[j][0])
                {
                    temp = order[i][0];
                    order[i][0] = order[j][0];
                    order[j][0] = temp;
                    temp = order[i][1];
                    order[i][1] = order[j][1];
                    order[j][1] = temp;
                }
            }
        }
        return order;
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
    public void displaygpaForStarter(){
        TextView currCGPA = (TextView) this.findViewById(R.id.setCGPAA);
        currCGPA.setText(Double.toString(DoubleNumricalOf(gpa_and_totalHours.get(0))));
        currCGPA.setTextColor(Color.parseColor("#FF0000"));
        TextView setNewGpaa = (TextView) this.findViewById(R.id.setNewGpaa);
        setNewGpaa.setText(Double.toString(DoubleNumricalOf(gpa_and_totalHours.get(0))));
        setNewGpaa.setTextColor(Color.parseColor("#FF0000"));
    }

}