package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class OrderAdapter extends BaseAdapter {
    Cursor cursor;
    String username;

    Context context;

    boolean isConsumer;

    String start, end;

    private static LayoutInflater inflater = null;


    public OrderAdapter (Context context, Cursor cursor, boolean isConsumer){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @SuppressLint({"Range", "SetTextI18n", "InflateParams"})
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
        String farmer = cursor.getString(cursor.getColumnIndex("farmer_name"));
        String consumer = cursor.getString(cursor.getColumnIndex("username"));

        ImageView cropImage = (ImageView) vi.findViewById(R.id.productImageView);
        DBHelper db = new DBHelper(context);



        if (status.equals("Delivered")) {
            updateOrder.setVisibility(View.GONE);
            viewMap.setVisibility(View.GONE);
        }

      if (isConsumer) {


          updateOrder.setVisibility(View.GONE);
          viewMap.setText("View Map");

          if (crop_name.getText().toString().toLowerCase().contains("Tomato".toLowerCase())){
              cropImage.setImageResource(R.drawable.tomato);
          }
          else if (crop_name.getText().toString().toLowerCase().contains("Cabbage".toLowerCase())){
              cropImage.setImageResource(R.drawable.cabbage);
          }
          else if (crop_name.getText().toString().toLowerCase().contains("Carrot".toLowerCase())){
              cropImage.setImageResource(R.drawable.carrot);
          }
          else if (crop_name.getText().toString().toLowerCase().contains("bitter".toLowerCase())){
              cropImage.setImageResource(R.drawable.bitter);
          }
          else if (crop_name.getText().toString().toLowerCase().contains("Green Chilli".toLowerCase())){
              cropImage.setImageResource(R.drawable.green_chilly);
          }

          else if (crop_name.getText().toString().toLowerCase().contains("carrot".toLowerCase())){
              cropImage.setImageResource(R.drawable.carrot);
          }
          else if (crop_name.getText().toString().toLowerCase().contains("beans".toLowerCase())){
              cropImage.setImageResource(R.drawable.beans);
          }
          else{
              String url =  db.getUrl(String.valueOf(crop_name));

              ImageLoaderTask imageLoaderTask = new ImageLoaderTask(cropImage);
              imageLoaderTask.execute(url);
          }


          if (status.equals("ordered") || status.equals("picked Up")) {
              viewMap.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent intent = new Intent(context, Navigation.class);
                      if (status.equals("ordered")) {
                          intent.putExtra("start", username);
                          intent.putExtra("end", farmer);
                      } else {
                          intent.putExtra("except",username);
                          intent.putExtra("start", farmer);
                          intent.putExtra("end", consumer);
                      }
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

          TextView mobile = vi.findViewById(R.id.farmerMobile);
          mobile.setVisibility(View.VISIBLE);

          String mobileNumber = db.getMobile(farmer);
          mobile.setText("Mobile: "+mobileNumber);



          if (status.equals("ordered")) {
              start = username;
              end = farmer;
              updateOrder.setText("pick up");
          } else if (status.equals("picked Up")) {
                start = farmer;
                end = consumer;
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
                  intent.putExtra("except",username);
                  intent.putExtra("start",start);
                  intent.putExtra("end",end);
                  context.startActivity(intent);
              }
          });
      }

        crop_name.setText("Product Name: " +cursor.getString(cursor.getColumnIndex("product_name")));
        crop_price.setText("Product price: "+cursor.getString(cursor.getColumnIndex("product_price")));
        crop_quantity.setText("Product Quantity:"+cursor.getString(cursor.getColumnIndex("product_quantity")));



        if (crop_name.getText().toString().toLowerCase().contains("Tomato".toLowerCase())){
            cropImage.setImageResource(R.drawable.tomato);
        }
        else if (crop_name.getText().toString().toLowerCase().contains("Cabbage".toLowerCase())){
            cropImage.setImageResource(R.drawable.cabbage);
        }
        else if (crop_name.getText().toString().toLowerCase().contains("Carrot".toLowerCase())){
            cropImage.setImageResource(R.drawable.carrot);
        }
        else if (crop_name.getText().toString().toLowerCase().contains("bitter".toLowerCase())){
            cropImage.setImageResource(R.drawable.bitter);
        }
        else if (crop_name.getText().toString().toLowerCase().contains("Green Chilli".toLowerCase())){
            cropImage.setImageResource(R.drawable.green_chilly);
        }

        else if (crop_name.getText().toString().toLowerCase().contains("carrot".toLowerCase())){
            cropImage.setImageResource(R.drawable.carrot);
        }
        else if (crop_name.getText().toString().toLowerCase().contains("beans".toLowerCase())){
            cropImage.setImageResource(R.drawable.beans);
        }
        else{
            String url =  db.getUrl(String.valueOf(crop_name));

            ImageLoaderTask imageLoaderTask = new ImageLoaderTask(cropImage);
            imageLoaderTask.execute(url);
        }

        return vi;
    }
}