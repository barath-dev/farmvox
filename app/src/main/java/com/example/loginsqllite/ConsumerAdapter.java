package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import java.util.Objects;

public class ConsumerAdapter extends BaseAdapter {

    Context context;

    Double latitude, longitude;

    DBHelper db;

    String username;
    private static LayoutInflater inflater = null;

    String crop_name, crop_quantity, crop_price, crop_unit, farmer_name;

    Cursor cursor;

    LatLng user_loc;

    public ConsumerAdapter(Context context, String username, boolean isFarmer, String search,String sortOption) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
         db = new DBHelper(context);
        this.username = username;
        if (sortOption!=null){
            Toast.makeText(context, sortOption, Toast.LENGTH_SHORT).show();
            cursor = db.sortProducts(sortOption);
        }else {
            cursor = !isFarmer ? db.getAllProducts() : db.getAllProductsForFarmer(username);
        }
        if(sortOption==null){
            if (search==null){
                cursor =  !isFarmer?db.getAllProducts():db.getAllProductsForFarmer(username);
            }else{
                cursor = db.searchProduct(search);
            }
        }

        user_loc = db.getUserLocation(username);
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

    @SuppressLint({"Range", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.my_crop, null);
        cursor.moveToPosition(position);
        TextView cropName = (TextView) vi.findViewById(R.id.cartProductName);
        TextView cropQuantity = (TextView) vi.findViewById(R.id.cartProductQuantity);
        TextView cropPrice = (TextView) vi.findViewById(R.id.cartProductPrice);
        TextView farmerName = (TextView) vi.findViewById(R.id.cartFarmerName);
        Button deleteButton = (Button) vi.findViewById(R.id.removeButton);
        Button buyNow = (Button) vi.findViewById(R.id.buyNowButton);
        TextView review = (TextView) vi.findViewById(R.id.ratingTextView);
         crop_name = cursor.getString(cursor.getColumnIndex("product_name"));
         crop_quantity = cursor.getString(cursor.getColumnIndex("product_quantity"));
         crop_price = cursor.getString(cursor.getColumnIndex("product_price"));
         crop_unit = cursor.getString(cursor.getColumnIndex("product_unit"));
         latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
         longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));



         String rating = cursor.getString(cursor.getColumnIndex("rating"));
         String ratingCount = cursor.getString(cursor.getColumnIndex("rating_count"));

        cropName.setText(String.format("Product Name: %s", crop_name));
        farmer_name = cursor.getString(cursor.getColumnIndex("username"));
        farmerName.setText(String.format("Farmer Name: %s", farmer_name));

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearTEM();
                try{
                    cursor.moveToPosition(position);
                    boolean res =  db.createCheckOut(username,user_loc.latitude,user_loc.longitude,cursor.getDouble(cursor.getColumnIndex("latitude")),cursor.getDouble(cursor.getColumnIndex("longitude")),Double.parseDouble(cursor.getString(cursor.getColumnIndex("product_price"))), cursor.getString(cursor.getColumnIndex("username")), Integer.parseInt(cursor.getString(cursor.getColumnIndex("product_quantity"))),cursor.getString(cursor.getColumnIndex("product_name")),cursor.getString(cursor.getColumnIndex("product_unit")));
                    if(res){
                        Intent intent = new Intent(context,GetQuantity.class);
                        intent.putExtra("username",username);
                        context.startActivity(intent);
                        notifyDataSetChanged();
                    }else{
                        Toast.makeText(context, "Order Failed", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    Log.i("Error", Objects.requireNonNull(e.getMessage()));
                }
            }
        });

        cropQuantity.setText(String.format("Product Quantity: %s %s", crop_quantity, crop_unit));
        cropPrice.setText(String.format("Product Price:₹ %s per %s", crop_price, crop_unit));
        if (rating == null){
            review.setText("Rating: Not Rated");
        }else{
            review.setText(String.format("Rating: %s/5 (%s)", rating, ratingCount));
        }

        deleteButton.setText("Add to Cart");

        ImageView cropImage = (ImageView) vi.findViewById(R.id.imageView);

        if (cropName.getText().toString().toLowerCase().contains("Tomato".toLowerCase())){
            cropImage.setImageResource(R.drawable.tomato);
        }
        else if (cropName.getText().toString().toLowerCase().contains("Cabbage".toLowerCase())){
            cropImage.setImageResource(R.drawable.cabbage);
        }
        else if (cropName.getText().toString().toLowerCase().contains("Carrot".toLowerCase())){
            cropImage.setImageResource(R.drawable.carrot);
        }
        else if (cropName.getText().toString().toLowerCase().contains("bitter".toLowerCase())){
            cropImage.setImageResource(R.drawable.bitter);
        }
        else if (cropName.getText().toString().toLowerCase().contains("Green Chilli".toLowerCase())){
            cropImage.setImageResource(R.drawable.green_chilly);
        }

        else if (cropName.getText().toString().toLowerCase().contains("carrot".toLowerCase())){
            cropImage.setImageResource(R.drawable.carrot);
        }
        else if (cropName.getText().toString().toLowerCase().contains("beans".toLowerCase())){
            cropImage.setImageResource(R.drawable.beans);
        }
        else{
            String url =  db.getUrl(crop_name);

            ImageLoaderTask imageLoaderTask = new ImageLoaderTask(cropImage);
            imageLoaderTask.execute(url);
        }



        deleteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                cursor.moveToPosition(position);

                db.createCart(username, crop_name = cursor.getString(cursor.getColumnIndex("product_name")), crop_quantity = cursor.getString(cursor.getColumnIndex("product_quantity")), cursor.getString(cursor.getColumnIndex("product_price")), cursor.getString(cursor.getColumnIndex("product_unit")), cursor.getString(cursor.getColumnIndex("username")));
                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        return vi;
    }
}
