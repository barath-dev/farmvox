package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyCropsAdapter extends BaseAdapter {

    Context context;

    DBHelper db = new DBHelper(context);

   String username;

    Cursor cursor;

    private static LayoutInflater inflater = null;

    public MyCropsAdapter(Context context,String username) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DBHelper db = new DBHelper(context);
        this.username = username;
        cursor = db.getCrops(username);
        Log.d("cursor count", "cursor count: "+cursor.getCount());
        Toast.makeText(context, "cursor count: "+cursor.getCount(), Toast.LENGTH_SHORT).show();
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

    @SuppressLint({"Range", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.my_crop, null);
            cursor.moveToPosition(position);
            TextView cropName = (TextView) vi.findViewById(R.id.cartProductName);
            TextView cropQuantity = (TextView) vi.findViewById(R.id.cartProductQuantity);
            TextView cropPrice = (TextView) vi.findViewById(R.id.cartProductPrice);
        Button deleteButton = (Button) vi.findViewById(R.id.removeButton);
        cropName.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("product_name"))));
        cropQuantity.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("product_quantity"))));
        cropPrice.setText(String.valueOf(cursor.getString(cursor.getColumnIndex("product_price"))));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(context);
                /*Toast.makeText(context, "delete clicked", Toast.LENGTH_SHORT).show();*/
                String res= db.deleteCrop(username);
                Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
            return vi;
    }
}
