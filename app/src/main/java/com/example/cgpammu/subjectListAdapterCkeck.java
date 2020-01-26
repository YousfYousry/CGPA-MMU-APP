package com.example.cgpammu;

import android.graphics.Color;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class subjectListAdapterCkeck extends ArrayAdapter<subject> {



    int[] color ;

    public void setColor(int[] color) {
        this.color = color;
    }


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
    public subjectListAdapterCkeck(Context context, int resource, ArrayList<subject> objects) {
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

        //Create the person object with the information
        subject person = new subject(code,name,hours,color[position]);

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

        if(color[position]==0){
            convertView.setBackgroundColor(Color.alpha(0));
        }else if(color[position]==1){
            convertView.setBackgroundColor(Color.GRAY);
        }else if(color[position]==3){
            convertView.setBackgroundColor(Color.RED);
        }else if(color[position]==4){
            convertView.setBackgroundColor(Color.LTGRAY);
        }


        return convertView;
    }
}
