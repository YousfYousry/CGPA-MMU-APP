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


public class setGradesListAdapter extends ArrayAdapter<subjectManually> {

    private static final String TAG = "SubjectListAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    setGradesManually setGradesManually = new setGradesManually();
    int color = 0;
    int[] selection;
    String[] codes = new String[200];
    String[] names = new String[200];
//    String[][] subInfo = new String[200][4];


    public void setSelection(int[] selection) {
        this.selection = selection;
    }
//
//    public void starter() {
//        for (int i = 0; i < subInfo.length; i++) {
//            subInfo[i][0] = "";
//            subInfo[i][1] = "";
//            subInfo[i][2] = "";//marks
//            subInfo[i][3] = "";
//        }
//    }
    public void setManually(com.example.cgpammu.setGradesManually setGradesManually) {
        this.setGradesManually = setGradesManually;
    }


    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView title;
        TextView code;
        TextView name;
        Spinner spinner;
    }

    /**
     * Default constructor for the PersonListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     */
    public setGradesListAdapter(Context context, int resource, ArrayList<subjectManually> objects) {
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
        codes[position] = code;
        names[position] = name;
//        subInfo[position][3] = title;

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
            holder.title = (TextView) convertView.findViewById(R.id.titleManually);
            holder.code = (TextView) convertView.findViewById(R.id.codeManually);
            holder.name = (TextView) convertView.findViewById(R.id.nameManually);
            holder.spinner = (Spinner) convertView.findViewById(R.id.spinner1);

            if (isFound("MPU", code)) {
                holder.spinner.setVisibility(View.VISIBLE);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.GradesForManuallyMPU, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.spinner.setAdapter(adapter);
            } else if (!code.isEmpty() && !isFound("Trimester", code)) {
                holder.spinner.setVisibility(View.VISIBLE);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.GradesForManually, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.spinner.setAdapter(adapter);
            } else {
                holder.spinner.setVisibility(View.INVISIBLE);
            }


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

        if (isFound("MPU", code)||isFound("MPU", name)) {
            holder.spinner.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.GradesForManuallyMPU, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter);
        } else if (!name.isEmpty() && !isFound("Trimester", title)) {
            holder.spinner.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.GradesForManually, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter);
        } else {
            holder.spinner.setVisibility(View.INVISIBLE);
        }


        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                System.out.println(i + "  "+ position);
                selection[position] = i;
//                subInfo[position][0] = codes[position];
//                subInfo[position][1] = names[position];
//                subInfo[position][2] = adapterView.getItemAtPosition(i).toString();//Grade
//                setGradesManually.setSubInfo(subInfo);
                setGradesManually.setSelection(position,i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        holder.spinner.setSelection(selection[position]);

        holder.title.setText(person.getTitle());
        holder.code.setText(person.getCode());
        holder.name.setText(person.getName());
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
