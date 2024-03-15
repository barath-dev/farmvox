package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CheckOutAdapter extends BaseAdapter {

    Cursor cursor;
    DBHelper db;

    private static LayoutInflater inflater = null;



    public CheckOutAdapter(CheckOut checkOut,String username) {
        inflater = (LayoutInflater) checkOut.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        db = new DBHelper(checkOut);
        cursor = db.getTEM(username);
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
            vi = inflater.inflate(R.layout.cart_item, null);
        cursor.moveToPosition(position);
        TextView cropName = (TextView) vi.findViewById(R.id.cartProductName);
        TextView cropQuantity = (TextView) vi.findViewById(R.id.cartProductQuantity);
        TextView cropPrice = (TextView) vi.findViewById(R.id.cartProductPrice);
        ImageView cropImage = (ImageView) vi.findViewById(R.id.cartProductImage);
        cropImage.setVisibility(View.GONE);
        cropName.setText(cursor.getString(cursor.getColumnIndex("crop_name")));
        cropQuantity.setText(cursor.getString(cursor.getColumnIndex("crop_quantity")));
        cropPrice.setText(cursor.getString(cursor.getColumnIndex("crop_price")));
        return vi;
    }
}
