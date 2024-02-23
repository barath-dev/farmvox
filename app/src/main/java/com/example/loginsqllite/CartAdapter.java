package com.example.loginsqllite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context; // Add a Context field
    private CartAdapterListener cartAdapterListener;

    public CartAdapter(Context context, List<CartItem> cartItems,CartAdapterListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.cartAdapterListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.cartProductName.setText(cartItem.getProduct().getProductName());
        holder.cartProductQuantity.setText("Quantity: " + cartItem.getQuantity());
        holder.cartProductImage.setImageResource(cartItem.getImageResId());
        holder.cartProductPrice.setText("Price: " + cartItem.getPrice());

        // Increase quantity button click listener
        holder.plusButton.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            notifyItemChanged(position);
            updateTotalPrice();
        });

        // Decrease quantity button click listener
        holder.minusButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                notifyItemChanged(position);
                updateTotalPrice();
            }
        });

        // Remove button click listener
        holder.removeButton.setOnClickListener(v -> {
            removeItem(position);
        });
    }
    private void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(position);
            updateTotalPrice();

            if (context instanceof CartAdapterListener) {
                ((CartAdapterListener) context).onCartUpdated(cartItems);
            }
        }
    }
    private void updateTotalPrice() {
        // Calculate total price including delivery charges
        double totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        // Add delivery charges (e.g., $5)
        double deliveryCharges = 5.0;
        totalPrice += deliveryCharges;

        // Update UI with the new total price
        // Assuming you have a method in your activity to update the total price UI
        if (context instanceof Cart) {
            ((Cart) context).updateTotalPriceUI(totalPrice);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
