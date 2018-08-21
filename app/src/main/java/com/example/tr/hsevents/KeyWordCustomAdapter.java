package com.example.tr.hsevents;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by T.R on 02/12/2017.
 */

public class KeyWordCustomAdapter extends ArrayAdapter<KeyWords> implements View.OnClickListener
{
    private ArrayList<KeyWords> dataSet;
    Context mContext;

    public KeyWordCustomAdapter(ArrayList<KeyWords> data, Context context)
    {
    super(context, R.layout.keyword_item, data);
    this.dataSet = data;
    this.mContext=context;

}

    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        KeyWords keyWords=(KeyWords) object;

        switch (v.getId())
        {
            case R.id.keyword:
                Snackbar.make(v, "id: " +keyWords.getId(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        KeyWords keyWords = getItem(position);

// Inflating the layout for the custom Spinner
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.keyword_item, parent, false);

// Declaring and Typecasting the textview in the inflated layout
        TextView tvKeyword = (TextView) layout.findViewById(R.id.keyword);

// Setting the text using the array
        tvKeyword.setText(keyWords.getKeyword());

        tvKeyword.setOnClickListener(this);
        tvKeyword.setTag(position);


        return layout;
    }
    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}

