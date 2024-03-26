package com.example.loginsqllite;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Rating;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.compose.material3.TopAppBarDefaults;


public class OrderAdapter extends BaseAdapter {
    Cursor cursor;
    DBHelper db;
    String username;

    Context context;

    String isConsumer;

    private static LayoutInflater inflater = null;


    public OrderAdapter (Context context, Cursor cursor,String isConsumer){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new DBHelper(context);
        this.cursor = cursor;
        this.isConsumer = isConsumer;
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


        String status = cursor.getString(cursor.getColumnIndex("status"));
        String order_id = cursor.getString(cursor.getColumnIndex("oid"));
        username = cursor.getString(cursor.getColumnIndex("deliveryBoy"));

        ImageView cropImage = (ImageView) vi.findViewById(R.id.productImageView);
        DBHelper db = new DBHelper(context);

        String url =  db.getUrl(cursor.getString(cursor.getColumnIndex("product_name")));

        ImageLoaderTask imageLoaderTask = new ImageLoaderTask(cropImage);
        imageLoaderTask.execute(url);



      if (isConsumer!=null && isConsumer.equals("true")) {
          updateOrder.setVisibility(View.GONE);
          viewMap.setText("View Map");

          if (status.equals("ordered") || status.equals("picked Up")) {
              viewMap.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent intent = new Intent(context, Navigation.class);
                      String lat_d = cursor.getString(cursor.getColumnIndex("latitude_dest"));
                      String long_d = cursor.getString(cursor.getColumnIndex("longitude_dest"));
                      String lat_p = cursor.getString(cursor.getColumnIndex("latitude_pickup"));
                      String long_p =cursor.getString(cursor.getColumnIndex("longitude_pickup"));

                      intent.putExtra("dest_lat",lat_d);
                      intent.putExtra("dest_long",long_d);
                      intent.putExtra("pick_lat",lat_p);
                      intent.putExtra("pick_long",long_p);
                      context.startActivity(intent);
                  }
              });
          } else {
              viewMap.setVisibility(View.GONE);
              updateOrder.setVisibility(View.VISIBLE);
              updateOrder.setText("Rate the Order");
                updateOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Review.class);
                        intent.putExtra("farmer", cursor.getString(cursor.getColumnIndex("farmer_name")));
                        intent.putExtra("product", cursor.getString(cursor.getColumnIndex("product_name")));
                        context.startActivity(intent);
                    }
                });
          }
      }else{
          if (status.equals("ordered")) {
              updateOrder.setText("pick up");
          } else if (status.equals("picked Up")) {
              updateOrder.setText("deliver");
              viewMap.setText("Delivery Directions");
          } else {
              updateOrder.setVisibility(View.GONE);
          }

          updateOrder.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  if(status.equals("ordered")){
                      db.updateOrder(order_id, "picked Up");
                      Toast.makeText(context, "Order Picked Up", Toast.LENGTH_SHORT).show();
                  }else if(status.equals("picked Up")){
                      db.updateOrder(order_id, "Delivered");
                      Toast.makeText(context, "Order Delivered", Toast.LENGTH_SHORT).show();
                      db.freeDispatcher(order_id,username);
                  }

                  Intent intent = new Intent(v.getContext(), DeliveryBoy.class);
                  intent.putExtra("username", username);
                  context.startActivity(intent);
              }});

          viewMap.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(context, Navigation.class);
                  String lat_d = cursor.getString(cursor.getColumnIndex("latitude_dest"));
                  String long_d = cursor.getString(cursor.getColumnIndex("longitude_dest"));
                  String lat_p = cursor.getString(cursor.getColumnIndex("latitude_pickup"));
                  String long_p =cursor.getString(cursor.getColumnIndex("longitude_pickup"));

                  intent.putExtra("dest_lat",lat_d);
                  intent.putExtra("dest_long",long_d);
                  intent.putExtra("pick_lat",lat_p);
                  intent.putExtra("pick_long",long_p);
                  context.startActivity(intent);
              }
          });
      }

        crop_name.setText("Product Name: " +cursor.getString(cursor.getColumnIndex("product_name")));
        crop_price.setText("Product price: "+cursor.getString(cursor.getColumnIndex("product_price")));
        crop_quantity.setText("Product Quantity:"+cursor.getString(cursor.getColumnIndex("product_quantity")));

        return vi;
    }
}