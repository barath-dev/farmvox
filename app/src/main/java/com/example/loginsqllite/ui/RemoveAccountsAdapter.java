package com.example.loginsqllite.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginsqllite.DBHelper;
import com.example.loginsqllite.R;


public class RemoveAccountsAdapter extends BaseAdapter {

    Context context;


    Cursor cursor;

    private static LayoutInflater inflater = null;

    public RemoveAccountsAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DBHelper db = new DBHelper(context);
        cursor = db.getAllAccounts();
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("Range")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.support_simple_spinner_dropdown_item, null);
        cursor.moveToPosition(position);
        TextView accountName = (TextView) vi.findViewById(R.id.accountName);
        accountName.setText(cursor.getString(cursor.getColumnIndex("username")));
        Button remove = (Button) vi.findViewById(R.id.deleteAccount);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(context);
                db.deleteAccount(cursor.getString(cursor.getColumnIndex("username")));
                notifyDataSetChanged();
            }
        });
        return vi;
    }
}
