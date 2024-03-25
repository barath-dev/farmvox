package com.example.loginsqllite;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


public class OrderAdapter extends BaseAdapter {
    Cursor cursor;
    DBHelper db;
    String username;

    Context context;

    private static LayoutInflater inflater = null;


    public OrderAdapter (Context context, Cursor cursor){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new DBHelper(context);
        this.cursor = cursor;
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

    @SuppressLint({"Range", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.product_item, null);
        cursor.moveToPosition(position);
        TextView crop_name = vi.findViewById(R.id.productNameTextView);
        TextView crop_price = vi.findViewById(R.id.productPriceTextView);
        TextView crop_quantity = vi.findViewById(R.id.productQuantityTextView);
        Button updateOrder = vi.findViewById(R.id.changeStatus);
        Button viewMap = vi.findViewById(R.id.directions);

        updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = cursor.getString(cursor.getColumnIndex("status"));
                String order_id = cursor.getString(cursor.getColumnIndex("order_id"));
                if(status.equals("pending")){
                    db.updateOrder(order_id, "accepted");
                    Intent intent = new Intent(v.getContext(), Order.class);
                    intent.putExtra("username", username);
                    intent.putExtra("status", "pending");
                    context.startActivity(intent);
                }else if(status.equals("accepted")){
                    db.updateOrder(order_id, "completed");
                    Intent intent = new Intent(v.getContext(), Order.class);
                    intent.putExtra("username", username);
                    intent.putExtra("status", "accepted");
                    context.startActivity(intent);
                }
            }});

        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Navigation.class);
                /*String lat_d = cursor.getString(cursor.getColumnIndex("latitude_dest"));
                String long_d = cursor.getString(cursor.getColumnIndex("longitude_dest"));
                String lat_p = cursor.getString(cursor.getColumnIndex("latitude_pickup"));
                String long_p =cursor.getString(cursor.getColumnIndex("longitude_pickup"));

                intent.putExtra("dest_lat",lat_d);
                intent.putExtra("dest_long",long_d);
                intent.putExtra("pick_lat",lat_p);
                intent.putExtra("pick_long",long_p);
*/
                context.startActivity(intent);
            }
        });

        crop_name.setText("Product Name: " +cursor.getString(cursor.getColumnIndex("product_name")));
        crop_price.setText("Product price: "+cursor.getString(cursor.getColumnIndex("product_price")));
        crop_quantity.setText("Product Quantity:"+cursor.getString(cursor.getColumnIndex("product_quantity")));

        return vi;
    }
}
