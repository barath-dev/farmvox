package com.example.loginsqllite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private Context context;
    private List<ProductItem> productList;
    private List<CartItem> cartItems;

    public ProductAdapter(Context context, List<ProductItem> productList, List<CartItem> cartItems) {
        this.context = context;
        this.productList = productList;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductItem productItem = productList.get(position);

        // Set views
        holder.productImageView.setImageResource(productItem.getImageResId());
        holder.productNameTextView.setText(productItem.getProductName());
        holder.productPriceTextView.setText("Price: " + productItem.getPrice());
        holder.productQuantityTextView.setText("Quantity: " + productItem.getQuantity());

        // Handle button click
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click, e.g., add the product to the cart
                // You can use productItem for additional actions
                addToCart(productItem);
                Toast.makeText(context, "Add to Cart: " + productItem.getProductName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart(ProductItem productItem) {
        // Check if the item is already in the cart
        CartItem existingCartItem = findCartItemByProduct(productItem);

        // If the item is in the cart, increment the quantity; otherwise, add a new cart item
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
        } else {
            CartItem cartItem = new CartItem(productItem, 1);
            cartItems.add(cartItem);
        }

        notifyDataSetChanged();
    }

    private CartItem findCartItemByProduct(ProductItem productItem) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().equals(productItem)) {
                return cartItem;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Method to update the product list
    public void updateProductList(List<ProductItem> newList) {
        productList = newList;
        notifyDataSetChanged();
    }

    public void updateCartItems(List<CartItem> updatedCartItems) {
        cartItems.clear();
        cartItems.addAll(updatedCartItems);
        notifyDataSetChanged();
    }
}
