package com.example.cgpammu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class DegreeListAdapter extends ArrayAdapter<MarksForDegree> {

    private Context mContext;
    private int mResource;
    int[] colors = new int[500];

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView code;
        TextView name;
        TextView hours;
        TextView TitleHolder;
        TextView GPA;
        TextView[] GradesHolder=new TextView[6];
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public DegreeListAdapter(Context context, int resource, ArrayList<MarksForDegree> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String code = getItem(position).getCode();
        String name = getItem(position).getName();
        String hours = getItem(position).getHours();

        String Title = getItem(position).getTitle();
        String GPA = getItem(position).getGPA();

        String[] grade=new String[6];
        grade[0] = getItem(position).getFirst();
        grade[1] = getItem(position).getSecond();
        grade[2] = getItem(position).getThird();
        grade[3] = getItem(position).getForth();
        grade[4] = getItem(position).getFifth();
        grade[5] = getItem(position).getSixth();
        colors[position]= getItem(position).getColor();
        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.subCode);
            holder.name = (TextView) convertView.findViewById(R.id.subName);
            holder.hours = (TextView) convertView.findViewById(R.id.subHours);
            holder.TitleHolder = (TextView) convertView.findViewById(R.id.ForTitle);
            holder.GPA = (TextView) convertView.findViewById(R.id.GPAD);
            holder.GradesHolder[0] = (TextView) convertView.findViewById(R.id.Gradestext1);
            holder.GradesHolder[1] = (TextView) convertView.findViewById(R.id.Gradestext2);
            holder.GradesHolder[2] = (TextView) convertView.findViewById(R.id.Gradestext3);
            holder.GradesHolder[3] = (TextView) convertView.findViewById(R.id.Gradestext4);
            holder.GradesHolder[4] = (TextView) convertView.findViewById(R.id.Gradestext5);
            holder.GradesHolder[5] = (TextView) convertView.findViewById(R.id.Gradestext6);


            convertView.setTag(holder);
        }
        else{ holder = (ViewHolder) convertView.getTag(); }

        if(colors[position]==0) {
            for (int i = 0; i < 6; i++) {
                if (isFound("A-", grade[i])) {
                    holder.GradesHolder[i].setTextColor(Color.rgb(0, 200, 0));
                } else if (isFound("A", grade[i])) {
                    holder.GradesHolder[i].setTextColor(Color.rgb(0, 100, 0));
                } else if (isFound("B+", grade[i])) {
                    holder.GradesHolder[i].setTextColor(Color.rgb(100, 100, 0));
                } else if (isFound("B-", grade[i])) {
                    holder.GradesHolder[i].setTextColor(Color.rgb(200, 200, 0));
                } else if (isFound("B", grade[i])) {
                    holder.GradesHolder[i].setTextColor(Color.rgb(150, 150, 0));
                } else if (isFound("C+", grade[i])) {
                    holder.GradesHolder[i].setTextColor(Color.rgb(255, 0, 0));
                } else if (isFound("C", grade[i])) {
                    holder.GradesHolder[i].setTextColor(Color.rgb(155, 0, 0));
                } else {
                    holder.GradesHolder[i].setTextColor(Color.rgb(0, 0, 0));
                }
            }
        }
        if(colors[position]==0){
            convertView.setBackgroundColor(Color.parseColor("#B3E6D7B0"));

            holder.code.setTextSize(16);
            holder.name.setTextSize(16);
            holder.hours.setTextSize(16);
            holder.TitleHolder.setTextSize(16);
            holder.GPA.setTextSize(16);
            holder.GradesHolder[0].setTextSize(16);
            holder.GradesHolder[1].setTextSize(16);
            holder.GradesHolder[2].setTextSize(16);
            holder.GradesHolder[3].setTextSize(16);
            holder.GradesHolder[4].setTextSize(16);
            holder.GradesHolder[5].setTextSize(16);
        } else if (colors[position]==1) {
            convertView.setBackgroundResource(R.drawable.titler);
            holder.code.setTextSize(20);
            holder.name.setTextSize(20);
            holder.hours.setTextSize(20);
            holder.TitleHolder.setTextSize(20);
            holder.GPA.setTextSize(20);
            holder.GPA.setTextColor(Color.RED);
            holder.GradesHolder[0].setTextSize(20);
            holder.GradesHolder[1].setTextSize(20);
            holder.GradesHolder[2].setTextSize(20);
            holder.GradesHolder[3].setTextSize(20);
            holder.GradesHolder[4].setTextSize(20);
            holder.GradesHolder[5].setTextSize(20);
        }else if (colors[position]==2) {
            convertView.setBackgroundResource(R.drawable.title2);
            holder.code.setTextSize(20);
            holder.name.setTextSize(20);
            holder.hours.setTextSize(20);
            holder.TitleHolder.setTextSize(20);
            holder.GPA.setTextSize(20);
            holder.GPA.setTextColor(Color.RED);
            holder.GradesHolder[0].setTextSize(20);
            holder.GradesHolder[1].setTextSize(20);
            holder.GradesHolder[2].setTextSize(20);
            holder.GradesHolder[3].setTextSize(20);
            holder.GradesHolder[4].setTextSize(20);
            holder.GradesHolder[5].setTextSize(20);
        }else if (colors[position]==3) {
            convertView.setBackgroundColor(Color.alpha(0));
            holder.code.setTextSize(14);
            holder.name.setTextSize(14);
            holder.hours.setTextSize(14);
            holder.TitleHolder.setTextSize(14);
            holder.GPA.setTextSize(14);
            holder.GradesHolder[0].setTextSize(14);
            holder.GradesHolder[1].setTextSize(14);
            holder.GradesHolder[2].setTextSize(14);
            holder.GradesHolder[3].setTextSize(14);
            holder.GradesHolder[4].setTextSize(14);
            holder.GradesHolder[5].setTextSize(14);
        }else if (colors[position]==4) {
            convertView.setBackgroundColor(Color.alpha(0));
            holder.code.setTextSize(14);
            holder.name.setTextSize(14);
            holder.hours.setTextSize(14);
            holder.TitleHolder.setTextSize(14);
            holder.GPA.setTextSize(14);
            holder.GradesHolder[0].setTextSize(14);
            holder.GradesHolder[1].setTextSize(14);
            holder.GradesHolder[2].setTextSize(14);
            holder.GradesHolder[3].setTextSize(14);
            holder.GradesHolder[4].setTextSize(14);
            holder.GradesHolder[5].setTextSize(14);
        }

        holder.code.setText(code);
        holder.name.setText(name);
        holder.hours.setText(hours);
        holder.TitleHolder.setText(Title);
        holder.GPA.setText(GPA);
        holder.GradesHolder[0].setText(grade[0]);
        holder.GradesHolder[1].setText(grade[1]);
        holder.GradesHolder[2].setText(grade[2]);
        holder.GradesHolder[3].setText(grade[3]);
        holder.GradesHolder[4].setText(grade[4]);
        holder.GradesHolder[5].setText(grade[5]);



        return convertView;
    }
    public boolean isFound(String p,String hph){
        boolean Found = hph.indexOf(p) !=-1? true: false;
        return Found;
    }

}
