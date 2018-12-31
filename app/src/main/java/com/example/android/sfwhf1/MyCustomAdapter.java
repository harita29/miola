package com.example.android.sfwhf1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * Created by archanayadawa on 11/4/17.
 */

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    int sum1;
    int sum21;
    int newSum1;
    TextView t3;
    final LinkedHashMap<String, Integer> hashmap=new LinkedHashMap<String, Integer>();


    public MyCustomAdapter(ArrayList<String> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        /**Button deletebutton = (Button) view.findViewById(R.id.delete_btn);
         Button  addbutton = (Button) view.findViewById(R.id.plus_btn);
         Button minusbutton = (Button) view.findViewById(R.id.minus_btn);
         //t2 = (TextView) view.findViewById(R.id.textView5);

         deletebutton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
        //do something
        list.remove(position);

        //or some other task
        notifyDataSetChanged();
        }
        });
         addbutton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
        list.add(""+ value);
        notifyDataSetChanged();
        }
        });
         minusbutton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
        list.remove(""+ value);
        notifyDataSetChanged();
        }
        });
         */


        return view;
    }
}
