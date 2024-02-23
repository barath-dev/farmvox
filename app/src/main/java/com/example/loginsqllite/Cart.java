// Cart.java
package com.example.loginsqllite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;
import android.widget.TextView;

public class Cart extends AppCompatActivity implements CartAdapterListener {

    private List<CartItem> cartItems;
    private CartAdapter cartAdapter;

    private List<ProductItem> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Retrieve the list of cart items from the intent
        cartItems = getIntent().getParcelableArrayListExtra("cartItems");
        productList = getIntent().getParcelableArrayListExtra("productList");
        // Set up RecyclerView for displaying cart items
        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
        cartAdapter = new CartAdapter((Context) this, cartItems, (CartAdapterListener) this); // Pass the context
        recyclerView.setAdapter(cartAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void onCartUpdated(List<CartItem> updatedCartItems) {
        // Update the cart items and UI in the Cart activity
        updateCartItems(updatedCartItems);
    }
    public void updateCartItems(List<CartItem> updatedCartItems) {
        //cartItems.clear();
       // cartItems.addAll(updatedCartItems);
        cartAdapter.notifyDataSetChanged();

        // Notify the Consumer activity about the updated cartItems list
        Intent resultIntent = new Intent();
        resultIntent.putParcelableArrayListExtra("updatedCartItems", (ArrayList<? extends Parcelable>) updatedCartItems);
        setResult(Activity.RESULT_OK, resultIntent);
    }
    public void updateTotalPriceUI(double totalPrice) {
        // Update your UI element with the total price
        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
        totalPriceTextView.setText(getString(R.string.total_price_format, totalPrice));
    }
}
