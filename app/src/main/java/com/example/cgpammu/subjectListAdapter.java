package com.example.cgpammu;

import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;


public class subjectListAdapter extends ArrayAdapter<subject> {

    private static final String TAG = "SubjectListAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView code;
        TextView name;
        TextView hours;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public subjectListAdapter(Context context, int resource, ArrayList<subject> objects) {
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
        int color = getItem(position).getColor();

        //Create the person object with the information
        subject person = new subject(code,name,hours,color);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.FirstText);
            holder.name = (TextView) convertView.findViewById(R.id.SecondText);
            holder.hours = (TextView) convertView.findViewById(R.id.ThirdText);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.code.setText(person.getCode());
        holder.name.setText(person.getName());
        holder.hours.setText(person.getHours());

        Color co =new Color();
        if(color==0) {
            convertView.setBackgroundColor(Color.WHITE);
        }else
        if(color==1) {
            convertView.setBackgroundColor(Color.GREEN);
        }else
        if(color==2) {
            convertView.setBackgroundColor(-1000);
        }else
        if(color==3) {
            convertView.setBackgroundColor(Color.RED);
        }
        else if(color == 4){
            convertView.setBackgroundColor(Color.WHITE);
        }else if(color == 5){
            convertView.setBackgroundColor(Color.BLUE);
        }else if(color==6){
            convertView.setBackgroundColor(Color.GRAY);
        }else if(color==7){
            convertView.setBackgroundColor(Color.LTGRAY);
        }else
        if(color==8) {
            convertView.setBackgroundColor(Color.GRAY);
        }else
        if(color==9) {
            convertView.setBackgroundColor(Color.BLACK);
        }

        return convertView;
    }
}
