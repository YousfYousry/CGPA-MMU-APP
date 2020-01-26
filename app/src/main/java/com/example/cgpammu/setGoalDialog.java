package com.example.cgpammu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.parse.ParseUser;

public class setGoalDialog extends AppCompatDialogFragment {

    EditText Goal;
    EditText myText;
    TextView CGPAT,MAXT,Erormessage;
    Button button1,button2,button3,button4,enter;
    degreePlanner degree;
    semester_planner Semester;
    double MAXGPA,CGPA;
    int choice;


    public void setDegree(degreePlanner degree) {
        this.degree = degree;
        choice=1;
    }
    public void setSemester(semester_planner semester) {
        this.Semester = semester;
        choice=2;
    }

    public void setMAXGPA(double MAXGPA) {
        this.MAXGPA = MAXGPA;
    }

    public void setCGPA(double CGPA) {
        this.CGPA = CGPA;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.change_goal_layout, null);

        myText = (EditText) view.findViewById(R.id.goal2);
        CGPAT = view.findViewById(R.id.CGPASetGoal);
        CGPAT.setText(Double.toString(roundNum(CGPA)));
        MAXT  = view.findViewById(R.id.textView43);
        MAXT.setText(Double.toString(roundNum(MAXGPA)));
        button1 = (Button) view.findViewById(R.id.button6);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myText.setText( "3.67" );            }
        });
        button2 = (Button) view.findViewById(R.id.button7);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myText.setText( "3.33" );            }
        });
        button3 = (Button) view.findViewById(R.id.button8);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myText.setText( "2.67" );            }
        });
        button4 = (Button) view.findViewById(R.id.button9);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myText.setText( "2.00" );            }
        });
        enter = (Button) view.findViewById(R.id.button4);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(myText.getText())) {
                    double getGoal = Double.parseDouble(myText.getText().toString());

                    if (getGoal > 4) {
//                        Erormessage.setText("Wrong CGPA");
                        myText.setError("Wrong CGPA");
                    } else {
                        if (choice == 1) {
                            degree.setGoal(getGoal);
                            degree.dismiss();
                        }else if(choice==2){
                            Semester.setGoal(getGoal);
                            Semester.dismiss();
                        }
                    }
                }else{
//                    Erormessage.setText("CGPA is required!");
                    myText.setError( "CGPA is required!" );
                }
            }
        });
        builder.setView(view);

        return builder.create();
    }

    public interface ExampleDialogListener {
        void applyTexts(String username, String password);
    }
    public double roundNum(double num){
        return (Math.round(num*100.0)/100.0);
    }


}
