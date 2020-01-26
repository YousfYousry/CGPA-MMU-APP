package com.example.cgpammu;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class spinnerAdapter extends ArrayAdapter<spinnercourseStructure> {

    LayoutInflater flater;

    public spinnerAdapter(Activity context, int resouceId, int textviewId, List<spinnercourseStructure> list) {
        super(context, resouceId, textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        spinnercourseStructure rowItem = getItem(position);

        View rowview = flater.inflate(R.layout.coursestructure, null, true);

        TextView txtTitle = (TextView) rowview.findViewById(R.id.title);
        txtTitle.setText(rowItem.getTitle());

        ImageView imageView = (ImageView) rowview.findViewById(R.id.icon);
        imageView.setImageResource(rowItem.getImageId());

        return rowview;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = flater.inflate(R.layout.coursestructure, parent, false);
        }
        spinnercourseStructure rowItem = getItem(position);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setText(rowItem.getTitle());
        ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
        imageView.setImageResource(rowItem.getImageId());
        return convertView;
    }
}

