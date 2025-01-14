package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class CartAdapter extends BaseAdapter {

    Cursor cartItems;

    private static LayoutInflater inflater = null;
    private final Context context;

    DBHelper db;

    String username;

    String crop_name, crop_quantity, crop_price, crop_unit, farmer_name;


    public CartAdapter(Context context, String username) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.username = username;
        db = new DBHelper(context);
        cartItems = db.getCart(username);
        if (cartItems.getCount() == 0) {
            Toast.makeText(context, "No items in cart", Toast.LENGTH_SHORT).show();
        }else if(cartItems==null){
            Toast.makeText(context, "cursor is null", Toast.LENGTH_SHORT).show();
        }


        Toast.makeText(context, username, Toast.LENGTH_SHORT).show();
    }



    @Override
    public int getCount() {
        return cartItems.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"Range", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.my_crop, null);
        cartItems.moveToPosition(position);

        TextView cropName = (TextView) vi.findViewById(R.id.cartProductName);
        TextView cropQuantity = (TextView) vi.findViewById(R.id.cartProductQuantity);
        TextView cropPrice = (TextView) vi.findViewById(R.id.cartProductPrice);
        TextView farmerName = (TextView) vi.findViewById(R.id.cartFarmerName);
        Button deleteButton = (Button) vi.findViewById(R.id.removeButton);
        Button buyNow = (Button) vi.findViewById(R.id.buyNowButton);
        crop_name = cartItems.getString(cartItems.getColumnIndex("cropName"));
        crop_quantity = cartItems.getString(cartItems.getColumnIndex("quantity"));
        crop_price = cartItems.getString(cartItems.getColumnIndex("price"));
        crop_unit = cartItems.getString(cartItems.getColumnIndex("unit"));

        ImageView cropImage = (ImageView) vi.findViewById(R.id.imageView);

        cropName.setText(crop_name);

        if (cropName.getText().toString().equalsIgnoreCase("Tomato")){
            cropImage.setImageResource(R.drawable.tomato);
            Toast.makeText(context, "Tomato", Toast.LENGTH_SHORT).show();
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


        farmer_name = cartItems.getString(cartItems.getColumnIndex("fName"));
        farmerName.setText(String.format("Farmer Name: %s", farmer_name));
        cropQuantity.setText(String.format("Available Product Quantity: %s %s", crop_quantity, crop_unit));
        cropPrice.setText(String.format("Product Price:₹ %s per %s", crop_price, crop_unit));
        deleteButton.setText("Remove from Cart");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(context);

                db.deleteCartItem(username, crop_name, crop_quantity, crop_price);
                Toast.makeText(context, "Removed from cart", Toast.LENGTH_SHORT).show();
                deleteButton.setText("Removed from Cart");
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    DBHelper db = new DBHelper(context);
                    LatLng user_loc = db.getUserLocation(username);
                    db.clearTEM();
                    cartItems.moveToPosition(position);
                    boolean res =  db.createCheckOut(username,user_loc.latitude,user_loc.longitude,cartItems.getDouble(cartItems.getColumnIndex("latitude")),cartItems.getDouble(cartItems.getColumnIndex("longitude")),Double.parseDouble(cartItems.getString(cartItems.getColumnIndex("product_price"))), cartItems.getString(cartItems.getColumnIndex("username")), Integer.parseInt(cartItems.getString(cartItems.getColumnIndex("product_quantity"))),cartItems.getString(cartItems.getColumnIndex("product_name")),cartItems.getString(cartItems.getColumnIndex("product_unit")));
                    if(res){
                        Intent intent = new Intent(context,GetQuantity.class);
                        intent.putExtra("username",username);
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context, "Order Failed", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    Log.i("Error", Objects.requireNonNull(e.getMessage()));
                }
            }
        });
        return vi;
    }
}
