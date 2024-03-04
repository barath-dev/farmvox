package com.example.loginsqllite;



import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumerAdapter extends BaseAdapter {

    Context context;

    DBHelper db = new DBHelper(context);

    String username;
    private static LayoutInflater inflater = null;

    Cursor cursor;

    public ConsumerAdapter(Context context, String username) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        DBHelper db = new DBHelper(context);
        this.username = username;
        cursor =  db.getAllProducts();
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
        deleteButton.setText("Add to Cart");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = cropName.getText().toString();
                String quantity = cropQuantity.getText().toString();
                String price = cropPrice.getText().toString();
                db.createCart(username, name, quantity, price);
                deleteButton.setText("Added to Cart");
                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });
        return vi;
    }
}
