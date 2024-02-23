package com.example.loginsqllite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class Consumer extends AppCompatActivity {

    private List<ProductItem> productList;
    List<CartItem> cartItems = new ArrayList<>();
    private ProductAdapter productAdapter;
    private EditText searchBar;
    private static final int REQUEST_CART = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);

        // Initialize and populate the product list
        productList = new ArrayList<>();
        cartItems = new ArrayList<>();

        // Fetch market prices asynchronously for each product
        fetchMarketPriceAsync("Tomato");
        fetchMarketPriceAsync("Potato");

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.productRecyclerView);
        productAdapter = new ProductAdapter(this, productList, cartItems);



        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up search bar
        searchBar = findViewById(R.id.searchBar);

        // Set up search button click listener
        ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
        ImageButton cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCartPage();
            }
        });
    }

    private void fetchMarketPriceAsync(String product) {
        PriceCalculator.getMarketPricePerKg(product, new PriceCalculator.PriceCallback() {
            @Override
            public void onPriceReceived(double marketPricePerKg) {
                runOnUiThread(() -> {
                    // Add the product to the list with the fetched market price
                    productList.add(new ProductItem(getProductImageResource(product), product, marketPricePerKg, "5"));

                    // Notify the adapter that the data set has changed
                    productAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(Consumer.this, "Error fetching market price for " + product + ": " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to get product image resource based on the product name
    private int getProductImageResource(String product) {
        // You can implement logic to map product names to image resources here
        // For example, if the product name is "Tomato", return R.drawable.tomato
        // Update this method according to your resource mapping
        // This is a placeholder, you need to replace it with your actual logic
        if ("Tomato".equals(product)) {
            return R.drawable.tomato;
        } else if ("Potato".equals(product)) {
            return R.drawable.potato;
        }

        // Return a default image resource if no match is found
        return 0;
    }

    private void openCartPage() {
        // Create an intent to open the CartActivity
        Intent intent = new Intent(this, Cart.class);

        // Pass the list of cart items to the CartActivity
        intent.putParcelableArrayListExtra("cartItems", (ArrayList<? extends Parcelable>) cartItems);

        // Start the CartActivity
        ActivityCompat.startActivityForResult(this, intent, REQUEST_CART, null);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CART && resultCode == Activity.RESULT_OK && data != null) {
            // Receive the updated cartItems from the Cart activity
            List<CartItem> updatedCartItems = data.getParcelableArrayListExtra("updatedCartItems");

            if (updatedCartItems != null) {
                // Assuming you have a reference to your CartAdapter

                    // Update the cartItems in your adapter and refresh the UI
                   // cartItems.clear();
                    cartItems.addAll(updatedCartItems);
                    productAdapter.updateCartItems(updatedCartItems);

            }
        }
    }
    private void performSearch() {
        String searchText = searchBar.getText().toString().trim().toLowerCase();

        // Filter products based on the search text
        List<ProductItem> filteredProducts = new ArrayList<>();
        for (ProductItem product : productList) {
            if (product.getProductName().toLowerCase().contains(searchText)) {
                filteredProducts.add(product);
            }
        }

        // Update RecyclerView with filtered products
        productAdapter.updateProductList(filteredProducts);
        productAdapter.notifyDataSetChanged();

        // Show a message if no matching products are found
        if (filteredProducts.isEmpty()) {
            Toast.makeText(this, "No matching products found", Toast.LENGTH_SHORT).show();
        }
    }

    public void showFilterOptions(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.filter_menu, popupMenu.getMenu());

        // Set up a listener for the menu items
        popupMenu.setOnMenuItemClickListener(item -> {
            // Handle menu item clicks here
            int itemId = item.getItemId();
            if (itemId == R.id.menu_price) {
                // Handle Price option
            } else if (itemId == R.id.menu_location) {
                // Handle Location option
            } else if (itemId == R.id.menu_recent_products) {
                // Handle Recent Products option
            }
            return true;
        });

        // Show the popup menu
        popupMenu.show();
    }
}
