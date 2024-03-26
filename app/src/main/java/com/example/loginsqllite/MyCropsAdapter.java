package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyCropsAdapter extends BaseAdapter {

    Context context;

    String username;

    Cursor cursor;

    private static LayoutInflater inflater = null;

    public MyCropsAdapter(Context context,String username) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DBHelper db = new DBHelper(context);
        this.username = username;
        cursor = username.equals("admin") ?db.getAllProducts():db.getCrops(username);
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
            Button buyNowButton = (Button) vi.findViewById(R.id.buyNowButton);
        Button deleteButton = (Button) vi.findViewById(R.id.removeButton);
        String crop_name = cursor.getString(cursor.getColumnIndex("product_name"));
        crop_name = "Product Name: "+crop_name;
        String crop_quantity = cursor.getString(cursor.getColumnIndex("product_quantity"));
        crop_quantity = "Product Quantity: "+crop_quantity;
        String crop_price = cursor.getString(cursor.getColumnIndex("product_price"));
        crop_price = "Product Price: ₹"+crop_price;
        String crop_unit = cursor.getString(cursor.getColumnIndex("product_unit"));
        cropName.setText(crop_name);
        cropQuantity.setText(crop_quantity + " " + crop_unit);
        cropPrice.setText(crop_price + " per " + crop_unit);
        buyNowButton.setVisibility(View.GONE);

        ImageView cropImage = (ImageView) vi.findViewById(R.id.imageView);
        DBHelper db = new DBHelper(context);

        String url =  db.getUrl(crop_name);

        ImageLoaderTask imageLoaderTask = new ImageLoaderTask(cropImage);
        imageLoaderTask.execute(url);

        if(username.equals("admin")){
            farmerName.setVisibility(View.VISIBLE);
            String farmer_name = cursor.getString(cursor.getColumnIndex("username"));
            farmer_name = "Farmer Name: "+farmer_name;
            farmerName.setText(farmer_name);

        }
        else{
            farmerName.setVisibility(View.GONE);
        }

        deleteButton.setText("Delete");

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
