package com.example.android.sfwhf1;

/**
 * Created by archanayadawa on 10/27/17.
 */

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class MyAdapter extends BaseAdapter {

    LinkedHashMap<String, Integer> originalData = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> mBackupData = new LinkedHashMap<>();
     LinkedHashMap<String, Integer> filteredData;
    private String[] mKeys;
    ArrayList<HashMap<String, Integer>> data1;

    public MyAdapter(LinkedHashMap<String, Integer> data){
       // mBackupData.addAll(data);
       originalData.putAll(data);
        filteredData = data;

    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position)
    {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        String key = mKeys[pos];
        String Value = getItem(pos).toString();
       // ((TextView) convertView.findViewById(R.id.textView4)).setText(key + Value);
        return convertView;
    }

}




