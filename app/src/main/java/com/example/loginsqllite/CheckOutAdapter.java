package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CheckOutAdapter extends BaseAdapter {

    Cursor cursor;
    DBHelper db;

    Context context;

    private static LayoutInflater inflater = null;



    public CheckOutAdapter(Context context,String username) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        db = new DBHelper(context);
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

    @SuppressLint({"Range", "InflateParams", "SetTextI18n"})
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
        String name = cursor.getString(cursor.getColumnIndex("product_name"));
        String quantity = cursor.getString(cursor.getColumnIndex("product_quantity"));
        String price = cursor.getString(cursor.getColumnIndex("product_price"));
        Button deleteButton = (Button) vi.findViewById(R.id.removeButtonCheckOut);
        cropName.setText( "Product Name : " + name);
        cropQuantity.setText("Available Product Quantity : " + quantity);
        cropPrice.setText("Product Price : " + price);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteTEM(name,quantity,price);
                Toast.makeText(context, "Product Removed", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
        return vi;
    }
}
