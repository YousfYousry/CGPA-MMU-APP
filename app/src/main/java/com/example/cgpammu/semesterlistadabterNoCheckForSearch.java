package com.example.cgpammu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class semesterlistadabterNoCheckForSearch extends ArrayAdapter<subjectWithTitleNoCheck> {

    private static final String TAG = "SubjectListAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private List<subjectWithTitleNoCheck> items, tempItems, suggestions;


    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView Title;
        TextView code;
        TextView name;
        TextView hours;
    }

    /**
     * Default constructor for the PersonListAdapter
//     *
//     * @param context
//     * @param resource
//     * @param objects
//     */
    public semesterlistadabterNoCheckForSearch(Context context, int resource, ArrayList<subjectWithTitleNoCheck> items) {
        super(context, resource, items);
        this.items = items;
        mContext = context;
        mResource = resource;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String title = getItem(position).getTitle();
        String code = getItem(position).getCode();
        String name = getItem(position).getName();
        String hours = getItem(position).getHours();
        int color = getItem(position).getColour();

        //Create the person object with the information
        subjectWithTitleNoCheck person = new subjectWithTitleNoCheck(title, code, name, hours, color);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.Title = (TextView) convertView.findViewById(R.id.Title);
            holder.code = (TextView) convertView.findViewById(R.id.FirstText);
            holder.name = (TextView) convertView.findViewById(R.id.SecondText);
            holder.hours = (TextView) convertView.findViewById(R.id.ThirdText);

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

        holder.Title.setText(person.getTitle());
        holder.code.setText(person.getCode());
        holder.name.setText(person.getName());
        holder.hours.setText(person.getHours());

        if (color == 0) {
            convertView.setBackgroundColor(Color.alpha(0));
        } else if (color == 1) {
            convertView.setBackgroundColor(Color.rgb(157, 209, 156));
        } else if (color == 3) {
            convertView.setBackgroundColor(Color.rgb(213, 103, 103));
        } else if (color == 2) {
            convertView.setBackgroundColor(Color.LTGRAY);
        } else if (color == 4) {
            convertView.setBackgroundColor(Color.BLUE);
        } else if (color == 5) {
            convertView.setBackgroundColor(Color.rgb(156, 166, 229));
        }

        return convertView;
    }


    @Nullable
    @Override
    public subjectWithTitleNoCheck getItem(int position) {
        return items.get(position);
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return fruitFilter;
    }


    private Filter fruitFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            subjectWithTitleNoCheck fruit = (subjectWithTitleNoCheck) resultValue;
            return fruit.getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (subjectWithTitleNoCheck fruit : tempItems) {
                    if (isFound(charSequence.toString().toLowerCase(),fruit.getName().toLowerCase())||isFound(charSequence.toString().toLowerCase(),fruit.getCode().toLowerCase())) {
                        suggestions.add(fruit);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<subjectWithTitleNoCheck> tempValues = (ArrayList<subjectWithTitleNoCheck>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (subjectWithTitleNoCheck fruitObj : tempValues) {
                    add(fruitObj);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
    public boolean isFound(String p,String hph){
        boolean Found = hph.indexOf(p) !=-1? true: false;
        return Found;
    }

}
