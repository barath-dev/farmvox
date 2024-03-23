package com.example.loginsqllite;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ChooseAdapter extends BaseAdapter {
    Cursor cursor;

    DBHelper db;

    ArrayList<String> vegetables;

    Context context;

    public ChooseAdapter(Context context, String username){
        this.context = context;
        db = new DBHelper(context);
        vegetables = db.getVegetables();
    }

    @Override
    public int getCount() {
        return vegetables.size();
    }

    @Override
    public Object getItem(int position) {
        return vegetables.get(position-1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
