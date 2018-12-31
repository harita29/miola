package com.example.android.sfwhf1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by archanayadawa on 11/16/17.
 */

public class HashMapAdapter extends BaseAdapter {
    private final ArrayList mData;
    //final HashMapAdapter adapter;


    public HashMapAdapter(Map<String, Integer> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, Integer> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;


        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_inputs, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, Integer> item = getItem(position);

        // TODO replace findViewById by ViewHolder
       // ((ListView) result.findViewById(android.R.id.list)).setAdapter(adapter);
       // ((TextView) result.findViewById(android.R.id.text1)).setText(item.getKey());
        //((TextView) result.findViewById(android.R.id.text2)).setText(item.getValue());

        return result;
    }
}