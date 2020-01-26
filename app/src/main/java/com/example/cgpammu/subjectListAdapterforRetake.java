package com.example.cgpammu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class subjectListAdapterforRetake extends ArrayAdapter<subject> implements AdapterView.OnItemSelectedListener {


    retakeSubPlanner retakeSubPlanner = new retakeSubPlanner();
    int num_of_sub;
    int[] color ;
    int[] selection=new int[200];
    String[] codes=new String[200];
    String[][] subInfo=new String[200][2];

    public void starter(){
        for(int i=0;i<selection.length;i++){
            selection[i]=1;
        }
    }
    public void setColor(int[] color) {
        this.color = color;
    }
    public void setNum_of_sub(int num_of_sub) {
        this.num_of_sub = num_of_sub;
    }
    public void setRetakeSubPlanner(com.example.cgpammu.retakeSubPlanner retakeSubPlanner) {
        this.retakeSubPlanner = retakeSubPlanner;
    }

    private static final String TAG = "SubjectListAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    public static class ViewHolder {
        TextView code;
        TextView name;
        Spinner spinner;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public subjectListAdapterforRetake(Context context, int resource, ArrayList<subject> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //get the persons information

        String code = getItem(position).getCode();
        String name = getItem(position).getName();
        String hours = getItem(position).getHours();
        codes[position]=code;
        //Create the person object with the information
        subject person = new subject(code,name,hours,color[position]);

        //create the view result for showing the animation
        final View result;

        ViewHolder holder;
        //ViewHolder object

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.FirstText);
            holder.name = (TextView) convertView.findViewById(R.id.SecondText);
            holder.spinner = (Spinner) convertView.findViewById(R.id.spinner1);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.Grades, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter);

            result = convertView;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;

        }

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                System.out.println(i + "  "+ position);
                selection[position] = i;
                subInfo[position][0] = codes[position];
                subInfo[position][1]=adapterView.getItemAtPosition(i).toString();
                retakeSubPlanner.changeGradDisplay(subInfo,position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        holder.spinner.setSelection(selection[position]);

        Animation animation = AnimationUtils.loadAnimation(mContext,
        (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.code.setText(person.getCode());
        holder.name.setText(person.getName());
        if(color[position]==0){
            convertView.setBackgroundColor(Color.alpha(0));
        }else if(color[position]==1){
            convertView.setBackgroundColor(Color.BLUE);
        }else if(color[position]==3){
            convertView.setBackgroundColor(Color.RED);
        }

        return convertView;
    }


}
