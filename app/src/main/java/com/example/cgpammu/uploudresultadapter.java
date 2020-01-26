package com.example.cgpammu;

import android.content.Context;
import android.graphics.Color;
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


public class uploudresultadapter extends ArrayAdapter<subjectManually> {

    private static final String TAG = "SubjectListAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;


    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView title;
        TextView code;
        TextView name;
        TextView hours;
    }

    /**
     * Default constructor for the PersonListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     */
    public uploudresultadapter(Context context, int resource, ArrayList<subjectManually> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //get the persons information
        String title = getItem(position).getTitle();
        String code = getItem(position).getCode();
        String name = getItem(position).getName();
        String hours = getItem(position).getHours();
        int color = getItem(position).getColor();
        //Create the person object with the information
        subjectManually person = new subjectManually(title,code, name, hours, color);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.titleUpload);
            holder.code = (TextView) convertView.findViewById(R.id.codeUpload);
            holder.name = (TextView) convertView.findViewById(R.id.nameUpload);
            holder.hours = (TextView) convertView.findViewById(R.id.hoursUpload);


            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;


        holder.title.setText(person.getTitle());
        holder.code.setText(person.getCode());
        holder.name.setText(person.getName());
        holder.hours.setText(person.getHours());


        if (color== 0) {
            convertView.setBackgroundResource(R.drawable.titler2man);
        } else if (color == 1) {
            convertView.setBackgroundResource(R.drawable.titlerman);
        } else if (color == 2) {
            convertView.setBackgroundColor(Color.alpha(0));
        }


        return convertView;
    }

    public boolean isFound(String p, String hph) {
        boolean Found = hph.indexOf(p) != -1 ? true : false;
        return Found;
    }

}
